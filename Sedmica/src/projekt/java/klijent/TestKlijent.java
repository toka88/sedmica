package projekt.java.klijent;

import java.util.ArrayList;

import projekt.java.klase.Igrac;
import projekt.java.klase.Soba;

/**
 * Impementacija jednostavnog klijenta za echo poslužitelja.
 * @author Mario
 */

public class TestKlijent {

	public static void main(String[] args){		
		Paket veza = new Paket("00.00.00.00",2222);//"127.0.0.1"		
		//System.out.println( veza.registrirajKorisnika("Napokon", "dva", "dfa") );
		
		
		  
		
		System.out.println("Klijent pocetak.");
		Igrac igrac = new Igrac(3, "Mario");
		veza.logout(igrac);
		/*
	
		String error = veza.login(igrac , "lovac");
		System.out.println("Error ima:" + error);
		System.out.println("Kljuè igraèa je:" +igrac.getKljucKorisnika());

		
		System.out.println( veza.udjiUSobu(igrac, 3) );
		/*		
		ArrayList<Igrac> igraci = veza.dohvatiStatistike();
		for(int i=0; i < igraci.size(); i++){
			System.out.println(i +" -> "+ igraci.get(i) );
		}	
		*/
		
		/*
		ArrayList<Soba> sobe = veza.dohvatiSobe();	
		for(int i=0; i < sobe.size(); i++){
			System.out.println(i +" -> "+ sobe.get(i) );
		}*/
		/*
		System.out.println("Ma doðem do tu kaj sereš");
		veza.logout(igrac);
		System.out.println("Ma doðem do tu kaj sereš. Dva?");
		System.exit(5);*/

	}
}