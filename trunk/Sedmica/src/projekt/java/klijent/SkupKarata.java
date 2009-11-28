package projekt.java.klijent;

import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComponent;

public class SkupKarata extends JComponent {

	private static final long serialVersionUID = 1L;
	/** Karte korisnika */
	private ArrayList<Card> karte;
	/** Pozicija igraèa na ploèi */
	private String pozicijaIgraca;
	/*TODO TREBA DODATI KONSTANTU BROJ KARATA */
	public SkupKarata(int brojKarata, Card pozadina, String pozicijaIgraca) {
		karte = new ArrayList<Card>();
		for(int i = 0; i < brojKarata; i++){
			karte.add(pozadina);
		}
		this.pozicijaIgraca = pozicijaIgraca;
		
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
	 * Umece jednu kartu u ruku - TODO TREBA PONOVNO OBOJATI DA SE VIDI PROMJENA
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

		if ( pozicijaIgraca.equals("gore")){			
			for(Card karta : karte){
				karta.moveTo(varX, varY);
				karta.draw(g, this);
				varX = varX + karta.getWidth()+5;
			}
		} else {
			for(Card karta : karte){
				karta.moveTo(varX, varY);
				karta.draw(g, this);
				varX = varX + karta.getWidth()/2;
				varY = varY + karta.getHeight()/10;
			}
		}

	}


	public Card getKarta(int index) {
		if(karte.size() == 0) return null;
		return karte.get(index);
	}
	
	
}
