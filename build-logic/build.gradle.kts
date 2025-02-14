/*
 * Copyright (C) 2024-2025 AntsyLich and The Mihon Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

kotlin {
    jvmToolchain(21)
}

// Configuration should be synced with [/build-logic/src/main/kotlin/mihon/gradle/configurations/spotless.kt]
spotless {
    val ktlintVersion = libs.ktlint.cli.get().version
    kotlin {
        target("src/**/*.kt")
        ktlint(ktlintVersion).setEditorConfigPath(rootProject.file("../.editorconfig"))
        licenseHeaderFile(rootProject.file("../spotless/copyright.kt")).updateYearWithLatest(true)
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion).setEditorConfigPath(rootProject.file("../.editorconfig"))
        licenseHeaderFile(rootProject.file("../spotless/copyright.kt"), "(^(?![\\/ ]\\**).*$)")
            .updateYearWithLatest(true)
    }
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.spotless.gradle)

    // These allow us to reference the dependency catalog inside of our compiled plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = mihon.plugins.android.application.get().pluginId
            implementationClass = "PluginAndroidApplication"
        }
        register("android-library") {
            id = mihon.plugins.android.library.get().pluginId
            implementationClass = "PluginAndroidLibrary"
        }
        register("compose-multiplatform") {
            id = mihon.plugins.compose.multiplatform.get().pluginId
            implementationClass = "PluginComposeMultiplatform"
        }
        register("kotlin-android") {
            id = mihon.plugins.kotlin.android.get().pluginId
            implementationClass = "PluginKotlinAndroid"
        }
        register("kotlin-multiplatform") {
            id = mihon.plugins.kotlin.multiplatform.get().pluginId
            implementationClass = "PluginKotlinMultiplatform"
        }
        register("spotless") {
            id = mihon.plugins.spotless.get().pluginId
            implementationClass = "PluginSpotless"
        }
    }
}
