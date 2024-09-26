package test.henry_meds.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlin.uuid.ExperimentalUuidApi
import test.henry_meds.data.User


@OptIn(ExperimentalUuidApi::class)
internal class UserProvider : PreviewParameterProvider<User> {
	companion object {
		val ALL = User(
			displayName = "Both",
			id = User.Id(),
			scopes = User.Scope.entries.toSet()
		)

		val CLIENT = User(
			displayName = "Client",
			id = User.Id(),
			scopes = setOf(User.Scope.CLIENT)
		)

		val PROVIDER = User(
			displayName = "Provider",
			id = User.Id(),
			scopes = setOf(User.Scope.PROVIDER)
		)
	}

	override val values = sequenceOf(
		ALL,
		CLIENT,
		PROVIDER
	)
}
