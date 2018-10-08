package com.example.dellpc.login.googleSignIn;

import com.google.android.gms.plus.model.people.Person;

public interface GoogleResponseListener {
    void onGSignInFail();

    void onGSignInSuccess(Person personData);
}
