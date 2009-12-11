package projekt.java.klijent;

import projekt.java.klase.LjudiUSobi;
import projekt.java.klase.NabaviPaket;
import projekt.java.klase.PaketSoba;
import projekt.java.klase.PaketStatistika;
import projekt.java.klase.Potez;
import projekt.java.klase.Soba;
import projekt.java.klase.Igrac;




import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
/**
 * Klasa šalje i prima serializirane podatke od servera.
 * @author Mario
 *
 */
public class Paket {
	/** Socket klijenta koji komunicira sa serverom. */
	private Socket klijentSocket;
	/** IP adresa servera kojem se šalje paket. */
	private InetAddress serverIP;
	/** Port servera kojem se šalje paket. */
	private int serverPort;

	/**
	 * Konstruira objekt klase Paket.
	 * @param ip IP adresa servera kojem se šalje paket
	 * @param port port severa kojem se šalje paket
	 */
	public Paket(String ip, int port) {	
		try {		
			serverIP = InetAddress.getByName(ip);
			serverPort = port;
		}catch (UnknownHostException uhe){
            System.out.println("Nepozdati server ip/name : " + ip);
            System.exit(1);
        }
	}
	/** Metoda za spajanje na server */
	private void connect(){
		try {		
			klijentSocket = new Socket(serverIP, serverPort);
		}catch (UnknownHostException uhe){
            System.out.println("Nepozdati server ip/name : " + serverIP);
            System.exit(1);
        }
        catch (IOException ioe){
            System.out.println("Nisam uspio dobiti I/O za: " + serverIP + ":" + serverPort);
            System.exit(1);
        }
	}
	/** Metoda za zatvaranje veze prema serveru */
	private void close(){
		try {
			klijentSocket.close();
		} catch (IOException e) {
			System.err.println("Veza se ne može zatvoriti.");
		}
	}
	/**
	 * Funkcija za autorizaciju korisnika.
	 * @param korisnik
	 * @param zaporka
	 * @return vraæa null ako je autorizacija uspjela inaèe vraæa poruku greške.
	 */
	public String login(Igrac korisnik, String zaporka) {			
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        paketZaServer.writeObject( new NabaviPaket(0, korisnik.getIme(), zaporka));  // send serilized payload
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket paketSKljucem = (NabaviPaket) paketOdServera.readObject();
	        if( paketSKljucem.isIndex(0)){
	        	korisnik.setKljucKorisnika(paketSKljucem.getKljuc());
	        	return null;
	        } else if( paketSKljucem.isIndex(-1)){
	        	return paketSKljucem.getErr();
	        }
	       	        
	        paketZaServer.close();
	        paketOdServera.close();
		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
		return "Iznimka reda 0";	
	}
	/**
	 * Funkcija za deautorizaciju korisnika
	 * @param korisnik
	 * @return vraæa null ako je autorizacija uspjela inaèe vraæa poruku greške.
	 */
	public String logout(Igrac korisnik) {
		connect();  
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
			NabaviPaket t = new NabaviPaket(6);
			t.setUser(korisnik.getIme());
			paketZaServer.writeObject( t ); 
			
			ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
			NabaviPaket paket = (NabaviPaket) paketOdServera.readObject();
			if( paket.isIndex(4)){
				return null;
			} else if( paket.isIndex(-1)){
				return paket.getErr();
			}
			
			paketZaServer.close();
			paketOdServera.close();
		}
		catch (IOException ioe){
			System.out.println("Iznimka ulaza/izlaza");
			System.exit(1);
		}
		catch(ClassNotFoundException k){
			System.out.println("Dobivena kriva klasa " + k);
		} finally{
			close();
		}
		return "Iznimka reda 0";	
	}
	
	/**
	 * Funkcija koja registrira korisnika na server.
	 * @param ime
	 * @param zaporka
	 * @param email
	 * @return vraæa "radi" ako je registracija uspjela, inaèe vrati poruku pogreške.
	 */
	public String registrirajKorisnika(String ime, String zaporka, String email) {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
			paketZaServer.writeObject( new NabaviPaket(1, ime, zaporka, email));
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket dolazniPaket = (NabaviPaket) paketOdServera.readObject();	        
	        if( dolazniPaket.isIndex(1)){
	        	return "radi";
	        }else if( dolazniPaket.isIndex(7) ){
	        	return dolazniPaket.getErr();
	        }
	       	        
	        paketZaServer.close();
	        paketOdServera.close();
		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }	   
		return "Iznimka reda 0";		
	}
	/**
	 *  Vraæa sve Sobe na serveru.
	 * @return popis svi Soba na serveru.
	 */
	public ArrayList<Soba> dohvatiSobe() {      
		connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
			paketZaServer.writeObject( new NabaviPaket(2));
	       
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        PaketSoba sobeNaServeru = (PaketSoba) paketOdServera.readObject();	        
	        paketZaServer.close();
	        paketOdServera.close();
	        return sobeNaServeru.getListaSoba();
		} catch (IOException ioe){	
	        System.out.println("dohvatiSobe: Iznimka ulaza/izlaza " + ioe.getLocalizedMessage());
	        System.out.println("dohvatiSobe:  error-> "+ ioe.getMessage());
	        System.out.println("dohvatiSobe:  uzrok-> "+ ioe.getCause());
	        System.exit(1);
	    } catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
	    
		return null;  
	}
	
	/**
	 * Vraæa popis svih igraèa na serveru.
	 * @return igraèi na serveru.
	 */
	public ArrayList<Igrac> dohvatiStatistike() {
		connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
			paketZaServer.writeObject( new NabaviPaket(3));
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        PaketStatistika listaIgraca = (PaketStatistika) paketOdServera.readObject();	        
	        paketZaServer.close();
	        paketOdServera.close();
	        return listaIgraca.getListaIgraca();
		}
	    catch (IOException ioe){
	        System.out.println("dohvatiStatistike: Iznimka ulaza/izlaza -> "+ ioe.getLocalizedMessage());
	        System.out.println("dohvatiStatistike:  error-> "+ ioe.getMessage());
	        System.out.println("dohvatiStatistike:  uzrok-> "+ ioe.getCause());
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
		return null; 
	}
	
	/**
	 * Stavlja korisnika u zadanu sobu.
	 * @param korisnik
	 * @param idSobe
	 * @return null ako je uspjelo inaèe poruka pogreške.
	 */
	public String udjiUSobu(Igrac korisnik, int idSobe) {
		connect();  
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        paketZaServer.writeObject( new NabaviPaket(4, korisnik.getKljucKorisnika(), idSobe));  // send serilized payload
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket paket = (NabaviPaket) paketOdServera.readObject();
	        if( paket.isIndex(4)){
	        	return null;
	        } else if( paket.isIndex(-1)){
	        	return paket.getErr();
	        }
	       	        
	        paketZaServer.close();
	        paketOdServera.close();
		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
		return "Iznimka reda 0";	
	}
	/**
	 * Pokreæe DretvaPartija na serveru.
	 * @param idSobe
	 * @return uspjesno pokrenuta dretva.
	 */
	public boolean zapocniIgru(int idSobe) {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(5);
	        paket.setIdSobe(idSobe);
			paketZaServer.writeObject( paket );
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket paketSKljucem = (NabaviPaket) paketOdServera.readObject();
	        if( paketSKljucem.isIndex(5)){
	        	return true;
	        } else if( paketSKljucem.isIndex(-1)){
	        	return false;
	        }
	       	        
	        paketZaServer.close();
	        paketOdServera.close();
		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
		return false;	
	}
	/**
	 * Funkcija vraæa korisniku skup njegovih karata.
	 *TODO -NAPOMENA2 - OVO KAD JEDAN ZADRAŽI TREBA BITI BRODCASTANO SVIMA!!!
	 * @param idKorisnika
	 * @return
	 */
	public Potez dohvatiKarte(int idKorisnika) {
        connect();
        Potez paketKarata = null;
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(21);
	        paket.setKljuc(idKorisnika);
			paketZaServer.writeObject( paket );
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        paketKarata = (Potez) paketOdServera.readObject();
	        
	       	
	        paketZaServer.close();
	        paketOdServera.close();
		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
		return paketKarata;
	}
	
	
	
	/**
	 * Revizija 1.0
	 */
	public void povuciPotez(int idSobe, int idIgraca, byte[] odigranaKarta) {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        Potez paket = new Potez();
	        paket.setKljucKorisnika(idIgraca);
	        paket.setKodKarte(odigranaKarta);
	        paket.setIdSobe(idSobe);
			
	        paketZaServer.writeObject( paket );      	        
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        paketOdServera.close();
	        paketZaServer.close();

		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    finally{
	    	close();
	    }
	}
	public String cekajPotez(int idSobe) {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(23);
	        paket.setIdSobe(idSobe);
			paketZaServer.writeObject( paket );
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket paketSKljucem = (NabaviPaket) paketOdServera.readObject();
        
	        paketZaServer.close();
	        paketOdServera.close();
	        System.out.println("CekajPotez -> " + paketSKljucem.getErr());
	        return paketSKljucem.getErr();
		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    catch(ClassNotFoundException k){
	        System.out.println("Dobivena kriva klasa " + k);
	    } finally{
	    	close();
	    }
		return null;	
	}
	public LjudiUSobi dohvatiLjudeUSobi( int idSobe, int kljucKorisnika) {
		 connect();
			try{
				ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
		        NabaviPaket paket = new NabaviPaket(16);
		        paket.setIdSobe(idSobe);
		        paket.setKljuc(kljucKorisnika);
				paketZaServer.writeObject( paket );
		        
		        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
		        NabaviPaket paketLjudima = (NabaviPaket) paketOdServera.readObject();
	        
		        paketZaServer.close();
		        paketOdServera.close();
		        
		        LjudiUSobi ljudi = new LjudiUSobi();
		        ljudi.setLjudiUSobi(paketLjudima.getUser().split(","));
		        ljudi.setMaxLjudi(paketLjudima.getKljuc());
		        return ljudi;
			}
		    catch (IOException ioe){
		        System.out.println("Iznimka ulaza/izlaza");
		        System.exit(1);
		    }
		    catch(ClassNotFoundException k){
		        System.out.println("Dobivena kriva klasa " + k);
		    } finally{
		    	close();
		    }
			return null;
	}
	public String cekajIgrace(int idSobe) {
		 connect();
			try{
				ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
		        NabaviPaket paket = new NabaviPaket(28);
		        paket.setIdSobe(idSobe);
				paketZaServer.writeObject( paket );
		        
		        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
		        NabaviPaket paketSKljucem = (NabaviPaket) paketOdServera.readObject();
	        
		        paketZaServer.close();
		        paketOdServera.close();
		        return paketSKljucem.getUser();
			}
		    catch (IOException ioe){
		        System.out.println("Iznimka ulaza/izlaza");
		        System.exit(1);
		    }
		    catch(ClassNotFoundException k){
		        System.out.println("Dobivena kriva klasa " + k);
		    } finally{
		    	close();
		    }
			return null;	
	}
	public void dodajIgracaTEST() {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(30);
			
	        paketZaServer.writeObject( paket );      	        
	        paketZaServer.close();

		}
	    catch (IOException ioe){
	        System.out.println("Iznimka ulaza/izlaza");
	        System.exit(1);
	    }
	    finally{
	    	close();
	    }
	}
	
}
