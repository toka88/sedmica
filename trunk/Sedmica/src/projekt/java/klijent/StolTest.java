package projekt.java.klijent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class StolTest extends JFrame{
	
	public StolTest(){
		super();
		initGUI();
	}
	
	private void initGUI(){
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Vizualizacija");
		
		// tabovi
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel panel1 = new JPanel();
		tabbedPane.add("Pregled",panel1);
		this.getContentPane().add(tabbedPane);
		panel1.setLayout(new GridLayout(2,1));
		panel1.add(new JLabel("Ovo je neki tekst"));
		
		CardTable stol = new CardTable();
		panel1.add(stol);

		Card[] deck =    SveKarte.dohvatiDeck();
		for(Card karta : deck){
			stol.dodajMojeKarte(karta);
			System.out.println("--> "+karta.getIdKarte());
		}
		stol.dodajGornjeKarte(SveKarte.dohvatiPozadinu());
		System.out.println(SveKarte.dohvatiPozadinu().getIdKarte());
		setSize(300,400);
		setVisible(true);
		this.pack();
	}
	
	public static void main(String[] args) throws InterruptedException, InvocationTargetException {
		SwingUtilities.invokeAndWait(
				new Runnable(){
					public void run(){
						StolTest app = new StolTest();
					}
				}
				);
	}
	

}
