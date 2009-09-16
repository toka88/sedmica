package projekt.java.klase;

import java.io.Serializable;
import java.util.ArrayList;


public class PaketSoba implements Serializable {

	/** Automatski generiran kljuè*/
	private static final long serialVersionUID = 1959502363021956915L;

	private ArrayList<Soba> listaSoba;

	/** Glavni konstruktor klase*/
	public PaketSoba(ArrayList<Soba> listaSoba) {
		this.listaSoba = listaSoba;
	}

	/** Dohvaæa listu Soba*/
	public ArrayList<Soba> getListaSoba() {
		return listaSoba;
	}
	/** Postavlja listu Soba */
	public void setListaSoba(ArrayList<Soba> listaSoba) {
		this.listaSoba = listaSoba;
	}

	public void ispisSoba(){
		int brojSoba = listaSoba.size();
		for( int i = 0; i < brojSoba; i ++){
			System.out.println(listaSoba.get(i));
		}
	}
	
	
	
	
}
