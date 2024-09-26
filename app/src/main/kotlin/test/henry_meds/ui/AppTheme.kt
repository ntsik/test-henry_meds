package test.henry_meds.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppTheme(
	content: @Composable () -> Unit
) {
	val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		val context = LocalContext.current

		if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
	} else {
		if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
	}

	MaterialTheme(
		colorScheme = colorScheme,
		content = content
	)
}
