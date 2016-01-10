# ZXing Orient

An Android Library based on [ZXing Library](https://github.com/zxing/zxing) (v.3.1.0) with support for `Portrait Orientation`.

# Setup
## 1. Provide the gradle dependency

```gradle
repositories {
    maven {
        url 'https://dl.bintray.com/sudarabisheck/maven'
    }
}

compile 'me.sudar:zxing-orient:1.0.0@aar'
```

## 2. Add to AndroidManifest.xml

```xml
<activity
            android:name="com.google.zxing.client.android.CaptureActivity"
            android:configChanges="keyboardHidden"
            android:windowSoftInputMode="stateAlwaysHidden" >

            <intent-filter>
                <action android:name="com.google.zxing.client.android.ZXSCAN" />

                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
```

## 3. Create an Intent

```java
Intent intent = new Intent("com.google.zxing.client.android.ZXSCAN");
intent.putExtra("com.google.zxing.client.android.ZXSCAN.SCAN_MODE", "QR_MODE");
startActivityForResult(intent, 0);
```

## 4. Scan and Get the Result

```java
public void onActivityResult(int requestCode, int resultCode, Intent intent){
    if(requestCode == 0){
        if(resultCode == RESULT_OK){
            String contents = intent.getStringExtra("SCAN_RESULT");
            Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();
        }
    }
}
```

# License

This library is available under the [Apache License, Version 2.0.](https://github.com/SudarAbisheck/ZXing-Orient/blob/master/LICENSE)