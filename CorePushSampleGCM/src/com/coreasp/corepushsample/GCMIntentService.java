package com.coreasp.corepushsample;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	/** ログのタグ */
	private static final String LOG_TAG = "corepushsample";
    /** CORE PUSHから送られてくるデータ */
	private static final String COREPUSH_DATA_TITLE = "title";
	private static final String COREPUSH_DATA_MESSAGE = "message";
	
	// TODO この２カ所を修正
	private static String COREPUSH_CONFIG_KEY = "XXXXXXXXXXXXXXXXX"; //config_keyパタメータの値。設定キー
	protected static final String SENDER_ID = "1234567890"; // GCMのproject ID（sender ID）
	
    
	/**
	 * デバイストークンの登録・削除を行うクラス
	 */
    public GCMIntentService() {
        super(SENDER_ID);
    }

    protected void onRegistered(Context context, String registrationId) {
		// デバイストークンの登録成功時の動作を記述
    	
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("config_key", COREPUSH_CONFIG_KEY)); //config_keyパラメータ(必須)。設定キー。
		nameValuePair.add(new BasicNameValuePair("device_token", registrationId)); //device_tokenパラメータ(必須)。デバイストークン(通知送信用のID)。
		nameValuePair.add(new BasicNameValuePair("device_id", "1")); //device_idパラメータ。UDID(初期値:1)。
		nameValuePair.add(new BasicNameValuePair("category_id", "1")); //category_idパラメータ。カテゴリID。2桁の整数の配列(初期値:1)。
		nameValuePair.add(new BasicNameValuePair("mode", "1")); //modeパラメータ。デバイストークン(登録:1/削除:2)
    	JSONObject data;
    	URI uri;
		try {
			String jsonText = "";
			uri = new URI("http://api.core-asp.com/android_token_regist.php");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(uri);
            
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse response = httpclient.execute(httpPost);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			response.getEntity().writeTo(byteArrayOutputStream);
            
			jsonText += byteArrayOutputStream.toString();
	        data = new JSONObject(jsonText);
		} catch (Exception e) {
			return;
		}
    	int status;
		try {
			status = data.getInt("status");
		} catch (JSONException e) {
			return;
		}
		Log.i(LOG_TAG, "デバイストークン登録結果 status:"+status); // 0:成功
    	
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
		// デバイストークンの削除成功時の動作を記述
    	
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("config_key", COREPUSH_CONFIG_KEY)); //config_keyパラメータ(必須)。設定キー。
		nameValuePair.add(new BasicNameValuePair("device_token", registrationId)); //device_tokenパラメータ(必須)。デバイストークン(通知送信用のID)。
		nameValuePair.add(new BasicNameValuePair("device_id", "1")); //device_idパラメータ。UDID(初期値:1)。
		nameValuePair.add(new BasicNameValuePair("category_id", "1")); //category_idパラメータ。カテゴリID。2桁の整数の配列(初期値:1)。
		nameValuePair.add(new BasicNameValuePair("mode", "2")); //modeパラメータ。デバイストークン(登録:1/削除:2)
    	JSONObject data;
    	URI uri;
		try {
			String jsonText = "";
			uri = new URI("http://api.core-asp.com/android_token_regist.php");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(uri);
            
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse response = httpclient.execute(httpPost);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			response.getEntity().writeTo(byteArrayOutputStream);
            
			jsonText += byteArrayOutputStream.toString();
	        data = new JSONObject(jsonText);
		} catch (Exception e) {
			return;
		}
    	int status;
		try {
			status = data.getInt("status");
		} catch (JSONException e) {
			return;
		}
		Log.i(LOG_TAG, "デバイストークン登録結果 status:"+status); // 0:成功
		
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
	// メッセージを受信した時の動作を記述
        String title = intent.getStringExtra(COREPUSH_DATA_TITLE);
        String message = intent.getStringExtra(COREPUSH_DATA_MESSAGE);

        NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
	Notification notification = new Notification(R.drawable.ic_launcher,message, System.currentTimeMillis());
	Intent notificationIntent = new Intent(context, NotificationActivity.class);
	PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	notification.setLatestEventInfo(context, title, message, pi);
	notificationManager.notify(0, notification);
        Log.i(LOG_TAG, title+":"+message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
    	// メッセージの削除を記述
    }

    @Override
    public void onError(Context context, String errorId) {
		// エラー時の動作を記述
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
    	return super.onRecoverableError(context, errorId);
    }	
	
}
