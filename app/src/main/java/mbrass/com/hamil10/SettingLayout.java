package mbrass.com.hamil10;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class SettingLayout extends AppCompatActivity {

    Spinner spinner1;
    TextView back_btn,contactus_btn,sharefnd_btn;
    //Animation animShake;

    public void gotoMainactivity(){
        Intent main_frame = new Intent(SettingLayout.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_in_anim, R.anim.slide_out_anim);
        startActivity(main_frame);
        Toast.makeText(getApplicationContext(), "Settings "+MainActivity.state, Toast.LENGTH_LONG).show();
    }

    public void Write2File(String fname,String str) {
        FileOutputStream f1 = null;
        try {
            f1 = openFileOutput(fname, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(f1);
            outputWriter.write(str);
            outputWriter.flush();
            outputWriter.close();
            f1.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //animShake = AnimationUtils.loadAnimation(this,R.anim.shake_anim);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //startAnimation(animShake);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diff_lvl, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner1.setSelection(MainActivity.diff_level);

        back_btn = (TextView)findViewById(R.id.back_id);
        sharefnd_btn = (TextView)findViewById(R.id.sharefrnd_id);
        contactus_btn =(TextView)findViewById(R.id.contactus_id);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainactivity();
            }
        });


        sharefnd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String appPackageName = getPackageName();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Hamil10");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+appPackageName+" \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });

        contactus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"jeeth011@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "email from hamil10");
                i.putExtra(Intent.EXTRA_TEXT   , "Please compose the email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingLayout.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
                String[] dlevel={"Beginner","Intermediate","Expert"};
                if(MainActivity.diff_level!=position) {
                    MainActivity.diff_level = position;
                    Write2File(MainActivity.sfname,"RESTART");
                    Write2File(MainActivity.dfname,String.valueOf(position));
                    Toast.makeText(getApplicationContext(), "Difficulty level set to " + dlevel[position], Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
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
        gotoMainactivity();
    }
}
