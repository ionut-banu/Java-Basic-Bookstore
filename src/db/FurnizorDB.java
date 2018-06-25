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
@Table(name = "furnizori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FurnizorDB.findAll", query = "SELECT f FROM FurnizorDB f"),
    @NamedQuery(name = "FurnizorDB.findById", query = "SELECT f FROM FurnizorDB f WHERE f.id = :id"),
    @NamedQuery(name = "FurnizorDB.findByNume", query = "SELECT f FROM FurnizorDB f WHERE f.nume = :nume"),
    @NamedQuery(name = "FurnizorDB.findByTel", query = "SELECT f FROM FurnizorDB f WHERE f.tel = :tel")})
public class FurnizorDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "tel")
    private String tel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "furnizor")
    private Collection<CarteDB> carteDBCollection;

    public FurnizorDB() {
    }

    public FurnizorDB(Integer id) {
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
        if (!(object instanceof FurnizorDB)) {
            return false;
        }
        FurnizorDB other = (FurnizorDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nume+" "+tel;
    }
    
}
