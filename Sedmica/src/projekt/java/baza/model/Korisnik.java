package projekt.java.baza.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *	Razred ima funkciju entiteta Korisnik.
 *
 * @author EmanuelLacic
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Korisnik.poImenu",query="select k from Korisnik as k where korisnik=:korisnik"),
        @NamedQuery(name="Korisnik.svi",query="select k from Korisnik as k order by k.korisnik"),
        @NamedQuery(name="Korisnik.imeSifra",query="select k from Korisnik as k where korisnik=:korisnik and lozinka=:sifra")
})
@Table(name="korisnici")
public class Korisnik implements Serializable {

        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String korisnik;
        private String lozinka;
        private int bodovi;
        private int broj_partija;
        private String email;
        private boolean online;
        private int version;
        
        @Id @GeneratedValue
        public Long getId() {
                return id;
        }
        public void setId(Long id) {
                this.id = id;
        }
        
        @Column(unique=true,length=50,nullable=false)
        public String getKorisnik() {
                return korisnik;
        }
        public void setKorisnik(String korisnik) {
                this.korisnik = korisnik;
        }

        @Column(unique=false,length=50,nullable=false)
        public String getLozinka() {
                return lozinka;
        }
        public void setLozinka(String lozinka) {
                this.lozinka = lozinka;
        }

        @Column(unique=false,length=20,nullable=true)
        public int getBodovi() {
                return bodovi;
        }
        public void setBodovi(int bodovi) {
                this.bodovi = bodovi;
        }

        @Column(unique=false,length=20,nullable=true)
        public String getEmail() {
                return email;
        }
        public void setEmail(String email) {
                this.email = email;
        }
        
        public void setBrojPartija(int broj_partija) {
			this.broj_partija = broj_partija;
		}
        
        @Column(unique=false,length=20,nullable=true)
		public int getBrojPartija() {
			return broj_partija;
		}
        
		public void setOnline(boolean online) {
			this.online = online;
		}
		
		@Column(unique=false,length=20,nullable=true)
		public boolean isOnline() {
			return online;
		}
		
		@Version
        public int getVersion() {
                return version;
        }
		
        public void setVersion(int version) {
                this.version = version;
        }
        
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                return result;
        }
        
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                final Korisnik other = (Korisnik) obj;
                if (id == null) {
                        if (other.id != null)
                                return false;
                } else if (!id.equals(other.id))
                        return false;
                return true;
        }
}
