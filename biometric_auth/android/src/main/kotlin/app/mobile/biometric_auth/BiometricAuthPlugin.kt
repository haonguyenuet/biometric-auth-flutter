package app.mobile.biometric_auth

import android.content.Context
import androidx.annotation.NonNull
import app.mobile.biometric_auth.core.BiometricAuthConfig
import app.mobile.biometric_auth.core.BiometricAuthDelegate
import app.mobile.biometric_auth.model.BiometricAuthStatus

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** BiometricAuthPlugin */
class BiometricAuthPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private lateinit var biometricAuthDelegate: BiometricAuthDelegate
    private lateinit var context: Context

    companion object {
        @Volatile
        private var channel: MethodChannel? = null

        fun getChannel(): MethodChannel {
            return channel!!
        }

        fun setChannel(methodChannel: MethodChannel) {
            channel = methodChannel
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, BiometricAuthConfig.CHANNEL)
        channel.setMethodCallHandler(this)
        setChannel(channel)
        biometricAuthDelegate = BiometricAuthDelegate()
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            BiometricAuthConfig.METHOD -> {
                val type: Int? = call.argument<Int>("type");
                val title: String? = call.argument("title")
                val subTitle: String? = call.argument("subtitle")
                val negativeButtonText: String? = call.argument("negativeButtonText")

                when (biometricAuthDelegate.isBiometricReady(context, type!!)) {
                    BiometricAuthStatus.Success -> {
                        biometricAuthDelegate.showBiometricPrompt(
                            context!!,
                            type!!,
                            title.toString(),
                            subTitle,
                            negativeButtonText!!
                        )
                        return result.success("success")
                    }
                    BiometricAuthStatus.Error -> {
                        return result.error(null, "Error", null)
                    }
                    BiometricAuthStatus.NoHardware -> {
                        return result.error(null, "NoHardware", null)
                    }
                    BiometricAuthStatus.NoEnrolled -> {
                        return result.error(null, "NoEnrolled", null)
                    }
                }
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        context = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}

    override fun onDetachedFromActivity() {}
}
