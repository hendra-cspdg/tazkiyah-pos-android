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

  CameraConfigurationManager(Context context) {
    this.context = context;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */


  //rafraf >> by http://stackoverflow.com/users/1348440/tamara
  private static Point getDisplaySize(final Display display) {
      final Point point = new Point();
      try {
          display.getSize(point);
      } catch (java.lang.NoSuchMethodError ignore) { // Older device
          point.x = display.getWidth();
          point.y = display.getHeight();
      }
      return point;
  }

  void initFromCameraParameters(Camera camera) {
    Camera.Parameters parameters = camera.getParameters();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    Point theScreenResolution = new Point();
    theScreenResolution = getDisplaySize(display);    //display.getSize(theScreenResolution);
    screenResolution = theScreenResolution;
Log.e("raf "+TAG, "Screen resolution: " + screenResolution);
    Log.i(TAG, "Screen resolution: " + screenResolution);
    cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
Log.e("raf "+TAG, "Camera resolution: " + cameraResolution);
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

    initializeTorch(parameters, prefs, safeMode);


//how zxing scan code when android device without auto-focus feature?
//>> QR codes scan without focus. 1D barcodes are hard to scan. Try using macro mode or EDoF, or alternative focus modes like continuous focus, even when regular auto focus is not available.
Log.e(TAG, "PreferencesActivity.KEY_AUTO_FOCUS" + prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, false) );
//ini ga disupport hpku:
Log.e(TAG, "PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS" + prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, false) );

    CameraConfigurationUtils.setFocus(
        parameters,
        prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true),
        prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, true),
        safeMode);

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
Log.e(TAG, " parameters.getPreviewSize() " + parameters.getPreviewSize().width + "   -   " + parameters.getPreviewSize().height  );
    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    Log.i(TAG, "Final camera parameters: " + parameters.flatten());
    camera.setParameters(parameters);
    Camera.Parameters afterParameters = camera.getParameters();
    Camera.Size afterSize = afterParameters.getPreviewSize();
    if (afterSize!= null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
      Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +
                 ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
      cameraResolution.x = afterSize.width;
      cameraResolution.y = afterSize.height;
    }


Log.e(TAG, "cameraResolution.x < cameraResolution.y  " + cameraResolution.x + " < " + cameraResolution.y );
Log.e(TAG, "screenResolution.x < screenResolution.y  " + screenResolution.x + " < " + screenResolution.y );
    if( screenResolution.x < screenResolution.y ) camera.setDisplayOrientation(90);    //by rafraf (or void below?:p ) .. anyway, setDisplayOrientation ini kudu setelah setParameter()
  }


/*
public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
     android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
     android.hardware.Camera.getCameraInfo(cameraId, info);
     int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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
        String flashMode = parameters.getFlashMode();
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

}
