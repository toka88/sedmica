package projekt.java.klijent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
/**
 * Predstavlja klasu za prikaz statistika.
 * @author Mario
 *
 */
public class Statistike extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	/**
	 * Konstruktor
	 */
	public Statistike(JPanel panel) {
		this.panel = panel;
		initGUI();
	}

	/** Inicijalizacija GUI.a */
	private void initGUI() {
		setLocation(140, 110);
		setSize(250, 400);
		setTitle("Statistike");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.getContentPane().add(panel);
		setVisible(true);
	}

}
