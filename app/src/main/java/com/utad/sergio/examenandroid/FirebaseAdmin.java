package com.utad.sergio.examenandroid;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sergio on 19/2/18.
 */

public class FirebaseAdmin {

    public FirebaseAuth mAuth;
    public FirebaseAdminListener listener;
    public FirebaseUser user;

    public FirebaseAdmin(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void setListener(FirebaseAdminListener listener){
        this.listener=listener;
    }
}
