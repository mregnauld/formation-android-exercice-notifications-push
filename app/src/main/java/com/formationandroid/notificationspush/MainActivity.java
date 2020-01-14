package com.formationandroid.notificationspush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
{
	
	// Constantes :
	private static final String TAG = MainActivity.class.getSimpleName();
	
	// broadcast receiver :
	private PushBroadcastReceiver pushBroadcastReceiver = null;
	
	// Eléments graphiques :
	private ScrollView scrollView = null;
	private TextView textViewMessages = null;
	private EditText editTextMessage = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// init :
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// broadcast receiver :
		pushBroadcastReceiver = new PushBroadcastReceiver();
		
		// éléments graphiques :
		scrollView = findViewById(R.id.scroll_messages);
		textViewMessages = findViewById(R.id.messages);
		editTextMessage = findViewById(R.id.saisie);
		
		// récupération de la sauvegarde :
		String messages = PreferenceManager.getDefaultSharedPreferences(this).getString(Constantes.SAUVEGARDE, "");
		textViewMessages.setText(messages);
		
		// topic :
		FirebaseMessaging.getInstance().subscribeToTopic(Constantes.TOPIC);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		registerReceiver(pushBroadcastReceiver, new IntentFilter(PushBroadcastReceiver.ACTION));
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		unregisterReceiver(pushBroadcastReceiver);
	}
	
	/**
	 * Action appelée par le broadcast receiver.
	 * @param message Message
	 */
	private void signalerMessage(String message)
	{
		// affichage :
		textViewMessages.append(message + "\n\n");
		
		// enregistrement :
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(Constantes.SAUVEGARDE, textViewMessages.getText().toString());
		editor.apply();
		
		// scroll automatique vers le bas :
		scrollView.post(new Runnable()
		{
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
	
	/**
	 * Listener clic bouton envoyer.
	 * @param view Bouton
	 */
	public void clicEnvoyer(View view)
	{
		// init :
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Constantes.CLE_TOKEN, null);
		
		// vérification :
		if (token != null)
		{
			// client HTTP :
			AsyncHttpClient client = new AsyncHttpClient();
			
			// paramètres :
			RequestParams requestParams = new RequestParams();
			requestParams.put("message", "Matthieu :\n" + editTextMessage.getText().toString().trim());
			requestParams.put("tokenPush", token);
			
			// suppression contenu champ de saisie :
			editTextMessage.setText("");
			
			// appel :
			client.post("http://s519716619.onlinehome.fr/exercices/push/envoyer_message_topic.php", requestParams, new AsyncHttpResponseHandler()
			{
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] response)
				{
					Log.i(TAG, new String(response));
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
				{
					Log.e(TAG, e.toString());
				}
			});
		}
		else
		{
			Toast.makeText(this, R.string.libelle_token_non_recu, Toast.LENGTH_LONG).show();
		}
	}
	
	
	/**
	 * Broadcast receiver pour réception des notifications push.
	 */
	public class PushBroadcastReceiver extends BroadcastReceiver
	{
		
		// Constantes :
		public static final String ACTION = "com.monentreprise.NOTIFICATION_MESSAGE";
		public static final String EXTRA_MESSAGE = "ExtraMessage";
		
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.hasExtra(EXTRA_MESSAGE))
			{
				signalerMessage(intent.getStringExtra(EXTRA_MESSAGE));
			}
		}
		
	}
	
}
