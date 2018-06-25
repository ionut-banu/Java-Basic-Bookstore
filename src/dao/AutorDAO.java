/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import db.AutorDB;
import db.CarteDB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Ionut
 */
public class AutorDAO implements Serializable {

    public AutorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AutorDB autorDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(autorDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AutorDB autorDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            autorDB = em.merge(autorDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autorDB.getId();
                if (findAutorDB(id) == null) {
                    throw new NonexistentEntityException("The autorDB with id " + id + " no longer exists.");
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
            AutorDB autorDB;
            try {
                autorDB = em.getReference(AutorDB.class, id);
                autorDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autorDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(autorDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AutorDB> findAutorDBEntities() {
        return findAutorDBEntities(true, -1, -1);
    }

    public List<AutorDB> findAutorDBEntities(int maxResults, int firstResult) {
        return findAutorDBEntities(false, maxResults, firstResult);
    }

    private List<AutorDB> findAutorDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AutorDB.class));
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

    public AutorDB findAutorDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AutorDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutorDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AutorDB> rt = cq.from(AutorDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public AutorDB findAutorByCNP(String cnp){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("AutorDB.findByCnp");
        q.setParameter("cnp", cnp);
        
        try{
            return (AutorDB) q.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
    public List<AutorDB> findAutoriByCarte(CarteDB carte){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT a.* FROM carti c, autori a, carte_has_autor cha WHERE "
                                            +"a.id = cha.autor && "
                                            +"cha.carte = c.id &&"
                                            +"c.id = ?", AutorDB.class);
        q.setParameter(1, carte.getId());
        return q.getResultList();
    }
}
