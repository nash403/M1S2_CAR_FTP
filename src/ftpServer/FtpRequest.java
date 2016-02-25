package ftpServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for processing request incoming from a client
 * 
 * @author honore nintunze and lucas delvallet
 *
 */
public class FtpRequest extends Thread {

	protected Socket client = null;
	protected String clientId = "";
	protected BufferedReader reader;
	protected BufferedWriter writer;
	protected String root = null;
	protected AuthHandler authHandler;
	protected FtpDataManager dataManager;
	protected FtpFilemanager fileOperationManager;
	protected boolean islistening;

	public FtpRequest(Socket client, String root, AuthHandler authHandler) throws IOException {
		this.init(client, root, authHandler);
	}

	public FtpRequest() {
	}

	public void init(Socket client, String root, AuthHandler authHandler) throws IOException {
		this.client = client;
		this.root = root;
		this.authHandler = authHandler;
		this.islistening = true;
		reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
	}

	@Override
	public void run() {
		envoie(FtpResponse.connect_ok);
		String new_request = "";
		while (this.islistening) {
			try {
				if ((new_request = this.reader.readLine()) != null) {
					System.out.println("Requete reçu " + new_request);
					processRequest(new_request.split(" "));
				}
			} catch (IOException e) {
				System.err.println("Can't process request");
				e.printStackTrace();
			}
		}
		this.kill();
	}

	protected boolean processRequest(String[] commands) {
		if (commands.length > 0) {
			switch (commands[0]) {
			case "USER":
				if (commands.length == 2) {
					processUSER(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter username.");
					return false;
				}
				break;
			case "PASS":
				if (commands.length == 2) {
					processPASS(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter password.");
					return false;
				}
				break;
			case "PWD":
				processPWD();
				break;
			case "CWD":
				if (commands.length == 2) {
					processCWD(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter a directory name.");
					return false;
				}
				break;
			case "CDUP":
				processCDUP();
				break;
			case "LIST":
				processLIST(commands.length == 2 ? commands[1] : null);
				break;
			case "RETR":
				if (commands.length == 2) {
					processRETR(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter a file name.");
					return false;
				}
				break;
			case "STOR":
				if (commands.length == 2) {
					processSTOR(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter a file name.");
					return false;
				}
				break;
			case "QUIT":
				processQUIT();
				break;
			case "PASV":
				processPASV();
				break;
			case "PORT":
				if (commands.length == 2) {
					processPORT(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter a port to connect to > ip1,ip2,ip3,ip4,port1,port2");
					return false;
				}
				break;
			case "SIZE":
				if (commands.length == 2) {
					processSIZE(commands[1]);
				} else {
					envoie(FtpResponse.arg_error + " Enter a file name.");
					return false;
				}
				break;
			case "TYPE":
				processTYPEI();
				break;
			default:
				envoie(FtpResponse.no_command);
				return false;
			}
			return true;
		} else {
			envoie(FtpResponse.no_command);
			return false;
		}
	}

	protected void processSIZE(String file) {
		System.out.println("    Process SIZE");
		try {
			envoie(200 + " " + fileOperationManager.getFileSize(file));
		} catch (IOException e) {
			System.err.println("Can't get file size");
			e.printStackTrace();
		}
	}

	protected void processPWD() {
		System.out.println("    Process PWD");

		if (fileOperationManager != null)
			envoie(257 + " \"" + fileOperationManager.getWD() + "\"");
		else
			envoie(FtpResponse.user_ko);
	}

	protected void processCWD(String dir) {
		envoie(this.fileOperationManager.changeWD(dir));
	}

	protected void processCDUP() {
		processCWD("..");
	}

	protected void processUSER(String msg) {
		System.out.println("	Process USER : " + msg);
		if (authHandler.userExist(msg)) {
			clientId = msg;
			envoie(FtpResponse.user_ok);
		} else {
			envoie(FtpResponse.user_ko);
		}
	}

	protected void processPASS(String pass) {
		System.out.println("	Process PASS : " + pass + " | for user " + clientId);
		if ((this.fileOperationManager != null) || authHandler.isAuthenticated(clientId)) {
			envoie(FtpResponse.user_already_in);
		} else if (authHandler.connect(clientId, pass)) {
			initSession();
		} else {
			envoie(FtpResponse.pass_invalid);
		}

	}

	protected void initSession() {
		try {
			this.fileOperationManager = new FtpFilemanager(clientId, this.root);
			envoie(FtpResponse.pass_ok);
		} catch (IOException e) {
			System.err.println("unable to access the file system");
			envoie(FtpResponse.pass_invalid);
		}

	}

	protected void processRETR(String file) {
		System.out.println("    Process RETR");
		if (dataManager != null && !dataManager.isClosed)
			this.dataManager.ask("RETR", file);
		else
			envoie(FtpResponse.user_ko);

	}

	protected void processSTOR(String file) {
		System.out.println("    Process STOR");
		if (dataManager != null && !dataManager.isClosed)
			this.dataManager.ask("STOR", file);
		else
			envoie(FtpResponse.user_ko);
	}

	protected void processLIST(String dir) {
		System.out.println("    Process LIST");
		if (dataManager != null && !dataManager.isClosed)
			this.dataManager.ask("LIST", dir);
		else
			envoie(FtpResponse.user_ko);
	}

	protected void processQUIT() {
		System.out.println("	Process QUIT");
		authHandler.disconnect(clientId);
		this.islistening = false;
	}

	protected void processTYPEI() {
		envoie(200 + " Command OK");
	}

	protected void processPORT(String localPort) {
		String[] parsedInfo = localPort.split(",");
		if (parsedInfo.length == 6) {
			String ip1 = parsedInfo[0], ip2 = parsedInfo[1], ip3 = parsedInfo[2], ip4 = parsedInfo[3],
					p1 = parsedInfo[4], p2 = parsedInfo[5];

			if (dataManager == null || dataManager.isClosed) {
				try {
					String addrIP = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
					int port = Integer.parseInt(p1) * 256 + Integer.parseInt(p2);
					Socket s = new Socket(addrIP, port);
					this.dataManager = new FtpDataManager(fileOperationManager, s, writer);
					this.dataManager.start();
					envoie(FtpResponse.port_ok);
				} catch (IOException e) {
					envoie(FtpResponse.connect_refused);
				}
			} else
				envoie(FtpResponse.connect_denied);
		} else {
			envoie(FtpResponse.arg_error + " Enter a port to connect to > ip1,ip2,ip3,ip4,port1,port2");
		}
	}

	protected void processPASV() {
		System.out.println("	Process PASV");
		if (this.dataManager == null || dataManager.isClosed) {

			try {
				ServerSocket dataServerSocket = new ServerSocket(5654);

				envoie(FtpResponse.entering_pm + " "
						+ formatAddr(dataServerSocket.getInetAddress().getAddress(), dataServerSocket.getLocalPort()));

				this.dataManager = new FtpDataManager(fileOperationManager, dataServerSocket, writer);
				this.dataManager.start();
			} catch (IOException e) {
				envoie(FtpResponse.connect_error);
				e.printStackTrace();
			}
		} else {
			envoie(FtpResponse.connect_denied);
		}
	}

	protected String formatAddr(byte[] address, int localPort) {
		String str = "(";
		for (int i : address) {
			str += i + ",";
		}
		str += localPort / 256 + ",";
		str += localPort % 256;
		str += ')';
		return str;
	}

	protected void kill() {
		try {
			envoie(FtpResponse.bye);
			if (this.writer != null)
				this.writer.close();
			if (this.reader != null)
				this.reader.close();
			if (this.client != null)
				this.client.close();
			if (this.dataManager != null)
				this.dataManager.close();

		} catch (IOException e) {
			System.err.println("Unable to close FtpRequest");
			e.printStackTrace();
		}
		System.out.println("FtpRequest is now closed");
	}

	protected void envoie(String msg) {
		try {
			System.out.println("	Message sent to client: " + msg);
			writer.write(msg);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			System.out.println("Can't send message " + msg + ": broken pipe.");
		}
	}
}
