plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

group = "dev.stashy"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()

    val nativeTargets = listOf(
        mingwX64(),
        linuxX64(),
        linuxArm64()
    )

    nativeTargets.forEach {
        it.binaries {
            executable {
                entryPoint = "main"
            }
        }
    }



    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.io)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.ktor)
            implementation(libs.kotlin.logging)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        linuxMain.dependencies {
            implementation(libs.ktor.client.curl)
        }

        mingwMain.dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }
}
