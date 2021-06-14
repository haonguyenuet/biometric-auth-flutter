import 'dart:async';

import 'package:flutter/services.dart';

// To define the types of authentication that your app supports
// The system allows you to declare the following types of authentication
// Authentication using a Class 3 biometric
const int BIOMETRIC_STRONG = 15; //0xOOOF
// Authentication using a Class 2 biometric
const int BIOMETRIC_WEAK = 255; //0xOOFF
// Authentication using a screen lock credential – the user's PIN, pattern, or password.
const int DEVICE_CREDENTIAL = 32768; // 1<<15

enum BiometricAuthType { BIOMETRIC_STRONG, BIOMETRIC_WEAK, DEVICE_CREDENTIAL }

Map<BiometricAuthType, int> _authType = {
  BiometricAuthType.BIOMETRIC_STRONG: BIOMETRIC_STRONG,
  BiometricAuthType.BIOMETRIC_WEAK: BIOMETRIC_WEAK,
  BiometricAuthType.DEVICE_CREDENTIAL: DEVICE_CREDENTIAL,
};

class BiometricAuth {
  static const MethodChannel _channel = const MethodChannel('biometric_auth');

  Future biometricLogin({
    required BiometricAuthType type,
    required String title,
    String? subtitle = "",
    required String negativeButtonText,
  }) async {
    return await _channel.invokeMethod('biometricLogin', {
      'type': _authType[type],
      'title': title,
      'subtitle': subtitle,
      'negativeButtonText': negativeButtonText
    });
  }

  static setBiometricHandler() {
    _channel.setMethodCallHandler((call) {
      if (call.method == 'biometricAuthResult') {
        Map result = call.arguments;
        switch (result['result']) {
          case 'success':
            {
              print('success');
              break;
            }
          case 'error':
            {
              print('error');
              break;
            }
          default:
            {
              print('fail');
              break;
            }
        }
      }
      return Future.delayed(Duration(microseconds: 0));
    });
  }
}
