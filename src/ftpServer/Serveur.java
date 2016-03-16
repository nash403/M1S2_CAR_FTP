package ftpServer;

import java.io.File;
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
		String root;
		if (args.length == 0){
			root = System.getProperty("user.dir") + File.separator + "ftproot";
		}
		else {
			root = args[0];
		}

		ServerSocket serveur;
		try {
			serveur = new ServerSocket(1111);
			System.out.println("[SERVER READY] listening on port 1111 - root folder is : \"./" + root+"\"");

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
