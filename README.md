# ZXing Orient

An Android Library based on [ZXing Library](https://github.com/zxing/zxing) (v.3.1.0) with support for `Portrait Orientation`.

# Scanning

## Basic Setup

### 1. Provide the gradle dependency

```gradle
compile 'me.sudar:zxing-orient:1.1.0@aar'
```

### 2. Create an Intent

```java
Intent intent = new Intent("me.sudar.zxing.SCAN");
startActivityForResult(intent, 0);
```

### 3. Scan and Get the Result

```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data){
    if(requestCode == 0){
        if(resultCode == RESULT_OK){
            String contents = data.getStringExtra("SCAN_RESULT");
        }
    }
}
```

## Additional Setup

For specific modes:
```java
intent.putExtra("SCAN_MODE", "PRODUCT_MODE");

intent.putExtra("SCAN_MODE", "ONE_D_MODE");

intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

intent.putExtra("SCAN_MODE", "DATA_MATRIX_MODE");

intent.putExtra("SCAN_MODE", "AZTEC_MODE");

intent.putExtra("SCAN_MODE", "PDF417_MODE");
```

For Scanning comma-separated list of formats
```java
intent.putExtra("SCAN_FORMATS", "PDF417_MODE");
```
# License

This library is available under the [Apache License, Version 2.0.](https://github.com/SudarAbisheck/ZXing-Orient/blob/master/LICENSE)