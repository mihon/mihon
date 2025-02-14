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
    alias(mihon.plugins.android.application)
    alias(mihon.plugins.compose.multiplatform)
    alias(mihon.plugins.kotlin.android)
}

val release = project.hasProperty("release")

android {
    namespace = "mihon.app"

    defaultConfig {
        applicationId = "app.mihon.android"
        versionCode = 1
        versionName = "2025.0"
    }
    buildTypes {
        val release by getting {
            versionNameSuffix = ".0"

            isMinifyEnabled = release
            isShrinkResources = release
            isPseudoLocalesEnabled = !release
        }
        create("fdroid") {
            initWith(release)

            applicationIdSuffix = ".fdroid"
        }
        create("beta") {
            initWith(release)

            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta0"
        }
        create("alpha") {
            initWith(release)

            applicationIdSuffix = ".alpha"
            versionNameSuffix = "-alpha0"
        }
    }
    splits {
        abi {
            isEnable = true
            isUniversalApk = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }
    buildFeatures {
        shaders = false
    }
}

dependencies {
    implementation(projects.core.ui)

    implementation(compose.foundation)
    implementation(compose.material3)

    implementation(libs.androidx.activity.compose)
}
