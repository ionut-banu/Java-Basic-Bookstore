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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ionut
 */
@Entity
@Table(name = "carte_has_autor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarteHasAutorDB.findAll", query = "SELECT c FROM CarteHasAutorDB c"),
    @NamedQuery(name = "CarteHasAutorDB.findById", query = "SELECT c FROM CarteHasAutorDB c WHERE c.id = :id")})
public class CarteHasAutorDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "autor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AutorDB autor;
    @JoinColumn(name = "carte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CarteDB carte;

    public CarteHasAutorDB() {
    }

    public CarteHasAutorDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AutorDB getAutor() {
        return autor;
    }

    public void setAutor(AutorDB autor) {
        this.autor = autor;
    }

    public CarteDB getCarte() {
        return carte;
    }

    public void setCarte(CarteDB carte) {
        this.carte = carte;
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
        if (!(object instanceof CarteHasAutorDB)) {
            return false;
        }
        CarteHasAutorDB other = (CarteHasAutorDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.CarteHasAutorDB[ id=" + id + " ]";
    }
    
}
