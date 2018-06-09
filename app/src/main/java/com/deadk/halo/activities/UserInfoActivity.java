package com.deadk.halo.activities;

import android.arch.core.util.Function;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deadk.halo.R;
import com.deadk.halo.data.DataProvider;
import com.deadk.halo.fragments.ProfileFragment;
import com.deadk.halo.models.FriendRequest;
import com.deadk.halo.models.User;
import com.deadk.halo.ultilities.GeneralUltilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.textview_name)
    TextView tvName;
    @BindView(R.id.img_cover_photo)
    ImageView imgCover;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.textview_display_name)
    TextView tvDisplayname;
    @BindView(R.id.textview_username)
    TextView tvUsername;
    @BindView(R.id.textview_phoneno)
    TextView tvPhoneNo;
    @BindView(R.id.textview_dob)
    TextView tvDateOfBirth;
    @BindView(R.id.textview_gender)
    TextView tvGender;
    @BindView(R.id.btn_add_friend)
    Button btnAddFriend;
    @BindView(R.id.btn_unfriend)
    Button btnUnfriend;
    @BindView(R.id.btn_send_message)
    Button btnSendMessage;
    @BindView(R.id.btn_cancel_request)
    Button btnCancleRequest;
    @BindView(R.id.tv_receive_request)
    TextView tvReceiveRequest;
    @BindView(R.id.layout_receive_request)
    LinearLayout layoutReceiveRequest;

    private User pickedUser;
    private FirebaseStorage storage = DataProvider.getInstance().getStorage();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ButterKnife.bind(this);

        preset();
        setUserInfo();
    }

    private void preset() {

        Toolbar appbar = (Toolbar) findViewById(R.id.app_bar);
        appbar.setTitle("Hihihihi");
        setSupportActionBar(appbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imgCover.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void setUserInfo() {

        User tempUser = (User)getIntent().getSerializableExtra("pickedUser");

        if(tempUser!=null){

            pickedUser = tempUser;

            tvName.setText(pickedUser.getDisplayName().toString());
            tvDateOfBirth.setText(pickedUser.getDateOfBirth().toString());
            tvDisplayname.setText(pickedUser.getDisplayName().toString());
            tvUsername.setText(pickedUser.getUsername().toString());
            tvPhoneNo.setText(pickedUser.getPhoneNo().toString());

            String gender = pickedUser.getGender().equals("Male")?getResources().getString(R.string.male):getResources().getString(R.string.female);
            tvGender.setText(gender);
            loadingImage.execute();

            checkFriend();
        }

    }

    private void checkFriend(){

        DatabaseReference friendsRef = database.getReference("friends/"+firebaseUser.getUid());

        friendsRef.orderByChild("uid").equalTo(pickedUser.getUid())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    btnAddFriend.setVisibility(View.GONE);
                    btnSendMessage.setVisibility(View.VISIBLE);
                    btnUnfriend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference friendsReqRef = database.getReference("friendrequests/"+pickedUser.getUid());

        friendsReqRef.orderByChild("uid").equalTo(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            btnCancleRequest.setVisibility(View.VISIBLE);
                        }
                        else
                            btnAddFriend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        DatabaseReference friendsReqRefForMainUser = database.getReference("friendrequests/"+firebaseUser.getUid());

        friendsReqRefForMainUser.orderByChild("uid").equalTo(pickedUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            btnAddFriend.setVisibility(View.GONE);
                            tvReceiveRequest.setVisibility(View.VISIBLE);
                            layoutReceiveRequest.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    @OnClick(R.id.btn_add_friend)
    void addFriend(){

        DatabaseReference friendsReqRef = database.getReference("friendrequests/"+pickedUser.getUid());

        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();

        String dateStr = day + "/" + month +"/" +year;

        FriendRequest fReq = new FriendRequest(firebaseUser.getUid().toString(),dateStr);

        friendsReqRef.push().setValue(fReq)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        btnCancleRequest.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnAddFriend.setVisibility(View.VISIBLE);
                    }
                });

        btnAddFriend.setVisibility(View.GONE);

    }


    private AsyncTask<Void,Integer,Void> loadingImage = new AsyncTask<Void, Integer, Void>() {
        @Override
        protected Void doInBackground(Void... urls) {

            Glide.get(getBaseContext()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            StorageReference storageReference = storage.getReference();

            StorageReference imageRef = storageReference.child("images/"+pickedUser.getUid());


            Glide.with(UserInfoActivity.this)
                    .load(imageRef.child("cover"))
                    .apply(GeneralUltilities.requestOptionsCover)
                    .into(imgCover);

            Glide.with(UserInfoActivity.this)
                    .load(imageRef.child("avatar"))
                    .apply(GeneralUltilities.requestOptionsAvt)
                    .into(imgAvatar);

        }
    };


}
