package app.mobile.biometric_auth.core

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import app.mobile.biometric_auth.BiometricAuthPlugin
import app.mobile.biometric_auth.model.BiometricAuthStatus

class BiometricAuthDelegate {

    fun isBiometricReady(context: Context, type: Int): BiometricAuthStatus {
        val biometricManager = BiometricManager.from(context)

        when (biometricManager.canAuthenticate(type)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                return BiometricAuthStatus.Success
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                return BiometricAuthStatus.NoHardware
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                return BiometricAuthStatus.Error
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                return BiometricAuthStatus.NoEnrolled
            }
            else -> {
                return BiometricAuthStatus.Error
            }
        }
    }


    fun showBiometricPrompt(
        context: Context, type: Int, title: String, subTitle: String?, negativeButtonText: String
    ) {
        val promptInfo = setBiometricPromptInfo(
            type,
            title,
            subTitle,
            negativeButtonText
        )

        val biometricPrompt = initBiometricPrompt(context)

        biometricPrompt.authenticate(promptInfo)
    }


    fun initBiometricPrompt(context: Context): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                BiometricAuthPlugin.getChannel().invokeMethod(
                    "biometricAuthResult", hashMapOf(
                        "result" to "error",
                        "errorCode" to errorCode,
                        "errorMessage" to errString
                    )
                )
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                BiometricAuthPlugin.getChannel().invokeMethod(
                    "biometricAuthResult", hashMapOf("result" to "success")
                )
                super.onAuthenticationSucceeded(result)
            }

            override fun onAuthenticationFailed() {
                BiometricAuthPlugin.getChannel().invokeMethod(
                    "biometricAuthResult", hashMapOf("result" to "fail")
                )
                super.onAuthenticationFailed()
            }
        }

        return BiometricPrompt(context as FragmentActivity, executor, callback)
    }


    fun setBiometricPromptInfo(
        type: Int, title: String, subTitle: String? = "", negativeButtonText: String = ""
    ): BiometricPrompt.PromptInfo {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
//            .setAllowedAuthenticators(type)
            .setNegativeButtonText(negativeButtonText)

        return builder.build()
    }
}