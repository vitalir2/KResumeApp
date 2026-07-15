plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(libs.konsist)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotest.assertions)
}

tasks.test {
    outputs.upToDateWhen { false }
    useJUnitPlatform()
}
