package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import ftpServer.AuthHandler;


public class TestAuthHandler extends AuthHandler {

	@Test
	public void testAddIdentifiant() {
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		this.addIdentifiant(id, password);
		assertEquals(1, this.clients.size());
		assertEquals(1, this.clientsAuth.size());
	}
	
	@Test
	public void testUserExist(){
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		
		assertFalse(this.userExist(id));
		clients.put(id, password);
		assertTrue(this.userExist(id));
	}
	
	@Test
	public void testConnectIfPasswordCorrect(){
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		this.addIdentifiant(id, password);
		assertTrue(connect(id, password));
		assertTrue(clientsAuth.get(id));
	}
	
	@Test
	public void testConnectIfPasswordNotCorrect(){
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		this.addIdentifiant(id, password);
		assertFalse(connect(id, "Wrong password"));
		assertFalse(clientsAuth.get(id));
	}
	
	@Test
	public void testisAuthenticatedTrue(){
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		this.addIdentifiant(id, password);
		connect(id, password);
		assertTrue(isAuthenticated(id));
	}
	
	@Test
	public void testisAuthenticatedFalse(){
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		this.addIdentifiant(id, password);
		assertFalse(isAuthenticated(id));
	}
	
	@Test
	public void testDisconnectIfIdTrue(){
		String id = "Identifiant";
		String password = "Password";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		this.addIdentifiant(id, password);
		assertTrue(disconnect(id));
	}
	
	@Test
	public void testDisconnectIfIdFalse(){
		String id = "Identifiant";
		
		this.clients = new HashMap<String, String>();
		this.clientsAuth = new HashMap<String, Boolean>();
		
		assertFalse(disconnect(id));
	}
}
