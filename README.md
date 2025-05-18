Add Nitrogen to your modding project.

Inside your `build.gradle`
```java
repositories {
  maven {
    name = "Modrinth"
    url = "https://api.modrinth.com/maven"
  }
}

// Add Nitrogen as a dependency in your environment
dependencies {
  modImplementation("maven.modrinth:nitrogen_rbn:${project.nitrogen version}")
}
```

Inside your `gradle.properties`

```java
nitrogen_version=VERSION
```

You can find Nitrogen's latest version on its [Modrinth Page](https://modrinth.com/mod/nitrogen_rbn/versions)
