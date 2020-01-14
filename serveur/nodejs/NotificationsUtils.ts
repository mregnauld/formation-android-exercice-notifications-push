
/**
 * Classe d'envoi de notifications push en NodeJS.
 * Pour initialiser le projet côté serveur : https://firebase.google.com/docs/admin/setup
 */
export class NotificationsUtils
{
	
	// Firebase admin :
	private readonly admin: any;
	
	
	/**
	 * Constructeur. Initialisation de firebase pour l'envoi de notifications push.
	 */
	public constructor()
	{
		this.admin = require('firebase-admin');
		const serviceAccount = require("/chemin/vers/firebase-account.json");
		this.admin.initializeApp({
			credential: this.admin.credential.cert(serviceAccount),
			databaseURL: "https://mon-projet.firebaseio.com"
		});
	}
	
	/**
	 * Envoi d'un message à un topic.
	 * @param topic Topic
	 * @param messagePush Message au format JSON
	 */
	public async envoyerNotificationTopic(topic: string, messagePush: any): Promise<void>
	{
		const promise = new Promise((resolve, reject) =>
		{
			const message = {
				data: { messagePush: JSON.stringify(messagePush) },
				topic: topic
			};
			this.admin.messaging().send(message)
				.then((response) => { resolve(); })
				.catch((error) => { reject(error); });
		});
		await promise
			.then(() => { console.log("NOTIFICATION PUSH OK : " + JSON.stringify(messagePush)); })
			.catch((error) => { console.error("ERREUR ENVOI NOTIFICATION PUSH TOPIC : " + topic + ", MESSAGE PUSH : " + JSON.stringify(messagePush) + ", " + error); });
	}
	
	/**
	 * Envoi d'un message à un token.
	 * @param token Token push
	 * @param messagePush Message au format JSON
	 */
	public async envoyerNotificationToken(token: string, messagePush: any): Promise<void>
	{
		const promise = new Promise((resolve, reject) =>
		{
			const message = {
				data: { messagePush: JSON.stringify(messagePush) },
				token: token
			};
			this.admin.messaging().send(message)
				.then((response) => { resolve(); })
				.catch((error) => { reject(error); });
		});
		await promise
			.then(() => { console.log("NOTIFICATION PUSH OK : " + JSON.stringify(messagePush)); })
			.catch((error) => { console.error("ERREUR ENVOI NOTIFICATION PUSH TOKEN : " + token + ", MESSAGE PUSH : " + JSON.stringify(messagePush) + ", " + error); });
	}
	
}