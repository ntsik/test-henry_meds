package test.henry_meds.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.collections.component1
import kotlin.collections.component2
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import test.henry_meds.AppViewModel
import test.henry_meds.NavDestination
import test.henry_meds.data.Availability
import test.henry_meds.data.DateRepository
import test.henry_meds.data.User
import test.henry_meds.data.toSlots
import test.henry_meds.ui.AppScaffold
import test.henry_meds.ui.AppTheme
import test.henry_meds.ui.preview.AvailabilityProvider
import test.henry_meds.ui.preview.UserProvider

object ClientScreen {
	@Composable
	operator fun invoke(
		navDestinations: Collection<NavDestination>,
		modifier: Modifier = Modifier,
		appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory),
		viewModel: ClientScreenViewModel = viewModel(factory = ClientScreenViewModel.Factory),
		onNavDestinationChanged: (NavDestination) -> Unit
	) {
		val coroutineScope = rememberCoroutineScope()

		val selectedDate by appViewModel.selectedDate.collectAsState()
		val today by appViewModel.today.collectAsState()
		val user by appViewModel.currentUser.collectAsState()

		val allAvailabilityForSelectedDate by viewModel.allAvailabilityForSelectedDate.collectAsState()
		val selectableDateRange by viewModel.selectableDateRange.collectAsState()

		invoke(
			allAvailability = allAvailabilityForSelectedDate,
			modifier = modifier,
			navDestinations = navDestinations,
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
		allAvailability: Map<User, Availability>,
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
		// TODO: Add ability to filter for specific provider
		// TODO: Add padding
		AppScaffold(
			currentNavDestination = NavDestination.CLIENT,
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

			if (allAvailability.isEmpty()) {
				Empty(
					modifier = contentModifier
				)
			} else {
				Content(
					allAvailability = allAvailability,
					modifier = contentModifier,
					selectedDate = selectedDate
				)
			}
		}
	}

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
		allAvailability: Map<User, Availability>,
		selectedDate: LocalDate,
		modifier: Modifier = Modifier
	) {
		// TODO: Add stateSaver
		var reservedSlot by remember {
			mutableStateOf<OpenEndRange<LocalTime>?>(null)
		}

		reservedSlot?.let {
			ReserveConfirmation(
				date = selectedDate,
				onCancel = {
					reservedSlot = null
				},
				onConfirm = {
					// TODO: Remove slot availability and persist for user
					reservedSlot = null
				},
				slot = it
			)
		}

		LazyColumn(
			modifier = modifier
		) {
			allAvailability.forEach { (user, availability) ->
				@OptIn(ExperimentalFoundationApi::class)
				stickyHeader(
					key = user.id.toString()
				) {
					// TODO: Make this prettier
					Text(
						user.displayName,
						style = MaterialTheme.typography.labelLarge
					)
				}

				// TODO: Provide key
				item {
					@OptIn(ExperimentalLayoutApi::class)
					FlowRow(
						horizontalArrangement = Arrangement.SpaceEvenly,
						modifier = Modifier.fillMaxWidth()
					) {
						availability.toSlots(selectedDate).forEach {
							InputChip(
								label = {
									// TODO: am/pm support
									// TODO: User-friendly format
									Text("${it.start} - ${it.endExclusive}")
								},
								leadingIcon = {
									Icon(
										Icons.Default.CalendarToday,
										contentDescription = null
									)
								},
								onClick = {
									reservedSlot = it
								},
								selected = false
							)
						}
					}
				}
			}
		}
	}

	@Composable
	private fun ReserveConfirmation(
		date: LocalDate,
		slot: OpenEndRange<LocalTime>,
		onCancel: () -> Unit,
		onConfirm: () -> Unit
	) {
		AlertDialog(
			confirmButton = {
				TextButton(
					onClick = onConfirm
				) {
					Text("Confirm") // TODO: Localize
				}
			},
			dismissButton = {
				TextButton(
					onClick = onCancel
				) {
					Text("Cancel") // TODO: Localize
				}
			},
			icon = {
				Icon(
					Icons.Default.EditCalendar,
					contentDescription = null // TODO
				)
			},
			onDismissRequest = onCancel,
			text = {
				Text(
					"Please confirm reservation", // TODO: Localize
					style = MaterialTheme.typography.bodyLarge
				)
			},
			title = {
				Text(
					// TODO: am/pm support
					// TODO: User-friendly format
					"You selected $date at ${slot.start} - ${slot.endExclusive}", // TODO: Localize
					style = MaterialTheme.typography.titleLarge
				)
			}
		)
	}
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun ClientScreenPreview(
	@PreviewParameter(AvailabilityProvider::class) availability: Availability
) {
	val navDestinations = NavDestination.entries
	val selectedDate by DateRepository.instance.selectedDate.collectAsState()
	val today by DateRepository.instance.today.collectAsState()
	val user = UserProvider.ALL

	AppTheme {
		ClientScreen(
			allAvailability = if (availability.isEmpty()) {
				emptyMap()
			} else {
				mapOf(
					user to availability
				)
			},
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
		)
	}
}
