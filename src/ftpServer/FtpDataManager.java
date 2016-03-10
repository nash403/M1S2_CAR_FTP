package ftpServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for managing FTP passive mode
 * 
 * @author honore nintunze and lucas delvallet
 *
 */
public class FtpDataManager extends Thread {
	protected FtpFileManager handler;
	protected ServerSocket serverSock;
	protected Socket clientSock;
	protected BufferedWriter writer;
	protected String arg;
	protected String command;
	public boolean isClosed;

	public FtpDataManager(FtpFileManager handler, Socket client, BufferedWriter writer) {
		init(handler, writer);
		this.clientSock = client;
	}

	public FtpDataManager(FtpFileManager handler, ServerSocket sv, BufferedWriter writer) {
		init(handler, writer);
		this.serverSock = sv;
	}

	protected void init(FtpFileManager handler, BufferedWriter writer) {
		this.handler = handler;
		this.writer = writer;
		this.arg = null;
		this.command = null;
		this.isClosed = false;

	}

	@Override
	public void run() {
		System.out.println("	starting FtpDataManager");
		waitConnexion();
		waitCommand();
		process();
		System.out.println("	closing FtpDataManager");
		this.close();
	}

	protected void process() {
		switch (this.command) {
		case "LIST":
			processLIST();
			break;
		case "STOR":
			processSTOR();
			break;
		case "RETR":
			processRETR();
			break;
		}
	}

	protected void processRETR() {
		System.out.println("    Starting RETR with " + this.arg);
		try {
			File f_to_send = new File(handler.getFullWD() + "/" + this.arg);
			System.out.println("	file to retr " + handler.getFullWD() + "/" + this.arg);
			if (f_to_send.exists()) {
				send(writer, FtpResponse.file_status_ok);
			}
			OutputStream target = clientSock.getOutputStream();
			handler.readFile(f_to_send, target);
			send(writer, FtpResponse.file_action_success);
		} catch (FileNotFoundException e) {
			System.out.println("	-> err STOR No such file");
			send(writer, FtpResponse.no_such_file);
		} catch (IOException e) {
			System.out.println("	-> err STOR IOException");
			System.err.println(e.getMessage());
		}

	}

	protected void processSTOR() {
		System.out.println("    Starting STOR with " + this.arg);
		try {
			File f_to_store = new File(handler.getFullWD() + "/" + this.arg);
			System.out.println("	file to store " + handler.getFullWD() + "/" + this.arg);
			send(writer, FtpResponse.file_status_ok);
			if (!f_to_store.exists()) {
				f_to_store.createNewFile();
			}
			InputStream input = clientSock.getInputStream();
			handler.writeFile(f_to_store, input);
			send(writer, FtpResponse.file_action_success);
		} catch (FileNotFoundException e) {
			System.out.println("	-> err STOR No such file");
			send(writer, FtpResponse.no_such_file);
		} catch (IOException e) {
			System.out.println("	-> err STOR IOException");
			System.err.println(e.getMessage());
		}

	}

	protected void processLIST() {
		System.out.println("    Starting LIST with "+arg);
		send(writer, FtpResponse.file_status_ok);
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
			send(bw, 200 + this.handler.listFiles(arg) + "200 END.");
			send(writer, FtpResponse.file_action_success);
			bw.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public void close() {
		this.isClosed = true;
		try {
			if (clientSock != null)
				this.clientSock.close();
			if (serverSock != null)
				this.serverSock.close();
		} catch (IOException e) {
			System.err.println("	-> Can't close FtpDataManager");
			e.printStackTrace();
		}
	}

	protected void send(BufferedWriter w, String msg) {
		try {
			w.write(msg +"\r\n");
			//w.newLine();
			w.flush();
		} catch (IOException e) {
			System.err.println("	-> can't send message :" + msg);
			e.printStackTrace();
		}
	}

	protected synchronized void waitConnexion() {
		if (clientSock == null)
			try {
				System.out.println("	FtpDataManager waiting for a connexion");
				this.clientSock = this.serverSock.accept();
			} catch (IOException e) {
				System.err.println("	-> Error wait connexion");
				this.close();
				e.printStackTrace();
			}
	}

	protected synchronized void waitCommand() {
		System.out.println("	FtpDataManager waiting for a command");
		try {
			while (command == null) {
				this.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("	stop waiting");
	}

	public synchronized void ask(String command, String arg) {
		System.out.println("	Asking for " + command + " " + arg);
		if (this.command != null || this.arg != null)
			return;
		this.command = command;
		this.arg = arg;
		this.notify();

	}

}
