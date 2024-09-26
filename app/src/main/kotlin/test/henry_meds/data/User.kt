package test.henry_meds.data

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class User(
	val displayName: String,
	val id: Id,
	val scopes: Set<Scope>
) {
	@JvmInline
	value class Id(val value: Uuid = Uuid.random()) {
		override fun toString() = value.toString()
	}

	enum class Scope {
		CLIENT,
		PROVIDER
	}

	init {
		require(scopes.isNotEmpty())
	}
}
