/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ionut
 */
@Entity
@Table(name = "comenzi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComandaDB.findAll", query = "SELECT c FROM ComandaDB c"),
    @NamedQuery(name = "ComandaDB.findById", query = "SELECT c FROM ComandaDB c WHERE c.id = :id"),
    @NamedQuery(name = "ComandaDB.findByDate", query = "SELECT c FROM ComandaDB c WHERE c.date = :date"),
    @NamedQuery(name = "ComandaDB.findByStatus", query = "SELECT c FROM ComandaDB c WHERE c.status = :status"),
    @NamedQuery(name = "ComandaDB.findByTotal", query = "SELECT c FROM ComandaDB c WHERE c.total = :total")})
public class ComandaDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "status")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private Double total;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ClientDB client;

    public ComandaDB() {
    }

    public ComandaDB(Integer id) {
        this.id = id;
    }

    public ComandaDB(Integer id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ClientDB getClient() {
        return client;
    }

    public void setClient(ClientDB client) {
        this.client = client;
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
        if (!(object instanceof ComandaDB)) {
            return false;
        }
        ComandaDB other = (ComandaDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id+" "+date+" "+status;
    }
    
}
