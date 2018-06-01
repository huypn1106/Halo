package com.deadk.halo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.deadk.halo.R;
import com.deadk.halo.ultilities.LocaleHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    TextView txtTiengViet;
    TextView txtEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        Locale.setDefault(new Locale("vi"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        txtEnglish = findViewById(R.id.textview_english);
        txtTiengViet = findViewById(R.id.textview_tiengviet);

        txtTiengViet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateViews("vi");
               txtEnglish.setTextColor(getResources().getColor(android.R.color.black));
               txtTiengViet.setTextColor(getResources().getColor(R.color.general));
            }
        });


        txtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("en");
                txtTiengViet.setTextColor(getResources().getColor(android.R.color.black));
                txtEnglish.setTextColor(getResources().getColor(R.color.general));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


    //    Toolbar appbar =(Toolbar)findViewById(R.id.app_bar);
      //  setSupportActionBar(appbar);
        //getSupportActionBar().hide();

        //Sickenough
    }

    public void changeLang(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        btnLogin.setText(resources.getString(R.string.btn_login_text));
        btnRegister.setText(resources.getString(R.string.btn_register_text));
    }
}
