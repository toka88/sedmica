package projekt.java.klijent;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import projekt.java.klase.*;


/**
 * Kartaška igra sedmica.
 * @author Mario
 *
 */
public class Proba extends JFrame{

	private static final long serialVersionUID = 1L;
	
	/** Korisnièko ime */
	private static String korisnickoIme;
	/** lozinka */
	private static String lozinka;
	/** Port*/
	private static int serverPort;
	/** Server ime ili ip*/
	private static String server;
	
	/** Soba na koju je korisnik kliknuo.*/
	private Soba odabranaSoba;
	
	private DefaultListModel modelSoba = new DefaultListModel();
	private DefaultListModel modelIgraci = new DefaultListModel();
	
	/** Lista soba koja se dobiva sa servera*/
	private List<Soba> listaSoba;
	
	/** Lista igraèa u odabranoj sobi*/
	private ArrayList<Igrac> listaIgraca;

	/** Predstavlaj vezu prema serveru. Nudi sve operacije komunikacije s serverom.*/
	private static Paket veza;
	/** Igrac koji je pokrenuo aplikaciju */
	private static Igrac igrac;
		
	
	/* Zapoèni aplikaciju. */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				prijava();
			}

		});
	}
	
	/**
	 * Konstruktor
	 */
	public Proba() {
		initGUI();
	}
	
	/**
	 * Inicijalizacija GUI-ja
	 */
	private void initGUI() {
		setLocation(110, 110);
		setSize(280, 320); 
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Pregled soba");
		
		napuniListu();
		
		/////////////////M E N U    B A R ////////////////////////////////////////////////////////////
		JMenu menu;
        JMenuBar menuBar;
        JMenuItem menuItem;

        //Stvori menu
        menuBar = new JMenuBar();

        //Prvi Menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);

        //a group of JMenuItems
        ////////////// R E F R E S H   S O B E /////////////////////////////////
        menuItem = new JMenuItem("Osvježi sobe", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {			
					@Override
					public void run() {
						napuniListu();	
					}
				});
				
			}
        }); 
       // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
       // menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(menuItem);
        
        ///////////////// S T A T I S T I K E ///////////////////////////////
        menuItem = new JMenuItem("Statistike", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				final DefaultListModel statistikaIgraci = new DefaultListModel();
				final DefaultListModel statistikaBodovi = new DefaultListModel();
				
				SwingWorker<List<Igrac>,Void> worker = new SwingWorker<List<Igrac>,Void>() {

					protected List<Igrac> doInBackground() throws Exception {
						ArrayList<Igrac> listaStatistika= new ArrayList<Igrac>();
						listaStatistika = veza.dohvatiStatistike();
						return listaStatistika;
					}
					
					protected void done() {
						ArrayList<Igrac> listaStatistika= new ArrayList<Igrac>();
						try {
							listaStatistika = (ArrayList<Igrac>) get();
						} catch (InterruptedException e) {
						} catch (ExecutionException e) {
						}
						for(Igrac igr : listaStatistika) {
							statistikaIgraci.addElement(igr.getIme());
							statistikaBodovi.addElement(igr.getBrojBodova());
						}
					}
					
				};
				worker.execute();
				
				JList listaIgr = new JList(statistikaIgraci);
				JList listaBodova = new JList(statistikaBodovi);
				
				final JPanel panel = new JPanel(new GridLayout(1,2));
				panel.add(listaIgr);
				panel.add(listaBodova);

				SwingUtilities.invokeLater(new Runnable() {			
					@Override
					public void run() {
						new Statistike(panel);
					}
				});
			}
        });
        menu.add(menuItem);

        
        menu.addSeparator();

        
        menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
        menu.add(menuItem);



        //Build second menu in the menu bar.
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuBar.add(menu);
        
        setJMenuBar(menuBar);
        
        //////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		
		final JList lista1 = new JList(modelSoba);
		final JList lista2 = new JList(modelIgraci);
		
		lista1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
       lista1.addListSelectionListener(new SobaListener(lista2));
        lista1.setSelectedIndex(1);
		lista1.setSelectedIndex(0);
        JScrollPane sobaScroll = new JScrollPane(lista1);
        
		lista2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista2.setSelectedIndex(0);
        JScrollPane igracScroll = new JScrollPane(lista2);
		
        
        /* Slaganje liste igraèa i soba */
        JPanel panel = new JPanel(new GridLayout(1,2));
        add(panel, BorderLayout.CENTER);
        JPanel panel1 = new JPanel(new GridLayout(0,1));
        JPanel panel2 = new JPanel(new GridLayout(0,1));
        panel1.add(sobaScroll);
        panel1.setBorder(BorderFactory.createTitledBorder("Sobe "));
        panel2.add(igracScroll);
        panel2.setBorder(BorderFactory.createTitledBorder("Igraèi "));
        panel.add(panel1);
        panel.add(panel2);
        
        
        
        
        
        
        JButton gumbZaIgru = new JButton("Uði u sobu");
        JPanel panelDonji = new JPanel(new GridLayout(1,3));
        
        add(panelDonji, BorderLayout.SOUTH);
        JPanel lijevoDolje = new JPanel(new GridLayout(1,2));
        lijevoDolje.setLayout(new BoxLayout(lijevoDolje, BoxLayout.Y_AXIS));
        JPanel sredinaDolje = new JPanel();
        sredinaDolje.setLayout(new BoxLayout(sredinaDolje, BoxLayout.Y_AXIS));
        JPanel desnoDolje = new JPanel();
        desnoDolje.setLayout(new BoxLayout(desnoDolje,BoxLayout.Y_AXIS));
        panelDonji.add(lijevoDolje);
        panelDonji.add(sredinaDolje);
        panelDonji.add(desnoDolje);
        
        gumbZaIgru.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lijevoDolje.add(gumbZaIgru);
  
                
        //////////////U Ð I   U   S O B U ///////////////////////////////////////////
        gumbZaIgru.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {			
					@Override
					public void run() {
						
						String error = veza.udjiUSobu(igrac, lista1.getSelectedIndex()); 
						if( error == null ){
							
							/*TODO treba neko rješenje da se ugasi pozadinska aplikacija 
							 * setVisible(false); */
							
							new Igra(veza);
						} else {
							JOptionPane.showMessageDialog(null, error, "Greska!", JOptionPane.ERROR_MESSAGE);
						}
					
						
					}
				});
				
			}
        });
      
        //////////////// L O G O U T //////////////////////////////////////
        JButton gumbLogOut = new JButton("Log off");
        gumbLogOut.setAlignmentX(Component.CENTER_ALIGNMENT);
        desnoDolje.add(gumbLogOut);
        gumbLogOut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {		
				String err = veza.logout(igrac);
				if ( err == null ){
					System.out.println("Nešto ne valja. ");
				}
				
				setVisible(false);
				dispose();
				prijava();
			
			}
        	
        });
        
		setVisible(true);
	}

	/**
	 * Metoda puni listu soba i igraca. Lista soba prima se sa
	 * servera preko objekta veza. Objektima iz liste soba puni se
	 * modelSoba.
	 */
	private void napuniListu() {
		
		SwingWorker<ArrayList<Soba>,Void> worker = new SwingWorker<ArrayList<Soba>,Void>() {

			protected ArrayList<Soba> doInBackground() throws Exception {
				ArrayList<Soba> listaSoba;
				listaSoba = veza.dohvatiSobe();
				return listaSoba;
			}
			
			protected void done() {
				try {
					listaSoba = get();
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				}
				modelSoba.clear();
				for(Soba s : listaSoba) {
					modelSoba.addElement(s.getIdSobe() + ". " + ( s.isSobaZakljucana()?"Zakljuèana":"Otvorena  "  )     
							+  " ( " + s.getPopisIgraca().size()+ "/" + s.getMaxBrojIgraca() + " )");
				}
			}
			
		};
		worker.execute();		
	}

	/**
	 * Otvara prozor za prijavu korisnika za igru.
	 */
	private static void prijava() {
		///////////////GUI KOMPONENTE/////////////////////////////////
		JLabel labServer = new JLabel("Server");
		JLabel labPort = new JLabel("Port");
		JLabel labKorIme = new JLabel("Korisnièko ime");
		JLabel labKorPass = new JLabel("Zaporka");
		
		JTextField tfServer = new JTextField("");
		JTextField tfPort = new JTextField("");
		JTextField tfKorIme = new JTextField("");
		JPasswordField tfKorPass = new JPasswordField("");		
		
		JPanel panel = new JPanel(new GridLayout(4,4));
		panel.add(labServer);
		panel.add(tfServer);
		panel.add(labPort);
		panel.add(tfPort);
		panel.add(labKorIme);
		panel.add(tfKorIme);
		panel.add(labKorPass);
		panel.add(tfKorPass);
		////////////////////////////////////////////////

		// Imena pojedinog gumba u dijalogu
		Object[] options = {"Poèni igru",
                "Odustani",
                "Registracija"};
		int result = JOptionPane.showOptionDialog(null, panel, "Poèetak igre", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		
		
		/* Ako je korisnik upisao podatke i kliknuo na gumobiæ Poèni Igru ( YES ) */
		if(result == JOptionPane.YES_OPTION) {
			/* Dohvat informacija od korisnika*/
				korisnickoIme = "Mario";//tfKorIme.getText();
				/*TODO hardkodirana šifra zbog sljanaj */
				lozinka = "lovac";//tfKorPass.getPassword().toString();
				try{
				serverPort = 2222;//Integer.parseInt(tfPort.getText());
				}catch (NumberFormatException ex){
					prijava();
					return;
				}
				/*TODO OPREZ HARDKODIRANI PARAMETRI ULAZA!!! */
				server =  tfServer.getText();//"00.00.00.00";//
				veza = new Paket(server,serverPort);			
				igrac = new Igrac(1,korisnickoIme);
				
				
				/* SwingWorker koji obavlja operaciju autorizacije korisnika. */
				SwingWorker<String,Void> worker = new SwingWorker<String,Void>(){

					@Override
					protected String doInBackground() throws Exception {
						String gr = "Greska";
						try{
							veza.logout(igrac);
							gr = veza.login(igrac, lozinka);
						}catch(Exception ex){
							
						}
						return gr;
					}
					@Override
					protected void done() {
						try {
							final String greska = get();
							System.out.println(greska+" !!");
							if (greska!=null){
								SwingUtilities.invokeLater(new Runnable() {			
									@Override
									public void run() {
										JOptionPane.showMessageDialog(null, greska, "Greska!", JOptionPane.ERROR_MESSAGE);
										prijava();
									}
								});
							}else {
							
								/* Pokretanje aplikacije za igranje*/	
								SwingUtilities.invokeLater(new Runnable() {			
									@Override
									public void run() {
										new Proba();
									}
								});
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						super.done();
					}
					
				};
				worker.execute();
		} else if(result == JOptionPane.NO_OPTION) {
			//Ako je korisnik odabrao odustati od aplikacije.
		} else if(result == JOptionPane.CANCEL_OPTION) {
			// Izabrana je registracija korisnika
			// Otvara se novi dijalog za registraciju
			///////////////////// R E G I S T R A C I J A //////////////////////////
			
			
			/////////////////////GUI KOMPONENTE///////////////////////////////////////
			final JLabel ime = new JLabel("Ime");
			final JLabel zaporka = new JLabel("Zaporka");
			JLabel ponovljenaZaporka = new JLabel("Ponovi zaporku");
			final JLabel eMail = new JLabel("e-mail");
			
			JTextField tfIme = new JTextField("");
			JPasswordField tfZaporka = new JPasswordField("");
			JPasswordField tfPonZaporka = new JPasswordField("");
			JTextField tfEMail = new JTextField("");
			
			JPanel panelReg = new JPanel(new GridLayout(4,4));
			panelReg.add(ime);
			panelReg.add(tfIme);
			panelReg.add(zaporka);
			panelReg.add(tfZaporka);
			panelReg.add(ponovljenaZaporka);
			panelReg.add(tfPonZaporka);			
			panelReg.add(eMail);
			panelReg.add(tfEMail);
			//////////////////////////////////////////////////////////////////////////
			int resultReg = JOptionPane.showConfirmDialog(null, panelReg, 
					"Registracija", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			// Ako korisnik odabere OK, unešeni podaci spremaju se
			// u bazu, i ponovno se otvara dijalog za prijavu
			// na server
			if(resultReg==JOptionPane.OK_OPTION) {
				SwingWorker<String,Void> worker = new SwingWorker<String,Void>() {

					protected String doInBackground() throws Exception {
						String rezultat = veza.registrirajKorisnika(ime.getText(), zaporka.getText(), 
								eMail.getText());
						return rezultat;
					}
					
					
					protected void done() {
						final String rezultat;
						try {
							rezultat = get();
							if(rezultat.equals("radi")) {
								SwingUtilities.invokeLater(new Runnable() {			
									@Override
									public void run() {
										JOptionPane.showMessageDialog(null, rezultat);
									}
								});
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					
					}
					
				};
				worker.execute();
				
				prijava();
			}
		}
		
	}

	/**
	 * Osluškuje promjene u listi soba i prema odabranom 
	 * elementu mijenja listu igraèa.
	 * @author Kristijan
	 *
	 */
	class SobaListener implements ListSelectionListener {
		JList listaIgr;
		
		public SobaListener(JList lista) {
			listaIgr = lista;
		}
		
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;
			JList theList = (JList)e.getSource();
			int index = theList.getSelectedIndex();
			if (index < 0) {
				return;
			}

			odabranaSoba = listaSoba.get(index);			
			listaIgraca = new ArrayList<Igrac>();

			ArrayList<Igrac> list = odabranaSoba.getPopisIgraca();
			for(Igrac igr : list){
				Igrac igrac = new Igrac(igr.getKljucKorisnika(),igr.getIme());
				listaIgraca.add(igrac);
			}
			modelIgraci.clear();
			for(Igrac igr : listaIgraca) {
				modelIgraci.addElement(igr.getIme());
			}
			listaIgr.setSelectedIndex(0);
		}
		
	}
		
	
}
