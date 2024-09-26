plugins {
	kotlin("android")
	kotlin("plugin.compose") version libs.versions.kotlin

	id("com.android.application")
}

// TODO: Centralize name?
// TODO: Move versions to catalog
android {
	compileSdk = 35
	namespace = "test.henry_meds"

	buildFeatures {
		compose = true
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			isShrinkResources = true

			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt")
			)
		}
	}

	defaultConfig {
		applicationId = "test.henry_meds"
		minSdk = 21
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"
	}
}

dependencies {
	implementation(libs.androidx.activity)
	implementation(libs.androidx.lifecycle.viewmodel)

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.m3)
	implementation(libs.compose.material.icons.core)
	implementation(libs.compose.material.icons.extended)
	debugImplementation(libs.compose.ui.tooling)
	implementation(libs.compose.ui.tooling.preview)

	implementation(libs.kotlinx.datetime)

	testImplementation(kotlin("test"))
}

kotlin {
	jvmToolchain(libs.versions.java.get().toInt())
}

tasks.withType<Test> {
	useJUnitPlatform()
}
