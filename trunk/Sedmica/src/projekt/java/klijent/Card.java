package projekt.java.klijent;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;

/**
 * Razred ima funkciju crtanja jedne karte 
 */
public class Card {
	
	private ImageIcon slika;
    private int x;
    private int y;
    private int id;
    
    /** Konstruktor */
    public Card(ImageIcon image, int idKarte) {
        slika = image;
        id=idKarte;
    }
    
    /**
     * Metoda vraca ime karte
     * @return ime karte
     */
    public int getIdKarte(){
    	return id;
    }
    
    /**
     * Metoda za pozicioniranje lokacije gdje ce se iscrtati karta
     * @param x x koordinata
     * @param y y koordinata
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Metoda provjerava da li se zadate koordinate nalaze unutar crteza karte
     * @param x x koordinata
     * @param y y koordinata
     * @return true ako su koordinate unutar slike, inace false
     */
    public boolean contains(int x, int y) {
        return (x > this.x && x < (this.x + getWidth()) && 
                y > this.y && y < (this.y + getHeight()));
    }
    
    /**
     * Metoda za dohvat sirine slike karte
     * @return sirina slike
     */
    public int getWidth() {
        return slika.getIconWidth();
    }
    
    /**
     * Metoda za dohvat visine slike karte
     * @return visina slike
     */
    public int getHeight() {
        return slika.getIconHeight();
    }
    
    /**
     * Metoda za dohvat pozicije karte
     * @return x koordinata
     */
    public int getX() {
        return this.x;
    }
    
    /**
     * Metoda za dohvat pozicije karte
     * @return y koordinata
     */
    public int getY() {
        return this.y;
    }
    
    /**
     * Metoda za crtanje karte
     * @param g Graphics po kojem se crta
     * @param c roditelj komponenta na koju se crta (parametar "<i>this</i>")
     */
    public void draw(Graphics g, Component c) {
        slika.paintIcon(c, g, this.x, this.y);
    }
}

