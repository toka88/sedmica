package projekt.java.klase;

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
	private byte[] kodKarte;
	/** Ako korisnik želi nastaviti igrat u drugi krug */
	private boolean mozesDalje = false;	
	/** Zastavica koja govori smije li korisnik igrati ili ne */
	boolean mojPotez = false;
	
		
	public Potez() {
	}
	public Potez(int kljucKorisnika, byte kodKarte) {
		this.kljucKorisnika = kljucKorisnika;
		this.kodKarte[0] = kodKarte;
	}
	public Potez(int kljucKorisnika, byte[] kodKarte) {
		this.kljucKorisnika = kljucKorisnika;
		this.kodKarte = kodKarte;
	}
	public int getKljucKorisnika() {
		return kljucKorisnika;
	}
	public void setKljucKorisnika(int kljucKorisnika) {
		this.kljucKorisnika = kljucKorisnika;
	}
	public byte getKodKarte(int index) {
		return kodKarte[index];
	}
	public void setKodKarte(byte kodKarte, int index) {
		this.kodKarte[index] = kodKarte;
	}
	public byte[] getKodKarte() {
		return kodKarte;
	}
	public void setKodKarte(byte[] kodKarte) {
		this.kodKarte = kodKarte;
	}
	public boolean isMozesDalje() {
		return mozesDalje;
	}
	public void setMozesDalje(boolean mozesDalje) {
		this.mozesDalje = mozesDalje;
	}
	public boolean isMojPotez() {
		return mojPotez;
	}
	public void setMojPotez(boolean mojPotez) {
		this.mojPotez = mojPotez;
	}

	
	
	
}
