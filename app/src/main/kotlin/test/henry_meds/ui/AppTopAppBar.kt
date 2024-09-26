package test.henry_meds.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.LocalDate
import test.henry_meds.R
import test.henry_meds.data.DateRepository

@Composable
fun AppTopAppBar(
	selectedDate: LocalDate,
	today: LocalDate,
	modifier: Modifier = Modifier,
	actions: @Composable RowScope.() -> Unit = {},
	selectableDateRange: ClosedRange<LocalDate>? = null,
	onDateChanged: (LocalDate) -> Unit
) {
	// TODO: Indicate in calendar which dates have data
	@OptIn(ExperimentalMaterial3Api::class)
	CalendarTopAppBar(
		actions = {
			AnimatedVisibility(
				selectedDate != today
			) {
				IconButton(
					onClick = {
						onDateChanged(today)
					}
				) {
					Icon(
						Icons.Default.Today,
						contentDescription = stringResource(R.string.calendar_today)
					)
				}
			}

			actions()
		},
		modifier = modifier,
		onDateChanged = onDateChanged,
		selectableDates = object : CalendarTopAppBar.SelectableDates() {
			private val yearRange = selectableDateRange?.let {
				it.start.year..it.endInclusive.year
			}

			override fun isSelectableDate(date: LocalDate) =
				selectableDateRange?.contains(date) != false

			override fun isSelectableYear(year: Int) =
				yearRange?.contains(year) != false
		},
		selectedDate = selectedDate
	)
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun AppTopAppBarPreview() {
	val selectedDate by DateRepository.instance.selectedDate.collectAsState()
	val today by DateRepository.instance.today.collectAsState()

	AppTheme {
		AppTopAppBar(
			selectedDate = selectedDate,
			today = today
		) {
			// No-op
		}
	}
}
