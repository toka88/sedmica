package projekt.java.baza.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *	Razred za prezistenciju podataka.
 *
 * @author EmanuelLacic
 */
public class PersistenceUtil {
	
	private static ThreadLocal<Data> threadLocal = new ThreadLocal<Data>();
	private static EntityManagerFactory emf;
	
	/**
	 * Metoda izvodi jednu operaciju prema bazi podataka.
	 * @param <T> tip podataka
	 * @param dbo operacija prema bazi podataka
	 * @return rezultat izvodenja
	 */
	public static <T> T executeSingleDatabaseOperation(DBOperacija<T> dbo) {
		T result = null;
		EntityManager em = PersistenceUtil.getEntityManager();
		try {
			PersistenceUtil.beginTransaction();
			result = dbo.executeOperation(em);
			PersistenceUtil.commitTransaction();
		} catch(RuntimeException ex) {
			PersistenceUtil.rollbackIfNeeded();
			throw ex;
		} finally {
			PersistenceUtil.closeEntityManager();
		}
		return result;
	}
	
	/**
	 * Metoda za postavljanje EntityManagerFactory-a
	 * @param emf EntityManagerFactory
	 */
	public static void initSingleTon(EntityManagerFactory emf) {
		PersistenceUtil.emf = emf;
	}
	
	/**
	 * Metoda za brisnje emf
	 */
	public static void clearSingleTon() {
		emf = null;
	}

	/**
	 * Metoda za dohvat EntityManagera
	 * @return EntityManager
	 */
	public static EntityManager getEntityManager() {
		Data d = threadLocal.get();
		if(d != null) {
			return d.em;
		}
		if(emf == null) throw new IllegalStateException("Persistence provider is not available.");
		d = new Data();
		d.em = emf.createEntityManager();
		try {
			d.tx = d.em.getTransaction();
		} catch(RuntimeException ex) {
			d.em.close();
			throw ex;
		}
		threadLocal.set(d);
		return d.em;
	}
	
	/**
	 * Metoda za zatvaranje EntityManager
	 */
	public static void closeEntityManager() {
		Data d = threadLocal.get();
		if(d == null) {
			return;
		}
		d.em.close();
		threadLocal.remove();
	}
	
	public static void rollbackIfNeeded() {
		Data d = threadLocal.get();
		if(d == null) {
			return;
		}
		if(d.tx.isActive()) d.tx.rollback();
	}
	
	public static boolean isTransactionActive() {
		Data d = threadLocal.get();
		if(d == null) {
			throw new IllegalStateException("Entity manager is not accessible. Are you calling PersistenceUtil.isTransactionActive() before PersistenceUtil.getEntityManager()?");
		}
		return d.tx.isActive();
	}
	
	public static void beginTransaction() {
		Data d = threadLocal.get();
		if(d == null) {
			throw new IllegalStateException("Entity manager is not accessible. Are you calling PersistenceUtil.beginTransaction() before PersistenceUtil.getEntityManager()?");
		}
		d.tx.begin();
	}
	
	public static void commitTransaction() {
		Data d = threadLocal.get();
		if(d == null) {
			throw new IllegalStateException("Entity manager is not accessible. Are you calling PersistenceUtil.commitTransaction() before PersistenceUtil.getEntityManager()?");
		}
		d.tx.commit();
	}
	
	public static void rollbackTransaction() {
		Data d = threadLocal.get();
		if(d == null) {
			throw new IllegalStateException("Entity manager is not accessible. Are you calling PersistenceUtil.rollbackTransaction() before PersistenceUtil.getEntityManager()?");
		}
		d.tx.rollback();
	}
	
	private static class Data {
		EntityManager em;
		EntityTransaction tx;
	}
}
