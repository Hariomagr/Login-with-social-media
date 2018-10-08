package com.example.dellpc.login;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dellpc.login.facebookSignIn.FacebookHelper;
import com.example.dellpc.login.facebookSignIn.FacebookResponse;
import com.example.dellpc.login.facebookSignIn.FacebookUser;
import com.example.dellpc.login.googleAuthSignin.GoogleAuthResponse;
import com.example.dellpc.login.googleAuthSignin.GoogleAuthUser;
import com.example.dellpc.login.googleAuthSignin.GoogleSignInHelper;
import com.example.dellpc.login.googleSignIn.GooglePlusSignInHelper;
import com.example.dellpc.login.googleSignIn.GoogleResponseListener;
import com.example.dellpc.login.linkedInSiginIn.LinkedInHelper;
import com.example.dellpc.login.linkedInSiginIn.LinkedInResponse;
import com.example.dellpc.login.linkedInSiginIn.LinkedInUser;
import com.example.dellpc.login.twitterSignIn.TwitterHelper;
import com.example.dellpc.login.twitterSignIn.TwitterResponse;
import com.example.dellpc.login.twitterSignIn.TwitterUser;
import com.google.android.gms.plus.model.people.Person;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,FacebookResponse,LinkedInResponse,TwitterResponse,GoogleAuthResponse {
    String name="",photo="",email="";
    private FacebookHelper mFbHelper;
    private LinkedInHelper mLinkedInHelper;
    private TwitterHelper mTwitterHelper;
    private GoogleSignInHelper mGAuthHelper;
    ProgressDialog progressDialog;
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.hide();
        //fb api initialization
        mFbHelper = new FacebookHelper(this,
                "id,name,email,gender,birthday,picture,cover",
                this);

        mLinkedInHelper = new LinkedInHelper(this, this);
        //set sign in button
        mTwitterHelper = new TwitterHelper(R.string.twitter_api_key, R.string.twitter_secrate_key,this,this);
        mGAuthHelper = new GoogleSignInHelper(this, null, this);

        findViewById(R.id.bt_act_login_fb).setOnClickListener(this);
        //findViewById(R.id.bt_act_logout_fb).setOnClickListener(this);
        findViewById(R.id.linkedin_login_button).setOnClickListener(this);
        findViewById(R.id.twitter_login_button).setOnClickListener(this);
       // findViewById(R.id.linkedin_logout_button).setOnClickListener(this);
        findViewById(R.id.g_plus_login_btn).setOnClickListener(this);
       // findViewById(R.id.g_plus_logout_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_act_login_fb:
                mFbHelper.performSignIn(this);
                break;
          //  case R.id.bt_act_logout_fb:
          //      mFbHelper.performSignOut();
            //    break;
            case R.id.linkedin_login_button:
                mLinkedInHelper.performSignIn();
                break;
          //  case R.id.linkedin_logout_button:
            //    mLinkedInHelper.logout();
              //  break;
            case R.id.twitter_login_button:
                mTwitterHelper.performSignIn();
                break;
            case R.id.g_plus_login_btn:
                mGAuthHelper.performSignIn(this);
                break;
          //  case R.id.g_plus_logout_btn:
            //    mGHelper.signOut();
              //  break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle results
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        mLinkedInHelper.onActivityResult(requestCode, resultCode, data);
        mTwitterHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onFbSignInFail() {
        Toast.makeText(this, "Facebook sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbSignInSuccess() {
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(MainActivity.this,detail.class);
                i.putExtra("name",name);
                i.putExtra("photo",photo);
                i.putExtra("email",email);
                startActivity(i);
                name="";email="";photo="";
                progressDialog.hide();
                finish();
            }
        },3000);

    }


    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        name=facebookUser.name;
        photo=facebookUser.profilePic;
        email=facebookUser.email;
    }

    @Override
    public void onFBSignOut() {
        Toast.makeText(this, "Facebook sign out success", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLinkedInSignInFail() {
        Toast.makeText(this, "LinkedIn sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkedInSignInSuccess(String accessToken) {
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(MainActivity.this,detail.class);
                i.putExtra("name",name);
                i.putExtra("photo",photo);
                i.putExtra("email",email);
                startActivity(i);
                name="";email="";photo="";
                progressDialog.hide();
                finish();
            }
        },3000);
    }

    @Override
    public void onLinkedInProfileReceived(LinkedInUser user) {
        name=user.name;
        photo=user.pictureUrl;
        email=user.email;
    }
    @Override
    public void onTwitterError() {
        Toast.makeText(this, "Twitter sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTwitterSignIn(@NonNull String userId, @NonNull String userName) {
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(MainActivity.this,detail.class);
                i.putExtra("name",name);
                i.putExtra("photo",photo);
                i.putExtra("email",email);
                startActivity(i);
                name="";email="";photo="";
                progressDialog.hide();
                finish();
            }
        },3000);
    }

    @Override
    public void onTwitterProfileReceived(TwitterUser user) {
        name=user.name;
        photo=user.pictureUrl;
        email=user.description;
    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        name=user.name;
        photo=user.photoUrl.toString();
        email=user.email;
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(MainActivity.this,detail.class);
                i.putExtra("name",name);
                i.putExtra("photo",photo);
                i.putExtra("email",email);
                startActivity(i);
                name="";email="";photo="";
                progressDialog.hide();
                finish();
            }
        },3000);
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        Toast.makeText(this, isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();
    }

}
