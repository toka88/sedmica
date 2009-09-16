package projekt.java.baza.dao;

import javax.persistence.EntityManager;

public interface DBOperacija<T> {
	public T executeOperation(EntityManager em);
}