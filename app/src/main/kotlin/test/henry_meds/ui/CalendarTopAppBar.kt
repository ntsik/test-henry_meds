@file:OptIn(ExperimentalMaterial3Api::class)

package test.henry_meds.ui

// https://youtrack.jetbrains.com/issue/KTIJ-16847
import androidx.compose.material3.SelectableDates as M3SelectableDates
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import test.henry_meds.data.DateRepository
import test.henry_meds.ui.preview.BooleanProvider

object CalendarTopAppBar {
	abstract class SelectableDates : M3SelectableDates {
		abstract fun isSelectableDate(date: LocalDate): Boolean

		override fun isSelectableDate(utcTimeMillis: Long) =
			isSelectableDate(utcTimeMillis.utcMillisToLocalDate)
	}

	@Composable
	operator fun invoke(
		selectedDate: LocalDate,
		onDateChanged: (LocalDate) -> Unit,
		modifier: Modifier = Modifier,
		actions: @Composable RowScope.() -> Unit = {},
		selectableDates: M3SelectableDates = DatePickerDefaults.AllDates
	) {
		var isExpanded by rememberSaveable {
			mutableStateOf(false)
		}

		invoke(
			actions = actions,
			isExpanded = isExpanded,
			modifier = modifier,
			onDateChanged = onDateChanged,
			onExpandChanged = {
				isExpanded = it
			},
			selectableDates = selectableDates,
			selectedDate = selectedDate
		)
	}

	@Composable
	operator fun invoke(
		selectedDate: LocalDate,
		isExpanded: Boolean,
		onDateChanged: (LocalDate) -> Unit,
		onExpandChanged: (Boolean) -> Unit,
		modifier: Modifier = Modifier,
		actions: @Composable RowScope.() -> Unit = {},
		selectableDates: M3SelectableDates = DatePickerDefaults.AllDates
	) {
		// TODO: Add content scrim?
		// TODO: Also collapse calendar on touch outside
		BackHandler(isExpanded) {
			onExpandChanged(false)
		}

		Column(
			modifier = modifier
		) {
			TopAppBar(
				actions = actions,
				title = {
					ExpandButton(
						isExpanded = isExpanded,
						onExpandChanged = onExpandChanged,
						text = selectedDate.toString() // TODO: User-friendly format
					)
				}
			)

			AnimatedVisibility(isExpanded) {
				val datePickerState = rememberDatePickerState(
					initialSelectedDateMillis = selectedDate.toEpochMillis,
					selectableDates = selectableDates
				)

				LaunchedEffect(selectedDate) {
					val currentMillis = selectedDate.toEpochMillis

					if (currentMillis != datePickerState.selectedDateMillis) {
						datePickerState.selectedDateMillis = currentMillis
					}
				}

				LaunchedEffect(datePickerState.selectedDateMillis) {
					val newDate = datePickerState.selectedLocalDate

					if (newDate != null && selectedDate != newDate) {
						onDateChanged(newDate)
					}
				}

				DatePicker(
					headline = null,
					showModeToggle = false,
					state = datePickerState,
					title = null
				)
			}
		}
	}

	private val DatePickerState.selectedLocalDate: LocalDate?
		get() = selectedDateMillis?.utcMillisToLocalDate

	private val Long.utcMillisToLocalDate: LocalDate
		get() = Instant.fromEpochMilliseconds(this)
			.toLocalDateTime(TimeZone.UTC)
			.date

	private val LocalDate.toEpochMillis: Long
		get() = atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun CalendarTopAppBarPreview(
	@PreviewParameter(BooleanProvider::class) isExpanded: Boolean
) {
	AppTheme {
		CalendarTopAppBar(
			isExpanded = isExpanded,
			onDateChanged = {
				// No-op
			},
			onExpandChanged = {
				// No-op
			},
			selectedDate = DateRepository.instance.today.value
		)
	}
}
