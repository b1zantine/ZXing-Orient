package me.sudar.zxingorient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.zxing.client.android.Intents;

public class ZxingOrient {

    public static final int REQUEST_CODE = 0x0000c0de; // Only use bottom 16 bits
    private static final String TAG = ZxingOrient.class.getSimpleName();

    private static final String BS_PACKAGE = "me.sudar.zxing";

    private final Activity activity;
    private final android.app.Fragment fragment;
    private final android.support.v4.app.Fragment supportFragment;

//    private boolean autoFocus = true;
//    private boolean flash = false;

    private boolean vibrate;
    private boolean playBeep;

    private Integer iconID;
    private Integer toolbarColor;
    private Integer infoBoxColor;
    private Boolean infoBoxVisibility;
    private String info;

    private final Map<String,Object> moreExtras = new HashMap<String,Object>(3);


    public ZxingOrient(Activity activity) {
        this.activity = activity;
        this.fragment = null;
        this.supportFragment = null;
        initialize();
    }

    public ZxingOrient(android.app.Fragment fragment) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.supportFragment = null;
        initialize();
    }

    public ZxingOrient(android.support.v4.app.Fragment supportFragment) {
        this.activity = supportFragment.getActivity();
        this.fragment = null;
        this.supportFragment = supportFragment;
        initialize();
    }

    private void initialize(){
        vibrate = false;
        playBeep = true;
        iconID = null;
        toolbarColor = null;
        infoBoxColor = null;
        infoBoxVisibility = null;
        info = null;
    }

//    public ZxingOrient setAutoFocus(boolean setting){
//        this.autoFocus = setting;
//        return this;
//    }
//
//    public ZxingOrient setFlash(boolean setting){
//        this.flash= setting;
//        return this;
//    }

    public ZxingOrient setVibration(boolean setting){
        this.vibrate = setting;
        return this;
    }

    public ZxingOrient setBeep(boolean setting){
        this.playBeep= setting;
        return this;
    }

    public ZxingOrient setIcon(int iconID){
        this.iconID = iconID;
        return this;
    }

    public ZxingOrient setToolbarColor(String colorString){
        this.toolbarColor = Color.parseColor(colorString);
        return this;
    }

    public ZxingOrient setInfoBoxColor(String colorString){
        this.infoBoxColor = Color.parseColor(colorString);
        return this;
    }

    public ZxingOrient showInfoBox(boolean visibility) {
        this.infoBoxVisibility =  visibility;
        return this;
    }

    public ZxingOrient setInfo(String info) {
        this.info = info;
        return this;
    }

    public Map<String,?> getMoreExtras() {
        return moreExtras;
    }

    public final void addExtra(String key, Object value) {
        moreExtras.put(key, value);
    }

    public final void initiateScan() {
        initiateScan(Barcode.DEFAULT_CODE_TYPES, -1);
    }

    public final void initiateScan(int cameraId) {
        initiateScan(Barcode.DEFAULT_CODE_TYPES, cameraId);
    }


    public final void initiateScan(Collection<String> desiredBarcodeFormats) {
        initiateScan(desiredBarcodeFormats, -1);
    }

    public final void initiateScan(Collection<String> desiredBarcodeFormats, int cameraId) {
        Intent intentScan = new Intent(BS_PACKAGE + ".SCAN");
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);

//        PERF issue : The following commented lines increases the camera resume time
//        intentScan.putExtra(Intents.Scan.AUTO_FOCUS, autoFocus);
//        intentScan.putExtra((Intents.Scan.FLASH), flash);

        intentScan.putExtra(Intents.Scan.VIBRATE, vibrate);
        intentScan.putExtra((Intents.Scan.BEEP), playBeep);

        if(iconID != null) intentScan.putExtra(Intents.Scan.ICON_ID,iconID);
        if(toolbarColor != null) intentScan.putExtra(Intents.Scan.TOOLBAR_COLOR,toolbarColor);
        if(infoBoxColor != null) intentScan.putExtra(Intents.Scan.INFO_BOX_COLOR,infoBoxColor);
        if(infoBoxVisibility != null) intentScan.putExtra(Intents.Scan.INFO_BOX_VISIBILITY,infoBoxVisibility);
        if(info != null) intentScan.putExtra(Intents.Scan.INFO,info);

        // check which types of codes to scan for
        if (desiredBarcodeFormats != null) {
            // set the desired barcode types
            StringBuilder joinedByComma = new StringBuilder();
            for (String format : desiredBarcodeFormats) {
                if (joinedByComma.length() > 0) {
                    joinedByComma.append(',');
                }
                joinedByComma.append(format);
            }
            intentScan.putExtra("SCAN_FORMATS", joinedByComma.toString());
        }

        // check requested camera ID
        if (cameraId >= 0) {
            intentScan.putExtra("SCAN_CAMERA_ID", cameraId);
        }

        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        attachMoreExtras(intentScan);
        startActivityForResult(intentScan, REQUEST_CODE);
    }

    protected void startActivityForResult(Intent intent, int code) {
        if (fragment == null && supportFragment == null) {
            activity.startActivityForResult(intent, code);
        } else if(supportFragment == null){
            fragment.startActivityForResult(intent, code);
        } else if(fragment == null){
            supportFragment.startActivityForResult(intent, code);
        }
    }


    public static ZxingOrientResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
                byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                Integer orientation = intentOrientation == Integer.MIN_VALUE ? null : intentOrientation;
                String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
                return new ZxingOrientResult(contents,
                        formatName,
                        rawBytes,
                        orientation,
                        errorCorrectionLevel);
            }
            return new ZxingOrientResult();
        }
        return null;
    }



    public final void shareText(CharSequence text) {
        shareText(text, "TEXT_TYPE");
    }


    public final void shareText(CharSequence text, CharSequence type) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(BS_PACKAGE + ".ENCODE");
        intent.putExtra("ENCODE_TYPE", type);
        intent.putExtra("ENCODE_DATA", text);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        attachMoreExtras(intent);
        if (fragment == null && supportFragment == null) {
            activity.startActivity(intent);
        } else if(supportFragment == null){
            fragment.startActivity(intent);
        } else if(fragment == null){
            supportFragment.startActivity(intent);
        }
    }

    private void attachMoreExtras(Intent intent) {
        for (Map.Entry<String,Object> entry : moreExtras.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // Kind of hacky
            if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Bundle) {
                intent.putExtra(key, (Bundle) value);
            } else {
                intent.putExtra(key, value.toString());
            }
        }
    }

}
