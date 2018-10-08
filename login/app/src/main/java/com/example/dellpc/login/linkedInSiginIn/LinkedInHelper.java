package com.example.dellpc.login.linkedInSiginIn;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

public class LinkedInHelper {
    private Activity mContext;
    private LinkedInResponse mListener;
    private LISessionManager mSessionManager;
    private static final String HOST = "api.linkedin.com";
    private static final String USER_DETAIL_URL = "https://" + HOST + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private AuthListener mAuthListener = new AuthListener() {
        @Override
        public void onAuthSuccess() {
            mListener.onLinkedInSignInSuccess(mSessionManager.getSession().getAccessToken() + "");

            //get the user data
            getUserData();
        }

        @Override
        public void onAuthError(LIAuthError error) {
            // Handle authentication errors
            mListener.onLinkedInSignInFail();
        }
    };

    public LinkedInHelper(@NonNull Activity context, @NonNull LinkedInResponse listener) {
        mContext = context;
        mListener = listener;
        mSessionManager = LISessionManager.getInstance(mContext.getApplicationContext());
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void performSignIn() {
        mSessionManager.init(mContext, buildScope(), mAuthListener, true);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSessionManager.onActivityResult(mContext, requestCode, resultCode, data);
    }

    public void logout() {
        if (mSessionManager != null) mSessionManager.clearSession();
    }

    private void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(mContext.getApplicationContext());
        apiHelper.getRequest(mContext, USER_DETAIL_URL, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    Log.d("response", result.getResponseDataAsString());
                    mListener.onLinkedInProfileReceived(parseUserResponse(result.getResponseDataAsJson()));
                } catch (Exception e) {
                    e.printStackTrace();
                    mListener.onLinkedInSignInFail();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                mListener.onLinkedInSignInFail();
            }
        });
    }

    @NonNull
    private LinkedInUser parseUserResponse(JSONObject response) throws JSONException {
        LinkedInUser user = new LinkedInUser();
        user.email = response.getString("emailAddress");
        user.name = response.getString("formattedName");
        user.pictureUrl = response.getString("pictureUrl");
//        user.phone = response.getString("emailAddress");
        return user;
    }
}
