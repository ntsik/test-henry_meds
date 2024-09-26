package test.henry_meds.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

// TODO: Update/delete
object ScheduleRepository {
	private val _schedule = MutableStateFlow(emptyMap<User, Schedule>())
	val schedule = _schedule.asStateFlow()

	suspend fun addAvailability(
		provider: User,
		timeRange: OpenEndRange<LocalDateTime>,
		timeZone: TimeZone = TimeZone.currentSystemDefault()
	) {
		_schedule.update {
			val schedule = it.getOrElse(provider, ::emptyMap)

			it + (provider to schedule.plus(timeRange, timeZone))
		}
	}
}
