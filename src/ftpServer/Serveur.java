package ftpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class containing the main method
 * 
 * @author nintunze et delvallet
 * 
 */
public class Serveur {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		AuthHandler authHandler = new AuthHandler();
		authHandler.addIdentifiant("Bobby", "123");
		authHandler.addIdentifiant("Pierre-Richard-Alexandre", "azertyui");
		String root = args[0];

		ServerSocket serveur;
		try {
			serveur = new ServerSocket(1111);
			System.out.println("démarrage du serveur sur le port 1111, dossier root: " + root);

			while (true) {
				Socket client = serveur.accept();
				System.out.println("Nouveau client: " + client.getInetAddress());
				FtpRequest ftpRequest = new FtpRequest();
				ftpRequest.init(client, root, authHandler);
				ftpRequest.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();

		}

	}
}
