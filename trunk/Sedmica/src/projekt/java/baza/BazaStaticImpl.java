package projekt.java.baza;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import projekt.java.baza.dao.DBOperacija;
import projekt.java.baza.dao.PersistenceUtil;
import projekt.java.baza.model.Korisnik;
import projekt.java.klase.Igrac;


public class BazaStaticImpl implements SuceljeBaze{
	
	private static Map<String,Korisnik> korisnici=null;
	private final static String xmlFileName="korisnici.xml";
	private static Document doc=null;
	
	/**
	 * Ova metoda <b>mora</b> biti pozvana na poèetku paljenja servera.<p>Ona iz eksterne lokalne datoteku
	 * na serveru èita postojeæe korisnike i sprema ih u radnu memoriju u listu kako bi server lakše i brže
	 * dinamièki obavljao unos novog korisnika, prijave korisnika ili izmjene postojeæih podataka korisnika.</p>
	 * <p>Metoda opcionalno vraæa referencu na statièku varijablu u razredu ukoliko se želi manualno vršiti manipulacija
	 * nad podatcima, no to se <b>ne preporuèa</b>, nego je bolje za to koristiti odgovarajuæe metode</p>
	 * @return lista postojeæih korisnika(ili prazna lista ako nema korisnika) ili null ako je doslo do greske
	 */
	public static Map<String,Korisnik> ucitajKorisnikeIzDatoteke(){
		ArrayList<Korisnik> lista = KorisnikManager.dohvatiStatistike();
		korisnici = new HashMap<String,Korisnik>();
		for(Korisnik k : lista){
			korisnici.put(k.getKorisnik(), k);
		}
		return korisnici;
		/*DocumentBuilder docBuilder;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try{
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch(Exception ex){
			System.out.println("Kriva parser konfiguracija: "+ex.getMessage());
			return null;
		}
		File datoteka = new File(xmlFileName);
		try{
			if(!datoteka.isFile())
				datoteka.createNewFile();
			doc = docBuilder.parse(datoteka);
		}catch(SAXException ex){
			System.out.println("Kriva xml struktura datoteke ili prazna datoteka: ");
			return null;
		}
		catch(IOException ex){
			System.out.println("Greška pri èitanju izvorišne datoteke: "+ex.getMessage());
			return null;
		}
		Node root = doc.getDocumentElement();
		String nodeName = root.getNodeName();
		String nodeValue = getElementValue(root);
		NamedNodeMap attributes = root.getAttributes();
		
		for(int i=0; i<attributes.getLength(); i++){
			Node attribute = attributes.item(i);
			//
		}
		
		NodeList children = root.getChildNodes();
		for(int i=0; i<children.getLength(); i++){
			Node child = children.item(i);
			if (child.getNodeType()==root.ELEMENT_NODE){
				//
			}
		}
		
		return null;*/
	}
	
    /** Vraca vrijednost elementa
     * @param elem element (XML tag)
     * @return Element vrijednost inace prazan string
     */
    private final static String getElementValue( Node elem ) {
        Node kid;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
                    if( kid.getNodeType() == Node.TEXT_NODE  ){
                        return kid.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

	
	/**
	 * Ova metoda se <b>mora</b> pozvati na kraju izvoðenja servera.
	 * <p>Namjena je ta da sve podatke o korisnicima spremi iz radne memorije na hard disk u eksternu datoteku
	 * kako bi se osigurala perzistencija podataka.</p>
	 * <p><i>Napomena</i> : ova metoda ne briše (postavlja na <i>null</i>) listu o podatcima o korisniku tako 
	 * da je moguæe koristiti operacije spremanja i usred rada servera. Razlog tomu je moguænost perzistencije 
	 * podataka zbog šanse gubljenja podataka radi nekih tehnièkih pogreški kao što je npr nestanak struje i sl.</p>
	 * @return
	 */
	public static boolean spremiKorisnikeUDatoteku(){
		List<Korisnik> lista = new ArrayList<Korisnik>();
		for(String s : korisnici.keySet()){
			lista.add(korisnici.get(s));
		}
		KorisnikManager.update(lista);
		return true;
		/*File xmlOutputFile = new File(xmlFileName);
		FileOutputStream fos;
		Transformer transformer;
		try{
			fos = new FileOutputStream(xmlOutputFile);
		}catch(FileNotFoundException ex){
			System.out.println("Error: " + ex.getMessage());
			return false;
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException ex) {
            System.out.println("Transformer configuration error: " + ex.getMessage());
            return false;
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fos);
        try {
            transformer.transform(source, result);
        }
        catch (TransformerException e) {
            System.out.println("Error transform: " + e.getMessage());
        }
        System.out.println("XML file saved.");
        return true;*/
	}

	/** {@inheritDoc} */
	@Override
	public boolean azuriraj(String korisnik, int bodovi) {
		Korisnik k = korisnici.get(korisnik);
		if(k==null)
			return false;
		k.setBodovi(k.getBodovi()+bodovi);
		korisnici.put(korisnik, k);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Igrac dohvatiIgraca(String korisnik) {
		Korisnik k = korisnici.get(korisnik);
		if (k==null)
			return null;
		Igrac ig = new Igrac(k.getId().intValue(),k.getKorisnik(),null);
		ig.setBrojBodova(k.getBodovi());
		return ig;
	}

	/** {@inheritDoc} */
	@Override
	public ArrayList<Igrac> dohvatiStatistike() {
		ArrayList<Igrac> igraci = new ArrayList<Igrac>();
		for(String s : korisnici.keySet()){
			Korisnik k = korisnici.get(s);
			Igrac ig = new Igrac(k.getId().intValue(),k.getKorisnik(),null);
			igraci.add(ig);
		}
		return igraci;
	}

	/** {@inheritDoc} */
	@Override
	public String login(String korisnik, String pass) {
		Korisnik k = korisnici.get(korisnik);
		if (k==null)
			return "NEUSJPJESNO#2#Krivo korisnicko ime i/ili lozinka";
		else if (!k.getLozinka().equals(pass))
			return "NEUSJPJESNO#2#Krivo korisnicko ime i/ili lozinka";
		if (k.isOnline()==true){
			k.setOnline(false);	
			return "NEUSJPJESNO#3#Korisnik je vec prijavljen";
		}
		k.setOnline(true);
		return "USPJESNO#1#Autorizacija uspjesna, korisnik je prijavljen";
	}

	/** {@inheritDoc} */
	@Override
	public boolean logout(String korisnik) {
		Korisnik k = korisnici.get(korisnik);
		if (k==null)
			return false;
		k.setOnline(false);
		korisnici.put(korisnik, k);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean registriraj(String korisnik, String lozinka, String email) {
		Korisnik k = new Korisnik();
		if (korisnici.get(korisnik)!=null)
			return false;
		k.setKorisnik(korisnik);
		k.setLozinka(lozinka);
		k.setEmail(email);
		k.setBodovi(0);
		k.setBrojPartija(0);
		k.setOnline(false);
		korisnici.put(korisnik, k);
		return true;
	}

}
