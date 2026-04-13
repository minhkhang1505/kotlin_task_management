import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
}

kotlin {

    // ========== ANDROID ==========
    androidLibrary {
        namespace = "com.nguyenminhkhang.shared"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {}

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // ========== iOS CONFIG ==========
    val xcfName = "sharedKit"
    val xcFramework = XCFramework(xcfName)

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = xcfName
            isStatic = false
            xcFramework.add(this)
        }
    }

    // ========== SOURCE SETS ==========
    sourceSets {

        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                // Koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                // DateTime
                implementation(libs.kotlinx.datetime)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Android-specific dependencies
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        iosMain {
            dependencies {
                // iOS-specific dependencies
            }
        }
    }
}