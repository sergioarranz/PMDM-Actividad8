package com.utad.sergio.examenandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginTwitterActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    LoginTwitterActivityEvents events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login_twitter);

        events=new LoginTwitterActivityEvents(this);

        DataHolder.firebaseAdmin = new FirebaseAdmin();
        DataHolder.instance.firebaseAdmin.setListener(events);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.v("Usuario", result.data.getUserName().toString());
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w("failure", "twitterLogin:failure", exception);
                //updateUI(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d("TWITTER LOGIN EXITO", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        DataHolder.instance.firebaseAdmin.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginSuccessful", "signInWithCredential:success");
                            DataHolder.instance.firebaseAdmin.user = DataHolder.instance.firebaseAdmin.mAuth.getCurrentUser();
                            DataHolder.instance.firebaseAdmin.listener.firebaseAdmin_LoginOK(true);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            DataHolder.instance.firebaseAdmin.listener.firebaseAdmin_LoginOK(false);
                            Log.w("LoginFailure", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginTwitterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}

class LoginTwitterActivityEvents implements FirebaseAdminListener {

    LoginTwitterActivity loginTwitterActivity;

    public LoginTwitterActivityEvents(LoginTwitterActivity loginTwitterActivity){
        this.loginTwitterActivity = loginTwitterActivity;
    }

    @Override
    public void firebaseAdmin_LoginOK(boolean blOK) {
        Log.v("LoginTwitterActivity", "LOGIN TWITTER CORRECTO");
        if (blOK) {
            Intent intent = new Intent(loginTwitterActivity,MainActivity.class);
            loginTwitterActivity.startActivity(intent);
            loginTwitterActivity.finish();
        } else {

        }
    }
}