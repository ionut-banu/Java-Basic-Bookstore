/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ionut
 */
@Entity
@Table(name = "autori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AutorDB.findAll", query = "SELECT a FROM AutorDB a"),
    @NamedQuery(name = "AutorDB.findById", query = "SELECT a FROM AutorDB a WHERE a.id = :id"),
    @NamedQuery(name = "AutorDB.findByNume", query = "SELECT a FROM AutorDB a WHERE a.nume = :nume"),
    @NamedQuery(name = "AutorDB.findByPrenume", query = "SELECT a FROM AutorDB a WHERE a.prenume = :prenume"),
    @NamedQuery(name = "AutorDB.findByCnp", query = "SELECT a FROM AutorDB a WHERE a.cnp = :cnp")})
public class AutorDB implements Serializable {
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

    public AutorDB() {
    }

    public AutorDB(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AutorDB)) {
            return false;
        }
        AutorDB other = (AutorDB) object;
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
