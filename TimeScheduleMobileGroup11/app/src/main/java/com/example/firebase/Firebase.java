package com.example.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class Firebase {
    public static FirebaseAuth mAuth;
    public static final void loadFirebase(){
        mAuth = FirebaseAuth.getInstance();
    }
}
