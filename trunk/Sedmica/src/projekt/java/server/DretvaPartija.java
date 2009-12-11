package projekt.java.server;


import java.util.ArrayList;
import java.util.LinkedList;

import projekt.java.klase.Igrac;
import projekt.java.klase.Potez;
import projekt.java.klase.Soba;

/**
 * Razred DretvaPartija brine se za sve oko partije
 * @author Mario
 *
 */
public class DretvaPartija implements Runnable {
	/** Soba koju æe dretva obraðivati */
	volatile Soba soba;
	//LinkedListe koje sluz za razmjenu poruka izmedu dretvi.
	volatile LinkedList<Potez> dolazniFIFO;
	//Igraci koji su u igri
	ArrayList<Igrac> popisIgraca;
	
	
	public DretvaPartija(Soba soba) {
		this.soba = soba;
		this.dolazniFIFO = soba.getListaPoteza();
	}

	private volatile boolean threadSuspended;


	public synchronized void test(){
		dolazniFIFO = soba.getListaPoteza();
		System.out.println("TEst ušao");
		
		threadSuspended = dolazniFIFO.isEmpty();
		//System.out.println("DretvaPartija: -> " + dolazniFIFO.getFirst());
        threadSuspended = !threadSuspended;

        if (!threadSuspended)
            notify();

	}

	private Object mutex = new Object();
	
	public void brodcast(){
		
	}
	
	
	/*
		while( true ){
			try {
			if( !dolazniFIFO.isEmpty()){
				System.out.println("DretvaPartija: -> " + dolazniFIFO.getFirst());
			}
			} catch ( NullPointerException ex){
				//Thread.yield();
				System.out.print("*");
			}
			
			//System.out.println("DretvaPartija radi.");
		}
	         while (true) {
            try {
                Thread.currentThread().sleep(1000);
                System.out.println("Testiram je li sve pet");
                synchronized(this) {
                    while (threadSuspended)
                        wait();
                }
            } catch (InterruptedException e){
            }
            test();
        }
        */
	
	
	@SuppressWarnings("static-access")
	@Override
	 public void run() {
		synchronized(mutex) {
			while ( dolazniFIFO.isEmpty()){
				try {
					mutex.wait();
				} catch (InterruptedException e) {} 
			}
		}
    }


}


