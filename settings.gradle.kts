pluginManagement {
    repositories {
        mavenCentral()
        maven("https://packages.jetbrains.team/maven/p/cmp/dev")
        google()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://packages.jetbrains.team/maven/p/cmp/dev")
        google()
    }
}

rootProject.name = "kresume"

// Main app
include(":desktop")

// PoC Web Server
include(":server")

// Linters
include(":tools:linter:konsist")
project(":tools:linter:konsist").projectDir = rootDir.resolve("tools/linter/konsist")
