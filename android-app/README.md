# Pushy Android (android-app branch)

This folder contains a minimal Android app scaffold that packages a WebView and a small Kotlin helper so the project contains both Java and Kotlin code.

Package id: com.arena.app
App name: Arena

Quick build steps in Codespaces

1. Open this repository in a Codespace and choose the `android-app` branch.
2. Install Android SDK components (you may need to install the Android SDK tools in the Codespace). Example steps (run in the Codespace terminal):

   sudo apt-get update && sudo apt-get install -y openjdk-17-gradle

   # Install Android command line tools (if not present) and required platforms/build-tools
   # Follow the official Android SDK docs; then use sdkmanager to install platforms; e.g.:
   # sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0"

3. From the `android-app` folder generate the Gradle wrapper if you don't have it:

   gradle wrapper

4. Build the debug APK (this will produce an APK at app/build/outputs/apk/debug/app-debug.apk):

   ./gradlew :app:assembleDebug

Or if you installed gradle globally:

   gradle :app:assembleDebug

5. Download the APK and install on a device:

   adb install -r app/build/outputs/apk/debug/app-debug.apk

Notes
- The app currently contains a placeholder HTML at app/src/main/assets/www/index.html. Copy your full site files into this directory so the WebView loads your actual content.
- I did not include the Gradle wrapper binaries in this initial commit to keep the repo small; you can generate the wrapper inside Codespaces with `gradle wrapper`.
- If you'd like, I can follow up and copy your repo's index.html and www/ files directly into assets, or add a GitHub Actions workflow that builds the APK automatically.
