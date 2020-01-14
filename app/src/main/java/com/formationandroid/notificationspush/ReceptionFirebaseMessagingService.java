package com.formationandroid.notificationspush;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;

public class ReceptionFirebaseMessagingService extends FirebaseMessagingService
{
	
	// Constantes :
	private static final String TAG = ReceptionFirebaseMessagingService.class.getSimpleName();
	
	
	@Override
	public void onNewToken(@NonNull String token)
	{
		// enregistrement du nouveau token en shared preferences :
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Constantes.CLE_TOKEN, token);
		editor.apply();
	}
	
	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
	{
		// logs :
		Log.i(TAG, "Expéditeur : " + remoteMessage.getFrom());
		if (remoteMessage.getNotification() != null)
		{
			Log.i(TAG, "Titre : " + remoteMessage.getNotification().getTitle());
			Log.i(TAG, "Contenu : " + remoteMessage.getNotification().getBody());
		}
		Log.i(TAG, "Data : " + remoteMessage.getData());
		Log.i(TAG, "Timestamp : " + remoteMessage.getSentTime());
		
		// message reçu :
		String message = remoteMessage.getData().get("message");
		
		// détection de l'expéditeur :
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Constantes.CLE_TOKEN, null);
		if (token != null && token.equals(remoteMessage.getData().get("tokenPush")))
		{
			message = "      " + message;
		}
		
		// envoi du message vers le broadcast receiver, qui enverra à son tour à l'activité :
		Intent intent = new Intent(MainActivity.PushBroadcastReceiver.ACTION);
		intent.putExtra(MainActivity.PushBroadcastReceiver.EXTRA_MESSAGE, message);
		sendBroadcast(intent);
	}
	
}
