package ftpServer;

/**
 * Class containing all the ftp responses that the server may use to dialog
 * with client
 * 
 * @author honore nintunze and lucas delvallet
 *
 */
public class FtpResponse {

	public static final String connect_ok = 220 + " Ok.";
	public static final String bye = 221 + " Service closing control connection.";

	public static final String user_ok = 331 + " User name okay, need password.";
	public static final String user_ko = 332 + " Need account for login.";
	public static final String user_already_in = 531 + " User already logged in.";

	public static final String pass_ok = 230 + " User logged in, proceed.";
	public static final String pass_invalid = 530 + " Not logged in.";
	public static final String pass_no_user = 530 + " Invalid user or password.";

	public static final String syst_type = 215 + " UNIX Type: I";
	public static final String type_I = 200 + " Command ok.";
	public static final String file_status_ok = 150
			+ " File status okay; about to open data connection and start transfert.";
	public static final String file_action_success = 226 + " Closing data connection. Requested file action successful";
	public static final String transfer_aborted = 426 + " Connection closed; transfer aborted.";
	public static final String no_such_file = 550 + " No such file or directory";
	public static final String denied_access = 550 + " Access denied.";
	public static final String no_command = 500 + " Syntax error, command unrecognized.";
	public static final String arg_error = 501 + " Parameter or Argument error.";
	public static final String entering_pm = 227 + " Entering Passive Mode";
	public static final String cwd_ok = 250 + " Okay.";
	public static final String connect_error = 10061
			+ " Cannot connect to remote server due to error. The connection is actively refused by the server.";
	public static String connect_refused = 10061
			+ " Refused to connect to remote server. The connection is actively refused by the server.";
	public static final String connect_denied = 421 + " You are not authorized to make the connection.";
	public static String port_ok = 200 + " Port command successful";
}
