# Greek Calculator

A native Android calculator built with Kotlin and Jetpack Compose. Its retro keypad uses Greek letters for digits and arithmetic operators while the display keeps input and results readable in the same Greek-key notation.

## Features

- Addition, subtraction, multiplication, and division with normal precedence
- Decimal input, sign toggle, clear, delete, and equals
- Greek labels from alpha through iota represent digits 1 through 9; omicron represents 0
- Greek operator labels map to the usual arithmetic operations

## Build and Test

Use Android Studio's bundled JDK (or another JDK supported by the Gradle wrapper) and an installed Android SDK. In PowerShell, set the paths for your installation, then run:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
./gradlew.bat assembleDebug
./gradlew.bat testDebugUnitTest
```

To run the Compose interaction test, connect an Android device or start an emulator and run `./gradlew.bat connectedDebugAndroidTest`.

In Android Studio, open this project, wait for Gradle sync, select the `app` configuration, and choose a connected device or emulator to run it.