# NYC Schools

A small demo app which uses data from the 2017 DOE High School Directory (modernized).

You can find my first go at it [here](https://github.com/bhidalgo/NYC-Schools).

# Notes
* This project was built and tested on an API 33 emulator. I did not have a physical device at my disposal at the time.
* Enhancements and considerations are marked as TODOs. Use the Android Studio TODO view to see them.
* Two big features that were omitted in the interest of time are: 1) data caching and 2) offline mode/Checking internet connectivity. Both features are implemented in the first NYC Schools demo. Nonetheless, the application runs under the assumption that the device has internet connectivity. The app must be restarted if internet connectivity is restored after the app is launched.
* Other work that could have been done given more time: dependency injection using Hilt (although the project is small, and its best to not introduce complexity in such cases for a low ROI) and test coverage (unit, integration, instrumented, snapshot, etc.)

# Building the app
`./gradlew assembleDebug`

# Installing the app
`./gradlew installDebug`

# Running the app
* If you ran the install command, and it pushed the application to the device successfully, then you just need to click the icon in your app drawer/home screen
* Take the output from the build command, push it to a device/emulator. If on a real device you may need to enable your security settings to install a debug/unsigned build. 
* Start an emulator or connect a real device with USB Debugging enabled and run the application in Android Studio.
