package projekt.java.klase;

import java.io.Serializable;
import java.util.ArrayList;


public class PaketStatistika implements Serializable{
	private static final long serialVersionUID = 7771864117904861996L;
	private ArrayList<Igrac> listaIgraca;
	
	public PaketStatistika(ArrayList<Igrac> listaIgraca) {
		this.listaIgraca = listaIgraca;
	}
	public ArrayList<Igrac> getListaIgraca() {
		return listaIgraca;
	}
	public void setListaIgraca(ArrayList<Igrac> listaIgraca) {
		this.listaIgraca = listaIgraca;
	}
	

}
