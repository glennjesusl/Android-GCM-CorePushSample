
package com.coreasp.corepushsample;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gcm.GCMRegistrar;

public class NotificationActivity extends Activity {
	
    /**
     * 画面(ビュー)を設定
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // デバイスが利用可能か確認
        GCMRegistrar.checkDevice(this);
        // マニフェストの設定が正しいか確認
        GCMRegistrar.checkManifest(this);
        
        // 解除
        removeToken();
        // 登録
        registToken();
    }
    
    /**
     * GCMへのデバイストークン登録
     */
    private void registToken() {
        // registration ID（デバイストークン）取得して未登録の場合GCMへ登録する
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
    }
    
    /**
     * GCMへのデバイストークン削除
     */
    private void removeToken() {
        if (GCMRegistrar.isRegistered(this)) {
        	GCMRegistrar.unregister(this);
        }
    }
    
    
}




