package projekt.java.klijent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 *	Razred ima funkciju stola na kojem ce se karta iscrtati kada ih igrac odigra
 */
public class CardTable extends JComponent{

	private static final long serialVersionUID = 1L;
	private static final Color BGColor = Color.GREEN;
	private Dimension tableSize = new Dimension();
	private int tableHeight = 100;
	private int tableWidth = 100;
	private List<Card> karteMoje;
	private List<Card> karteDesno;
	private List<Card> karteLijevo;
	private List<Card> karteGore;

	
	/**
	 * Konstruktor stola. Prima polje koje sadrzi sve karte špila.
	 * @param deck sve karte u špilu(32 karte).
	 */
	public CardTable() {
		setPreferredSize(new Dimension(this.tableWidth, this.tableHeight));
        setBackground(Color.blue);
        karteMoje=new ArrayList<Card>();
        karteDesno=new ArrayList<Card>();
        karteLijevo=new ArrayList<Card>();
        karteGore=new ArrayList<Card>();
        this.repaint();
	}

	
	/**
	 * Metoda za postavljanje dimenzija stola
	 * @param tableSize velicina stola
	 */
	public void setTableSize(int tableWidth, int tableHeight){
		this.tableSize.height = tableHeight;
		this.tableSize.width = tableWidth;
	}
	
	/**
	 * Metoda za dohvat dimenzija stola
	 * @return velicina stola
	 */
	public Dimension getTableSize(){
		return tableSize;
	}
	
	/**
	 * Metoda za dodavanje karte na stol koju je upravo igrac bacio.
	 * Ovu metodu koristi igrac koji je trenutacno na potezu (onaj pri dnu GUI-a)
	 * @param karta karta koja se bacila na stol
	 */
	public void dodajMojeKarte(Card karta){
		karteMoje.add(karta);
		this.repaint();
	}
	
	/**
	 * Metoda za dodavanje karte na stol koju je upravo bacio igrac koji je inace drugi na potezu.
	 * Ovu metodu koristi igrac koji je s <b>desne</b> strane stola
	 * @param karta karta koja se bacila na stol
	 */
	public void dodajDesneKarte(Card karta){
		karteDesno.add(karta);
		this.repaint();
	}
	
	/**
	 * Metoda za dodavanje karte na stol koju je upravo bacio igrac koji je inace treci na potezu.
	 * Ovu metodu koristi igrac koji je s <b>gornje</b> strane stola
	 * @param karta karta koja se bacila na stol
	 */
	public void dodajGornjeKarte(Card karta){
		karteGore.add(karta);
		this.repaint();
	}
	
	/**
	 * Metoda za dodavanje karte na stol koje je upravo bacio igrac koji je inace cetvrti na potezu.
	 * Ovu metodu koristi igrac koji je s <b>lijeve</b> strane stola
	 * @param karta karta koja se bacila na stol
	 */
	public void dodajLijeveKarte(Card karta){
		karteLijevo.add(karta);
		this.repaint();
	}
	
	public void makniSveKarteSaStola(){
		karteMoje.clear();
		karteDesno.clear();
		karteGore.clear();
		karteLijevo.clear();
		this.repaint();
	}
	
	
	
	/**
	 * Metoda crta stol i karte na njemu.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		this.removeAll();
		Insets ins = this.getInsets();
		int sirina = this.getWidth()-ins.left-ins.right;
		int visina = this.getHeight()-ins.top-ins.bottom;
		Graphics2D g2d= (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.fillRect(ins.left, ins.right, sirina, visina);
		g2d.setColor(BGColor);
		g2d.fillRect(ins.left+5, ins.top+5, sirina-10, visina-10);
		
		int brojKarti = karteMoje.size();
		if (brojKarti!=0){
			int offset=0;
			for(Card karta : karteMoje){
				karta.moveTo(offset+sirina/2-(karta.getWidth()+(brojKarti-1)*15)/2, 
						visina/2+(visina/2-karta.getHeight())/2);
				karta.draw(g, this);
				offset=offset+15;
			}
		}
		
		brojKarti = karteDesno.size();
		if (brojKarti!=0){
		    int offset=0;
		    for(Card karta : karteDesno){
		    	AffineTransform origXform = g2d.getTransform();
			    AffineTransform newXform = (AffineTransform)(origXform.clone());
			    newXform.rotate(Math.toRadians(90),sirina/2+(sirina/2-karta.getWidth()),
			    		visina/2-offset+(brojKarti-1)*15);
			    g2d.setTransform(newXform);
				karta.moveTo((sirina-karta.getWidth())/2+(sirina/2-karta.getWidth()),
						(visina-karta.getHeight())/2-offset+(brojKarti-1)*15);
				karta.draw(g, this);
				offset=offset+15;
			    g2d.setTransform(origXform);
			}

		}
			
		brojKarti = karteGore.size();
		if (brojKarti!=0){
			int offset=0;
			for(Card karta : karteGore){
				karta.moveTo(sirina/2-offset-(karta.getWidth()+(brojKarti-1)*15)/2,
						(visina/2-karta.getHeight())/2);
				karta.draw(g, this);
				offset=offset+15;
			}	
		}
		
		brojKarti = karteLijevo.size();
		if (brojKarti!=0){
			int offset=0;
		    for(Card karta : karteLijevo){
		    	AffineTransform origXform = g2d.getTransform();
			    AffineTransform newXform = (AffineTransform)(origXform.clone());
			    newXform.rotate(Math.toRadians(90),sirina/2-(sirina/2-karta.getWidth()),
			    		visina/2+offset-(brojKarti-1)*15);
			    g2d.setTransform(newXform);
				karta.moveTo((sirina-karta.getWidth())/2-(sirina/2-karta.getWidth()),
						(visina-karta.getHeight())/2+offset-(brojKarti-1)*15);
				karta.draw(g, this);
				offset=offset+15;
			    g2d.setTransform(origXform);
			}
		}
	}

}
