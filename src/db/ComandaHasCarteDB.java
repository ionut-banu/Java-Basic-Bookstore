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
@Table(name = "comanda_has_carte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComandaHasCarteDB.findAll", query = "SELECT c FROM ComandaHasCarteDB c"),
    @NamedQuery(name = "ComandaHasCarteDB.findById", query = "SELECT c FROM ComandaHasCarteDB c WHERE c.id = :id"),
    @NamedQuery(name = "ComandaHasCarteDB.findByCantitate", query = "SELECT c FROM ComandaHasCarteDB c WHERE c.cantitate = :cantitate")})
public class ComandaHasCarteDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "cantitate")
    private Integer cantitate;
    @JoinColumn(name = "carte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CarteDB carte;
    @JoinColumn(name = "comanda", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ComandaDB comanda;

    public ComandaHasCarteDB() {
    }

    public ComandaHasCarteDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantitate() {
        return cantitate;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
    }

    public CarteDB getCarte() {
        return carte;
    }

    public void setCarte(CarteDB carte) {
        this.carte = carte;
    }

    public ComandaDB getComanda() {
        return comanda;
    }

    public void setComanda(ComandaDB comanda) {
        this.comanda = comanda;
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
        if (!(object instanceof ComandaHasCarteDB)) {
            return false;
        }
        ComandaHasCarteDB other = (ComandaHasCarteDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.ComandaHasCarteDB[ id=" + id + " ]";
    }
    
}
