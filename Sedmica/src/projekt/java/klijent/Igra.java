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
        Card pozadina = SveKarte.dohvatiPozadinu();
       
        
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
        SkupKarata gornjeKarte = new SkupKarata(4,pozadina,"gore");
        cp.add(gornjeKarte);//gornje karte
        
        SkupKarata lijevoKarte = new SkupKarata(4,pozadina,"lijevo");
        cp.add(lijevoKarte);//lijevo karte
        
        SkupKarata desnoKarte = new SkupKarata(4,pozadina,"desno");
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
				if (e.getX()<=80){
					//System.out.println("Stisnuta prva karta");
					if (naReduSam && mozeSeBaciti) {
						try {
							stol.dodajMojeKarte(mojeKarte.getKarta(0));
						} catch (Exception ex) {
						}
						veza.povuciPotez(idSobe, igrac.getKljucKorisnika(), mojeKarte.getKarta(0).getIdKarte());
						mojeKarte.izbaciKartu(0);
						mozeSeBaciti = false;
						/*
						for( int i = 0; i < 2; i++){
							//stol.dodajDesneKarte(deck[veza.cekajPotez(idSobe)]);
							System.out.println("id Karte" + veza.cekajPotez(idSobe));
							stol.dodajLijeveKarte(deck[12]);
						}*/
						
					}
				}
				if (e.getX()>80&& e.getX()<160){
					//System.out.println("Stisnuta druga karta");
					if (naReduSam && mozeSeBaciti) {
						try {
							stol.dodajMojeKarte(mojeKarte.getKarta(1));
							//doda karte na druge pozicije...
						//	stol.dodajDesneKarte(mojeKarte.getKarta(0));
						//	stol.dodajGornjeKarte(mojeKarte.getKarta(0));
						//	stol.dodajLijeveKarte(mojeKarte.getKarta(0));
						} catch (Exception ex) {
						}
						mojeKarte.izbaciKartu(1);
						mozeSeBaciti = false;
					}
				}
				if (e.getX()>=160 && e.getX()<240){
					//System.out.println("Stisnuta treca karta");
					if (naReduSam && mozeSeBaciti) {
						try {
							stol.dodajMojeKarte(mojeKarte.getKarta(2));
						} catch (Exception ex) {
						}
						mojeKarte.izbaciKartu(2);
						mozeSeBaciti = false;
					}
				}
				if (e.getX()>=240){
					//System.out.println("Stisnuta cetvrta karta");
					if (naReduSam && mozeSeBaciti) {
						try {
							stol.dodajMojeKarte(mojeKarte.getKarta(3));
						} catch (Exception ex) {
						}
						mojeKarte.izbaciKartu(3);
						mozeSeBaciti = false;
					}
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
        
        JButton gumbDosta = new JButton("Dosta");
        cp.add(gumbDosta);
        gumbDosta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
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
					
					switch( rbrKorisnika ){
						case 1 : stol.dodajDesneKarte(deck[idKarte]);break;
						case 2 : stol.dodajGornjeKarte(deck[idKarte]);break;
						case 3 : stol.dodajLijeveKarte(deck[idKarte]);break;
					}
					int odigraliSvi = Integer.parseInt(parm[2]);
					if ( odigraliSvi == 0 ) continue;
					System.out.println();
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
