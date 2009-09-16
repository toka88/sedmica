package projekt.java.klijent;

import javax.swing.SwingUtilities;

public class Bezveze {
	
	
	public static void main(String[] args) {
		final Paket veza = null;
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				new Igra(veza, null, 0);
			}
		});
	}
	
}
