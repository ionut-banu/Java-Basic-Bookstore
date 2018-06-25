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
@Table(name = "edituri")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EdituraDB.findAll", query = "SELECT e FROM EdituraDB e"),
    @NamedQuery(name = "EdituraDB.findById", query = "SELECT e FROM EdituraDB e WHERE e.id = :id"),
    @NamedQuery(name = "EdituraDB.findByNume", query = "SELECT e FROM EdituraDB e WHERE e.nume = :nume"),
    @NamedQuery(name = "EdituraDB.findByOras", query = "SELECT e FROM EdituraDB e WHERE e.oras = :oras")})
public class EdituraDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "oras")
    private String oras;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "editura")
    private Collection<CarteDB> carteDBCollection;

    public EdituraDB() {
    }

    public EdituraDB(Integer id) {
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

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    @XmlTransient
    public Collection<CarteDB> getCarteDBCollection() {
        return carteDBCollection;
    }

    public void setCarteDBCollection(Collection<CarteDB> carteDBCollection) {
        this.carteDBCollection = carteDBCollection;
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
        if (!(object instanceof EdituraDB)) {
            return false;
        }
        EdituraDB other = (EdituraDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nume+" "+oras;
    }
    
}
