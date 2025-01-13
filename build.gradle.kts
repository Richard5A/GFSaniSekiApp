import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "2.1.0"
}

group = "de.richard5a"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://raw.githubusercontent.com/terjedahl/junique/master/maven2")
    google()
}

dependencies {
    implementation("it.sauronsoftware:junique:1.0.4")
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation(compose.material3)
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation(compose.components.resources)
    implementation(compose.materialIconsExtended)
}

compose.resources {
    customDirectory(
        sourceSetName = "desktopMain",
        directoryProvider = provider { layout.projectDirectory.dir("src/main/composeResources") }
    )
    packageOfResClass = "ui.resources"
    generateResClass = auto
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GFSaniSekiApp"
            packageVersion = "1.0.1"
            vendor = "Richard5A"
            windows.iconFile.set(layout.projectDirectory.file("src/main/resources/logo.ico"))

            windows.perUserInstall = true
            windows.shortcut = true
        }
    }
}

