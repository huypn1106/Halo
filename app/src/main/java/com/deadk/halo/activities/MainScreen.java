package com.deadk.halo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deadk.halo.R;
import com.deadk.halo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreen extends AppCompatActivity {


    @BindView(R.id.username)
    TextView tvUsername;
    @BindView(R.id.displayname)
    TextView tvDisplayname;
    @BindView(R.id.email)
    TextView tvEmail;
    @BindView(R.id.dateofbirth)
    TextView tvDoB;
    @BindView(R.id.gender)
    TextView tvGender;
    @BindView(R.id.btn_sign_out)
    Button btnSignOut;


    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ButterKnife.bind(this);

        User user = (User) getIntent().getSerializableExtra("user");

        if(user!=null) {
            tvUsername.setText(user.getUsername());
            tvDisplayname.setText(user.getDisplayName());
            tvEmail.setText(user.getEmailAddress());
            tvDoB.setText(user.getDateOfBirth());
            tvGender.setText(user.getGender());
        }

        Toast.makeText(this, firebaseUser.getDisplayName() + "  " + firebaseUser.getEmail(),Toast.LENGTH_LONG ).show();

    }

    @OnClick(R.id.btn_sign_out)
    void setBtnSignOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainScreen.this, MainActivity.class);
        startActivity(intent);
    }
}
