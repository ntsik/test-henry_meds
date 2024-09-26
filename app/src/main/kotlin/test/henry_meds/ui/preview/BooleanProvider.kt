package test.henry_meds.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class BooleanProvider : PreviewParameterProvider<Boolean> {
	override val values = sequenceOf(false, true)
}
