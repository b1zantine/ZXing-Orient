# ZXing Orient

An Android Library based on [ZXing Library](https://github.com/zxing/zxing) with support for `Portrait Orientation` and some cool stuffs.

## Basic Setup

1. Provide the gradle dependency

    ```gradle
    compile 'me.sudar:zxing-orient:2.0.0@aar'
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

# License

This library is available under the [Apache License, Version 2.0.](https://github.com/SudarAbisheck/ZXing-Orient/blob/master/LICENSE)