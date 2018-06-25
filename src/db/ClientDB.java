/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ionut
 */
@Entity
@Table(name = "clienti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientDB.findAll", query = "SELECT c FROM ClientDB c"),
    @NamedQuery(name = "ClientDB.findById", query = "SELECT c FROM ClientDB c WHERE c.id = :id"),
    @NamedQuery(name = "ClientDB.findByNume", query = "SELECT c FROM ClientDB c WHERE c.nume = :nume"),
    @NamedQuery(name = "ClientDB.findByPrenume", query = "SELECT c FROM ClientDB c WHERE c.prenume = :prenume"),
    @NamedQuery(name = "ClientDB.findByCnp", query = "SELECT c FROM ClientDB c WHERE c.cnp = :cnp"),
    @NamedQuery(name = "ClientDB.findByOras", query = "SELECT c FROM ClientDB c WHERE c.oras = :oras"),
    @NamedQuery(name = "ClientDB.findByStrada", query = "SELECT c FROM ClientDB c WHERE c.strada = :strada"),
    @NamedQuery(name = "ClientDB.findByNr", query = "SELECT c FROM ClientDB c WHERE c.nr = :nr"),
    @NamedQuery(name = "ClientDB.findByTel", query = "SELECT c FROM ClientDB c WHERE c.tel = :tel"),
    @NamedQuery(name = "ClientDB.findByEmail", query = "SELECT c FROM ClientDB c WHERE c.email = :email")})
public class ClientDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "prenume")
    private String prenume;
    @Column(name = "CNP")
    private String cnp;
    @Column(name = "oras")
    private String oras;
    @Column(name = "strada")
    private String strada;
    @Column(name = "nr")
    private Integer nr;
    @Column(name = "tel")
    private String tel;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Collection<ComandaDB> comandaDBCollection;

    public ClientDB() {
    }

    public ClientDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getStrada() {
        return strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public Integer getNr() {
        return nr;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public Collection<ComandaDB> getComandaDBCollection() {
        return comandaDBCollection;
    }

    public void setComandaDBCollection(Collection<ComandaDB> comandaDBCollection) {
        this.comandaDBCollection = comandaDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientDB)) {
            return false;
        }
        ClientDB other = (ClientDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nume+" "+prenume;
    }
    
}
