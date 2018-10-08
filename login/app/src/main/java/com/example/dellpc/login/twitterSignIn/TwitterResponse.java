package com.example.dellpc.login.twitterSignIn;

import android.support.annotation.NonNull;

public interface TwitterResponse {

    void onTwitterError();

    void onTwitterSignIn(@NonNull String userId, @NonNull String userName);

    void onTwitterProfileReceived(TwitterUser user);
}
