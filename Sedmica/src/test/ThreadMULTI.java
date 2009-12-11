package test;

import projekt.java.klase.Potez;
import projekt.java.klase.Soba;
import projekt.java.server.DretvaPartija;
public class ThreadMULTI {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Soba soba = new Soba(1);
		soba.getListaPoteza().add(new Potez());
		soba.getListaPoteza().add(new Potez());
		soba.getListaPoteza().add(new Potez());
		
		
		Thread partija = new Thread ( new DretvaPartija(soba) );
		//partija.setDaemon(true);
		partija.start();
		/*
		Thread.sleep(5000);
		soba.getListaPoteza().add(new Potez());
		*/
	}

}
