package test.henry_meds.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import test.henry_meds.NavDestination
import test.henry_meds.ui.preview.NavDestinationProvider

@Composable
fun AppNavigationBar(
	currentNavDestination: NavDestination,
	navDestinations: Iterable<NavDestination>,
	modifier: Modifier = Modifier,
	onNavDestinationChanged: (NavDestination) -> Unit
) {
	NavigationBar(
		modifier = modifier
	) {
		navDestinations.forEach {
			NavigationBarItem(
				icon = {
					Icon(
						it.icon,
						contentDescription = null
					)
				},
				label = {
					Text(stringResource(it.label))
				},
				onClick = {
					onNavDestinationChanged(it)
				},
				selected = currentNavDestination == it
			)
		}
	}
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun AppNavigationBarPreview(
	@PreviewParameter(NavDestinationProvider::class) currentNavDestination: NavDestination
) {
	AppTheme {
		AppNavigationBar(
			currentNavDestination = currentNavDestination,
			navDestinations = NavDestinationProvider.ALL
		) {
			// No-op
		}
	}
}
