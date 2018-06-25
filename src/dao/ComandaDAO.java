/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.ClientDB;
import db.ComandaDB;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ionut
 */
public class ComandaDAO implements Serializable {

    public ComandaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComandaDB comandaDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientDB client = comandaDB.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getId());
                comandaDB.setClient(client);
            }
            em.persist(comandaDB);
            if (client != null) {
                client.getComandaDBCollection().add(comandaDB);
                client = em.merge(client);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComandaDB comandaDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComandaDB persistentComandaDB = em.find(ComandaDB.class, comandaDB.getId());
            ClientDB clientOld = persistentComandaDB.getClient();
            ClientDB clientNew = comandaDB.getClient();
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getId());
                comandaDB.setClient(clientNew);
            }
            comandaDB = em.merge(comandaDB);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getComandaDBCollection().remove(comandaDB);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getComandaDBCollection().add(comandaDB);
                clientNew = em.merge(clientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comandaDB.getId();
                if (findComandaDB(id) == null) {
                    throw new NonexistentEntityException("The comandaDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComandaDB comandaDB;
            try {
                comandaDB = em.getReference(ComandaDB.class, id);
                comandaDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comandaDB with id " + id + " no longer exists.", enfe);
            }
            ClientDB client = comandaDB.getClient();
            if (client != null) {
                client.getComandaDBCollection().remove(comandaDB);
                client = em.merge(client);
            }
            em.remove(comandaDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComandaDB> findComandaDBEntities() {
        return findComandaDBEntities(true, -1, -1);
    }

    public List<ComandaDB> findComandaDBEntities(int maxResults, int firstResult) {
        return findComandaDBEntities(false, maxResults, firstResult);
    }

    private List<ComandaDB> findComandaDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComandaDB.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ComandaDB findComandaDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComandaDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getComandaDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComandaDB> rt = cq.from(ComandaDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<ComandaDB> findComenziByClient(ClientDB client){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT comenzi.* FROM clienti, comenzi WHERE "
                                            +"comenzi.client = clienti.id &&"
                                            +"clienti.id = ?",ComandaDB.class);
        q.setParameter(1, client.getId());
        
        return q.getResultList();
    }  
    
    public double findTotalForComanda(ComandaDB comanda){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT SUM(pret*cantitate) FROM comanda_has_carte chc, carti WHERE "
                                                +"chc.carte = carti.id && "
                                                +"chc.comanda = ?");
        q.setParameter(1, comanda.getId());
        try{
            return (double)q.getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }
    
}
