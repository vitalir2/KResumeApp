package me.vitalir.kmptemplate

/**
 * JVM platform name from system properties.
 */
actual fun platformName(): String = "JVM ${System.getProperty("java.version")} (${System.getProperty("os.name")})"
