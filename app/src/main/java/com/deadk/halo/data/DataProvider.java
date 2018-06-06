package com.deadk.halo.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DataProvider {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    private static final DataProvider ourInstance = new DataProvider();

    public static DataProvider getInstance() {
        return ourInstance;
    }

    private DataProvider() {
    }


    public FirebaseAuth getAuth(){
        return mAuth;
    }

    public FirebaseDatabase getDatabase(){
        return database;
    }


}
