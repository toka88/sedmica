package projekt.java.klijent;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
					/* Saklira sliku na zaljenu velicinu */
					Image inImage = new ImageIcon(file.toString()).getImage();
					Image img = inImage.getScaledInstance(73 , 97, Image.SCALE_SMOOTH);
					ImageIcon newIcon = new ImageIcon(img);
					
					
					deck[i++] = new Card(newIcon,Integer.valueOf(niz[0]));
					
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
