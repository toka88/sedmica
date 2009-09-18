package projekt.java.baza;

import java.util.ArrayList;

import projekt.java.klase.Igrac;

interface  SuceljeBaze {
	
	/** Metoda za login korisnika 
	 * @return vraca tekst o uspjesnosti izvedbe operacije login - poruka je oblika : <b>porukaUspjeha#brojPoruke#tekst</b>
	 *  <br><i>sifra=1 - USPJESNO#1#Autorizacija uspjesna, korisnik je prijavljen</i>
	 *  <br><i>sifra=2 - NEUSJPJESNO#2#Krivo korisnicko ime i/ili lozinka</i>
	 *  <br><i>sifra=3 - NEUSJPJESNO#3#Korisnik je vec prijavljen</i>*/
	public String login ( String korisnik, String pass);
	
	/** Metoda za logout korisnika 
	 * @return vraca true ako je uspjesna autorizacija, inace false (nepostojeci korisnik i pass) */
	public boolean logout ( String korisnik);
	
	/** Metoda za azurianje bodovnog stanja korisnika
	 *  @return vraca true ako je azuriranje bodova bilo uspjesno, u suprotnom vraca false*/
	public boolean azuriraj(String korisnik, int bodovi );
	
	/** Metoda za dohvat korisnika po imenu korisnika
	 *  @return vraca Igrac-a ili null ukoliko je doslo do greske */
	public Igrac dohvatiIgraca ( String korisnik );
	
	/** Metoda za registraciju korisnika 
	 * @return vraca true ako je registracija uspjesna, inace false*/
	public boolean registriraj( String korisnik, String lozinka, String email);
	
	/** Metoda za dohvat rang liste korisnika */
	public ArrayList<Igrac> dohvatiStatistike();
}
