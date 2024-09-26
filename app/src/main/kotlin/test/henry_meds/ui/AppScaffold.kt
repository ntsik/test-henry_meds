package test.henry_meds.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import kotlin.sequences.forEach
import kotlinx.datetime.LocalDate
import test.henry_meds.NavDestination
import test.henry_meds.data.DateRepository
import test.henry_meds.data.User
import test.henry_meds.ui.preview.NavDestinationProvider
import test.henry_meds.ui.preview.UserProvider

// TODO: Expose a state object that can be kept between screens
@Composable
fun AppScaffold(
	currentNavDestination: NavDestination,
	onNavDestinationChanged: (NavDestination) -> Unit,
	selectedDate: LocalDate,
	today: LocalDate,
	onDateChanged: (LocalDate) -> Unit,
	user: User,
	onUserChanged: (User) -> Unit,
	modifier: Modifier = Modifier,
	actions: @Composable () -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	navDestinations: Collection<NavDestination> = NavDestination.entries,
	selectableDateRange: ClosedRange<LocalDate>? = null,
	content: @Composable (PaddingValues) -> Unit
) {
	LaunchedEffect(navDestinations) {
		if (currentNavDestination !in navDestinations) {
			onNavDestinationChanged(navDestinations.first())
		}
	}

	// TODO: Set up bar behavior to auto-hide elements on scroll?
	// TODO: Use adaptive Scaffold to better support the range of screen sizes
	Scaffold(
		bottomBar = {
			if (navDestinations.size > 1) {
				AppNavigationBar(
					currentNavDestination = currentNavDestination,
					navDestinations = navDestinations,
					onNavDestinationChanged = onNavDestinationChanged
				)
			}
		},
		content = content,
		floatingActionButton = floatingActionButton,
		modifier = modifier,
		topBar = {
			AppTopAppBar(
				actions = {
					actions()

					ProfileDropdownIcon(
						onUserChanged = onUserChanged,
						currentUser = user
					)
				},
				onDateChanged = onDateChanged,
				selectableDateRange = selectableDateRange,
				selectedDate = selectedDate,
				today = today
			)
		}
	)
}

@Composable
private fun ProfileDropdownIcon(
	currentUser: User,
	onUserChanged: (User) -> Unit,
	modifier: Modifier = Modifier
) {
	var isExpanded by rememberSaveable {
		mutableStateOf(false)
	}

	Box(
		modifier = modifier.wrapContentSize(Alignment.TopEnd)
	) {
		IconButton(
			onClick = {
				isExpanded = !isExpanded
			}
		) {
			Icon(
				Icons.Default.AccountCircle,
				contentDescription = "Switch account"
			)
		}

		DropdownMenu(
			expanded = isExpanded,
			onDismissRequest = {
				isExpanded = false
			}
		) {
			UserProvider().values.forEach {
				DropdownMenuItem(
					onClick = {
						onUserChanged(it)
					},
					text = {
						Text(it.displayName)
					},
					trailingIcon = if (currentUser == it) {
						{
							Icon(
								Icons.Default.Check,
								contentDescription = "Active account"
							)
						}
					} else null
				)
			}
		}
	}
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun AppScaffoldPreview(
	@PreviewParameter(NavDestinationProvider::class) currentNavDestination: NavDestination
) {
	val selectedDate by DateRepository.instance.selectedDate.collectAsState()
	val today by DateRepository.instance.today.collectAsState()
	val user = UserProvider.ALL

	AppTheme {
		AppScaffold(
			currentNavDestination = currentNavDestination,
			onDateChanged = {
				// No-op
			},
			onNavDestinationChanged = {
				// No-op
			},
			onUserChanged = {
				// No-op
			},
			selectedDate = selectedDate,
			today = today,
			user = user
		) {
			// No-op
		}
	}
}

@Composable
@PreviewLightDark
private fun AppScaffoldPreview(
	@PreviewParameter(UserProvider::class) user: User
) {
	val navDestinations = NavDestination.entries
	val selectedDate by DateRepository.instance.selectedDate.collectAsState()
	val today by DateRepository.instance.today.collectAsState()

	AppTheme {
		AppScaffold(
			currentNavDestination = navDestinations.first(),
			navDestinations = navDestinations,
			onDateChanged = {
				// No-op
			},
			onNavDestinationChanged = {
				// No-op
			},
			onUserChanged = {
				// No-op
			},
			selectedDate = selectedDate,
			today = today,
			user = user
		) {
			// No-op
		}
	}
}
