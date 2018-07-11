package mbrass.com.hamil10;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class HighscoreActivity extends AppCompatActivity {

    TextView sharescr_btn,hscore_hscore,backscr_btn;
    Animation animBounce;

    public void tSleep(){
        Thread sleepThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        sleepThread.run();
    }

    public void show_toast(String str) {
        Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = rootView.getDrawingCache();
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("GREC", e.getMessage(), e);
        }
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "My highest score";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Highscore");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void goToMainactivity(){
        Intent mainactivity_frame = new Intent(HighscoreActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_out_anim, R.anim.slide_in_anim);
        startActivity(mainactivity_frame);
        show_toast("New Game!");
    }

    public void ReadBtn(){
        try {
            FileInputStream fileIn = openFileInput(MainActivity.fname);
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[MainActivity.READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            s = s.trim();
            InputRead.close();
            int hscore=s.length()==0?0:Integer.valueOf(s);
            hscore_hscore.setText(String.valueOf(hscore));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        sharescr_btn = (TextView)findViewById(R.id.share_screenshot);
        backscr_btn = (TextView)findViewById(R.id.back_screenshot);
        hscore_hscore = (TextView)findViewById(R.id.hscore_hscore);
        sharescr_btn.setTextColor(Color.WHITE);
        hscore_hscore.setTextColor(Color.WHITE);
        ReadBtn();
        animBounce = AnimationUtils.loadAnimation(this,R.anim.bounce_anim);
        animBounce.setInterpolator(new Bouncer(0.2, 20));

        hscore_hscore.startAnimation(animBounce);
        //tSleep();
        //goToMainactivity();
        sharescr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        });

        backscr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainactivity();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        goToMainactivity();
    }
}
