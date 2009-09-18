package projekt.java.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import projekt.java.klase.Igrac;
import projekt.java.klase.Potez;
import projekt.java.klase.Soba;

/**
 * Razred DretvaPartija brine se za sve oko partije
 * @author Martin Morava
 *
 */
public class DretvaPartija implements Runnable {
	
	volatile Soba soba;
	//LinkedListe koje sluz za razmjenu poruka izmedu dretvi.
	volatile LinkedList<Potez> dolazniFIFO;
	//Igraci koji su u igri
	ArrayList<Igrac> popisIgraca;
	//Igraju li 2,3 ili 4 igraca
	int igraciMod;
	byte[] karte;
	Stack<Byte> talon;
	
	/**
	 * Inicijalizira variable i mje≈°a karte
	 * 
	 * @param soba Soba kojom baratamo
	 * @param dolazniFIFO Lista za dolazne pruke
	 * @param odlazniFIFO Lista za odlazne poruke
	 */
	public DretvaPartija(Soba soba){
		this.soba=soba;
		this.dolazniFIFO=soba.getListaPoteza();
		this.popisIgraca=soba.getPopisIgraca();
		this.talon=new Stack<Byte>();
		igraciMod=popisIgraca.size();
		IzmjesajKarte();
	}
	
	/**
	 * Puni polje karte[] brojevima od 1 do 32,pri cemu
	 * svaki broj oznacava pojedinu kartu..
	 * 
	 * Mjesa karte, odredeni slucajni broj puta, a mjesa tako
	 * da uzme 2 slucajna broja izmedu 0 i 31, te zanmjeni
	 * elemente na mjestu 1 i mjestu 2 ovisno o odabranim
	 * brojevima
	 */
	private void IzmjesajKarte() {
		karte=new byte[32];
		
		for(int i=0;i<32;i++){
			karte[i]=(byte) (i+1);
		}
		
		//Inicijaliziraj generator brojeva
		Random generiraj=new Random(2000L);
		int brojPermutacija,prviBroj,drugiBroj;
		byte temp;
		brojPermutacija=300+generiraj.nextInt()%701;//min 300,max 1000mjesanja
		
		//Promjesaj karte
		for(int i=0;i<brojPermutacija;i++){
			prviBroj=generiraj.nextInt()%32;
			drugiBroj=generiraj.nextInt()%32;
			temp=karte[prviBroj];
			karte[prviBroj]=karte[drugiBroj];
			karte[drugiBroj]=temp;
		}
		//Spremi izmjesane karte na talon
		for(int i=0;i<32;i++){
			talon.add(karte[i]);
		}
	}

	/**
	 * Overrideana run metoda sucelja Runnable.Pokrece odredeni
	 * model igre ovisno o broju igraca koji igru igraju
	 */
	
	@Override
	public void run() {
		if((igraciMod==2)||(igraciMod==3)){
			//igraBezParova();
		}
		else{
			igraSaParovima();
		}
	}

	/**
	 * Implementacija igre s parovima.Oba igraca u pobjednickom
	 * timu dobivaju bodove
	 */
	private void igraSaParovima() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Implementacija partije u igri bez parova.Samo
	 * pobjednik dobiva bodove
	 *//*
	private void igraBezParova() {
		//Trenutni igrac opisuje igraca u vodstvu
		int trenutniIgrac=0,primljeno = 0,pocetniIgrac=0;
		byte prvaKarta = 0;
		ArrayList<Igrac> listaIgraca=soba.getPopisIgraca();
		//Kreiraj stog za svakog igraca koji pamti igraceve osvojene karte
		ArrayList<Stack<Byte>> osvojeneKarte=new ArrayList<Stack<Byte>>();
		//Ova kolekcija prati karte koje trenutno drzi svaki igrac
		ArrayList<LinkedList<Byte>> uRuci=new ArrayList<LinkedList<Byte>>();
		
		Stack<Byte> naStolu=new Stack<Byte>();
		for(int i=0;i<listaIgraca.size();i++){
			osvojeneKarte.add(new Stack<Byte>());
			uRuci.add(new LinkedList<Byte>());
		}
		podjeliKarte(uRuci,4,0);
		
		//Glavna partija
		//»ekaj da svi odigraju
		boolean zastavica=true;//oznacava resetiranje primljenog
		int zaPrimiti=listaIgraca.size();
		while(true){
			if(zastavica){
				primljeno=0;
				zastavica=false;
			}
			for(int i=0;i<listaIgraca.size();i++){//Primi 4 karte
				while(dolazniFIFO.isEmpty()){};//Pricekaj da dode poruka
				Potez odigranPotez=dolazniFIFO.getFirst();
				dolazniFIFO.removeFirst();
				int indexIgraca=VratiIndexIgraca(odigranPotez.getKljucKorisnika());
				naStolu.push(odigranPotez.getKodKarte());
				if(primljeno==0){
					prvaKarta=odigranPotez.getKodKarte();
				}
				primljeno++;
				MakniKartuIzRuke(odigranPotez.getKodKarte(),indexIgraca,uRuci);
				if(OdrediVodeceg(prvaKarta,odigranPotez.getKodKarte())){
					trenutniIgrac=indexIgraca;
				}
				PosaljiIgracimaOdigranuKartu(odigranPotez.getKodKarte(),indexIgraca);
			}
				//Ima li prvi igrac pravo tuc?
				if(isSmijeTuci(uRuci.get(pocetniIgrac),prvaKarta)){
					Potez potez=new Potez();
					potez.setKodKarte((byte) 1);
					potez.setKljucKorisnika(listaIgraca.get(pocetniIgrac).getKljucKorisnika());
					potez.setTuce(true);
					posaljiKartu(potez, 14);
					//TODO Pricekaj odgovor
					while(dolazniFIFO.isEmpty());
					Potez odigranPotez=dolazniFIFO.getFirst();
					dolazniFIFO.removeFirst();
					if(potez.getKodKarte()==34){
						PribrojiBodove(listaIgraca.get(trenutniIgrac),naStolu);
						pocetniIgrac=trenutniIgrac;
						//TODO preskakanje igraca
						podjeliKarte(uRuci,4-uRuci.get(trenutniIgrac).size(),trenutniIgrac);
						zastavica=true;
					}
					else{//TODO tuci
						}
					}
			else{
				PribrojiBodove(listaIgraca.get(trenutniIgrac),naStolu);
				pocetniIgrac=trenutniIgrac;
				//TODO preskakanje igraca
				podjeliKarte(uRuci,4-uRuci.get(trenutniIgrac).size(),trenutniIgrac);
				zastavica=true;
			}
		
		}
		
		}
*/
	private void PribrojiBodove(Igrac igrac, Stack<Byte> naStolu) {
		int bodovi=igrac.getBrojBodova();
		while(!naStolu.isEmpty()){
			Byte karta=naStolu.pop();
			if((karta==8)||(karta==4)||(karta%8==0)||(karta%8==4)){
				bodovi++;
			}
		}
		igrac.setBrojBodova(bodovi);
		
	}

	/**
	 * Na temelju odigrane i prve karte na stolu odredi vodeceg igraca
	 * Ako trenutni igrac postaje vodeci vraca true inace false
	 * @param prvaKarta
	 * @param kodKarte
	 * @return
	 */
	private boolean OdrediVodeceg(byte prvaKarta, byte kodKarte) {
		if((kodKarte-1)%8==0){
			return true;
		}
		
		if(kodKarte>8){
			kodKarte%=8;
		}
		if(prvaKarta>8){
			prvaKarta%=8;
		}
		if(kodKarte==prvaKarta){
			return true;
		}
		return false;
	}

	/**
	 * Ima li priliku prvi igrac tuci?
	 * 
	 * @param linkedList
	 * @param prvaKarta Donja karta na stolu
	 * @return
	 */
	private boolean isSmijeTuci(LinkedList<Byte> uRuci, byte prvaKarta) {
		Iterator<Byte> i=uRuci.iterator();
		while(i.hasNext()){
			Byte karta=(Byte)i.next();
			if(((karta-1)%8==0)){
				return true;//Igrac ima 7icu u ruci ili kartu iste vrjednosti prvoj bacenoj
			}
			//Namjesit vrjednosti karte na "bazicne" 1,2,3,4,5,6,7,8
			byte prviMod=prvaKarta,drugiMod=karta;
			if(prviMod>8){
				prviMod=(byte) (prviMod%8);
			}
			if(drugiMod>8){
				drugiMod=(byte) (drugiMod%8);
			}
			//Ako su im vrjednosti iste onda igrac moze tuc
			if(prviMod==drugiMod){
				return true;
			}
			
			
		}
		return false;
	}

	/**
	 * Mice kartu koju je odigrao iz ruke igraca
	 * @param kodKarte
	 * @param indexIgraca
	 * @param uRuci
	 */
	private void MakniKartuIzRuke(byte kodKarte, int indexIgraca,
			ArrayList<LinkedList<Byte>> uRuci) {
			LinkedList<Byte> ruka=uRuci.get(indexIgraca);
			Iterator i=ruka.iterator();
			while(i.hasNext()){
				Byte karta=(Byte) i.next();
				if(karta.equals(Byte.valueOf(kodKarte))){
					i.remove();
					return;
				}
			}
	}

	/**
	 * Vraca mjesto igraca u Listi
	 * @param kljucKorisnika
	 */
	private int VratiIndexIgraca(int kljucKorisnika) {
			ArrayList<Igrac> igraci=soba.getPopisIgraca();
			for(int i=0;i<igraci.size();i++){
				if(igraci.get(i).getKljucKorisnika()==kljucKorisnika){
					return i;
				}
			}
			return 0;
		
	}

	/**
	 * Djeli karte na pocetku partije
	 *//*
	private void podjeliKarte(ArrayList<LinkedList<Byte>> uRuci,int brojKarata,int sljedeciIgrac) {
		int brojIgraca=soba.getPopisIgraca().size();
		
		for(int i=0;i<brojKarata;i++){//Prolazi brojKarata puta
			for(int j=0;j<brojIgraca;j++){//Svako dobije 1 kartu
			Potez potez=new Potez();
			//Kad prvom igracu djelis zadnju kartu dozvoli mu igrati
			if((i==brojKarata-1)&&(j==sljedeciIgrac)){
				potez.setSmijesIgrati(true);	
			}
		
			potez.setKljucKorisnika(soba.getPopisIgraca().get(j)
						.getKljucKorisnika());
			//Kartu s vrha stoga stavi korisniku u ruku i posalji mu poruku o tome
			byte karta=talon.pop();
			potez.setKodKarte(karta);
			uRuci.get(i).add(karta);
			posaljiKartu(potez,12);	//Kod 12 saljem ti kartu	
			}
		}
		
	}
	*/
	/**
	 * Salje poruku svim igracima o odigranoj karti
	 * @param kodKarte Kod odigrane karte
	 * @param indexIgraca Indeks igraca koji je kartu odigrao
	 *//*
	private void PosaljiIgracimaOdigranuKartu(byte kodKarte,int indexIgraca) {
		ArrayList<Igrac> igraci=soba.getPopisIgraca();
		for(int i=0;i<igraci.size();i++){
			Potez potez=new Potez();
			if(i==(indexIgraca+1)%igraci.size()){
				potez.setSmijesIgrati(true);
			}
			potez.setKodKarte(kodKarte);
			potez.setKljucKorisnika(igraci.get(i).getKljucKorisnika());
			posaljiKartu(potez, 13);//Kod 13 znaci koja je karta odigrana
		}
		
	}
	*/
	/**
	 * Salje poruku o kartu
	 * 
	 * @param potez Potez kome se salje, koja karta, smije li igrati
	 * @param kod Kod 3 znaci slanje nove karte,kod 4 znaci slanje karte koja
	 * je odigrana
	 *//*
	private void posaljiKartu(Potez potez,int kod) {
	Igrac trenutniIgrac=soba.getPopisIgraca().get(VratiIndexIgraca(potez.getKljucKorisnika()));
	//TODO posalji kartu
	DataOutputStream posiljatelj=trenutniIgrac.getOdlazniTok();
	try {
		posiljatelj.writeBytes(kod+"#"+potez.getKodKarte()+"#"+potez.isSmijesIgrati()+"#"+potez.isTuce());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Slanje karte"+potez.getKodKarte()+" korisniku "+potez.getKljucKorisnika()+" nije uspjelo!");
	}
	}
	*/
}


