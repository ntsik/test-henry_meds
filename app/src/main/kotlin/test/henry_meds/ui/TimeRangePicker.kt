package test.henry_meds.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimeRangePicker(
	date: LocalDate,
	onCancel: () -> Unit,
	modifier: Modifier = Modifier,
	initialTimeRange: OpenEndRange<LocalTime>? = null,
	onTimeRangeConfirmed: (OpenEndRange<LocalDateTime>) -> Unit
) {
	val coroutineScope = rememberCoroutineScope()

	val pagerState = rememberPagerState {
		2
	}

	val startTimePickerState = rememberTimePickerState(
		initialHour = initialTimeRange?.start?.hour ?: 0,
		initialMinute = initialTimeRange?.start?.minute ?: 0
	)

	val endTimePickerState = rememberTimePickerState(
		initialHour = initialTimeRange?.endExclusive?.hour ?: 0,
		initialMinute = initialTimeRange?.endExclusive?.minute ?: 0
	)

	Column(
		modifier = modifier
	) {
		HorizontalPager(
			state = pagerState,
			userScrollEnabled = false
		) {
			val titleModifier = Modifier
				.align(Alignment.CenterHorizontally)
				.padding(vertical = 16.dp)

			val titleStyle = MaterialTheme.typography.titleLarge

			when (it) {
				0 -> Column {
					Text(
						"Start time", // TODO: Localize
						modifier = titleModifier,
						style = titleStyle
					)

					TimePicker(
						modifier = Modifier.fillMaxWidth(),
						state = startTimePickerState
					)
				}

				1 -> Column {
					Text(
						"End time", // TODO: Localize
						modifier = titleModifier,
						style = titleStyle
					)

					TimePicker(
						modifier = Modifier.fillMaxWidth(),
						state = endTimePickerState
					)
				}
			}
		}

		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier.fillMaxWidth()
		) {
			val isFirstPage = pagerState.currentPage == 0
			val isLastPage = pagerState.currentPage == pagerState.pageCount - 1

			TextButton(
				onClick = {
					if (isFirstPage) {
						onCancel()
					} else {
						coroutineScope.launch {
							pagerState.scrollToPage(pagerState.currentPage - 1)
						}
					}
				}
			) {
				val text = if (isFirstPage) "Cancel" else "Back" // TODO: Localize

				Text(text)
			}

			TextButton(
				onClick = {
					if (isLastPage) {
						val start = date.atTime(
							hour = startTimePickerState.hour,
							minute = startTimePickerState.minute
						)

						val endTime = LocalTime(
							hour = endTimePickerState.hour,
							minute = endTimePickerState.minute
						)

						val endDate = if (start.time > endTime) {
							date.plus(1, DateTimeUnit.DAY)
						} else {
							date
						}

						onTimeRangeConfirmed(start..<endDate.atTime(endTime))
					} else {
						coroutineScope.launch {
							pagerState.scrollToPage(pagerState.currentPage + 1)
						}
					}
				}
			) {
				val text = if (isLastPage) "Confirm" else "Next" // TODO: Localize

				Text(text)
			}
		}
	}
}

@Composable
@PreviewLightDark
private fun TimeRangeEntryScreenPreview() {
	AppTheme {
		val now = Clock.System.now()
		val timeZone = TimeZone.currentSystemDefault()

		val start = now.toLocalDateTime(timeZone)
		val end = now.plus(1.hours).toLocalDateTime(timeZone)

		TimeRangePicker(
			date = start.date,
			initialTimeRange = start.time..<end.time,
			onCancel = {
				// No-op
			}
		) {
			// No-op
		}
	}
}
