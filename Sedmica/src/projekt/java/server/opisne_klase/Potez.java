package projekt.java.server.opisne_klase;

import java.io.Serializable;

/**
 * (Serijaliziran)Predstavljat ce jednu poruku od klijenta
 * i opisivait odigran potez
 * 
 * @author Martin & Mario
 */

public class Potez implements Serializable {
	/** Serijal UID */
	private static final long serialVersionUID = 39691151508830988L;
	/** Koji user je poruku poslao*/
	private int kljucKorisnika;
	/** Koja karta je odigrana*/
	private byte kodKarte;
	/** Ako korisnik želi nastaviti igrat u drugi krug */
	private boolean tuce = false;	
	/** Zastavica koja govori smije li korisnik igrati ili ne */
	boolean smijesIgrati = false;
	
		
	public Potez() {
		super();
	}
	public Potez(int kljucKorisnika, byte kodKarte) {
		super();
		this.kljucKorisnika = kljucKorisnika;
		this.kodKarte = kodKarte;
	}
	public int getKljucKorisnika() {
		return kljucKorisnika;
	}
	public void setKljucKorisnika(int kljucKorisnika) {
		this.kljucKorisnika = kljucKorisnika;
	}
	public byte getKodKarte() {
		return kodKarte;
	}
	public void setKodKarte(byte kodKarte) {
		this.kodKarte = kodKarte;
	}
	public boolean isTuce() {
		return tuce;
	}
	public void setTuce(boolean tuce) {
		this.tuce = tuce;
	}
	public boolean isSmijesIgrati() {
		return smijesIgrati;
	}
	public void setSmijesIgrati(boolean smijesIgrati) {
		this.smijesIgrati = smijesIgrati;
	}
	
	
	
}
