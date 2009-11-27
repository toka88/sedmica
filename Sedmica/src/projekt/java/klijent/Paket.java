package projekt.java.klijent;

import projekt.java.klase.NabaviPaket;
import projekt.java.klase.PaketSoba;
import projekt.java.klase.PaketStatistika;
import projekt.java.klase.Soba;
import projekt.java.klase.Igrac;




import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
/**
 * Klasa �alje i prima serializirane podatke od servera.
 * @author Mario
 *
 */
public class Paket {
	/** Socket klijenta koji komunicira sa serverom. */
	private Socket klijentSocket;
	/** IP adresa servera kojem se �alje paket. */
	private InetAddress serverIP;
	/** Port servera kojem se �alje paket. */
	private int serverPort;

	/**
	 * Konstruira objekt klase Paket.
	 * @param ip IP adresa servera kojem se �alje paket
	 * @param port port severa kojem se �alje paket
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
			System.err.println("Veza se ne mo�e zatvoriti.");
		}
	}
	/**
	 * Funkcija za autorizaciju korisnika.
	 * @param korisnik
	 * @param zaporka
	 * @return vra�a null ako je autorizacija uspjela ina�e vra�a poruku gre�ke.
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
	 * Funkcija koja registrira korisnika na server.
	 * @param ime
	 * @param zaporka
	 * @param email
	 * @return vra�a "radi" ako je registracija uspjela, ina�e vrati poruku pogre�ke.
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
	 *  Vra�a sve Sobe na serveru.
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
	 * Vra�a popis svih igra�a na serveru.
	 * @return igra�i na serveru.
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
	 * @return null ako je uspjelo ina�e poruka pogre�ke.
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
	 * Funkcija vra�a korisniku skup njegovih karata.
	 * -NAPOMENA - ZADNJI BYTE STAVLJEN KAO BOOLEAN VARIJABLA -> sMIJE LI IGRA� IGRATI.
	 * @param idKorisnika
	 * @return
	 */
	public byte[] dohvatiKarte(int idKorisnika) {
        connect();
		byte[] karte = new byte[5];
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(21);
	        paket.setKljuc(idKorisnika);
			paketZaServer.writeObject( paket );
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket paketKarata = (NabaviPaket) paketOdServera.readObject();
	        
	       	
	        String kSKarte = paketKarata.getErr();
	        String[] temp = kSKarte.split(",");
	        for( int i = 0; i < temp.length; i++){
	        	karte[i] = (byte) Integer.parseInt(temp[i]);
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
		return karte;
	}
	
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
	
	/**TODO BRI�I ME!
	 * Funkcija prima id igraca, sebe i odigrane karte sto potom salje serveru. U polje karte stavlja karte koje
	 * su odigrali drugi igraci. A jednom kad su svi igraci odigrali i kad je paket s korisnikovom kartom stigao,
	 * funkcija se prekida i vraca id sljedece karte.
	 * @param idSobe - is sobe u kojoj se nalazi igrac
	 * @param idIgraca - id igraca koji igra
	 * @param odigranaKarta - id karte koju igrac igra
	 * @param karte - referenca na polje koje predstavlja karte na stolu. U GUI.u postoji actionListener koji �eka na promjene ovog polja.
	 * @return id karte koju je poslao server
	 */
	public int povuciPotez(int idSobe, int idIgraca, int odigranaKarta, int[] karte) {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(5);
	        paket.setIdSobe(idSobe);
			paketZaServer.writeObject( paket );
	        
	        ObjectInputStream paketOdServera = new ObjectInputStream(klijentSocket.getInputStream());
	        NabaviPaket paketSKljucem = (NabaviPaket) paketOdServera.readObject();
	        if( paketSKljucem.isIndex(5)){
	        	return 1;
	        } else if( paketSKljucem.isIndex(-1)){
	        	return -1;
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
		return -1;	
	}
	/**
	 * Revizija 1.0
	 */
	public void povuciPotez(int idSobe, int idIgraca, int odigranaKarta) {
        connect();
		try{
			ObjectOutputStream paketZaServer = new ObjectOutputStream(klijentSocket.getOutputStream());          
	        NabaviPaket paket = new NabaviPaket(5);
	        paket.setIdSobe(idSobe);
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
	
}
