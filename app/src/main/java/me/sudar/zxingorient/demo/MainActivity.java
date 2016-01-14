package me.sudar.zxingorient.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
                        .initiateScan(Barcode.QR_CODE);
                break;
            case R.id.button_3:
                new ZxingOrient(MainActivity.this)
                        .setIcon(R.drawable.custom_icon)
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


}
