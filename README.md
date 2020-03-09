[![CircleCI](https://circleci.com/gh/asappinc/churros/tree/master.svg?style=svg)](https://circleci.com/gh/asappinc/churros/tree/master)
[![Download](https://api.bintray.com/packages/asapp/Churros/com.asapp.churros/images/download.svg)](https://bintray.com/asapp/Churros/com.asapp.churros/_latestVersion)

Churros
====
This is a humble library dedicated to making the life of developers easier.

Installation
====
Be sure to have JCenter in the base gradle file.
```groovy
allprojects {
    repositories {
        jcenter()
    }
}
```

Then it's a matter of adding the dependency.
```groovy
dependencies {
    // Churros
    implementation "com.asapp.churros:churros:0.2.0"
}
```

Things we have
====
## runIf
```kotlin
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .runIf(isVerbose) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            addNetworkInterceptor(loggingInterceptor)
        }
        .build()
```

## exhaustive

This code will work happily.
```kotlin
    enum class Snack {
        HUMUS, CHIPS, GUACAMOLE
    }
    val today = GUACAMOLE

    when (today) {
        HUMUS -> getPita()
        CHIPS -> buyGuacamole()
    }
```
This code will complain that you missed thinking about Guacamole
```kotlin
    enum class Snack {
        HUMUS, CHIPS, GUACAMOLE
    }
    val today = GUACAMOLE

    when (today) {
        HUMUS -> getPita()
        CHIPS -> buyGuacamole()
    }
    .exhaustive()
```

## Context.toast
```kotlin
    activitiy.toast(R.string.auth_error)
```