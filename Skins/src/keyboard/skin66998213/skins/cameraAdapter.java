package keyboard.skin66998213.skins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import keyboard.skin66998213.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class cameraAdapter extends Activity {

  private cameraPreview mPreview;
  private Camera mCamera;
  private Button captureButton;
  private ImageButton switchButton;
  private FrameLayout preview;
  private Context context = this;
  public static final int MEDIA_TYPE_IMAGE = 1;
  public static final int MEDIA_TYPE_VIDEO = 2;
  public boolean isFront;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    System.out.println("start of onCreate");

    setContentView(R.layout.camera_surface);
    switchButton = (ImageButton) findViewById(R.id.button_switch);

    // Create our Preview view and set it as the content of our activity.
    int frontOnly = findFrontFacingCamera();
    if (frontOnly == 0) {
      mCamera = getCameraInstance(0);
    }
    else {
      mCamera = getCameraInstance(-1);
      isFront = false;
    }
    if (Camera.getNumberOfCameras() != 2) {
      switchButton.setVisibility(View.GONE);
    }

    System.out.println("is there? " + checkCameraHardware(context));
    if (mCamera == null) {
      System.out.println("NO CAMERA");
    }

    mPreview = new cameraPreview(this, mCamera);
    preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(mPreview);

    captureButton = (Button) findViewById(R.id.button_capture);
    captureButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {

        mCamera.takePicture(null, null, mPicture);
      }
    });

    switchButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        System.out.println("switching it");
        preview.removeAllViews();
        mCamera = flipIt();
        mPreview = new cameraPreview(context, mCamera);
        preview.addView(mPreview);
        System.out.println("here");

      }
    });
  } // OnCreate

  /** A safe way to get an instance of the Camera object. */
  public static Camera getCameraInstance(int num) {
    Camera c = null;
    try {
      if (num == -1) {
        c = Camera.open(); // attempt to get a Camera instance
      }
      else {
        c = Camera.open(num);
      }
      if (c != null) {
        Camera.Parameters params = c.getParameters();
        c.setParameters(params);
      }
    } catch (Exception e) {
      System.out.println("DEBUG" + " Camera did not open");
      // Camera is not available (in use or does not exist)
    }
    return c; // returns null if camera is unavailable
  }

  /** Create a file Uri for saving an image or video */
  private static Uri getOutputMediaFileUri(int type) {
    return Uri.fromFile(getOutputMediaFile(type));
  }

  private static File getOutputMediaFile(int type) {
    // To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.

    File mediaStorageDir = new File(
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "MyCameraApp");
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        Log.d("MyCameraApp", "failed to create directory");
        return null;
      }
    }

    // Create a media file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
        .format(new Date());
    File mediaFile;
    if (type == MEDIA_TYPE_IMAGE) {
      mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"
          + timeStamp + ".jpg");
    }
    else if (type == MEDIA_TYPE_VIDEO) {
      mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"
          + timeStamp + ".mp4");
    }
    else {
      return null;
    }

    return mediaFile;
  }

  public void onOrientationChanged(int orientation) {
    if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN)
      return;

    System.out.println("changed");
    Display display = ((WindowManager) context).getDefaultDisplay();

    int rotation = display.getRotation();
    int degrees = 0;
    switch (rotation) {
    case Surface.ROTATION_0:
      break;
    case Surface.ROTATION_90:
      break;
    case Surface.ROTATION_180:
      break;
    case Surface.ROTATION_270:
      break;
    }
  }

  private PictureCallback mPicture = new PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

      File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

      if (pictureFile == null) {
        System.out
            .println("Error creating media file, check storage permissions: ");
        return;
      }

      try {
        FileOutputStream fos = new FileOutputStream(pictureFile);
        fos.write(data);
        fos.close();
      } catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
        return;
      } catch (IOException e) {
        System.out.println("Error accessing file: " + e.getMessage());
        return;
      }

      // Getting the image we just took
      Bitmap mImage = BitmapFactory.decodeFile(pictureFile.getPath());

      // Some math calculations to adjust the crop for all different 
      // screen sizes
      int scaleX = ((mImage.getWidth() + 2240) / 8);
      int scaleY = ((mImage.getHeight() + 2160) / 12);

      int x = mImage.getWidth() / 2;
      x = x - (scaleX / 2);
      int y = mImage.getHeight() / 2;
      y = y - (scaleY / 2);

      //Cropping the image to about the size of the box on the screen
      Bitmap newImage = Bitmap.createBitmap(mImage, x, y, scaleX, scaleY);
      String cropUrl = saveImage(newImage);

      //returning the original image path and the cropped image path
      Intent intent = new Intent();
      intent.putExtra("returnKey", pictureFile.getPath());
      intent.putExtra("cropKey", cropUrl);
      setResult(RESULT_OK, intent);
      finish();
    }
  };

  public String saveImage(Bitmap myBitmap) {

    File myDir = getOutputMediaFile(MEDIA_TYPE_IMAGE);

    // String fname = "Keyboard.jpg";
    // File file = new File (myDir, fname);
    // if (file.exists ())
    // file.delete ();

    try {
      FileOutputStream out = new FileOutputStream(myDir);
      myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
      out.flush();
      out.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return myDir.getPath();
  }

  /** Check if this device has a camera */
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

  private int findFrontFacingCamera() {
    int cameraId = -1;
    // Search for the front facing camera
    int numberOfCameras = Camera.getNumberOfCameras();
    for (int i = 0; i < numberOfCameras; i++) {
      CameraInfo info = new CameraInfo();
      Camera.getCameraInfo(i, info);
      if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
        System.out.println("Debug Camera found");
        cameraId = i;
        break;
      }
    }
    return cameraId;
  }

  public Camera flipIt() {

    // myCamera is the Camera object
    if (Camera.getNumberOfCameras() >= 2) {
      try {
        if (!isFront) {
          mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
          System.out.println("back camera");
          // which = 1;
        }
        else {
          mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
          System.out.println("front camera");
        }
        isFront = !isFront;
        Camera.Parameters params = mCamera.getParameters();
        mCamera.setParameters(params);
      } catch (Exception exception) {
        System.out.println("camera didnt open");
        mCamera.release();
        mCamera = null;
      }
    }
    return mCamera;
  }

  @Override
  protected void onPause() {
    super.onPause();
    System.out.println("pause");
  }

  @Override
  protected void onStop() {
    super.onStop();
  }
}