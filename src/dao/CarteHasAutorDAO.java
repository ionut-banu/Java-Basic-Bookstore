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
import db.CarteHasAutorDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ionut
 */
public class CarteHasAutorDAO implements Serializable {

    public CarteHasAutorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CarteHasAutorDB carteHasAutorDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CarteDB carte = carteHasAutorDB.getCarte();
            if (carte != null) {
                carte = em.getReference(carte.getClass(), carte.getId());
                carteHasAutorDB.setCarte(carte);
            }
            em.persist(carteHasAutorDB);
            if (carte != null) {
                carte.getCarteHasAutorDBCollection().add(carteHasAutorDB);
                carte = em.merge(carte);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CarteHasAutorDB carteHasAutorDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CarteHasAutorDB persistentCarteHasAutorDB = em.find(CarteHasAutorDB.class, carteHasAutorDB.getId());
            CarteDB carteOld = persistentCarteHasAutorDB.getCarte();
            CarteDB carteNew = carteHasAutorDB.getCarte();
            if (carteNew != null) {
                carteNew = em.getReference(carteNew.getClass(), carteNew.getId());
                carteHasAutorDB.setCarte(carteNew);
            }
            carteHasAutorDB = em.merge(carteHasAutorDB);
            if (carteOld != null && !carteOld.equals(carteNew)) {
                carteOld.getCarteHasAutorDBCollection().remove(carteHasAutorDB);
                carteOld = em.merge(carteOld);
            }
            if (carteNew != null && !carteNew.equals(carteOld)) {
                carteNew.getCarteHasAutorDBCollection().add(carteHasAutorDB);
                carteNew = em.merge(carteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carteHasAutorDB.getId();
                if (findCarteHasAutorDB(id) == null) {
                    throw new NonexistentEntityException("The carteHasAutorDB with id " + id + " no longer exists.");
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
            CarteHasAutorDB carteHasAutorDB;
            try {
                carteHasAutorDB = em.getReference(CarteHasAutorDB.class, id);
                carteHasAutorDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carteHasAutorDB with id " + id + " no longer exists.", enfe);
            }
            CarteDB carte = carteHasAutorDB.getCarte();
            if (carte != null) {
                carte.getCarteHasAutorDBCollection().remove(carteHasAutorDB);
                carte = em.merge(carte);
            }
            em.remove(carteHasAutorDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CarteHasAutorDB> findCarteHasAutorDBEntities() {
        return findCarteHasAutorDBEntities(true, -1, -1);
    }

    public List<CarteHasAutorDB> findCarteHasAutorDBEntities(int maxResults, int firstResult) {
        return findCarteHasAutorDBEntities(false, maxResults, firstResult);
    }

    private List<CarteHasAutorDB> findCarteHasAutorDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CarteHasAutorDB.class));
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

    public CarteHasAutorDB findCarteHasAutorDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CarteHasAutorDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarteHasAutorDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CarteHasAutorDB> rt = cq.from(CarteHasAutorDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<CarteHasAutorDB> findChaByCarte(CarteDB carte){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT cha.* FROM carte_has_autor cha WHERE cha.carte = ?",CarteHasAutorDB.class);
        q.setParameter(1, carte.getId());
    
        return q.getResultList();
    }
}
