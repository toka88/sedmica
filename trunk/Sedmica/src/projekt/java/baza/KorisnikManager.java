package projekt.java.baza;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import projekt.java.baza.dao.DBOperacija;
import projekt.java.baza.dao.PersistenceUtil;
import projekt.java.baza.model.Korisnik;

/**
 * Razred koji obavlja operacije prema bazi;
 * 
 * @author EmanuelLacic
 */
public class KorisnikManager {
	
	private static EntityManagerFactory emf;

	/** Logout */
	public static boolean logout(final String korisnik){
		zapocni();
		final Korisnik k;
		try{
		 k = PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Korisnik>() {
					@Override
					public Korisnik executeOperation(EntityManager em) {
						return (Korisnik)em.createNamedQuery("Korisnik.poImenu").setParameter("korisnik", korisnik).getSingleResult();
					}
				}
			);
		} catch (Exception ex){
			zavrsi();
			return false;
		}
		k.setOnline(false);
		PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Void>() {
					@Override
					public Void executeOperation(EntityManager em) {
							em.merge(k);
						return null;
					}
				}
			);
		zavrsi();
		return true;
	}
	
	/** Login */
	public static int login(final String korisnik, final String sifra){
		zapocni();
		final Korisnik k;
		try{
		k = PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Korisnik>() {
					@Override
					public Korisnik executeOperation(EntityManager em) {
						return (Korisnik)em.createNamedQuery("Korisnik.imeSifra").setParameter("korisnik", korisnik)
						.setParameter("sifra", sifra).getSingleResult();
					}
				}
			);
		} catch(Exception ex){
			zavrsi();
			return 2;
		}
		if (k.isOnline()==true){
			k.setOnline(false);
			PersistenceUtil.executeSingleDatabaseOperation(
					new DBOperacija<Void>() {
						@Override
						public Void executeOperation(EntityManager em) {
								em.merge(k);
							return null;
						}
					}
				);
			zavrsi();
			return 3;
		}
		k.setOnline(true);
		PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Void>() {
					@Override
					public Void executeOperation(EntityManager em) {
							em.merge(k);
						return null;
					}
				}
			);
		zavrsi();
		return 1;
	}
	
	/** Azuriranje bodova */
	public static void azuriraj(final String korisnik, int bodovi){
		zapocni();
		final Korisnik k = PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Korisnik>() {
					@Override
					public Korisnik executeOperation(EntityManager em) {
						return (Korisnik)em.createNamedQuery("Korisnik.poImenu").setParameter("korisnik", korisnik).getSingleResult();
					}
				}
			);
		k.setBodovi(k.getBodovi()+bodovi);
		PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Void>() {
					@Override
					public Void executeOperation(EntityManager em) {
							em.merge(k);
						return null;
					}
				}
			);
		zavrsi();	
	}
	
	/** Dohvat korisnika po zadanom imenu */
	public static Korisnik dohvatiKorisnika(final String korisnik) {
		zapocni();
		Korisnik k = PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Korisnik>() {
					@Override
					public Korisnik executeOperation(EntityManager em) {
						return (Korisnik)em.createNamedQuery("Korisnik.poImenu").setParameter("korisnik", korisnik).getSingleResult();
					}
				}
			);
		zavrsi();
		return k;
	}
	
	/** Dohvat svih korisnika */
	public static ArrayList<Korisnik> dohvatiStatistike() {
		zapocni();
		ArrayList<Korisnik> k = PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<ArrayList<Korisnik>>() {
					@SuppressWarnings("unchecked")
					@Override
					public ArrayList<Korisnik> executeOperation(EntityManager em) {
						return (ArrayList<Korisnik>)em.createNamedQuery("Korisnik.svi").getResultList();
					}
				}
			);
		zavrsi();
		return k;
	}
	
	/** Registracija korisnika */
	public static void registriraj(String korisnik, String lozinka, String email) {
		zapocni();
		final Korisnik k = new Korisnik();
		k.setId(null);
		k.setBodovi(0);
		k.setBrojPartija(0);
		k.setEmail(email);
		k.setKorisnik(korisnik);
		k.setLozinka(lozinka);
		k.setOnline(false);
		PersistenceUtil.executeSingleDatabaseOperation(
				new DBOperacija<Void>() {
					@Override
					public Void executeOperation(EntityManager em) {
						if(k.getId()==null) {
							em.persist(k);
						} else {
							em.merge(k);
						}
						return null;
					}
				}
			);
		zavrsi();
	}
	
	
	private static void zapocni(){
		emf = Persistence.createEntityManagerFactory("projekt");
		PersistenceUtil.initSingleTon(emf);
	}
	
	private static void zavrsi(){
		emf.close();
		PersistenceUtil.clearSingleTon();
	}
}
