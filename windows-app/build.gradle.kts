plugins {
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.compose")
  alias(libs.plugins.compose.compiler)
}

dependencies {
  implementation(compose.desktop.currentOs)
  implementation(compose.material3)
  testImplementation(kotlin("test"))
}

kotlin {
  jvmToolchain(17)
}

compose.desktop {
  application {
    mainClass = "com.greekcalculator.windows.MainKt"
    nativeDistributions {
      packageName = "Greek Calculator"
      packageVersion = "1.0.0"
    }
  }
}