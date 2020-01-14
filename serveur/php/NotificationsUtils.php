<?php

class NotificationsUtils
{
	static function envoyerNotification($fields)
	{
		// initialisation :
		$url = "https://fcm.googleapis.com/fcm/send";
		$headers = array(
			"Authorization: key=AAAAytIVjC0:APA91bHbpoZlmBCBi_G7tVOOnN6KKQdRN3XrfPjjaoJre4kDnv7NpBddDySmUH1zqe0I8BPuUr0G1l5OqGv2RZGh2_2Eeqel3y0V1qJsYjmGwrnAOvkU0fG8bYPTeP3UgDkNydcUOhJj",
			"Content-Type: application/json"
		);

		// ouverture de connexion :
		$ch = curl_init();

		// informations pour l'envoi :
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

		// désactivation temporaire du certificat SSL :
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

		// envoi :
		$result = curl_exec($ch);

		// fermeture connexion :
		curl_close($ch);

		return $result; // si false, erreur
	}
}