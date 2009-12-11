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
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import projekt.java.klase.Igrac;
import projekt.java.klase.LjudiUSobi;
import projekt.java.klase.Potez;

/**
 * Predstavlja ploèu za igru koju korisnik vidi tijekom igre.
 * @author Mario
 * @version 1.0
 */
public class Igra extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static Card[] deck;
	/** Veza prema serveru */
	private Paket veza;	
	/** Ako je true, korisnik je na redu za bacanje karte*/
	private boolean naReduSam;/*OVO OMOGUÆUJE DA STAVLJAŠ KARTE NA STOL */
	/** Ime igraca */
	private Igrac igrac;
	/** Identifikacijiski broj sobe */
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
        
        /* Karata koja je prva bacena */
        JLabel prvaKarta = new JLabel("_________________");
        prvaKarta.setBorder(BorderFactory.createTitledBorder("Prva Karta"));
        cp.add(prvaKarta);
        
        
        /* Postavljanje slike karata i pozadine. */
        deck = SveKarte.dohvatiDeck();
        final Card pozadina = SveKarte.dohvatiPozadinu();
          
        /* Inicijalizacija prikaza karata. */      
        final SkupKarata desnoKarte = new SkupKarata(pozadina,"desno");
        desnoKarte.setVisible(false);
        cp.add(desnoKarte);//desno karte   
        
        final SkupKarata gornjeKarte = new SkupKarata(pozadina,"gore");
        gornjeKarte.setVisible(false);
        cp.add(gornjeKarte);//gornje karte
        
        final SkupKarata lijevoKarte = new SkupKarata(pozadina,"lijevo");
        lijevoKarte.setVisible(false);
        cp.add(lijevoKarte);//lijevo karte
        /* Inicijalizacija stola. */
        final CardTable stol = new CardTable();
        cp.add(stol);
        stol.setTableSize(400, 300);

        
        /* Dohvati najnovije stanje o broju igraca u sobi */
        LjudiUSobi ljudiUSobi = veza.dohvatiLjudeUSobi(idSobe, igrac.getKljucKorisnika());     
        String[] imenaLjudi = ljudiUSobi.getLjudiUSobi();
        try {     
        
        	if( imenaLjudi.length > 0 && !imenaLjudi[0].isEmpty()){
        	lijevoKarte.setImeIgraca(imenaLjudi[0]);
        	cp.getComponent(3).setVisible(true);

	        gornjeKarte.setImeIgraca(imenaLjudi[1]);
	        cp.getComponent(2).setVisible(true);

	        desnoKarte.setImeIgraca(imenaLjudi[2]);
	        cp.getComponent(1).setVisible(true);
        	}
        } catch (IndexOutOfBoundsException ex ){
        	//nema veze ;)
        }
        //ako nema nikoga u sobi trenutno ljudi je 0
        int temp = imenaLjudi.length + 1;// inace ce biti 1,2,3 + 1(broji mene)
        if( imenaLjudi[0].isEmpty() ){
        	temp = 1; //samo sam ja u sobi
        } 
        	
        final int trenutnoLjudi = temp;
        final int maxLjudi = ljudiUSobi.getMaxLjudi();
      
        
      
        
        final SkupKarataKorisnik mojeKarte = new SkupKarataKorisnik(pozadina,pozadina,pozadina,pozadina);
        cp.add(mojeKarte);//moje karte        	   
        
        
        mojeKarte.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				int indeksKarte = -1; 
				if (e.getX()<=80){ indeksKarte = 0; }
				if (e.getX()>80&& e.getX()<160){ indeksKarte = 1; }
				if (e.getX()>=160 && e.getX()<240){ indeksKarte = 2; }
				if (e.getX()>=240){ indeksKarte = 3; }
				if (naReduSam && indeksKarte != -1 ) {
					try {
						stol.dodajMojeKarte(mojeKarte.getKarta(indeksKarte));
					} catch (Exception ex) {
					}
					byte[] posaljiKartu = new byte[1];
					posaljiKartu[0] = (byte)mojeKarte.getKarta(indeksKarte).getIdKarte();
					veza.povuciPotez(idSobe, igrac.getKljucKorisnika(),posaljiKartu );
					mojeKarte.izbaciKartu(indeksKarte);
					naReduSam = false;
					indeksKarte = -1;
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				/*TODO to treba dodati metodu koja ce povecati kartu nad kojom se nalazi mis */
			}
			@Override
			public void mouseExited(MouseEvent e) {
				/*TODO to treba dodati metodu koja ce vratiti kartu u pr*/
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
        /* TODO Osigurat da se Dosta ne može stisnuti kad igrac ima sve karte u ruci */
        gumbDosta.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if( naReduSam ){
					Potez potez = veza.dohvatiKarte(igrac.getKljucKorisnika());
					byte[] karte = potez.getKodKarte();
					for( int i = 0; i < karte.length; i++ ) {
						mojeKarte.umetniKartu(deck[karte[i]]);
						desnoKarte.umetniKartu(pozadina);
						gornjeKarte.umetniKartu(pozadina);
						lijevoKarte.umetniKartu(pozadina);
					}
					stol.makniSveKarteSaStola();
				}
				else JOptionPane.showMessageDialog(null, 
						"<html><center>Nisi trenutno na redu ;).</center><br></html>",
						"Poruka sustava",JOptionPane.ERROR_MESSAGE);
			}
        	
        });
        
        
        /* SwingWorker èeka u pozadini za nove odigrane karte */
        final SwingWorker<String,Void> workerCekajPotez = new SwingWorker<String,Void>(){
			@Override
			protected String doInBackground() throws Exception {
			while(true){
				if(!naReduSam){//dodata zastavicu da ne trazi stvari bezveze.
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
					Potez potez = veza.dohvatiKarte(igrac.getKljucKorisnika());
					//Ako imam donju kartu onda mogu dalje igrati
					if ( potez.isMozesDalje()){
						naReduSam = true;
						stol.makniSveKarteSaStola();
						continue;
					}

					byte[] karte = potez.getKodKarte();
					for( int i = 0; i < karte.length; i++ ) {
						mojeKarte.umetniKartu(deck[karte[i]]);
						desnoKarte.umetniKartu(pozadina);
						gornjeKarte.umetniKartu(pozadina);
						lijevoKarte.umetniKartu(pozadina);
					}

					/*TODO tu se moze dodati delay da se karte stignu pogledati */
					stol.makniSveKarteSaStola();;
					naReduSam = true;
				}
			}
			}
        
        };
        

        
        
        
        /* SwingWorker èeka u pozadini igraèe da doðu igrati... */
        SwingWorker<String,Void> workerCekaIgrace = new SwingWorker<String,Void>(){
        	private int noviIgrac = 1;
        	private int tL = trenutnoLjudi;
			@Override
			protected String doInBackground() throws Exception {
				while ( maxLjudi > tL ){
					String imeIgraca = veza.cekajIgrace( idSobe );
					SkupKarata karte = (SkupKarata) getContentPane().getComponent(noviIgrac++);
					karte.setVisible(true);
					karte.setImeIgraca(imeIgraca);
					tL++;
				}				
				Potez potez = veza.dohvatiKarte(igrac.getKljucKorisnika());
				byte[] karte = potez.getKodKarte();
				mojeKarte.ubaciKarte(deck[karte[0]],deck[karte[1]],deck[karte[2]],deck[karte[3]]);
				naReduSam = potez.isMojPotez();
				workerCekajPotez.execute();
				
				veza.zapocniIgru(idSobe);
				return null;
			}
        
        };
        workerCekaIgrace.execute();
        
   
        ////setResizable(false);
		setVisible(true);
	}
	

		
	
}
