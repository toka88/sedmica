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
	
	/** Ako je true karta se može baciti, u protivnom, karta je baèena.*/
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
        SkupKarata gornjeKarte = new SkupKarata(3,pozadina,"gore");
        cp.add(gornjeKarte);//gornje karte
        
        SkupKarata lijevoKarte = new SkupKarata(1,pozadina,"lijevo");
        cp.add(lijevoKarte);//lijevo karte
        
        SkupKarata desnoKarte = new SkupKarata(1,pozadina,"lijevo");
        cp.add(desnoKarte);//desno karte       
        
        /* Inicijalizacija stola. */
        final CardTable stol = new CardTable();
        cp.add(stol);
        stol.setTableSize(400, 300);


        
        final SkupKarataKorisnik mojeKarte = new SkupKarataKorisnik(deck[1],deck[2],deck[3],deck[4]);
        cp.add(mojeKarte);//moje karte        	   
        
        // hardkodiranje
            naReduSam = true;
            mozeSeBaciti = true;
        mojeKarte.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getX()<=80){
					System.out.println("Stisnuta prva karta");
					if (naReduSam && mozeSeBaciti) {
						try {
							stol.dodajMojeKarte(mojeKarte.getKarta(0));
						} catch (Exception ex) {
						}
						int[] karte = null;
						veza.povuciPotez(idSobe, igrac.getKljucKorisnika(), 0, karte);
						mojeKarte.izbaciKartu(0);
						mozeSeBaciti = false;
					}
				}
				if (e.getX()>80&& e.getX()<160){
					System.out.println("Stisnuta druga karta");
					if (naReduSam && mozeSeBaciti) {
						try {
							stol.dodajMojeKarte(mojeKarte.getKarta(1));
						} catch (Exception ex) {
						}
						mojeKarte.izbaciKartu(1);
						mozeSeBaciti = false;
					}
				}
				if (e.getX()>=160 && e.getX()<240){
					System.out.println("Stisnuta treca karta");
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
					System.out.println("Stisnuta cetvrta karta");
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
        
        SwingWorker<String,Void> worker = new SwingWorker<String,Void>(){

			@Override
			protected String doInBackground() throws Exception {
	
				return null;
			}
        
        };
        worker.execute();
        
        setResizable(false);
		setVisible(true);
	}
}
