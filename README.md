[![](https://jitpack.io/v/kjf3006/kftools-android.svg)](https://jitpack.io/#kjf3006/kftools-android)

Build with the checked-in Gradle wrapper (Gradle 9.6.0, Android Gradle Plugin 9.5.1):

```sh
./gradlew build
```

The Gradle daemon uses JDK 25, as defined in
`gradle/gradle-daemon-jvm.properties`. CLI and Android Studio builds use the same
criteria: an installed JDK 25 is detected automatically, or a matching JDK is
downloaded if none is available. The Foojay resolver in `settings.gradle` supplies
the download URLs when regenerating the criteria:

```sh
./gradlew updateDaemonJvm --jvm-version=25
```

Android sources target Java 21 with core library desugaring
(minimum Android API 23, required by Material 1.14.0).
