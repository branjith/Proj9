package mbrass.com.hamil10;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected static int diff_level = 0;
    public static String score_str = "SCORE: ", hscore_str = "HIGHSCORE: ";
    TextView opt_a, opt_b, opt_c, opt_d, score_id, hscore_id, showans_id, settings_btn, next_btn, qsn_id, ansId;
    static final int READ_BLOCK_SIZE = 100, increment = 5;
    EqEngine engine;
    int score = 0, hscore = 0;
    static String fname = "hamil10_src.txt",dfname = "hamil10_dif.txt",sfname="hamil10_state.txt";
    String qsn, value, work;
    static String state="START";
    String[] options;
    boolean flag = false;
    LinearLayout options_layout, answer_layout;
    Animation animScale, animAlpha, animBounce;
    Bouncer interpolator;

    public void tSleep(){
        Thread sleepThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        sleepThread.run();
    }

    public void success(View view) {
        setScore(score + increment);
        view.startAnimation(animScale);
        WriteBtn();
        score_id.setTextColor(Color.MAGENTA);
        score_id.startAnimation(animAlpha);
        showans_id.setVisibility(View.INVISIBLE);
        next_btn.setVisibility(View.VISIBLE);
       // tSleep();
       // reset();
    }

    public String scoreFormat(int x) {
        return String.valueOf(100000000 + x).substring(1);
    }

    public void failure(View view) {
        view.startAnimation(animAlpha);
        resetGame();
        WriteBtn();
        score_id.startAnimation(animScale);
        showans_id.setVisibility(View.INVISIBLE);
        next_btn.setVisibility(View.VISIBLE);
    }

    public void resetGame() {
        //if(score>hscore)
        {
            state="NEWGAME";
            WriteState();
            Intent highscore_frame = new Intent(MainActivity.this, HighscoreActivity.class);
            overridePendingTransition(R.anim.slide_out_anim, R.anim.slide_in_anim);
            startActivity(highscore_frame);
        }
        setScore(0);
        ReadBtn();
    }

    public void setScore(int x) {
        score = x;
        score_id.setText(score_str + scoreFormat(x));
    }

    public void reset() {
        state="START";
        score_id.setTextColor(Color.WHITE);
        hscore_id.setTextColor(Color.WHITE);
        animBounce.setInterpolator(interpolator);

        showans_id.setVisibility(View.VISIBLE);
        opt_a.setVisibility(View.VISIBLE);
        opt_b.setVisibility(View.VISIBLE);
        opt_c.setVisibility(View.VISIBLE);
        opt_d.setVisibility(View.VISIBLE);

        opt_a.setBackgroundColor(Color.WHITE);
        opt_b.setBackgroundColor(Color.WHITE);
        opt_c.setBackgroundColor(Color.WHITE);
        opt_d.setBackgroundColor(Color.WHITE);

        flag = true;
        qsn_id.clearComposingText();
        M10 h1 = engine.nextQuestion();
        qsn = h1.meqn + "\n" + h1.mvalue +"\n"+h1.mvalues;
        value = h1.mvalue + h1.mvalues;
        work = h1.mwork;
        String[] options = h1.opts;

        qsn_id.setText(Html.fromHtml(qsn));
        qsn_id.startAnimation(animBounce);
        opt_a.setText(options[0]);
        opt_b.setText(options[1]);
        opt_c.setText(options[2]);
        opt_d.setText(options[3]);
        opt_a.setTextColor(Color.BLACK);
        opt_b.setTextColor(Color.BLACK);
        opt_c.setTextColor(Color.BLACK);
        opt_d.setTextColor(Color.BLACK);

        options_layout.setVisibility(View.VISIBLE);
        ansId.clearComposingText();
        ansId.setText("");
        next_btn.setVisibility(View.INVISIBLE);
        showans_id.setVisibility(View.VISIBLE);
        //answer_layout.setVisibility(View.INVISIBLE);
        savePrefs("RESET");
    }

    public void WriteBtn() {
        FileOutputStream f1 = null;
        try {
            if (score > hscore) {
                hscore=score;
                f1 = openFileOutput(fname, Context.MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(f1);
                outputWriter.write(String.valueOf(hscore));
                outputWriter.flush();
                outputWriter.close();
                f1.close();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void ReadBtn() {
        try {
            FileInputStream fileIn = openFileInput(fname);
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            s = s.trim();
            InputRead.close();
            hscore = s.length() == 0 ? 0 : Integer.valueOf(s);
            hscore_id.setText(hscore_str + scoreFormat(hscore));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReadDif() {
        try {
            FileInputStream fileIn = openFileInput(dfname);
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            s = s.trim();
            InputRead.close();
            diff_level = s.length() == 0 ? 0 : Integer.valueOf(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReadState() {
        try {
            FileInputStream fileIn = openFileInput(sfname);
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            s = s.trim();
            InputRead.close();
            state = s.length() == 0 ? "START" : s.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void WriteState() {
        FileOutputStream f1 = null;
        try {
                f1 = openFileOutput(sfname, Context.MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(f1);
                outputWriter.write(String.valueOf(state));
                outputWriter.flush();
                outputWriter.close();
                f1.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String ReadFile(String filename) {
        String s="";
        try {
            FileInputStream fileIn = openFileInput(sfname);
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            s = s.trim();
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public void WriteFile(String filename,String value) {
        FileOutputStream f1 = null;
        try {
            f1 = openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(f1);
            outputWriter.write(value);
            outputWriter.flush();
            outputWriter.close();
            f1.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void show_toast(String str) {
        Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void show_alert(String str) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage(str);
        builder1.setCancelable(true);
        //InputMethodManager imm = (InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void showAnswer() {
        options_layout.setVisibility(View.INVISIBLE);
        ansId.setText(work);
        next_btn.setVisibility(View.VISIBLE);
        showans_id.setVisibility(View.INVISIBLE);
        state="ANSWER";
        savePrefs("SHOWANSWER");
    }

    public void openSetting() {
        //font has to become smaller as the difficulty increases
        Intent setting_frame = new Intent(MainActivity.this, SettingLayout.class);
        overridePendingTransition(R.anim.slide_out_anim, R.anim.slide_in_anim);
        startActivity(setting_frame);
    }

    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        opt_a = (TextView) findViewById(R.id.a_ID);
        opt_b = (TextView) findViewById(R.id.b_ID);
        opt_c = (TextView) findViewById(R.id.c_ID);
        opt_d = (TextView) findViewById(R.id.d_ID);
        score_id = (TextView) findViewById(R.id.scoreID);
        hscore_id = (TextView) findViewById(R.id.hscoreID);
        settings_btn = (TextView) findViewById(R.id.settingID);
        showans_id = (TextView) findViewById(R.id.showAnsID);
        next_btn = (TextView) findViewById(R.id.nextID);
        qsn_id = (TextView) findViewById(R.id.qsnID);
        ansId = (TextView) findViewById(R.id.ansID);
        options_layout = (LinearLayout) findViewById(R.id.options_layout);
        answer_layout = (LinearLayout) findViewById(R.id.answer_layout);
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce_anim);
        interpolator = new Bouncer(0.2, 20);
        ReadDif();

        showans_id.setVisibility(View.INVISIBLE);

        /*
        if (savedInstanceState != null) {
            String tmp = savedInstanceState.getString("DIFF_LEVEL");
            try {
                diff_level = tmp != null ? Integer.valueOf(tmp) : 0;
            } catch (Exception e) {
                diff_level = 0;
            }
        }
        */

        ReadBtn();
        ReadState();

        next_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reset();

            }
        });
        settings_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSetting();
            }
        });
        showans_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });


        opt_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    if (value.equals(opt_a.getText().toString().trim())) {
                        opt_a.setBackgroundColor(Color.GREEN);
                        success(view);
                    } else {
                        opt_a.setBackgroundColor(Color.RED);
                        if (value.equals(opt_b.getText().toString().trim()))
                            opt_b.setBackgroundColor(Color.GREEN);
                        else if (value.equals(opt_c.getText().toString().trim()))
                            opt_c.setBackgroundColor(Color.GREEN);
                        if (value.equals(opt_d.getText().toString().trim()))
                            opt_d.setBackgroundColor(Color.GREEN);
                        failure(view);
                    }
                    showans_id.setVisibility(View.INVISIBLE);
                }
                flag = false;
                state="RESTART";
            }
        });

        opt_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    if (value.equals(opt_b.getText().toString().trim())) {
                        opt_b.setBackgroundColor(Color.GREEN);
                        success(view);
                    } else {
                        opt_b.setBackgroundColor(Color.RED);
                        if (value.equals(opt_a.getText().toString().trim()))
                            opt_a.setBackgroundColor(Color.GREEN);
                        else if (value.equals(opt_c.getText().toString().trim()))
                            opt_c.setBackgroundColor(Color.GREEN);
                        if (value.equals(opt_d.getText().toString().trim()))
                            opt_d.setBackgroundColor(Color.GREEN);
                        failure(view);
                    }
                    showans_id.setVisibility(View.INVISIBLE);
                }
                flag = false;
                state="RESTART";
            }
        });

        opt_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    if (value.equals(opt_c.getText().toString().trim())) {
                        opt_c.setBackgroundColor(Color.GREEN);
                        success(view);
                    } else {
                        opt_c.setBackgroundColor(Color.RED);
                        if (value.equals(opt_b.getText().toString().trim()))
                            opt_b.setBackgroundColor(Color.GREEN);
                        else if (value.equals(opt_a.getText().toString().trim()))
                            opt_a.setBackgroundColor(Color.GREEN);
                        if (value.equals(opt_d.getText().toString().trim()))
                            opt_d.setBackgroundColor(Color.GREEN);
                        failure(view);
                    }
                    showans_id.setVisibility(View.INVISIBLE);
                }
                flag = false;
                state="RESTART";
            }
        });

        opt_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    if (value.equals(opt_d.getText().toString().trim())) {
                        opt_d.setBackgroundColor(Color.GREEN);
                        success(view);
                    } else {
                        opt_d.setBackgroundColor(Color.RED);
                        if (value.equals(opt_b.getText().toString().trim()))
                            opt_b.setBackgroundColor(Color.GREEN);
                        else if (value.equals(opt_c.getText().toString().trim()))
                            opt_c.setBackgroundColor(Color.GREEN);
                        if (value.equals(opt_a.getText().toString().trim()))
                            opt_a.setBackgroundColor(Color.GREEN);
                        failure(view);
                    }
                    showans_id.setVisibility(View.INVISIBLE);
                }
                flag = false;
                state="RESTART";
            }
        });

        if(engine==null)
            engine = new EqEngine(diff_level);
        String username1 = getUsername();
        if (username1 == null)
            username1 = "Friend!";
        qsn_id.setText("Hello " + username1);
        ansId.setText("click next to start");
        ReadState();
        show_toast("create "+state);
        if(state!=null&&state.contains("START")) {
            ansId.clearComposingText();
            ansId.setText("");
        }
        opt_a.setText("");
        opt_b.setText("");
        opt_c.setText("");
        opt_d.setText("");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        /*
        show_toast("restoring....");
        qsn_id.setText(savedInstanceState.getString("LAST_QSN"));
        opt_a.setText(savedInstanceState.getString("OPTA"));
        opt_b.setText(savedInstanceState.getString("OPTB"));
        opt_c.setText(savedInstanceState.getString("OPTC"));
        opt_d.setText(savedInstanceState.getString("OPTD"));
        work = savedInstanceState.getString("WORK");
        String tmp = savedInstanceState.getString("DIFF_LEVEL");
        try {
            diff_level = tmp != null ? Integer.valueOf(tmp) : 0;
        } catch (Exception e) {
            diff_level = 0;
        }
        */
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        /*
        show_toast("onSaveInsState " + state);
        outState.putString("LAST_QSN", qsn_id.getText().toString().trim());
        outState.putString("OPTA", opt_a.getText().toString().trim());
        outState.putString("OPTB", opt_b.getText().toString().trim());
        outState.putString("OPTC", opt_c.getText().toString().trim());
        outState.putString("OPTD", opt_d.getText().toString().trim());
        outState.putString("WORK", work);
        outState.putString("VALUE", value);
        outState.putString("SCORE", String.valueOf(score));
        outState.putString("STATE", state);
        show_toast("from outstate "+outState.getString("STATE","MERCURY"));
        if(state.isEmpty())
            state="START";
        super.onSaveInstanceState(outState);
        */
    }

    public void savePrefs(String str){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        show_toast(str+" "+state);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LAST_QSN", qsn_id.getText().toString().trim());
        editor.putString("OPTA", opt_a.getText().toString().trim());
        editor.putString("OPTB", opt_b.getText().toString().trim());
        editor.putString("OPTC", opt_c.getText().toString().trim());
        editor.putString("OPTD", opt_d.getText().toString().trim());
        editor.putString("WORK", work);
        editor.putString("VALUE", value);
        editor.putString("SCORE", String.valueOf(score));
        editor.putString("STATE", state);
        WriteState();
        editor.commit();
    }

    public void restorePrefs(String str){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        show_toast(String.valueOf(str)+" "+state);
        options_layout.setVisibility(View.VISIBLE);
        qsn_id.setText(preferences.getString("LAST_QSN","Hello!"));
        opt_a.setText(preferences.getString("OPTA",""));
        opt_b.setText(preferences.getString("OPTB",""));
        opt_c.setText(preferences.getString("OPTC",""));
        opt_d.setText(preferences.getString("OPTD",""));
        work=(preferences.getString("WORK", work));
        //ansId.setText(work);
        value=(preferences.getString("VALUE", value));
        state=preferences.getString("STATE", state);
        String t1=preferences.getString("SCORE", "0").trim();
        t1=t1.isEmpty()?"0":t1;
        score=Integer.parseInt(t1);
        setScore(score);
        show_toast("RESTORE PREFS "+state);
        ReadState();
        show_toast("RESTORE PREFS 2"+state);
        next_btn.setVisibility(View.INVISIBLE);
        showans_id.setVisibility(View.VISIBLE);
        flag=true;
        switch(state){
            case "START":break;
            case "NEWGAME":show_toast("New Game!");reset();break;
            case "RESTART":reset();break;
            case "ANSWER":showAnswer();break;
            default:reset();break;
        }
    }

    public void resumeAns(){
        show_toast("show answer "+state);
    }

    protected void onStart(Bundle savedInstanceState) {
        show_toast("ON START");
    }

    protected void onStop(Bundle savedInstanceState) {
        WriteBtn();
        savePrefs("STOP");
    }

    protected void onPause() {
        super.onPause();
        savePrefs("PAUSE");
        /*
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        show_toast("Pausing..."+opt_a.getText().toString().trim());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LAST_QSN", qsn_id.getText().toString().trim());
        editor.putString("OPTA", opt_a.getText().toString().trim());
        editor.putString("OPTB", opt_b.getText().toString().trim());
        editor.putString("OPTC", opt_c.getText().toString().trim());
        editor.putString("OPTD", opt_d.getText().toString().trim());
        editor.putString("WORK", work);
        editor.putString("VALUE", value);
        editor.putString("SCORE", String.valueOf(score));
        editor.commit();
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        restorePrefs("RESUME");
        /*
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        show_toast(String.valueOf("RESUME "+preferences.getString("LAST_QSN","")));
        qsn_id.setText(preferences.getString("LAST_QSN","Hello!"));
        opt_a.setText(preferences.getString("OPTA",""));
        opt_b.setText(preferences.getString("OPTB",""));
        opt_c.setText(preferences.getString("OPTC",""));
        opt_d.setText(preferences.getString("OPTD",""));
        work=(preferences.getString("WORK", work));
        work=(preferences.getString("VALUE", value));
        */
    }

    protected void onDestroy(Bundle savedInstanceState) {
        WriteBtn(); savePrefs("DESTROY");
    }

    public void animateSomething(){
        Integer colorFrom = getResources().getColor(R.color.colorAccent);
        Integer colorTo = getResources().getColor(R.color.colorPrimary);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new

                                                 ValueAnimator.AnimatorUpdateListener() {

                                                     @Override
                                                     public void onAnimationUpdate (ValueAnimator animator){
                                                         score_id.setTextColor((Integer) animator.getAnimatedValue());
                                                     }

                                                 });
        colorAnimation.start();
    }
}
