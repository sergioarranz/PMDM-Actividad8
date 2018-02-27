package com.utad.sergio.examenandroid;

/**
 * Created by sergio on 19/2/18.
 */

public class DataHolder {

    public static DataHolder instance = new DataHolder();

    public static FirebaseAdmin firebaseAdmin;

    public DataHolder(){
        firebaseAdmin=new FirebaseAdmin();
    }
}
