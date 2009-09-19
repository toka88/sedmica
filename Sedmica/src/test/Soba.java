package test;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Ova klasa predstavlja jednu Sobu na serveru serijalitzirana.
 * @author Mario
 */
public class Soba implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2780109394171051040L;
	/** Popis igra�a, */
	private ArrayList<String> popisIgraca = new ArrayList<String>();
	/** Maksimalan broj igra�a u sobi */
	private int maxBrojIgraca = 4;
	/** Trenutno stanje sobe. Je li soba zakljucana */
	private boolean sobaZakljucana = false;
	/** Indentifikacijski broj sobe */
	private int idSobe;

	/*komentar*/

	
	/** Konstruktor klase Soba */
	public Soba(int idSobe) {
		this.idSobe = idSobe;
	}

	/** Metoda za dohvat maksimalnog broja igraca u sobi */
	public int getMaxBrojIgraca() {
		return maxBrojIgraca;
	}
	/** Metoda postavlja maksimalan broj igraca koji mo�e biti u sobi 
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
	 * Metoda vra�a toString u obliku 
	 * idSobe#sobaZaklju�ana#maxBrojIgra�a#ime$idIgra�a!ime$idIgra�a!...
	 * */
	@Override
	public String toString() {
		String igraci = new String();
		int brojIgraca = popisIgraca.size();
		for( int i = 0; i < brojIgraca; i++){
			igraci += popisIgraca.get(i).toString()+ "!";
		}
		return idSobe +"#"+ sobaZakljucana +"#"+ maxBrojIgraca +"#"+ igraci;
	}
	
}
