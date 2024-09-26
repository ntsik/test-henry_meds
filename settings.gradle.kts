rootProject.name = "test-henry-meds"

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		google()
		mavenCentral()
	}

	versionCatalogs {
		create("libs") {
			version("agp", "8.5.0") // TODO: Use 8.6 when IDEA Android plugin supports it
			version("androidx-activity", "1.9.2")
			version("androidx-lifecycle", "2.6.1")
			version("compose", "2024.09.02")
			version("java", "22")
			version("kotlin", "2.0.20")
			version("kotlinx-datetime", "0.6.1")

			library("agp", "com.android.tools.build", "gradle").versionRef("agp")
			library("androidx-activity", "androidx.activity", "activity-compose").versionRef("androidx-activity")
			library(
				"androidx-lifecycle-viewmodel",
				"androidx.lifecycle",
				"lifecycle-viewmodel-compose"
			).versionRef("androidx-lifecycle")
			library("compose-bom", "androidx.compose", "compose-bom").versionRef("compose")
			library("compose-m3", "androidx.compose.material3", "material3").withoutVersion()
			library("compose-material-icons-core", "androidx.compose.material", "material-icons-core").withoutVersion()
			library(
				"compose-material-icons-extended",
				"androidx.compose.material",
				"material-icons-extended"
			).withoutVersion()
			library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
			library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
			library("kgp", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
			library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime").versionRef("kotlinx-datetime")
		}
	}
}

include("app")
