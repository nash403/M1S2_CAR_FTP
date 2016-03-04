package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ftpServer.FtpFileManager;
import ftpServer.FtpResponse;

public class TestFtpFileManager {

	@Test
	public void testGetWD() {
		try {
			FtpFileManager FFM = new FtpFileManager("", "");
			assertEquals("ftproot", FFM.getWD());
		} catch (IOException e) {
			// Not supposed to happen
		}
	}

	@Test
	public void testGetFullWD() {
		try {
			FtpFileManager FFM = new FtpFileManager("", "");
			assertEquals(new java.io.File("").getAbsolutePath() + "/ftproot",
					FFM.getFullWD());
		} catch (IOException e) {
			// Not supposed to happen
		}
	}

	@Test
	public void testChangeWDIfFolderExist() {
		try {
			FtpFileManager FFM = new FtpFileManager("", "");
			String testFolder = "testDir";
			File dir = new File(new java.io.File("").getAbsolutePath()
					+ "/ftproot/" + testFolder);
			dir.mkdir();

			assertEquals(FtpResponse.cwd_ok, FFM.changeWD(testFolder));
			dir.delete();

		} catch (IOException e) {
			// Not supposed to happen
		}
	}

	@Test
	public void testChangeWDIfNavigationWork() {
		try {
			FtpFileManager FFM = new FtpFileManager("", "");
			String testFolder = "testDir";
			File dir = new File(new java.io.File("").getAbsolutePath()
					+ "/ftproot/" + testFolder);
			dir.mkdir();
			FFM.changeWD(testFolder);

			assertEquals(testFolder, FFM.getWD());
			dir.delete();

		} catch (IOException e) {
			// Not supposed to happen
		}
	}

	@Test
	public void testChangeWDIfFolderDontExist() {
		try {
			FtpFileManager FFM = new FtpFileManager("", "");
			String testFolder = "UnexistentDir";

			assertEquals(FtpResponse.no_such_file + "ftproot/" + testFolder,
					FFM.changeWD(testFolder));

		} catch (IOException e) {
			// Not supposed to happen
		}
	}

}
