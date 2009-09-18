package projekt.java.baza;

import java.util.ArrayList;

import projekt.java.baza.model.Korisnik;
import projekt.java.klase.Igrac;

/**
 * Razred nudi metode za razjmenu podataka sa bazom podataka.
 *
 * @author EmanuelLacic
 */
public class BazaImpl implements SuceljeBaze{
		
	/** {@inheritDoc} */
	@Override
	public String login(String korisnik, String pass) {
		int tipPoruke = KorisnikManager.login(korisnik, pass);
		if (tipPoruke==3)
			return "NEUSJPJESNO#3#Korisnik je vec prijavljen";
		if (tipPoruke==2)
			return "NEUSJPJESNO#2#Krivo korisnicko ime i/ili lozinka";
		return "USPJESNO#1#Autorizacija uspjesna, korisnik je prijavljen";
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean logout(String korisnik) {
		return KorisnikManager.logout(korisnik);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean azuriraj(String korisnik, int bodovi) {
		try{
		KorisnikManager.azuriraj(korisnik,bodovi);
		} catch(Exception ex){
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public Igrac dohvatiIgraca(String korisnik) {
		Korisnik k;
		try{
		k = KorisnikManager.dohvatiKorisnika(korisnik);
		} catch(Exception ex){
			System.err.println("Ne postoji korisnik sa zadanim imenom.");
			return null;
		}
		Igrac ig = new Igrac(k.getId().intValue(),k.getKorisnik(),null);
		ig.setBrojBodova(k.getBodovi());
		return ig;
	}
	
	/** {@inheritDoc} */
	@Override
	public ArrayList<Igrac> dohvatiStatistike() {
		ArrayList<Korisnik> korisnici = KorisnikManager.dohvatiStatistike();
		ArrayList<Igrac> igraci = new ArrayList<Igrac>();
		for(Korisnik k : korisnici){
			Igrac ig = new Igrac(k.getId().intValue(),k.getKorisnik(),null);
			igraci.add(ig);
		}
		return igraci;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean registriraj(String korisnik, String lozinka, String email) {
		try{
		KorisnikManager.registriraj(korisnik, lozinka, email);
		} catch (Exception ex){
			return false;
		}
		return true;
	}

}
