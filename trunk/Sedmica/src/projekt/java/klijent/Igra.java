package projekt.java.klijent;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import projekt.java.klase.Igrac;

/**
 * Predstavlja ploèu za igru koju korisnik vidi tijekom igre.
 * @author Kristijan
 *
 */
public class Igra extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static Card[] deck;
	
	private Paket veza;
	
	/** Labele igraèa za stolom */
	private JLabel lijeviIgrac, desniIgrac, gornjiIgrac;
	
	/** Ako je true, korisnik je na redu za bacanje karte*/
	private boolean naReduSam;/*OVO OMOGUÆUJE DA STAVLJAŠ KARTE NA STOL */
	
	/** Ako je true karta se može baciti, u protivnom, karta je baèena.*/ /* Èemu ovo kad veæ postoji naReduSam?? */
	private boolean mozeSeBaciti;

	private Igrac igrac;

	private int idSobe;

	/**
	 * Konstruktor ploèe.
	 */
	public Igra(Paket veza, Igrac igrac, int idSobe) {
		this.veza = veza;
		this.igrac = igrac;
		this.idSobe = idSobe;
		initGUI();
	}
	public Igra() {
		initGUI();
	}
	public static void main(String[] args) {
		new Igra();

	}

	/**
	 * Inicijalizacija ploèe za igranje. Postavljanje karata za svakog korisnika
	 * i stola.
	 */
	private void initGUI() {
		setLocation(110, 110);
		setSize(800, 600); 
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Sedmica v1.0");
		Container cp = getContentPane();
        cp.setLayout(new SedmicaLayout());
        
        /* Postavljanje slike karata i pozadine. */
        deck = SveKarte.dohvatiDeck();
        final Card pozadina = SveKarte.dohvatiPozadinu();
       
        
        /* Postavljanje imena igraèa za stolom*/
        lijeviIgrac = new JLabel("Lijevi");
        desniIgrac = new JLabel("Desni");
        gornjiIgrac = new JLabel("Gornji Igraè");
        /*
        cp.add(lijeviIgrac);
        cp.add(desniIgrac);
        cp.add(gornjiIgrac);
        */
        
        JLabel donjaKarta = new JLabel("Donja karta");
        donjaKarta.setBorder(BorderFactory.createTitledBorder("Donja karta"));
        cp.add(donjaKarta);
        
        /* Inicijalizacija prikaza karata. */
        final SkupKarata gornjeKarte = new SkupKarata(4,pozadina,"gore");
        cp.add(gornjeKarte);//gornje karte
        
        final SkupKarata lijevoKarte = new SkupKarata(4,pozadina,"lijevo");
        cp.add(lijevoKarte);//lijevo karte
        
        final SkupKarata desnoKarte = new SkupKarata(4,pozadina,"desno");
        cp.add(desnoKarte);//desno karte       
        
        /* Inicijalizacija stola. */
        final CardTable stol = new CardTable();
        cp.add(stol);
        stol.setTableSize(400, 300);
        /*
         * Provjeri jesi li na redu.
         * Ako jesi onda igraj inaæe èekaj da doðeš na red.
         * */
        
        /* Nabavi od servera koje su tvoje karte */ 
        byte[] karte = veza.dohvatiKarte(igrac.getKljucKorisnika());
        final SkupKarataKorisnik mojeKarte = new SkupKarataKorisnik(deck[karte[0]],deck[karte[1]],deck[karte[2]],deck[karte[3]]);
        cp.add(mojeKarte);//moje karte        	   
        /* Ako je peti byte veæi od nule to znaèi da je igraè na potezu */
        if ( karte[4] > 0 ){
        	naReduSam = true;
        } else {
        	naReduSam = false;
        }
        
        // hardkodiranje    
        mozeSeBaciti = true;
        
        mojeKarte.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				int indeksKarte = -1; 
				if (e.getX()<=80){ indeksKarte = 0; }
				if (e.getX()>80&& e.getX()<160){ indeksKarte = 1; }
				if (e.getX()>=160 && e.getX()<240){ indeksKarte = 2; }
				if (e.getX()>=240){ indeksKarte = 3; }
				if (naReduSam && mozeSeBaciti && indeksKarte != -1 ) {
					try {
						stol.dodajMojeKarte(mojeKarte.getKarta(indeksKarte));
					} catch (Exception ex) {
					}
					veza.povuciPotez(idSobe, igrac.getKljucKorisnika(), mojeKarte.getKarta(indeksKarte).getIdKarte());
					mojeKarte.izbaciKartu(indeksKarte);
					mozeSeBaciti = false;
					indeksKarte = -1;
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {	
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			@Override
			public void mouseReleased(MouseEvent e) {	
			}      	
        });
        
        /* Ako mogu dalje igrati ali to ne zelim */
        JButton gumbDosta = new JButton("Dosta");
        cp.add(gumbDosta);
        gumbDosta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				byte[] karte = veza.dohvatiKarte(igrac.getKljucKorisnika());
				for( int i = 0; i < karte.length - 1; i++ ) {
					mojeKarte.umetniKartu(deck[karte[i]]);
				}
				stol.makniSveKarteSaStola();
			}
        	
        });
        
        
        /* SwingWorker èeka u pozadini za nove odigrane karte */
        SwingWorker<String,Void> worker = new SwingWorker<String,Void>(){
			@Override
			protected String doInBackground() throws Exception {
			while(true){
				if(!mozeSeBaciti){
					String temp = veza.cekajPotez(idSobe);
					String[] parm = temp.split(",");
					int idKarte = Integer.parseInt(parm[0]);
					int rbrKorisnika = Integer.parseInt(parm[1]);
					/* Ovisno o tome koji igrac igra na to mjesto stavi kartu */
					switch( rbrKorisnika ){
						case 1 : stol.dodajDesneKarte(deck[idKarte]);
								desnoKarte.izbaciKartu(0);
								break;
						case 2 : stol.dodajGornjeKarte(deck[idKarte]);
								gornjeKarte.izbaciKartu(0);
								break;
						case 3 : stol.dodajLijeveKarte(deck[idKarte]);
								lijevoKarte.izbaciKartu(0);
								break;
					}
					int odigraliSvi = Integer.parseInt(parm[2]);
					if ( odigraliSvi == 0 ) continue;
					/* Nakon sto su svi ostali odigrali daj meni nove karte */
					byte[] karte = veza.dohvatiKarte(igrac.getKljucKorisnika());
					if(karte.length == 1){ //Ako moze igrati opet onda ce doci samo jedan byte, inace uvijek dva(karte...,dozvola)
						naReduSam = true;
						mozeSeBaciti = true;
						continue;
					}

					for( int i = 0; i < karte.length - 1; i++ ) {
						mojeKarte.umetniKartu(deck[karte[i]]);
						/*TODO kad proradi server ovo ukljuèiti umjesto segmeta 1.0 
						desnoKarte.umetniKartu(pozadina);
						gornjeKarte.umetniKartu(pozadina);
						lijevoKarte.umetniKartu(pozadina);*/
					}
					/*Segment 1.0 */
					desnoKarte.umetniKartu(pozadina);
					gornjeKarte.umetniKartu(pozadina);
					lijevoKarte.umetniKartu(pozadina);
					/*TODO tu se moze dodati delay da se karte stignu pogledati */
					stol.makniSveKarteSaStola();;
					mozeSeBaciti = true;
				}
			}
			}
        
        };
        worker.execute();

        setResizable(false);
		setVisible(true);
	}
}
