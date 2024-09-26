package test.henry_meds.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import test.henry_meds.NavDestination

internal class NavDestinationProvider : PreviewParameterProvider<NavDestination> {
	companion object {
		val ALL = NavDestination.entries
	}

	override val values = ALL.asSequence()
}
