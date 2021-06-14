import 'package:flutter/material.dart';
import 'dart:async';

import 'package:biometric_auth/biometric_auth.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final BiometricAuth biometricAuth = BiometricAuth();

  @override
  void initState() {
    super.initState();
    BiometricAuth.setBiometricHandler();
  }

  Future<void> startAuth() async {
    await biometricAuth.biometricLogin(
      type: BiometricAuthType.BIOMETRIC_STRONG,
      title: 'Biomectric Login',
      subtitle: 'Click to login in',
      negativeButtonText: 'Use other method',
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: TextButton(
            onPressed: () {
              startAuth();
            },
            child: Text('Start'),
          ),
        ),
      ),
    );
  }
}
