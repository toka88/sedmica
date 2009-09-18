package projekt.java.klase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import projekt.java.klase.Potez;

/**
 * (Srijalizirana)Ova klasa predstavlja jednu Sobu na serveru.
 * @author Mario
 */
public class Soba implements Serializable{
	private static final long serialVersionUID = 2780109394171051040L;
	/** Popis igraï¿½a, */
	private ArrayList<Igrac> popisIgraca = new ArrayList<Igrac>();
	/** Maksimalan broj igraèa u sobi */
	private int maxBrojIgraca = 4;
	/** Trenutno stanje sobe. Je li soba zakljucana */
	private boolean sobaZakljucana = false;
	/** Indentifikacijski broj sobe */
	private int idSobe;

	/** Lista u koju server šalje sve zahtjeve od korisnika dretvi koja regulira partiju. */
	private volatile LinkedList<Potez> odigraniPotez = new LinkedList<Potez>();

	/** Metoda za dohvat listeOdigranih poteza */
	public LinkedList<Potez> getListaPoteza(){
		return odigraniPotez;
	}
	
	public ArrayList<Igrac> getPopisIgraca() {
		return popisIgraca;
	}

	public void setPopisIgraca(ArrayList<Igrac> popisIgraca) {
		this.popisIgraca = popisIgraca;
	}

	/** Konstruktor klase Soba */
	public Soba(int idSobe) {
		this.idSobe = idSobe;
	}

	/** Metoda za dohvat maksimalnog broja igraca u sobi */
	public int getMaxBrojIgraca() {
		return maxBrojIgraca;
	}
	/** Metoda postavlja maksimalan broj igraca koji moï¿½e biti u sobi 
	 * @throws IndexOutOfBoundsException ako je broj igraca neispravan*/
	public void setMaxBrojIgraca(int maxBrojIgraca) throws IndexOutOfBoundsException{
		if ( ( maxBrojIgraca < 2 )||( maxBrojIgraca > 4 ) ) throw new IndexOutOfBoundsException("Broj igraca je neispravan.");
		else this.maxBrojIgraca = maxBrojIgraca;
	}
	/** Metoda koja provjerava je li soba zakljucana */
	public boolean isSobaZakljucana() {
		return sobaZakljucana;
	}
	/** Metoda zakljucava i otkljucava sobu */
	public void setSobaZakljucana(boolean sobaZakljucana) {
		this.sobaZakljucana = sobaZakljucana;
	}
	/** Metoda za postavljanje id.a sobe */
	public int getIdSobe() {
		return idSobe;
	}
	/** Metoda za dohvat id.a sobe */
	public void setIdSobe(int id) {
		this.idSobe = id;
	}

	/** 
	 * Metoda vraï¿½a toString u obliku 
	 * idSobe#sobaZakljuï¿½ana#maxBrojIgraï¿½a#ime$idIgraï¿½a!ime$idIgraï¿½a!...
	 * */
	@Override
	public String toString() {
		String igraciSobe = new String();
		int brojIgraca = popisIgraca.size();
		for( int i = 0; i < brojIgraca; i++){
			igraciSobe += popisIgraca.get(i) + "$";
		}
		return idSobe +"#"+ sobaZakljucana +"#"+ maxBrojIgraca +"#"+ popisIgraca.toString();
	}
	
}
