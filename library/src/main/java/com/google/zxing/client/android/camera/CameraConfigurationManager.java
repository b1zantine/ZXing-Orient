/*
 * Copyright (C) 2010 ZXing authors
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

package com.google.zxing.client.android.camera;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.google.zxing.client.android.PreferencesActivity;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

  private static final String TAG = "CameraConfiguration";

  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;
  private int rotation;
  private boolean autoFocusSetting;
  private boolean flashSetting;

  CameraConfigurationManager(Context context, int rotation,boolean autoFocusSetting, boolean flashSetting) {
    this.context = context;
    this.rotation = rotation;
    this.autoFocusSetting = autoFocusSetting;
    this.flashSetting = flashSetting;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    Point theScreenResolution = new Point();
    display.getSize(theScreenResolution);
    screenResolution = theScreenResolution;
    
    
    if (rotation == Surface.ROTATION_0){
    	int temp = screenResolution.x;
    	screenResolution.x = screenResolution.y;
    	screenResolution.y = temp;
    }
    
    Log.i(TAG, "Screen resolution: " + screenResolution);
    cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
    Log.i(TAG, "Camera resolution: " + cameraResolution);
  }

  void setDesiredCameraParameters(Camera camera, boolean safeMode) {
    Camera.Parameters parameters = camera.getParameters();

    if (parameters == null) {
      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
      return;
    }

    Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

    if (safeMode) {
      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
    }

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

//    initializeTorch(parameters, prefs, safeMode);
//
//    
//    CameraConfigurationUtils.setFocus(
//        parameters,
//        prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true),
//        prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, true),
//        safeMode);
    
    
    ////////My additional code
    setFlash(camera,parameters, flashSetting);
//    setAutoFocus(camera, parameters,autoFocusSetting);
    
    /////end of additional code

    if (!safeMode) {
      if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false)) {
        CameraConfigurationUtils.setInvertColor(parameters);
      }

      if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_BARCODE_SCENE_MODE, true)) {
        CameraConfigurationUtils.setBarcodeSceneMode(parameters);
      }

      if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_METERING, true)) {
        CameraConfigurationUtils.setVideoStabilization(parameters);
        CameraConfigurationUtils.setFocusArea(parameters);
        CameraConfigurationUtils.setMetering(parameters);
      }

    }

    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    camera.setParameters(parameters);
    
    ////////My additional code begin///////////
    switch (rotation) {
      case Surface.ROTATION_0:
    	  camera.setDisplayOrientation(90);
    	  Log.i(TAG,"MY ROTATION_0 check: "+ rotation);
    	  break;
      case Surface.ROTATION_90:
    	  camera.setDisplayOrientation(0);
    	  Log.i(TAG,"MY ROTATION_90 check: "+ rotation);
    	  break;
      case Surface.ROTATION_270:
    	  camera.setDisplayOrientation(180);
    	  Log.i(TAG,"MY ROTATION_270 check: "+ rotation);
    	  break;
      default:
    	  camera.setDisplayOrientation(0);
    	  Log.i(TAG,"MY Default ROTATION check: "+ rotation);
    	  break;
    }
    Log.i(TAG,"MY ROTATION : ///////////////");
    
    ////////My additional code end///////////

    Camera.Parameters afterParameters = camera.getParameters();
    Camera.Size afterSize = afterParameters.getPreviewSize();
    if (afterSize!= null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
      Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +
                 ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
      cameraResolution.x = afterSize.width;
      cameraResolution.y = afterSize.height;
    }
  }
  
  
  /*
  ////////Really dunno why i added this
  
  public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	     
	 }
  
  */
  
  
  

  Point getCameraResolution() {
    return cameraResolution;
  }

  Point getScreenResolution() {
    return screenResolution;
  }

  boolean getTorchState(Camera camera) {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      if (parameters != null) {
        String flashMode = camera.getParameters().getFlashMode();
        return flashMode != null &&
            (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
             Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
      }
    }
    return false;
  }
  


  void setTorch(Camera camera, boolean newSetting) {
    Camera.Parameters parameters = camera.getParameters();
    doSetTorch(parameters, newSetting, false);
    camera.setParameters(parameters);
  }

  private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
    boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
    doSetTorch(parameters, currentSetting, safeMode);
  }

  private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
    CameraConfigurationUtils.setTorch(parameters, newSetting);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    if (!safeMode && !prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, true)) {
      CameraConfigurationUtils.setBestExposure(parameters, newSetting);
    }
  }

  
  //////My additional code... Don't get freaked out
  ///if u remove this, uncomment cameraConfigurationutils.setFocus() in setDesiredParameters()
  void setAutoFocus(Camera camera, Camera.Parameters parameters, boolean newSetting){
	  CameraConfigurationUtils.setFocus(
		        parameters,
		        true, // this is always true to avoid auto focus switching to macro
		        true,
		        false);
	  camera.setParameters(parameters);
  }
  
  void setFlash(Camera camera,Camera.Parameters parameters, boolean newSetting) {
    CameraConfigurationUtils.setTorch(parameters, newSetting);
    camera.setParameters(parameters);
  }
}
