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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "carti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarteDB.findAll", query = "SELECT c FROM CarteDB c"),
    @NamedQuery(name = "CarteDB.findById", query = "SELECT c FROM CarteDB c WHERE c.id = :id"),
    @NamedQuery(name = "CarteDB.findByIsbn", query = "SELECT c FROM CarteDB c WHERE c.isbn = :isbn"),
    @NamedQuery(name = "CarteDB.findByTitlu", query = "SELECT c FROM CarteDB c WHERE c.titlu = :titlu"),
    @NamedQuery(name = "CarteDB.findByStoc", query = "SELECT c FROM CarteDB c WHERE c.stoc = :stoc"),
    @NamedQuery(name = "CarteDB.findByPret", query = "SELECT c FROM CarteDB c WHERE c.pret = :pret")})
public class CarteDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "titlu")
    private String titlu;
    @Column(name = "stoc")
    private Integer stoc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pret")
    private Double pret;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carte")
    private Collection<ComandaHasCarteDB> comandaHasCarteDBCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carte")
    private Collection<CarteHasAutorDB> carteHasAutorDBCollection;
    @JoinColumn(name = "editura", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EdituraDB editura;
    @JoinColumn(name = "furnizor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FurnizorDB furnizor;

    public CarteDB() {
    }

    public CarteDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public Integer getStoc() {
        return stoc;
    }

    public void setStoc(Integer stoc) {
        this.stoc = stoc;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    @XmlTransient
    public Collection<ComandaHasCarteDB> getComandaHasCarteDBCollection() {
        return comandaHasCarteDBCollection;
    }

    public void setComandaHasCarteDBCollection(Collection<ComandaHasCarteDB> comandaHasCarteDBCollection) {
        this.comandaHasCarteDBCollection = comandaHasCarteDBCollection;
    }

    @XmlTransient
    public Collection<CarteHasAutorDB> getCarteHasAutorDBCollection() {
        return carteHasAutorDBCollection;
    }

    public void setCarteHasAutorDBCollection(Collection<CarteHasAutorDB> carteHasAutorDBCollection) {
        this.carteHasAutorDBCollection = carteHasAutorDBCollection;
    }

    public EdituraDB getEditura() {
        return editura;
    }

    public void setEditura(EdituraDB editura) {
        this.editura = editura;
    }

    public FurnizorDB getFurnizor() {
        return furnizor;
    }

    public void setFurnizor(FurnizorDB furnizor) {
        this.furnizor = furnizor;
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
        if (!(object instanceof CarteDB)) {
            return false;
        }
        CarteDB other = (CarteDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return titlu;
    }
    
}
