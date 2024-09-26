package test.henry_meds.client

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
import kotlinx.datetime.plus
import test.henry_meds.data.DateRepository
import test.henry_meds.data.ScheduleRepository

class ClientScreenViewModel(
	private val dateRepo: DateRepository,
	private val scheduleRepo: ScheduleRepository
) : ViewModel() {
	object Factory : ViewModelProvider.Factory by viewModelFactory({
		initializer {
			ClientScreenViewModel(
				dateRepo = DateRepository.instance,
				scheduleRepo = ScheduleRepository
			)
		}
	})

	val allAvailabilityForSelectedDate = scheduleRepo.schedule
		.combine(dateRepo.selectedDate) { schedule, selectedDate ->
			schedule.mapValues {
				it.value.getOrElse(selectedDate, ::emptySet)
			}
		}.stateIn(
			initialValue = emptyMap(),
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed()
		)

	// TODO: Should start at today + 1?
	val selectableDateRange = dateRepo.today.map {
		val start = it
		val end = start.plus(1, DateTimeUnit.YEAR)

		start..end
	}.stateIn(
		initialValue = dateRepo.today.value..dateRepo.today.value,
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed()
	)
}
