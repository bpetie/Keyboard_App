package keyboard.skin66998213.skins;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.hardware.Camera;

public class cameraPreview extends SurfaceView implements
    SurfaceHolder.Callback
{
  private SurfaceHolder mHolder;
  private Camera mCamera;
  private Context mContext;
  private static final int ORIENTATION_PORTRAIT_NORMAL = 1;
  private static final int ORIENTATION_PORTRAIT_INVERTED = 2;
  private static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
  private static final int ORIENTATION_LANDSCAPE_INVERTED = 4;

  public cameraPreview(Context context, Camera camera) {
    super(context);
    mCamera = camera;
    mContext = context;
    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    mHolder = getHolder();
    mHolder.addCallback(this);
    // deprecated setting, but required on Android versions prior to 3.0
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    try {
      // Open the Camera in preview mode
      mCamera.setPreviewDisplay(holder);
    } catch (IOException ioe) {
      ioe.printStackTrace(System.out);
    }
  }


  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {
    // If your preview can change or rotate, take care of those events here.
    // Make sure to stop the preview before resizing or reformatting it.

    if (mHolder.getSurface() == null) {
      // preview surface does not exist
      return;
    }

    // stop preview before making changes
    try {
      mCamera.stopPreview();
    } catch (Exception e) {
      // ignore: tried to stop a non-existent preview
    }

    // set preview size and make any resize, rotate or
    // reformatting changes here

    // start preview with new settings
    try {
      setCameraDisplayOrientation();
      mCamera.setPreviewDisplay(mHolder);
      mCamera.startPreview();

    } catch (Exception e) {
      System.out.println("Error starting camera preview: " + e.getMessage());
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    // Surface will be destroyed when replaced with a new screen
    // Always make sure to release the Camera instance
    mCamera.stopPreview();
    mCamera.release();
    mCamera = null;
  }

  private boolean checkCameraHardware(Context context) {
    if (context.getPackageManager().hasSystemFeature(
        PackageManager.FEATURE_CAMERA)) {
      // this device has a camera
      return true;
    }
    else {
      // no camera on this device
      return false;
    }
  }

  public void setCameraDisplayOrientation() {
    if (mCamera == null) {
      return;
    }

    Camera.CameraInfo info = new Camera.CameraInfo();
    // Camera.getCameraInfo(camNum, info);

    WindowManager winManager = (WindowManager) mContext
        .getSystemService(Context.WINDOW_SERVICE);
    int rotation = winManager.getDefaultDisplay().getRotation();

    int degrees = 0;

    switch (rotation) {
    case Surface.ROTATION_0:
      degrees = 0;
      break;
    case Surface.ROTATION_90:
      degrees = 90;
      break;
    case Surface.ROTATION_180:
      degrees = 180;
      break;
    case Surface.ROTATION_270:
      degrees = 270;
      break;
    }

    degrees -= 90;
    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    }
    else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    System.out.println("result " + result);
    mCamera.setDisplayOrientation(result);
  }
    
}