/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.sudar.zxingorient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Sean Owen
 * @author Fred Lin
 * @author Isaac Potoczny-Jones
 * @author Brad Drehmer
 * @author gcstang
 *
 * @author Sudar Abisheck
 */
public class ZxingOrient {

    public static final int REQUEST_CODE = 0x0000c0de; // Only use bottom 16 bits
    private static final String TAG = ZxingOrient.class.getSimpleName();

    private static final String BS_PACKAGE = "me.sudar.zxing";

    private final Activity activity;
    private final Fragment fragment;

    private final Map<String,Object> moreExtras = new HashMap<String,Object>(3);

    /**
     * @param activity {@link Activity} invoking the integration
     */
    public ZxingOrient(Activity activity) {
        this.activity = activity;
        this.fragment = null;
    }

    /**
     * @param fragment {@link Fragment} invoking the integration.
     *  {@link #startActivityForResult(Intent, int)} will be called on the {@link Fragment} instead
     *  of an {@link Activity}
     */
    public ZxingOrient(Fragment fragment) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
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

//        String targetAppPackage = findTargetAppPackage(intentScan);
//        if (targetAppPackage == null) {
//            return showDownloadDialog();
//        }
//        intentScan.setPackage(targetAppPackage);
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        attachMoreExtras(intentScan);
        startActivityForResult(intentScan, REQUEST_CODE);
    }

    protected void startActivityForResult(Intent intent, int code) {
        if (fragment == null) {
            activity.startActivityForResult(intent, code);
        } else {
            fragment.startActivityForResult(intent, code);
        }
    }

    /**
     * <p>Call this from your {@link Activity}'s
     * {@link Activity#onActivityResult(int, int, Intent)} method.</p>
     *
     * @param requestCode request code from {@code onActivityResult()}
     * @param resultCode result code from {@code onActivityResult()}
     * @param intent {@link Intent} from {@code onActivityResult()}
     * @return null if the event handled here was not related to this class, or
     *  else an {@link ZxingOrientResult} containing the result of the scan. If the user cancelled scanning,
     *  the fields will be null.
     */
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


    /**
     * Defaults to type "TEXT_TYPE".
     *
     * @param text the text string to encode as a barcode
     * @return the {@link AlertDialog} that was shown to the user prompting them to download the app
     *   if a prompt was needed, or null otherwise
     * @see #shareText(CharSequence, CharSequence)
     */
    public final void shareText(CharSequence text) {
        shareText(text, "TEXT_TYPE");
    }

    /**
     * Shares the given text by encoding it as a barcode, such that another user can
     * scan the text off the screen of the device.
     *
     * @param text the text string to encode as a barcode
     * @param type type of data to encode. See {@code com.google.zxing.client.android.Contents.Type} constants.
     * @return the {@link AlertDialog} that was shown to the user prompting them to download the app
     *   if a prompt was needed, or null otherwise
     */
    public final void shareText(CharSequence text, CharSequence type) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(BS_PACKAGE + ".ENCODE");
        intent.putExtra("ENCODE_TYPE", type);
        intent.putExtra("ENCODE_DATA", text);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        attachMoreExtras(intent);
        if (fragment == null) {
            activity.startActivity(intent);
        } else {
            fragment.startActivity(intent);
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
