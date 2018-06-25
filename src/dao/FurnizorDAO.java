/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.CarteDB;
import db.FurnizorDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ionut
 */
public class FurnizorDAO implements Serializable {

    public FurnizorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FurnizorDB furnizorDB) {
        if (furnizorDB.getCarteDBCollection() == null) {
            furnizorDB.setCarteDBCollection(new ArrayList<CarteDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<CarteDB> attachedCarteDBCollection = new ArrayList<CarteDB>();
            for (CarteDB carteDBCollectionCarteDBToAttach : furnizorDB.getCarteDBCollection()) {
                carteDBCollectionCarteDBToAttach = em.getReference(carteDBCollectionCarteDBToAttach.getClass(), carteDBCollectionCarteDBToAttach.getId());
                attachedCarteDBCollection.add(carteDBCollectionCarteDBToAttach);
            }
            furnizorDB.setCarteDBCollection(attachedCarteDBCollection);
            em.persist(furnizorDB);
            for (CarteDB carteDBCollectionCarteDB : furnizorDB.getCarteDBCollection()) {
                FurnizorDB oldFurnizorOfCarteDBCollectionCarteDB = carteDBCollectionCarteDB.getFurnizor();
                carteDBCollectionCarteDB.setFurnizor(furnizorDB);
                carteDBCollectionCarteDB = em.merge(carteDBCollectionCarteDB);
                if (oldFurnizorOfCarteDBCollectionCarteDB != null) {
                    oldFurnizorOfCarteDBCollectionCarteDB.getCarteDBCollection().remove(carteDBCollectionCarteDB);
                    oldFurnizorOfCarteDBCollectionCarteDB = em.merge(oldFurnizorOfCarteDBCollectionCarteDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FurnizorDB furnizorDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FurnizorDB persistentFurnizorDB = em.find(FurnizorDB.class, furnizorDB.getId());
            Collection<CarteDB> carteDBCollectionOld = persistentFurnizorDB.getCarteDBCollection();
            Collection<CarteDB> carteDBCollectionNew = furnizorDB.getCarteDBCollection();
            List<String> illegalOrphanMessages = null;
            for (CarteDB carteDBCollectionOldCarteDB : carteDBCollectionOld) {
                if (!carteDBCollectionNew.contains(carteDBCollectionOldCarteDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CarteDB " + carteDBCollectionOldCarteDB + " since its furnizor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<CarteDB> attachedCarteDBCollectionNew = new ArrayList<CarteDB>();
            for (CarteDB carteDBCollectionNewCarteDBToAttach : carteDBCollectionNew) {
                carteDBCollectionNewCarteDBToAttach = em.getReference(carteDBCollectionNewCarteDBToAttach.getClass(), carteDBCollectionNewCarteDBToAttach.getId());
                attachedCarteDBCollectionNew.add(carteDBCollectionNewCarteDBToAttach);
            }
            carteDBCollectionNew = attachedCarteDBCollectionNew;
            furnizorDB.setCarteDBCollection(carteDBCollectionNew);
            furnizorDB = em.merge(furnizorDB);
            for (CarteDB carteDBCollectionNewCarteDB : carteDBCollectionNew) {
                if (!carteDBCollectionOld.contains(carteDBCollectionNewCarteDB)) {
                    FurnizorDB oldFurnizorOfCarteDBCollectionNewCarteDB = carteDBCollectionNewCarteDB.getFurnizor();
                    carteDBCollectionNewCarteDB.setFurnizor(furnizorDB);
                    carteDBCollectionNewCarteDB = em.merge(carteDBCollectionNewCarteDB);
                    if (oldFurnizorOfCarteDBCollectionNewCarteDB != null && !oldFurnizorOfCarteDBCollectionNewCarteDB.equals(furnizorDB)) {
                        oldFurnizorOfCarteDBCollectionNewCarteDB.getCarteDBCollection().remove(carteDBCollectionNewCarteDB);
                        oldFurnizorOfCarteDBCollectionNewCarteDB = em.merge(oldFurnizorOfCarteDBCollectionNewCarteDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = furnizorDB.getId();
                if (findFurnizorDB(id) == null) {
                    throw new NonexistentEntityException("The furnizorDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FurnizorDB furnizorDB;
            try {
                furnizorDB = em.getReference(FurnizorDB.class, id);
                furnizorDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The furnizorDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CarteDB> carteDBCollectionOrphanCheck = furnizorDB.getCarteDBCollection();
            for (CarteDB carteDBCollectionOrphanCheckCarteDB : carteDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FurnizorDB (" + furnizorDB + ") cannot be destroyed since the CarteDB " + carteDBCollectionOrphanCheckCarteDB + " in its carteDBCollection field has a non-nullable furnizor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(furnizorDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FurnizorDB> findFurnizorDBEntities() {
        return findFurnizorDBEntities(true, -1, -1);
    }

    public List<FurnizorDB> findFurnizorDBEntities(int maxResults, int firstResult) {
        return findFurnizorDBEntities(false, maxResults, firstResult);
    }

    private List<FurnizorDB> findFurnizorDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FurnizorDB.class));
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

    public FurnizorDB findFurnizorDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FurnizorDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getFurnizorDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FurnizorDB> rt = cq.from(FurnizorDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public FurnizorDB findFurnizorByNume(String nume){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("FurnizorDB.findByNume");
        q.setParameter("nume", nume);
        
        try{
            return (FurnizorDB)q.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
    public List<FurnizorDB> findFurnizoriByComenzi(){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT f.* "
                                        +"FROM comenzi com, carti c, comanda_has_carte chc, furnizori f "
                                        +"WHERE com.id IN (SELECT com2.id "
                                                          +"FROM comenzi com2 "
                                                          +"WHERE com2.status LIKE 'IN DESFASURARE') && " 
                                        +"chc.comanda = com.id && "
                                        +"chc.carte = c.id && "
                                        +"c.furnizor = f.id",FurnizorDB.class);
        return q.getResultList();
    }
    
}
