<?php

require_once("NotificationsUtils.php");

// lecture informations :
$message = $_POST["message"];
$tokenPush = $_POST["tokenPush"];

// envoi de notification à un topic:
$fields = array(
	'to' => '/topics/discussion',
	'data' => array("message" => $message, "tokenPush" => $tokenPush));
$result = NotificationsUtils::envoyerNotification($fields);

echo "retour = " . $result;