package projekt.java.klijent;

import java.io.File;

import javax.swing.ImageIcon;

/**
 * Razred ima funkciju inicijalizacije karti i spremanje u spil karata
 * @author Emanuel
 *
 */
public class SveKarte {
	
	/**
	 * Metoda dohvaca slike karte iz odgovarajuceg direktorija te inicijalizira svaku kartu
	 * i sprema ju u niz - špil karata
	 * @return sve karte
	 */
	public static Card[] dohvatiDeck(){
		File dir = new File("images");
		File[] lista = dir.listFiles();
		Card[] deck = new Card[32];
		int i=0;
		for (File file : lista){
			if (file.isFile()){
				if (file.toString().endsWith(".gif") && !file.toString().contains("pozadina")){
					String[] niz = file.getName().toString().split("\\.");
					String[] niz2 = niz[0].split("_");
					deck[i]=new Card(new ImageIcon(file.toString()),Integer.valueOf(niz2[1]));
					i++;
				}
			}
		}
		return deck;
	}
	
	/**
	 * Metoda dohvaca sliku pozadine karte i inicijalizira razred Card koji koristi tu pozadinu
	 * @return karta pozadine
	 */
	public static Card dohvatiPozadinu(){
		File dir = new File("images");
		File[] lista = dir.listFiles();
		Card kartaPozadine = null;
		for (File file : lista){
			if (file.isFile()){
				if (file.toString().contains("pozadina_0.gif")){
					String[] niz = file.getName().toString().split("\\.");
					kartaPozadine =new Card(new ImageIcon(file.toString()),0);
					break;
				}
			}
		}
		return kartaPozadine;
	}

}
