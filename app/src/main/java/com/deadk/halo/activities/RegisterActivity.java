package com.deadk.halo.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.deadk.halo.R;
import com.deadk.halo.ultilities.LocaleHelper;

public class RegisterActivity extends AppCompatActivity {

    Spinner spinner_gender;
    ImageButton btn_datepicker;
    TextView textViewDateofBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar appbar =(Toolbar)findViewById(R.id.app_bar);
        appbar.setTitle(R.string.title_register);
        setSupportActionBar(appbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        btn_datepicker = (ImageButton) findViewById(R.id.btn_datepicker);
        textViewDateofBirth = (TextView) findViewById(R.id.textview_dob);

        btn_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear,
                                          int dayOfMonth) {
                        //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                        textViewDateofBirth.setText(
                                (dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);
                    }
                };
                //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
                //sẽ giống với trên TextView khi mở nó lên
                String s=textViewDateofBirth.getText()+"";
                String strArrtmp[]=s.split("/");
                int ngay=Integer.parseInt(strArrtmp[0]);
                int thang=Integer.parseInt(strArrtmp[1])-1;
                int nam=Integer.parseInt(strArrtmp[2]);
                DatePickerDialog pic=new DatePickerDialog(
                        RegisterActivity.this,
                        callback, nam, thang, ngay);
                pic.setTitle(getResources().getString(R.string.birthdaytitle));
                pic.show();
            }
        });

        String array_spinner[];
        array_spinner=new String[2];
        array_spinner[0]= getResources().getString(R.string.male);
        array_spinner[1]=getResources().getString(R.string.female);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.spinner_item, array_spinner);
        spinner_gender.setAdapter(adapter);


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}
