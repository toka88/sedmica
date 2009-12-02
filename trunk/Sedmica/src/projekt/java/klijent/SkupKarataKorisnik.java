package projekt.java.klijent;

import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComponent;

public class SkupKarataKorisnik extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Card> karte;
	
	public SkupKarataKorisnik(Card prvaKarta, Card drugaKarta, Card trecaKarta, Card cetvrtaKarta) {
		karte= new ArrayList<Card>();
		karte.add(prvaKarta);
		karte.add(drugaKarta);
		karte.add(trecaKarta);
		karte.add(cetvrtaKarta);
	}
	
	public void ubaciKarte(Card prvaKarta, Card drugaKarta, Card trecaKarta, Card cetvrtaKarta) {
		karte= new ArrayList<Card>();
		karte.add(prvaKarta);
		karte.add(drugaKarta);
		karte.add(trecaKarta);
		karte.add(cetvrtaKarta);
		this.repaint();
	}
	
	/**
	 * Metoda izbacuje jednu kartu iz ruke
	 * @param index index karte koja se izbacuje
	 */
	public void izbaciKartu(int index){
		if(index<karte.size())
			karte.remove(index);
		this.repaint();
	}
	
	/**
	 * Umece jednu kartu u ruku
	 * @param karta karta koja se umece u ruku
	 */
	public void umetniKartu(Card karta){
		karte.add(karta);
		this.repaint();
	}

	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Insets ins = this.getInsets();
		int varX = ins.left+5;
		int varY = ins.top+5;
		
		for(Card karta : karte){
			karta.moveTo(varX, varY);
			karta.draw(g, this);
			varX = varX + karta.getWidth()+5;
		}
	}


	public Card getKarta(int index) {
		return karte.get(index);
	}
	
	
}
