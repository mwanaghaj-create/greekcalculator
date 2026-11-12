# Greek Calculator for Windows

This is the Kotlin Compose Desktop version of the Greek calculator. It uses the same retro palette, Greek keypad labels, and arithmetic precedence as the Android app.

## Run and Test

Use the JDK configured for Gradle, then run these commands from the repository root:

```powershell
./gradlew.bat :windows-app:run
./gradlew.bat :windows-app:test
./gradlew.bat :windows-app:createDistributable
```

The distributable app image, including a Windows launcher, is created under `windows-app/build/compose/binaries/main/app/`. Creating it requires a full JDK that includes `jpackage`; Android Studio's bundled JBR does not include that tool.