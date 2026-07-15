package me.vitalir.kmptemplate

import platform.UIKit.UIDevice

/**
 * iOS platform name from UIDevice.
 */
actual fun platformName(): String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
