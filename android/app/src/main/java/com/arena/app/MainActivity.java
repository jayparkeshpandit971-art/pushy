package com.arena.app;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FCM Token fetch karo aur WebView ko bhejo
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String token = task.getResult();
                    runOnUiThread(() -> {
                        getBridge().getWebView().evaluateJavascript(
                            "window._nativeFcmToken = '" + token + "';" +
                            "if(typeof window._onFcmToken === 'function') { window._onFcmToken('" + token + "'); }",
                            null
                        );
                    });
                }
            });
    }
}
