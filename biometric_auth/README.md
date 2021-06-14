# biometric_auth

- Tích hợp xác thực bằng sinh trắc học

## Thiết lập

1. Thiết lập cho Android

biometric_auth yêu cầu sử dụng FragmentActivity thay vì Activity. Điều này có thể dễ dàng thực hiện bằng cách chuyển sang sử dụng FlutterFragmentActivity thay vì FlutterActivity:

- Cập nhật file `main/kotlin/MainActivity.kt` hoặc tương ứng với java:

```kotlin
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterFragmentActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
    }
}
```

- Thêm dependency:

```
dependencies {
	implementation 'androidx.appcompat:appcompat:1.2.0'
}
```

- Config android:theme tại file `/android/app/src/main/AndroidManifest.xml`:

```xml
android:theme="@style/Theme.AppCompat.Light"
```

- Thêm quyền tại file `/android/app/src/main/AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.USE_FINGERPRINT"/>
```

## Cài đặt

- Thêm đường dẫn tới biometric_auth tại file pubspec.yaml :

```yaml
dependencies:
  biometric_auth:
    path: ../
```

## Sử dụng

- Import thư viện

```Dart
import 'package:biometric_auth/biometric_auth.dart';
```

- Khai báo

```Dart
final BiometricAuth biometricAuth = BiometricAuth();
```

- Đăng nhập

* BiometricAuthType cung cấp 3 loại: BIOMETRIC_STRONG, BIOMETRIC_WEAK, DEVICE_CREDENTIAL

```Dart
 await biometricAuth.biometricLogin(
      type: BiometricAuthType.BIOMETRIC_STRONG,
      title: 'Biomectric Login',
      subtitle: 'Click to login in',
      negativeButtonText: 'Use other method',
 );
```
