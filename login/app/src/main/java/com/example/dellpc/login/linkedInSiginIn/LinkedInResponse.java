package com.example.dellpc.login.linkedInSiginIn;

public interface LinkedInResponse {


    void onLinkedInSignInFail();


    void onLinkedInSignInSuccess(String accessToken);

    void onLinkedInProfileReceived(LinkedInUser user);
}
