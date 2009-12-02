package projekt.java.server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import projekt.java.baza.BazaImpl;
import projekt.java.klase.Igrac;
import projekt.java.klase.NabaviPaket;
import projekt.java.klase.PaketSoba;
import projekt.java.klase.PaketStatistika;
import projekt.java.klase.Potez;
import projekt.java.klase.Soba;



/**
 * Implementacija Servera za online igru.
 * Korišteni protokol TCP/IP
 * @author Mario
 */
public class Server {

	/** Port servera */
	static short port = 2222;
	/** Maksimalan broj soba na serveru */
	private static int maxBrojSoba = 10;
	/** Lista soba na serveru */
	private volatile static ArrayList<Soba> listaSoba = new ArrayList<Soba>();
	/** Mapa kljuèevi korisnika Igraèi */
	private HashMap<Integer, Igrac>  mapaIgraca = new HashMap<Integer, Igrac>();

	/**
	 * Metoda pokreæe i inicijalizira server.
	 * 
	 * @author Mario
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		initServerSobe();
		Server server = new Server();
		server.start();
	}
	
	/**
	 * Pokretanje posluzitelja.
	 * @throws IOException ako dode do pogreske u zaprimanju zahtjeva
	 */
	private void start() throws IOException {
		ServerSocket socket = new ServerSocket(port);
		System.out.println("Listening on: " + socket.getLocalSocketAddress());
		while(true) {
			Socket client = socket.accept();		
			new Thread(new prihvatiZahtjev(client)).start();		
		}
	}
	
	/**
	 * Inicijalizira listu soba
	 */
	private static void initServerSobe() {
		for (int i = 0; i < maxBrojSoba; i++) {
			Soba soba = new Soba(i);
			listaSoba.add(soba);
		}
		System.out.println("Server: Sobe inicijalizirane.");
	}
	
	int TEMPbrIgraca = 1; /*TODO IZBRIŠI MENE, SLOBODNO ;) */
	/**
	 * Pomoæni razred za obradu klijenata. Svaki novi zahtjev æe dobiti 
	 * svoju dretvu koja æe prihvatiti i obraðivati njegove zahtjeve.
	 * 
	 */
	private class prihvatiZahtjev implements Runnable {		
		/** Pristupna tocka za razgovor s klijentom */
		Socket client;
		/** Stream za primanje paketa */
		ObjectInputStream dolazniPaket;
		/** Stream za slajnje paketa */
		ObjectOutputStream paketZaKlijenta;	
		
		/**
		 * Konstruktor.
		 * @param client pristupna tocka za razgovor s klijentom
		 */
		public prihvatiZahtjev(Socket client) {
			this.client = client;
		}

		/**
		 * Metoda koja se automatski pokrene kada poène nova dretva.
		 */
		public void run() {
			System.out.println("IP adresa klijenta: "+ client.getInetAddress());
			try {
				dolazniPaket = new ObjectInputStream(client.getInputStream());
				paketZaKlijenta = new ObjectOutputStream(client.getOutputStream());
				prihvatiPoruku();				
			} catch(Exception ex) {
			} finally {
				try { client.close(); } catch(Exception ignorable) {}
			}
		}

       
		private void prihvatiPoruku() throws IOException {
			
			/* Veza prema bazi podataka. */
			/*TODO OVO VRATITI - TRENUTNO BAZA NE TREBA AUTORIZACIJA SAMO ODUGOVLAÈI S VREMENOM 
			 * BazaImpl baza = new BazaImpl();
			 */
			/* Kod dolaznog paketa */
			NabaviPaket zahtjevaniPakt = null;
			
			try {
				zahtjevaniPakt = (NabaviPaket) dolazniPaket.readObject();
			} catch (ClassNotFoundException e) {
				System.err.println(" Problemi s dolaznim klasom.");
			}
            int kod = zahtjevaniPakt.getIndexPaketa();
            /*
			System.out.println("Kod ->" + kod);
			System.out.println("User ->" + zahtjevaniPakt.getUser());
			System.out.println("Pass ->" + zahtjevaniPakt.getPass());
			System.out.println("Email ->" + zahtjevaniPakt.getEmail());
			System.out.println("Kljuè ->" + zahtjevaniPakt.getKljuc());
			System.out.println("Id Sobe ->" + zahtjevaniPakt.getIdSobe());	*/		
			try {/*TODO ÈEMU OVO? */
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
			switch( kod ){				
						/* Autorizacija  RETURN int kljucKorisnika*/
				case 0: String user = zahtjevaniPakt.getUser();
						String  pass = zahtjevaniPakt.getPass();
						String poruka = "haha#1#Izbrisi me! JA AUTORIZIRAM SVAKOGA;)";//baza.login ( user, pass);
						
						String[] op  = poruka.split("#");
						
						//Inicijaliziraj generator brojeva
						Random randomGenerator = new Random(); 
					    int idKorisnika = randomGenerator.nextInt(3480);

						/* Doda korisnika u mapu kljuc, Igrac*/
						mapaIgraca.put(idKorisnika, new Igrac( idKorisnika, user, client.getOutputStream()));

						/* Javi je li autorizirano i ako je pošalji id korisniku*/
						if ( op[1].equals("1")){ //USPJEH
							paketZaKlijenta.writeObject( new NabaviPaket(0,idKorisnika));
						} else{
							paketZaKlijenta.writeObject( new NabaviPaket(-1,op[2]));	
						}
								
						break;						
						/* Registracija RETURN boolean registracijaUspjela */
				case 1: user = zahtjevaniPakt.getUser();
						pass = zahtjevaniPakt.getUser();
						String email = zahtjevaniPakt.getEmail();
						//System.out.println("Registriraj:" + user + pass + email);
						/* Javi korisniku je li registriran ili nije */
						if ( true ){//*TODO VRATI ME <-   baza.registriraj(user, pass, email)
							paketZaKlijenta.writeObject( new NabaviPaket(1));
						} else{
							paketZaKlijenta.writeObject( new NabaviPaket(-1,"Registracija nije uspjela"));	
						}				
						break;
						/* Popis soba RETRUN ArrayList<Soba> popisSoba*/
				case 2:	paketZaKlijenta.writeObject( new PaketSoba(listaSoba) );
						break;
						/* Statistika RETURN ArrayList<Igrac> statistikaIgraèa */
				case 3: ArrayList<Igrac> izBaze =  null; //*TODO VRATI ME <- baza.dohvatiStatistike();						
						ArrayList<Igrac> igraci = new ArrayList<Igrac>();
						/*TODO srediti da je klasa Igrac ona iz projekt.java.klase	*/
						for( int i = 0; i < izBaze.size(); i++){
							Igrac ind = izBaze.get(i);
							Igrac tmp = new Igrac(ind.getKljucKorisnika());
							tmp.setIme(ind.getIme());
							igraci.add(tmp);
						}
						
					
						paketZaKlijenta.writeObject( new PaketStatistika(igraci) );
						break;
				case 4: /* Dodaj korisnika u sobu */
						int idSobe = zahtjevaniPakt.getIdSobe();
						Igrac korisnik = mapaIgraca.get(zahtjevaniPakt.getKljuc());
						listaSoba.get(idSobe).getPopisIgraca().add(new Igrac(korisnik.getKljucKorisnika(),korisnik.getIme()));
						//soba.setSobaZakljucana(true); // soba se zakljuèava
						paketZaKlijenta.writeObject( new NabaviPaket(4));
						//Pokreæe se dretva koja obraðuje partiju.
						//new Thread ( new DretvaPartija(soba));
						break;
						/* Zapoèni igru */
				case 5: idSobe = zahtjevaniPakt.getIdSobe();
						Soba soba = listaSoba.get(idSobe);
						soba.setSobaZakljucana(true); // soba se zakljuèava
											
						paketZaKlijenta.writeObject( new NabaviPaket(5));
						//Pokreæe se dretva koja obraðuje partiju.
						//new Thread ( new DretvaPartija(soba));
						break;		
						/* Vrši odjavljivanje od sustava. */
				case 6: user = zahtjevaniPakt.getUser();
						if ( true ) { //* TODO VRATI ME <- baza.logout(user)
							//System.out.println("Logout :: " + user);
							paketZaKlijenta.writeObject( new NabaviPaket(4));
						} else {
							paketZaKlijenta.writeObject( new NabaviPaket(-1,"Odjavljivanje od sustava nije uspjelo."));
						}
						break;
						/* Uði u sobu */
				case 7: System.out.println(" Test PAKET. ");
						break;
	
				/* TODO ovo inaèe radi DretvaPartija() */
						
				case 21:byte[] karte = {1,1,4,12};
						
						Potez posaljiPotez = new Potez (0,karte); 
						posaljiPotez.setMojPotez(true);
						paketZaKlijenta.writeObject( posaljiPotez );
						/*NabaviPaket paket = new NabaviPaket(21); 
						paket.setErr("1,1,4,12,1");
						paketZaKlijenta.writeObject( paket );*/
						break;

				case 23:
						NabaviPaket paketKarte = new NabaviPaket(23); 
						paketKarte.setErr( (TEMPbrIgraca + 8) + "," + TEMPbrIgraca + "," + ( ( TEMPbrIgraca > 2 )?1:0 ) );
						TEMPbrIgraca = TEMPbrIgraca + 1;
						if( TEMPbrIgraca == 4 ) TEMPbrIgraca = 1;
						paketZaKlijenta.writeObject( paketKarte );
						System.out.println("Netko me traži kartu!!" + TEMPbrIgraca );
						break;
						
				case 28:NabaviPaket paketLjudiUSobi = new NabaviPaket(28);
						paketLjudiUSobi.setUser("TEstUser");
						paketZaKlijenta.writeObject( paketLjudiUSobi );
						System.out.println("SERVER: poslao sam podatke o novom korisniku ;)");
						try {Thread.sleep(4000);} catch (InterruptedException e) {}
						break;
				case 30:
						break;
				////////////////////////////////////////////////		
						
						
						
						/* Neprepoznat paket */
				default:paketZaKlijenta.writeObject( new NabaviPaket(-1,"Neispravan kôd poruke."));	
						System.out.println("Kod poruke je neispravan.");
						break;
			}
			/* Pošalji paket korisniku.*/
			paketZaKlijenta.flush(); 
			
		}

	}



}
