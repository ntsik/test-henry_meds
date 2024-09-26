package test.henry_meds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import test.henry_meds.client.ClientScreen
import test.henry_meds.client.ClientScreen.invoke
import test.henry_meds.data.User
import test.henry_meds.provider.ProviderScreen
import test.henry_meds.provider.ProviderScreen.invoke
import test.henry_meds.ui.AppTheme
import test.henry_meds.ui.preview.NavDestinationProvider
import test.henry_meds.ui.preview.UserProvider

object MainScreen {
	@Composable
	operator fun invoke(
		modifier: Modifier = Modifier,
		initialNavDestination: NavDestination? = null,
		viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
	) {
		val user by viewModel.currentUser.collectAsState()

		invoke(
			initialNavDestination = initialNavDestination,
			modifier = modifier,
			user = user
		)
	}

	@Composable
	operator fun invoke(
		user: User,
		modifier: Modifier = Modifier,
		initialNavDestination: NavDestination? = null
	) {
		val navDestinations = NavDestination.getForUser(user)
		var currentNavDestination by rememberSaveable {
			mutableStateOf(initialNavDestination ?: navDestinations.first())
		}

		// TODO: Animate between screens?
		// TODO: movableContentOf to keep state
		when (currentNavDestination) {
			NavDestination.CLIENT -> {
				ClientScreen(
					modifier = modifier,
					navDestinations = navDestinations
				) {
					currentNavDestination = it
				}
			}

			NavDestination.PROVIDER -> {
				ProviderScreen(
					modifier = modifier,
					navDestinations = navDestinations
				) {
					currentNavDestination = it
				}
			}
		}
	}
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun MainScreenPreview(
	@PreviewParameter(NavDestinationProvider::class) currentNavDestination: NavDestination
) {
	val user = UserProvider.ALL

	AppTheme {
		MainScreen(
			initialNavDestination = currentNavDestination,
			user = user
		)
	}
}

@Composable
@PreviewLightDark
private fun MainScreenPreview(
	@PreviewParameter(UserProvider::class) user: User
) {
	AppTheme {
		MainScreen(
			user = user
		)
	}
}
