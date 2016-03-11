# ZXing Orient  [ ![Download](https://api.bintray.com/packages/sudarabisheck/maven/ZXing-Orient/images/download.svg) ](https://bintray.com/sudarabisheck/maven/ZXing-Orient/_latestVersion)  [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ZXing--Orient-green.svg?style=true)](https://android-arsenal.com/details/1/3213)

An Android Library based on [ZXing Library](https://github.com/zxing/zxing) with support for `Portrait Orientation` and some cool stuffs.

### Screenshots
<img src="https://raw.githubusercontent.com/SudarAbisheck/ZXing-Orient/master/screenshots/screenshot_1.png" alt="Portrait Screenshot" width="30%"/> <img src="https://raw.githubusercontent.com/SudarAbisheck/ZXing-Orient/master/screenshots/screenshot_2.png" alt="Landscape Screenshot" width="50%"/> 

### Demo App
<a href="https://play.google.com/store/apps/details?id=me.sudar.zxingorient.demo&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1"><img width="150" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" /></a>

## Basic Setup

1. Provide the gradle dependency

    ```gradle
    compile 'me.sudar:zxing-orient:2.1.1@aar'
    ```

2. Start the Scanner

    ```java
    new ZxingOrient(MainActivity.this).initiateScan();
    ```

3. Handle the Result

    ```java
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
    
        ZxingOrientResult scanResult = 
                           ZxingOrient.parseActivityResult(requestCode, resultCode, intent);
                            
        if (scanResult != null) {
            // handle the result
            ...
        }
        ...
    }
    ```

**Note :** In `API 23` and above, don't forget to request permission for `Manifest.permission.CAMERA` before calling ZXing Orient.

## Advanced Setup

- Selection of Specific Barcode Types:

    ```java
    void initiateScan(Collection<String> desiredBarcodeFormats);
    
    void initiateScan(Collection<String> desiredBarcodeFormats, int cameraId)
    ```

    Some Formats include:
    ```java
    Barcode.PRODUCT_CODE_TYPES /** Collection of UPC_A, UPC_E, EAN_8, EAN_13, RSS_14 **/
    
    Barcode.ONE_D_CODE_TYPES /** Collection of UPC_A, UPC_E, UPC_EAN_EXTENSION, EAN_8, EAN_13, 
                                     CODABAR, CODE_39, CODE_93, CODE_128, ITF, RSS_14, RSS_EXPANDED **/
    
    Barcode.TWO_D_CODE_TYPES /** Collection of QR_CODE, DATA_MATRIX, PDF_417, AZTEC **/
    
    
    Barcode.EAN_8 
    Barcode.RSS_14
    Barcode.CODABAR ...
    
    Barcode.QR_CODE
    Barcode.DATA_MATRIX ...
    ```
    
    For all other supported types, take a look at [Barcode.java](https://github.com/SudarAbisheck/ZXing-Orient/blob/master/zxing-orient/src/main/java/me/sudar/zxingorient/Barcode.java)
    
- Selection of Camera

    ```java
    void initiateScan(int cameraId);
    
    void initiateScan(Collection<String> desiredBarcodeFormats, int cameraId);
    ```

- UI Settings

    ```java
    ZxingOrient integrator = new ZxingOrient(MainActivity.this);
    integrator.setIcon(R.drawable.custom_icon)   // Sets the custom icon
    .setToolbarColor("#AA3F51B5")       // Sets Tool bar Color
    .setInfoBoxColor("#AA3F51B5")       // Sets Info box color
    .setInfo("Scan a QR code Image.")   // Sets info message in the info box
    .initiateScan(Barcode.QR_CODE);
    
    new ZxingOrient(Activity.this)
    .showInfoBox(false) // Doesn't display the info box  
    .setBeep(false)  // Doesn't play beep sound
    .setVibration(true)  // Enables the vibration
    .initiateScan();   
    ```

## Generate QR Codes
To generate QR codes add this line when necessary
```java
new ZxingOrient(thisActivity).shareText("Some Random Text");
```

# License

This library is available under the [Apache License, Version 2.0.](https://github.com/SudarAbisheck/ZXing-Orient/blob/master/LICENSE)
