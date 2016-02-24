package test;

import static org.junit.Assert.*;

import org.junit.Test;

import ftpServer.FtpRequest;

public class TestFtpRequest{

	@Test
	public void testProcessRequestUSER() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"USER", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestPASS() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"PASS", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestPWD() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"PWD", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestLIST() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"LIST", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestRETR() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"RETR", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestSTOR() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"STOR", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestQUIT() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"QUIT", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestPASV() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"PASV", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestPORT() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"PORT", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestTYPEI() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"TYPE", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestCWD() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"CWD", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestCDUP() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"CDUP", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestSIZE() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] recognized_command = {"SIZE", "message"};
		assertTrue(stubFtpRequest.processRequest(recognized_command));
	}
	
	@Test
	public void testProcessRequestWithoutArgument() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] unrecognized_command = {};
		assertFalse(stubFtpRequest.processRequest(unrecognized_command));
	}
	
	@Test
	public void testProcessRequestWithOneWrongArgument() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] unrecognized_command = {"ABCD"};
		assertFalse(stubFtpRequest.processRequest(unrecognized_command));
	}
	
	@Test
	public void testProcessRequestWithTwoWrongArgument() {
		StubFtpRequest stubFtpRequest = new StubFtpRequest();
		String[] unrecognized_command = {"ABCD", "message"};
		assertFalse(stubFtpRequest.processRequest(unrecognized_command));
	}
}

