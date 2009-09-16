package projekt.java.server.opisne_klase;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Ova klasa predstavlja jednu Sobu na serveru.
 * @author Mario
 */
public class Soba {
	/** Popis igraï¿½a, */
	private ArrayList<Igrac> popisIgraca = new ArrayList<Igrac>();
	/** Maksimalan broj igraï¿½a u sobi */
	private int maxBrojIgraca = 4;
	/** Trenutno stanje sobe. Je li soba zakljucana */
	private boolean sobaZakljucana = false;
	/** Indentifikacijski broj sobe */
	private int idSobe;

	/** Lista u koju server šalje sve zahtjeve od korisnika dretvi koja regulira partiju. */
	private volatile LinkedList<Potez> odigraniPotez = new LinkedList<Potez>();


	
	/** Konstruktor klase Soba */
	public Soba(int idSobe) {
		this.idSobe = idSobe;
	}
	/** Metoda za dohvat listeOdigranih poteza */
	public LinkedList<Potez> getListaPoteza(){
		return odigraniPotez;
	}
	/** Metoda za dohvat igraca trenutno u sobi */
	public ArrayList<Igrac> getPopisIgraca() {
		return popisIgraca;
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
		String igraci = new String();
		int brojIgraca = popisIgraca.size();
		for( int i = 0; i < brojIgraca; i++){
			igraci += popisIgraca.get(i).toString()+ "!";
		}
		return idSobe +"#"+ sobaZakljucana +"#"+ maxBrojIgraca +"#"+ igraci;
	}
	
	public static Soba parsirajStringReprezentaciju(String sobaString) {
		String[] elementi = sobaString.split("#");

		Soba soba = null;
		try {
			soba = new Soba(Integer.parseInt(elementi[0]));
			soba.setSobaZakljucana(Boolean.parseBoolean(elementi[1]));
			soba.setMaxBrojIgraca(Integer.parseInt(elementi[2]));
			
		String[] igraci = elementi[3].split("$");
		for (int i = 0; i < igraci.length; i++) {
			
			String[] podatak = igraci[i].split("!");
			
			Igrac igrac = new Igrac(Integer.parseInt(podatak[1]));
			igrac.setIme(podatak[0]);
			soba.popisIgraca.add(igrac);
		}
		} catch (NumberFormatException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		return soba;
	}
}
