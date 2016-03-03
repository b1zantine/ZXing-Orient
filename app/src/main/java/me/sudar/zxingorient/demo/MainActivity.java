package me.sudar.zxingorient.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.devicon_typeface_library.DevIcon;
import com.mikepenz.iconics.IconicsDrawable;

import me.sudar.zxingorient.Barcode;
import me.sudar.zxingorient.R;
import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView resultTextView;
    private EditText shareEditText;
    private static final int REQUEST_CAMERA = 0x00000011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resultTextView = (TextView) findViewById(R.id.result_text_view);
        shareEditText = (EditText) findViewById(R.id.encodeEditText);
        findViewById(R.id.button_1).setOnClickListener(this);
        findViewById(R.id.button_2).setOnClickListener(this);
        findViewById(R.id.button_3).setOnClickListener(this);
        findViewById(R.id.button_4).setOnClickListener(this);
        findViewById(R.id.button_5).setOnClickListener(this);

        TextView textView =(TextView)findViewById(R.id.github_link);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Check out the <a href='https://github.com/SudarAbisheck/ZXing-Orient'>Github Repo !!</a>";
        textView.setText(Html.fromHtml(text));

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_1:
                    new ZxingOrient(MainActivity.this).initiateScan();
                break;
            case R.id.button_2:
                new ZxingOrient(MainActivity.this)
                        .setInfo("QR code Scanner with UI customization")
                        .setToolbarColor("#c099cc00")
                        .setInfoBoxColor("#c099cc00")
                        .setBeep(false)
                        .initiateScan(Barcode.QR_CODE);
                break;
            case R.id.button_3:
                    new ZxingOrient(MainActivity.this)
                            .setIcon(R.drawable.custom_icon)
                            .setVibration(true)
                            .initiateScan(Barcode.ONE_D_CODE_TYPES);
                break;
            case R.id.button_4:
                new ZxingOrient(MainActivity.this)
                        .setIcon(R.drawable.custom_icon)
                        .setInfo("Scans 2D barcodes")
                        .initiateScan(Barcode.TWO_D_CODE_TYPES);
                break;

            case R.id.button_5:
                if(shareEditText.getText().length() == 0)
                    new ZxingOrient(MainActivity.this).shareText("https://github.com/SudarAbisheck/ZXing-Orient");
                else
                    new ZxingOrient(MainActivity.this).shareText(shareEditText.getText().toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            resultTextView.setText(scanResult.getContents());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar.getMenu()
                .findItem(R.id.about_icon)
                .setIcon(new IconicsDrawable(this, DevIcon.Icon.dev_github_plain).actionBar().colorRes(R.color.iconColor));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_icon) {
            new LibsBuilder()
                    .withFields(R.string.class.getFields())
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getApplication().getString(R.string.title_about))
                    .withLicenseShown(true)
                    .withAboutIconShown(true)
                    .withAboutVersionShown(true)
                    .withAboutDescription("This is demo app displaying the features of <b>ZXing-Orient</b> library.")
                    .start(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(findViewById(R.id.coordinator_layout), R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new ZxingOrient(MainActivity.this).initiateScan();

                } else {

                    finish();
                }
            }
            break;
        }
    }

}
