package projekt.java.klijent.opisne_klase;

/**
 * Razred Igrac predstavlja sadrzi sve potrebne podatke o igracu za klijenta
 * @author Ognjen
 */
public class Igrac {
	
	/** Kljuc igraca dodjeljen prilikom autorizacije */
	private int kljucKorisnika;	
	/** Korisnicko ime igraca */
	private String ime;
	/** Broj bodova igraƒça */
	private int brojBodova = 0;
	

	/** Podrazumijevani konstruktor klase */
	public Igrac(int kljucKorisnika, String ime) {
		this.kljucKorisnika = kljucKorisnika;
		this.ime = ime;
	}
	/**  PomoÊni konstruktor */
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
	/** @return Vraca indentifikator korisnika */
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
	/** Parisa zadati string u novu klasu Igrac.a*/
	public static Igrac parsirajStringReprezentaciju(String igracString) {
		String[] elementi = igracString.split("$");
		if (elementi.length != 2 || elementi.length != 3) {
			return null;
		}
		Igrac igrac = null;
		try {
			igrac = new Igrac(
					Integer.valueOf(elementi[1]), elementi[0]);
			if (elementi.length == 3) {
				igrac.setBrojBodova(Integer.valueOf(elementi[2]));
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return igrac;
	}


}
