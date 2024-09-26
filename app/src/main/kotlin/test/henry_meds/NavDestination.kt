package test.henry_meds

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.ui.graphics.vector.ImageVector
import test.henry_meds.data.User

enum class NavDestination(
	val icon: ImageVector,
	@StringRes val label: Int
) {
	CLIENT(
		icon = Icons.Default.PersonSearch,
		label = R.string.nav_label_client
	) {
		override fun canUserVisit(user: User) =
			User.Scope.CLIENT in user.scopes
	},
	PROVIDER(
		icon = Icons.Default.EditCalendar,
		label = R.string.nav_label_provider
	) {
		override fun canUserVisit(user: User) =
			User.Scope.PROVIDER in user.scopes
	}
	;

	abstract fun canUserVisit(user: User): Boolean

	companion object {
		fun getForUser(user: User) =
			NavDestination.entries.filter {
				it.canUserVisit(user)
			}
	}
}
