package projekt.java.klase;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * (Srijaliziran) Razred Igraè sadržava sve bitne podatke o korisniku(igra)
 * @author Mario
 *
 */
public class Igrac implements Serializable {
	/** Serijal UID */
	private static final long serialVersionUID = 8337875871177635908L;
	/** Kljuc igraca dodjeljen prilikom autorizacije */
	private int kljucKorisnika;	
	/** Korisnicko ime igraca */
	private String ime;
	/** Broj bodova igraèa */
	private int brojBodova = 0;	
	/** Tok podataka za slanje korisniku - transient, neæe biti serijaliziran. */
	private transient DataOutputStream odlazniTok;	
	

	/**
	 * Pomoæni konstruktor
	 */
	public Igrac(int kljucKorisnika, String ime) {
		this.kljucKorisnika = kljucKorisnika;
		this.ime = ime;
	}
	/**
	 * Pomoæni konstruktor
	 */		
	public Igrac(int kljucKorisnika, String ime,OutputStream odlazniTok) {
		this.kljucKorisnika = kljucKorisnika;
		this.ime = ime;
		this.odlazniTok = new DataOutputStream(odlazniTok);
	}
	/**
	 * Pomoæni konstruktor
	 */
	public Igrac( int kljucKorisnika){
		this.kljucKorisnika = kljucKorisnika;	
	}
	/** Metoda za dohvat broj bodova */
	public int getBrojBodova() {
		return brojBodova;
	}
	/** Metoda za postavljanej broja bodova */
	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}
	/**
	 * @return Vraca indentifikator korisnika
	 */
	public int getKljucKorisnika() {
		return kljucKorisnika;
	}	
	/**
	 * Postavlja identifikator trenutnog korisnika
	 * @param kljucKorisnika
	 */
	public void setKljucKorisnika(int kljucKorisnika) {
		this.kljucKorisnika = kljucKorisnika;
	}
	/** Metoda za dohvat imena */
	public String getIme() {
		return ime;
	}
	/** Metoda za postavljanje imena igracu */
	public void setIme(String ime) {
		this.ime = ime;
	}
	/** Dohvat toka */
	public DataOutputStream getOdlazniTok() {
		return odlazniTok;
	}
	/** Postavljanje toka */
	public void setOdlazniTok(DataOutputStream odlazniTok) {
		this.odlazniTok = odlazniTok;
	}
	@Override
	/** Vraæa imeKorisnika$kljuèKorisnika*/
	public String toString() {
		return ime+"$"+kljucKorisnika;
	}

}
