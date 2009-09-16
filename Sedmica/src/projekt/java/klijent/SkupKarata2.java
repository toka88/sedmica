package projekt.java.klijent;

import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComponent;

public class SkupKarata2 extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Card> karte;
	
	public SkupKarata2(Card prvaKarta, Card drugaKarta, Card trecaKarta, Card cetvrtaKarta) {
		karte= new ArrayList<Card>();
		karte.add(prvaKarta);
		karte.add(drugaKarta);
		karte.add(trecaKarta);
		karte.add(cetvrtaKarta);
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
	}

	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Insets ins = this.getInsets();
		int varX = ins.left+5;
		int varY = ins.top+5;
		
		for(Card karta : karte){
			karta.moveTo(varX, varY);
			karta.draw(g, this);
			varX = varX + karta.getWidth()/2;
			varY = varY + karta.getHeight()/10;
		}
		
		//this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}


	public Card getKarta(int index) {
		return karte.get(index);
	}
	
	
}
