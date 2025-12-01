package com.animeforum.app.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class NativeResourceManager(private val context: Context) {

    private fun resolveVibrator(): Vibrator? {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        return vibrator?.takeIf { it.hasVibrator() }
    }

    fun vibrateOnAction(duration: VibrationType) {
        val vibrator = resolveVibrator() ?: return
        val milliseconds = when (duration) {
            VibrationType.SHORT -> 50L
            VibrationType.MEDIUM -> 120L
            VibrationType.LONG -> 220L
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }
}

enum class VibrationType { SHORT, MEDIUM, LONG }
