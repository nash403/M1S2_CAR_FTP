package ftpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for managing file operation such as listing a directory, read or write
 * a file, etc.
 * 
 * @author honore nintunze and lucas delvallet
 *
 */
public class FtpFileManager {

	protected Path workingDir;
	protected File root;

	public FtpFileManager(String clientId, String root) throws IOException {
		this.root = new File(root);
		this.workingDir = Paths.get(root);//"ftproot/");
		Files.createDirectories(this.workingDir);

	}

	public String getWD() {
		return this.workingDir.toFile().getName();
	}

	public String getFullWD() {
		return this.workingDir.toFile().getAbsolutePath();
	}

	public String listFiles(String path) {
		path = path == null ? "" : path;
		File file_to_test = new File(
				transformPath((new File(this.workingDir.toString() + "/" + path)).getAbsolutePath()));
		File res = file_to_test.exists() && isChildOfRoot(file_to_test.getAbsolutePath())
				? new File(this.workingDir.toString() + "/" + path) : this.root;
		File files_in_dir[] = res.listFiles();
		System.out.println("listfiles "+res+" - "+(this.workingDir.toString() + "/" + path)+" - "+files_in_dir.length);
		String message = " Directory /" + res.getName() + ":\n ";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		for (File file : files_in_dir) {
			System.out.println("A file "+file);
			message += (file.isDirectory() ? file.getName() + "/" : file.getName()) + "\t"
					+ dateFormat.format(new Date(file.lastModified()))
					+ (file.isFile() ? "\t" + file.length() + " octets" : "") + "\n ";
		}
		message = message.substring(0, message.length() - " ".length());
		return message;
	}

	public String changeWD(String new_dir) {
		File dir_to_test = new File(
				transformPath((new File(this.workingDir.toString() + "/" + new_dir)).getAbsolutePath()));
		if (dir_to_test.exists() && dir_to_test.isDirectory()) {
			if (!isChildOfRoot(dir_to_test.getAbsolutePath())) {
				return FtpResponse.denied_access + "" + this.workingDir.toFile().getName() + "/" + new_dir;
			} else {
				this.workingDir = dir_to_test.toPath();
				System.out.println("cwd -> " + workingDir.toString());
				return FtpResponse.cwd_ok;
			}
		} else {
			return FtpResponse.no_such_file + "" + this.workingDir.toString() + "/" + new_dir;
		}
	}

	public void writeFile(File file, InputStream instream) throws FileNotFoundException {

		if (file.exists() && file.isFile())
			try {
				OutputStream outstream = new FileOutputStream(file);

				while (instream.available() > 0)
					outstream.write(instream.read());

				outstream.close();

			} catch (IOException e) {
				System.err.println("Error while reading file " + file.getName());
			}
		else
			throw new FileNotFoundException();

	}

	public void readFile(File file, OutputStream outtream) throws FileNotFoundException {

		if (file.exists() && file.isFile())
			try {
				InputStream instream = new FileInputStream(file);

				while (instream.available() > 0)
					outtream.write(instream.read());

				instream.close();

			} catch (IOException e) {
				System.err.println("Error while reading file " + file.getName());
			}
		else
			throw new FileNotFoundException();
	}

	public long getFileSize(String file) throws IOException {

		return Files.size(Paths.get(this.workingDir.toAbsolutePath() + "/" + file));
	}

	protected boolean isChildOfRoot(String child) {
		String parent = this.root.getAbsolutePath();
		return child.startsWith(parent);
	}

	protected String transformPath(String path) {
		String res = path, old = "";
		while (res.contains("..") && !res.equals(old)) {
			old = res;
			res = res.replaceFirst("/[^/]*/[.][.]", "");
		}
		return res;
	}

}
