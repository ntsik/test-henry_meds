package test.henry_meds.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import test.henry_meds.data.DateRepository
import test.henry_meds.data.ScheduleRepository
import test.henry_meds.data.User
import test.henry_meds.data.UserRepository

class ProviderScreenViewModel(
	private val dateRepo: DateRepository,
	private val scheduleRepo: ScheduleRepository,
	private val userRepo: UserRepository
) : ViewModel() {
	object Factory : ViewModelProvider.Factory by viewModelFactory({
		initializer {
			ProviderScreenViewModel(
				dateRepo = DateRepository.instance,
				scheduleRepo = ScheduleRepository,
				userRepo = UserRepository
			)
		}
	})

	val availabilityForSelectedDate = scheduleRepo.schedule
		.combine(userRepo.currentUser) { schedule, currentUser ->
			schedule.getOrElse(currentUser, ::emptyMap)
		}.combine(dateRepo.selectedDate) { schedule, selectedDate ->
			schedule.getOrElse(selectedDate, ::emptySet)
		}.stateIn(
			initialValue = emptySet(),
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed()
		)

	val selectableDateRange = dateRepo.today.map {
		val start = it
		val end = start.plus(1, DateTimeUnit.YEAR)

		start..end
	}.stateIn(
		initialValue = dateRepo.today.value..dateRepo.today.value,
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed()
	)

	suspend fun addAvailability(
		timeRange: OpenEndRange<LocalDateTime>,
		provider: User = userRepo.currentUser.value,
		timeZone: TimeZone = TimeZone.currentSystemDefault()
	) {
		scheduleRepo.addAvailability(
			provider = provider,
			timeRange = timeRange,
			timeZone = timeZone
		)
	}
}
