package test.henry_meds.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import test.henry_meds.ui.preview.UserProvider

object UserRepository {
	private val _currentUser = MutableStateFlow(UserProvider.ALL)
	val currentUser = _currentUser.asStateFlow()

	suspend fun setCurrentUser(user: User) =
		_currentUser.emit(user)
}
