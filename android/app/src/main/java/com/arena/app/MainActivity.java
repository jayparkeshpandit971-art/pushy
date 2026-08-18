package com.arena.app;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String token = task.getResult();
                getBridge().getWebView().evaluateJavascript(
                    "window._nativeFcmToken = '" + token + "'; " +
                    "if(window._onFcmToken) window._onFcmToken('" + token + "');",
                    null
                );
            }
        });
    }
}
