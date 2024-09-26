package test.henry_meds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.datetime.LocalDate
import test.henry_meds.data.DateRepository
import test.henry_meds.data.User
import test.henry_meds.data.UserRepository

class AppViewModel(
	private val dateRepo: DateRepository,
	private val userRepo: UserRepository
) : ViewModel() {
	object Factory : ViewModelProvider.Factory by viewModelFactory({
		initializer {
			AppViewModel(
				dateRepo = DateRepository.instance,
				userRepo = UserRepository
			)
		}
	})

	val currentUser = userRepo.currentUser
	val selectedDate = dateRepo.selectedDate
	val today = dateRepo.today

	suspend fun setCurrentUser(user: User) =
		userRepo.setCurrentUser(user)

	suspend fun setSelectedDate(date: LocalDate) =
		dateRepo.setSelectedDate(date)
}
