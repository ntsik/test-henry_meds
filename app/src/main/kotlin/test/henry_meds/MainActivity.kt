package test.henry_meds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import test.henry_meds.ui.AppTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()

		super.onCreate(savedInstanceState)

		setContent {
			AppTheme {
				MainScreen()
			}
		}
	}
}
