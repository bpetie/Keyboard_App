package keyboard.skin66998213.skins;

import java.util.List;

import keyboard.skin66998213.softkeyboard.ImePreferences;
import keyboard.skin66998213.softkeyboard.LatinKeyboardView;
import keyboard.skin66998213.softkeyboard.SkinsKeyboard;
import keyboard.skin66998213.R;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.input.InputManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Home extends Activity {

  private static final int ACTIVITY_CAMERA = 0;
  private static final int APPS = 1;

  private Button goToCamera;
  private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
  private Uri fileUri;
  private Context context = this;
  private Bitmap mImage;

  public static String mFile = "";

  private ProgressBar progBar;
  private ImageView mView;
  private Button mActive;
  private Button mChoose;
  private Button retakeButton;
  private Button okButton;
  private LinearLayout buttonBar;
  private Boolean firstTime = true;

  public static final int MEDIA_TYPE_IMAGE = 1;
  public static final int MEDIA_TYPE_VIDEO = 2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    mView = (ImageView) findViewById(R.id.picture_result);
    mView.setVisibility(ImageView.GONE);
    progBar = (ProgressBar) findViewById(R.id.marker_progress);
    progBar.setVisibility(ProgressBar.VISIBLE);
    mActive = (Button) findViewById(R.id.activate_button);
    mActive.setVisibility(Button.GONE);
    mChoose = (Button) findViewById(R.id.setting_button);
    mChoose.setVisibility(Button.GONE);
    retakeButton = (Button) findViewById(R.id.retake_button);
    retakeButton.setVisibility(Button.GONE);
    okButton = (Button) findViewById(R.id.ok_button);
    okButton.setVisibility(Button.GONE);
    buttonBar = (LinearLayout) findViewById(R.id.button_bar);
    buttonBar.setVisibility(LinearLayout.GONE);
    goToCamera = (Button) findViewById(R.id.camera);
    goToCamera.setVisibility(Button.INVISIBLE);

    int numCameras = Camera.getNumberOfCameras();
    if (numCameras == 0) {
      toastOut("No camera detected"); 
    }
    else {
      Intent i = new Intent(context, cameraAdapter.class);
      progBar.setVisibility(ProgressBar.VISIBLE);
      startActivityForResult(i, ACTIVITY_CAMERA);
    }

    /*
     * goToCamera.setOnClickListener(new OnClickListener() {
     * 
     * @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void onClick(View arg0)
     * { int numCameras; InputMethodManager imd = (InputMethodManager)
     * context.getSystemService(INPUT_METHOD_SERVICE); InputManager ims =
     * (InputManager) context.getSystemService(INPUT_SERVICE);
     * 
     * int [] deviceNums = ims.getInputDeviceIds();
     * 
     * System.out.println(imd.getCurrentInputMethodSubtype());
     * System.out.println(deviceNums.length); numCameras =
     * Camera.getNumberOfCameras(); if (numCameras == 0) {
     * toastOut("No camera detected"); } else { Intent i = new Intent(context,
     * cameraAdapter.class); progBar.setVisibility(ProgressBar.VISIBLE);
     * startActivityForResult(i, ACTIVITY_CAMERA); } } });
     */

  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    progBar.setVisibility(ProgressBar.GONE);
    switch (requestCode) {
    case ACTIVITY_CAMERA:
      if (resultCode == RESULT_OK) {
        Bundle extras = intent.getExtras();

        mFile = intent.getStringExtra("cropKey");
        System.out.println("main @ " + mFile);
        mView.setVisibility(ImageView.VISIBLE);
        okButton.setVisibility(Button.VISIBLE);
        retakeButton.setVisibility(Button.VISIBLE);
        buttonBar.setVisibility(LinearLayout.VISIBLE);

        mImage = BitmapFactory.decodeFile(mFile);
        int x = mImage.getWidth();
        int y = mImage.getHeight();
        if (x > y) {
          System.out.println("x bigger");
        }
        mView.setImageBitmap(mImage);

        toastOut("Remember to unselect and reselect Skins keyboard\n everytime you take a new picture.");
        /*
         * InputMethodManager inputMgr =
         * (InputMethodManager)context.getSystemService
         * (Context.INPUT_METHOD_SERVICE); InputMethodSubtype lastOne =
         * inputMgr.getLastInputMethodSubtype();
         * 
         * if (firstTime) { System.out.println("null"); firstTime = false;
         * 
         * } else { inputMgr.setCurrentInputMethodSubtype(lastOne); }
         */
      }
      else {
        System.out.println("Result not ok");
        finish();
      }
      break;

    case APPS:
      System.out.println("done");
      break;
    default:
      break;
    }
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

  public void sendRetake(View view) {
    int numCameras = Camera.getNumberOfCameras();
    if (numCameras == 0) {
      toastOut("No camera detected");
    }
    else {
      Intent i = new Intent(context, cameraAdapter.class);
      progBar.setVisibility(ProgressBar.VISIBLE);
      startActivityForResult(i, ACTIVITY_CAMERA);
    }
  }

  public void sendOk(View view) {
    mChoose.setVisibility(Button.VISIBLE);
    mActive.setVisibility(Button.VISIBLE);
    okButton.setVisibility(Button.GONE);
    retakeButton.setVisibility(Button.GONE);
  }

  public void sendSettings(View view) {

    // Intent addKeyboard = new Intent(context, ImePreferences.class);
    // startActivity(addKeyboard);
    toastOut("Select the 'Skins' keyboard");
    InputMethodManager inputMgr = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMgr.restartInput(view);
    Intent keyIntent = new Intent(
        android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
    startActivityForResult(keyIntent, APPS);

  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public void sendActivate(View view) {

    toastOut("Select 'Skins'");

    InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
        .getSystemService(INPUT_METHOD_SERVICE);
    imeManager.showInputMethodPicker();

    List<InputMethodInfo> mlist = imeManager.getInputMethodList();
    InputMethodInfo info;
    System.out.println(mlist.size());
    for (int i = 0; i < mlist.size(); i++) {
      info = mlist.get(i);
      if (info == null) {
        System.out.println("its null");
      }
      else {
        System.out.println(info.getSettingsActivity());
      }
    }

    // activated, ready to use
    System.out.println(isInputMethodEnabled());
    // retakeButton.setVisibility(Button.VISIBLE);

  }

  public void onResume() {
    super.onResume();
    System.out.println("here");
  }

  /*
   * InputManager curKeyboard = (InputManager)
   * getApplicationContext().getSystemService(INPUT_SERVICE); int [] boards =
   * curKeyboard.getInputDeviceIds(); System.out.println(boards.length);
   * InputDevice inDevice; for(int i = 0; i < boards.length; i++){ inDevice =
   * curKeyboard.getInputDevice(i); if (inDevice == null) {
   * System.out.println("its null"); } else {
   * System.out.println(inDevice.getName()); } }
   * 
   * }
   * 
   * public void onResume() { super.onResume(); InputMethodManager imeManager =
   * (InputMethodManager)
   * getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
   * imeManager.showInputMethodPicker(); List<InputMethodInfo> mlist =
   * imeManager.getInputMethodList(); InputMethodInfo info;
   * System.out.println(mlist.size()); Boolean found = false; for(int i = 0; i <
   * mlist.size(); i++){ info = mlist.get(i); if (info == null) {
   * System.out.println("its null"); } else { if (info.getSettingsActivity() ==
   * "com.example.android.softkeyboard.ImePreferences") { i = mlist.size();
   * found = true; } System.out.println(info.getSettingsActivity()); } }
   * 
   * if (found) { //info. } }
   */

  private void toastOut(String message) {
    Toast toast;
      toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();
  }

  public boolean isInputMethodEnabled() {
    String id = Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.DEFAULT_INPUT_METHOD);

    ComponentName defaultInputMethod = ComponentName.unflattenFromString(id);

    ComponentName myInputMethod = new ComponentName(context,
        SkinsKeyboard.class);

    return myInputMethod.equals(defaultInputMethod);
  }

  public static String returnFile() {
    return mFile;
  }
}
