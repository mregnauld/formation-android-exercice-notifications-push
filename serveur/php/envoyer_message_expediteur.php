<?php

require_once("NotificationsUtils.php");

// lecture informations :
$message = $_POST["message"];
$tokenPush = $_POST["tokenPush"];

// envoi de notification à un utilisateur unique identifié par son token:
$fields = array(
	'to' => $tokenPush,
	'data' => array("message" => $message, "tokenPush" => $tokenPush));
$result = NotificationsUtils::envoyerNotification($fields);

echo "retour = " . $result;