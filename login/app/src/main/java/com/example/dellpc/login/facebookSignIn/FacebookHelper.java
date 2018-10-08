package com.example.dellpc.login.facebookSignIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FacebookHelper {
    private FacebookResponse mListener;
    private String mFieldString;
    private CallbackManager mCallBackManager;

    public FacebookHelper(@NonNull FacebookResponse responseListener,
                          @NonNull String fieldString,
                          @NonNull Activity context) {
        FacebookSdk.sdkInitialize(context.getApplicationContext());

        //noinspection ConstantConditions
        if (responseListener == null)
            throw new IllegalArgumentException("FacebookResponse listener cannot be null.");

        //noinspection ConstantConditions
        if (fieldString == null) throw new IllegalArgumentException("field string cannot be null.");

        mListener = responseListener;
        mFieldString = fieldString;
        mCallBackManager = CallbackManager.Factory.create();

        //get access token
        FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mListener.onFbSignInSuccess();

                //get the user profile
                getUserProfile(loginResult);
            }

            @Override
            public void onCancel() {
                mListener.onFbSignInFail();
            }

            @Override
            public void onError(FacebookException e) {
                mListener.onFbSignInFail();
            }
        };
        LoginManager.getInstance().registerCallback(mCallBackManager, mCallBack);
    }


    private void getUserProfile(LoginResult loginResult) {
        // App code
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.e("response: ", response + "");
                        try {
                            mListener.onFbProfileReceived(parseResponse(object));
                        } catch (Exception e) {
                            e.printStackTrace();

                            mListener.onFbSignInFail();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", mFieldString);
        request.setParameters(parameters);
        request.executeAsync();
    }


    @NonNull
    @CheckResult
    public CallbackManager getCallbackManager() {
        return mCallBackManager;
    }

    private FacebookUser parseResponse(JSONObject object) throws JSONException {
        FacebookUser user = new FacebookUser();
        user.response = object;

        if (object.has("id")) user.facebookID = object.getString("id");
        if (object.has("email")) user.email = object.getString("email");
        if (object.has("name")) user.name = object.getString("name");
        if (object.has("gender"))
            user.gender = object.getString("gender");
        if (object.has("about")) user.about = object.getString("about");
        if (object.has("bio")) user.bio = object.getString("bio");
        if (object.has("cover"))
            user.coverPicUrl = object.getJSONObject("cover").getString("source");
        if (object.has("picture"))
            user.profilePic = object.getJSONObject("picture").getJSONObject("data").getString("url");
        return user;
    }


    public void performSignIn(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends", "email"));
    }


    public void performSignIn(Fragment fragment) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "user_friends", "email"));
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void performSignOut() {
        LoginManager.getInstance().logOut();
        mListener.onFBSignOut();
    }
}
