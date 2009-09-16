package projekt.java.klase;

import java.io.Serializable;

public class NabaviPaket implements Serializable {
	private static final long serialVersionUID = 4728715773899581199L;
	
	private int indexPaketa;
	private String user;
	private String pass;
	private String email;
	private int kljuc;
	private int idSobe;
	private String err;
	

	public NabaviPaket(int indexPaketa, int kljuc, int idSobe) {
		this.indexPaketa = indexPaketa;
		this.kljuc = kljuc;
		this.idSobe = idSobe;
	}


	public NabaviPaket(int indexPaketa, String err) {
		this.indexPaketa = indexPaketa;
		this.err = err;
	}
	

	public NabaviPaket(int indexPaketa, int kljuc) {
		this.indexPaketa = indexPaketa;
		this.kljuc = kljuc;
	}


	public int getIdSobe() {
		return idSobe;
	}


	public void setIdSobe(int idSobe) {
		this.idSobe = idSobe;
	}


	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getKljuc() {
		return kljuc;
	}

	public void setKljuc(int kljuc) {
		this.kljuc = kljuc;
	}

	public int getIndexPaketa() {
		return indexPaketa;
	}

	public void setIndexPaketa(int indexPaketa) {
		this.indexPaketa = indexPaketa;
	}
	public boolean isIndex( int index){
		if( indexPaketa == index) return true;
		else return false;
	}
	
	public NabaviPaket(int indexPaketa) {
		this.indexPaketa = indexPaketa;
	}

	public NabaviPaket(int indexPaketa, String user, String pass) {
		this.indexPaketa = indexPaketa;
		this.user = user;
		this.pass = pass;
	}

	public NabaviPaket(int indexPaketa, String user, String pass, String email) {
		this.indexPaketa = indexPaketa;
		this.user = user;
		this.pass = pass;
		this.email = email;
	}
	
}
