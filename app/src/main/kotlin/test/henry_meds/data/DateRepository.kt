package test.henry_meds.data

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class DateRepository @VisibleForTesting internal constructor(
	private val clock: Clock = Clock.System,
	private val timeZoneProvider: () -> TimeZone = TimeZone::currentSystemDefault
) {
	companion object {
		val instance = DateRepository()
	}

	private val _today = MutableStateFlow(clock.today)
	val today = _today.asStateFlow()

	private val _selectedDate = MutableStateFlow<LocalDate>(today.value)
	val selectedDate = _selectedDate.asStateFlow()

	suspend fun setSelectedDate(date: LocalDate) =
		_selectedDate.emit(date)

	// TODO: Update selectedDate if it was previous today value?
	// TODO: Call when device date/timezone changes
	suspend fun updateToday() =
		_today.emit(clock.today)

	private val Clock.today: LocalDate
		get() = clock.todayIn(timeZoneProvider())
}
