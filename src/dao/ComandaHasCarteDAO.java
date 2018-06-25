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
import db.CarteDB;
import db.ComandaDB;
import db.ComandaHasCarteDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ionut
 */
public class ComandaHasCarteDAO implements Serializable {

    public ComandaHasCarteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComandaHasCarteDB comandaHasCarteDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CarteDB carte = comandaHasCarteDB.getCarte();
            if (carte != null) {
                carte = em.getReference(carte.getClass(), carte.getId());
                comandaHasCarteDB.setCarte(carte);
            }
            em.persist(comandaHasCarteDB);
            if (carte != null) {
                carte.getComandaHasCarteDBCollection().add(comandaHasCarteDB);
                carte = em.merge(carte);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComandaHasCarteDB comandaHasCarteDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComandaHasCarteDB persistentComandaHasCarteDB = em.find(ComandaHasCarteDB.class, comandaHasCarteDB.getId());
            CarteDB carteOld = persistentComandaHasCarteDB.getCarte();
            CarteDB carteNew = comandaHasCarteDB.getCarte();
            if (carteNew != null) {
                carteNew = em.getReference(carteNew.getClass(), carteNew.getId());
                comandaHasCarteDB.setCarte(carteNew);
            }
            comandaHasCarteDB = em.merge(comandaHasCarteDB);
            if (carteOld != null && !carteOld.equals(carteNew)) {
                carteOld.getComandaHasCarteDBCollection().remove(comandaHasCarteDB);
                carteOld = em.merge(carteOld);
            }
            if (carteNew != null && !carteNew.equals(carteOld)) {
                carteNew.getComandaHasCarteDBCollection().add(comandaHasCarteDB);
                carteNew = em.merge(carteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comandaHasCarteDB.getId();
                if (findComandaHasCarteDB(id) == null) {
                    throw new NonexistentEntityException("The comandaHasCarteDB with id " + id + " no longer exists.");
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
            ComandaHasCarteDB comandaHasCarteDB;
            try {
                comandaHasCarteDB = em.getReference(ComandaHasCarteDB.class, id);
                comandaHasCarteDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comandaHasCarteDB with id " + id + " no longer exists.", enfe);
            }
            CarteDB carte = comandaHasCarteDB.getCarte();
            if (carte != null) {
                carte.getComandaHasCarteDBCollection().remove(comandaHasCarteDB);
                carte = em.merge(carte);
            }
            em.remove(comandaHasCarteDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComandaHasCarteDB> findComandaHasCarteDBEntities() {
        return findComandaHasCarteDBEntities(true, -1, -1);
    }

    public List<ComandaHasCarteDB> findComandaHasCarteDBEntities(int maxResults, int firstResult) {
        return findComandaHasCarteDBEntities(false, maxResults, firstResult);
    }

    private List<ComandaHasCarteDB> findComandaHasCarteDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComandaHasCarteDB.class));
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

    public ComandaHasCarteDB findComandaHasCarteDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComandaHasCarteDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getComandaHasCarteDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComandaHasCarteDB> rt = cq.from(ComandaHasCarteDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public int findChaByComandaAndCarte(ComandaDB comanda, CarteDB carte){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT cantitate FROM comanda_has_carte chc WHERE "
                                            +"chc.comanda = ? &&"
                                            +"chc.carte = ?");
        q.setParameter(1, comanda.getId());
        q.setParameter(2, carte.getId());
        
        return (int)q.getSingleResult();
    }
    
    public List<ComandaHasCarteDB> findChcByCarte(CarteDB carte){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT chc.* FROM comanda_has_carte chc WHERE chc.carte = ?",ComandaHasCarteDB.class);
        q.setParameter(1, carte.getId());
        
        return q.getResultList();
    }
     
}
