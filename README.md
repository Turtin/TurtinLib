[![](https://jitpack.io/v/Turtin/TurtinLib.svg)](https://jitpack.io/#Turtin/TurtinLib)

### Usage

In order to use this library, you must add the following lines to your `build.gradle`

```gradle
repositories {
    maven { url 'https://jitpack.io' }
    maven { // ModMenu support
        name = "Terraformers"
        url = "https://maven.terraformersmc.com/"
    }
}

dependencies {
    implementation "com.github.Turtin:TurtinLib:${project.turtinlib_version}"
    annotationProcessor "com.github.Turtin:TurtinLib:${project.turtinlib_version}:processor'"
}
```

Then go to `gradle.properties` and add the line:
```gradle
turtinlib_version={version}
```

Remember to replace {version} with a release or commit from https://jitpack.io/#Turtin/TurtinLib
