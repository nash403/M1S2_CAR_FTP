package test;

import ftpServer.FtpRequest;

public class StubFtpRequest extends FtpRequest {

	@Override
	protected void processUSER(String msg) {
		
	}
	
	@Override
	protected void processPASS(String msg) {
		
	}
	
	@Override
	protected void processPWD() {
		
	}
	
	@Override
	protected void processLIST(String msg) {
		
	}
	
	@Override
	protected void processRETR(String msg) {
		
	}
	
	@Override
	protected void processSTOR(String msg) {
		
	}
	
	@Override
	protected void processQUIT() {
		
	}
	
	@Override
	protected void processPORT(String msg) {
		
	}
	
	@Override
	protected void processPASV() {
		
	}
	
	@Override
	protected void processTYPEI() {
		
	}
	
	@Override
	protected void processSIZE(String msg) {
		
	}
	
	@Override
	protected void processCDUP() {
		
	}
	
	@Override
	protected void processCWD(String msg) {
		
	}
	
	@Override
	protected void envoie(String msg) {
		
	}
	
	public boolean processRequest(String[] commands){
		return super.processRequest(commands);
	}
}
