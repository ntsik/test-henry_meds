package test.henry_meds.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import test.henry_meds.R
import test.henry_meds.ui.preview.BooleanProvider

@Composable
fun ExpandButton(
	text: String,
	isExpanded: Boolean,
	modifier: Modifier = Modifier,
	interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
	onExpandChanged: (Boolean) -> Unit
) {
	Surface(
		interactionSource = interactionSource,
		modifier = modifier,
		onClick = {},
		shape = ButtonDefaults.textShape
	) {
		Row(
			modifier = Modifier
				.padding(ButtonDefaults.TextButtonContentPadding)
				.toggleable(
					indication = null,
					interactionSource = interactionSource,
					onValueChange = onExpandChanged,
					role = Role.DropdownList, // TODO: Verify
					value = isExpanded
				),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(text)

			Icon(
				if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
				contentDescription = stringResource(R.string.calendar_expand)
			)
		}
	}
}

// TODO: Interactive preview support
@Composable
@PreviewLightDark
private fun ExpandButtonPreview(
	@PreviewParameter(BooleanProvider::class) isExpanded: Boolean
) {
	AppTheme {
		ExpandButton(
			isExpanded = isExpanded,
			text = "test"
		) {
			// No-op
		}
	}
}
