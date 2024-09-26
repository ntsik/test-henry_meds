package test.henry_meds.provider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import test.henry_meds.AppViewModel
import test.henry_meds.NavDestination
import test.henry_meds.data.Availability
import test.henry_meds.data.DateRepository
import test.henry_meds.data.User
import test.henry_meds.ui.AppScaffold
import test.henry_meds.ui.AppTheme
import test.henry_meds.ui.TimeRangePicker
import test.henry_meds.ui.preview.AvailabilityProvider
import test.henry_meds.ui.preview.UserProvider

object ProviderScreen {
	@Composable
	operator fun invoke(
		navDestinations: Collection<NavDestination>,
		modifier: Modifier = Modifier,
		appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory),
		viewModel: ProviderScreenViewModel = viewModel(factory = ProviderScreenViewModel.Factory),
		onNavDestinationChanged: (NavDestination) -> Unit
	) {
		val coroutineScope = rememberCoroutineScope()

		val selectedDate by appViewModel.selectedDate.collectAsState()
		val today by appViewModel.today.collectAsState()
		val user by appViewModel.currentUser.collectAsState()

		val availabilityForSelectedDate by viewModel.availabilityForSelectedDate.collectAsState()
		val selectableDateRange by viewModel.selectableDateRange.collectAsState()

		invoke(
			availability = availabilityForSelectedDate,
			modifier = modifier,
			navDestinations = navDestinations,
			onAddAvailability = {
				coroutineScope.launch {
					viewModel.addAvailability(it)
				}
			},
			onDateChanged = {
				coroutineScope.launch {
					appViewModel.setSelectedDate(it)
				}
			},
			onNavDestinationChanged = onNavDestinationChanged,
			onUserChanged = {
				coroutineScope.launch {
					appViewModel.setCurrentUser(it)
				}
			},
			selectableDateRange = selectableDateRange,
			selectedDate = selectedDate,
			today = today,
			user = user
		)
	}

	@Composable
	operator fun invoke(
		availability: Availability,
		onAddAvailability: (OpenEndRange<LocalDateTime>) -> Unit,
		navDestinations: Collection<NavDestination>,
		onNavDestinationChanged: (NavDestination) -> Unit,
		selectedDate: LocalDate,
		today: LocalDate,
		onDateChanged: (LocalDate) -> Unit,
		user: User,
		onUserChanged: (User) -> Unit,
		modifier: Modifier = Modifier,
		selectableDateRange: ClosedRange<LocalDate>? = null
	) {
		var isAddAvailabilityOpen by rememberSaveable {
			mutableStateOf(false)
		}

		if (isAddAvailabilityOpen) {
			@OptIn(ExperimentalMaterial3Api::class)
			ModalBottomSheet(
				onDismissRequest = {
					isAddAvailabilityOpen = false
				},
				sheetState = rememberModalBottomSheetState(
					skipPartiallyExpanded = true
				)
			) {
				// TODO: Provide initialTimeRange?
				TimeRangePicker(
					date = selectedDate,
					onCancel = {
						isAddAvailabilityOpen = false
					},
					onTimeRangeConfirmed = {
						onAddAvailability(it)

						isAddAvailabilityOpen = false
					}
				)
			}
		}

		AppScaffold(
			currentNavDestination = NavDestination.PROVIDER,
			floatingActionButton = {
				// TODO: Animation in/out via scaling?
				FloatingActionButton(
					onClick = {
						isAddAvailabilityOpen = true
					}
				) {
					Icon(
						Icons.Filled.Add,
						contentDescription = "Add" // TODO: Localize
					)
				}
			},
			modifier = modifier,
			navDestinations = navDestinations,
			onDateChanged = onDateChanged,
			onNavDestinationChanged = onNavDestinationChanged,
			onUserChanged = onUserChanged,
			selectableDateRange = selectableDateRange,
			selectedDate = selectedDate,
			today = today,
			user = user
		) {
			val contentModifier = Modifier.padding(it)

			if (availability.isEmpty()) {
				Empty(
					modifier = contentModifier
				)
			} else {
				Content(
					availability = availability,
					modifier = contentModifier
				)
			}
		}
	}

	// TODO: Add padding
	@Composable
	private fun Empty(
		modifier: Modifier = Modifier
	) {
		// TODO: Make this prettier
		Text(
			"No availability for selected date",
			modifier = modifier
		)
	}

	@Composable
	private fun Content(
		availability: Availability,
		modifier: Modifier = Modifier
	) {
		LazyColumn(
			modifier = modifier
		) {
			items(
				// TODO: Provide key
				items = availability.toList()
			) {
				// TODO: Delete on swipe-to-dismiss
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.height(100.dp)
						.padding(
							horizontal = 16.dp,
							vertical = 8.dp
						),
					onClick = {
						// TODO: Update with TimeRangeEntryScreen
					}
				) {
					Text(
						// TODO: am/pm support
						// TODO: User-friendly format
						"${it.start} - ${it.endExclusive}",
						modifier = Modifier.padding(16.dp),
						style = MaterialTheme.typography.bodyLarge
					)
				}
			}
		}
	}
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun ProviderScreenPreview(
	@PreviewParameter(AvailabilityProvider::class) availability: Availability
) {
	val navDestinations = NavDestination.entries
	val selectedDate by DateRepository.instance.selectedDate.collectAsState()
	val today by DateRepository.instance.today.collectAsState()
	val user = UserProvider.ALL

	AppTheme {
		ProviderScreen(
			availability = availability,
			navDestinations = navDestinations,
			onAddAvailability = {
				// No-op
			},
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
		)
	}
}
