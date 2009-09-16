package projekt.java.klijent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class About extends JFrame  {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Konstruktor
	 */
	public About() {
		initGUI();
	}
	
	/** Inicijalizacija GUI.a */
	private void initGUI() {
		setLocation(140, 110);
		setSize(300, 400); 
		setTitle("O Sedmici");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JLabel labela = new JLabel("Sedmica v1.0");
		this.getContentPane().add(labela);
        setResizable(false);
		setVisible(true);
	}
}
