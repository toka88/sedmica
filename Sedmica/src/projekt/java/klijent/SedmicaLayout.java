package projekt.java.klijent;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class SedmicaLayout implements LayoutManager {

	private int minSirina=400;
	private int minVisina=400;
	private int preferredSirina = 400;
	private int preferredVisina=400;
	
	/**
	 * Konstruktor
	 */
	public SedmicaLayout() {
	}
	
	
	/**
	 * Dodaje komponentu u <tt>layout</tt>.
	 */
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * Razmješta komponente iz <tt>container</tt>-a unutar 
	 * <tt>layout</tt>-a.
	 * @param parent <tt>cointainer</tt> koji sadrži komponente koje se
	 * razmještaju
	 */
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		double x=insets.left;
		double y=insets.top;
		
		Component komponenta = null;
		Dimension dimenzija = null;
		
		postaviVelicine(parent);
		
		
		komponenta = parent.getComponent(0);
		dimenzija = komponenta.getPreferredSize();
		x=x+parent.getSize().width/(double)16;
		y=y+parent.getSize().height/(double)16;
		komponenta.setBounds((int)x, (int)y, (int)(dimenzija.width), (int)dimenzija.height);
		
		SkupKarata komponenta2 = (SkupKarata)parent.getComponent(1);
		dimenzija = komponenta2.getPreferredSize();
		x=insets.left+parent.getSize().width/(double)2-komponenta2.getKarta(0).getWidth()*2;
		y=insets.top+parent.getSize().height/(double)40;
		komponenta2.setBounds((int)x, (int)y, (int)(komponenta2.getKarta(0).getWidth()*(double)4+25), 
				(int)komponenta2.getKarta(0).getHeight()+10);
		
		SkupKarata komponenta3 = (SkupKarata)parent.getComponent(2);
		dimenzija = komponenta3.getPreferredSize();
		x=insets.left;
		y=insets.top+parent.getSize().height/(double)2-komponenta3.getKarta(0).getHeight()/2;
		komponenta3.setBounds((int)x, (int)y, (int)(komponenta3.getKarta(0).getWidth()*(double)2.5+10), 
				(int)(komponenta3.getKarta(0).getHeight()*13/10)+10);
		
		
		
		
		
		
		CardTable komponenta4 = (CardTable)parent.getComponent(4);
		dimenzija = komponenta4.getPreferredSize();
		x=insets.left+parent.getSize().width/(double)2-komponenta4.getTableSize().width/(double)2;
		y=insets.top+parent.getSize().height/(double)2-komponenta4.getTableSize().height/(double)2;
		komponenta4.setBounds((int)x, (int)y, komponenta4.getTableSize().width, 
				komponenta4.getTableSize().height);
		
		SkupKarata komponenta5 = (SkupKarata)parent.getComponent(3);
		dimenzija = komponenta5.getPreferredSize();
		x=parent.getSize().getWidth()-insets.right-(komponenta5.getKarta(0).getWidth()*(double)2.5+10);
		y=insets.top+parent.getSize().height/(double)2-komponenta5.getKarta(0).getHeight()/2;
		komponenta5.setBounds((int)x, (int)y, (int)(komponenta5.getKarta(0).getWidth()*(double)2.5+10), 
				(int)(komponenta5.getKarta(0).getHeight()*13/10)+10);
		
		SkupKarataKorisnik komponenta6 = (SkupKarataKorisnik)parent.getComponent(5);
		dimenzija = komponenta6.getPreferredSize();
		x=insets.left+parent.getSize().width/(double)2-komponenta6.getKarta(0).getWidth()*2;
		y=parent.getSize().getHeight()-insets.bottom-parent.getSize().height/(double)40-
			komponenta6.getKarta(0).getHeight();
		komponenta6.setBounds((int)x, (int)y, (int)(komponenta6.getKarta(0).getWidth()*(double)4+25), 
				(int)komponenta6.getKarta(0).getHeight()+10);
		
		Component komponenta7 = parent.getComponent(6);
		dimenzija = komponenta7.getPreferredSize();
		x=parent.getSize().getWidth()-insets.right-parent.getSize().width/(double)16-
			dimenzija.width;
		y=parent.getSize().getHeight()-insets.bottom-parent.getSize().height/(double)16-
			dimenzija.height;
		komponenta7.setBounds((int)x, (int)y, (int)(dimenzija.width), (int)dimenzija.height);
		
		
	}

	/**
	 * Postavlja velièine <tt>layout</tt>-a.
	 * <p>
	 * Prolazi kroz sve komponente <tt>container</tt>-a te raèuna
	 * preferirane i minimalne visine i širine <tt>layout</tt>-a.
	 * @param parent <tt>container</tt> koji zove StackLayout
	 */
	private void postaviVelicine(Container parent) {
		//Dimension d = null;
		//Resetiranje preferiranih/minimalnih širina i dužina
		//preferredSirina=0;
		//preferredVisina=0;
		//minSirina=0;
		//minVisina=0;
		
		//Component comp=null;
		
		//preferredSirina=parent.getComponent(0).getWidth()+parent.getComponent(1).getWidth();
		//preferredVisina=parent.getSize().height;
		//minSirina=preferredSirina;
		//minVisina=preferredVisina;
	}
	
	/**
	 * Postavlja minimalne dimenzije <tt>layout</tt>-a.<p>
	 * Container ovu metodu zove da bi saznao koliko velik
	 * <tt>layout</tt> mora biti. 
	 * @return dim minimalne dimenzije
	 */
	public Dimension minimumLayoutSize(Container parent) {
		Insets insets;
		
		synchronized (parent.getTreeLock()) {
			insets = parent.getInsets();
			minSirina = 0;
			minVisina = 0;
			Component[] comp = parent.getComponents();
			if (minSirina < comp[0].getMinimumSize().width
					+ comp[1].getMinimumSize().width) {
				minSirina = comp[0].getMinimumSize().width
						+ comp[1].getMinimumSize().width;
			}
			if (minSirina < comp[2].getMinimumSize().width
					+ comp[3].getMinimumSize().width
					+ comp[4].getMinimumSize().width) {
				minSirina = comp[2].getMinimumSize().width
						+ comp[3].getMinimumSize().width
						+ comp[4].getMinimumSize().width;
			}
			if (minSirina < comp[5].getMinimumSize().width
					+ comp[6].getMinimumSize().width) {
				minSirina = comp[5].getMinimumSize().width
						+ comp[6].getMinimumSize().width;
			}
		}
		return new Dimension(insets.left + insets.right + minSirina,
				insets.top + insets.bottom + minVisina);
	}
	

	/**
	 * Postavlja preferirane dimenzije <tt>layout</tt>-a.<p>
	 * Container ovu metodu zove da bi saznao koliko velik
	 * <tt>layout</tt> treba biti. 
	 * @return dim potrebne dimenzije
	 */
	public Dimension preferredLayoutSize(Container parent) {
		Insets insets;
		
		synchronized (parent.getTreeLock()) {
			insets = parent.getInsets();
			preferredSirina = 0;
			preferredVisina = 0;
			Component[] comp = parent.getComponents();
			if (preferredSirina < comp[0].getPreferredSize().width
					+ comp[1].getPreferredSize().width) {
				preferredSirina = comp[0].getPreferredSize().width
						+ comp[1].getPreferredSize().width;
			}
			if (preferredSirina < comp[2].getPreferredSize().width
					+ comp[3].getPreferredSize().width
					+ comp[4].getPreferredSize().width) {
				preferredSirina = comp[2].getPreferredSize().width
						+ comp[3].getPreferredSize().width
						+ comp[4].getPreferredSize().width;
			}
			if (preferredSirina < comp[5].getPreferredSize().width
					+ comp[6].getPreferredSize().width) {
				preferredSirina = comp[5].getPreferredSize().width
						+ comp[6].getPreferredSize().width;
			}
		}
		return new Dimension(insets.left + insets.right + preferredSirina,
				insets.top + insets.bottom + preferredVisina);
	}

	/**
	 * Mièe komponentu iz <tt>layout</tt>-a.
	 */
	public void removeLayoutComponent(Component comp) {
		
	}

}
