package test.henry_meds.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.LocalTime
import test.henry_meds.data.Availability

class AvailabilityProvider : PreviewParameterProvider<Availability> {
	override val values = sequenceOf<Availability>(
		emptySet(),
		setOf(
			LocalTime(
				hour = 8,
				minute = 15
			)..<LocalTime(
				hour = 16,
				minute = 30
			)
		)
	)
}
