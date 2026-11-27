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
    implementation 'com.github.Turtin:TurtinLib:0.1.1-ALPHA'
    annotationProcessor 'com.github.Turtin:TurtinLib:0.1.1-ALPHA:processor'
}
```

