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
import db.EdituraDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ionut
 */
public class EdituraDAO implements Serializable {

    public EdituraDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EdituraDB edituraDB) {
        if (edituraDB.getCarteDBCollection() == null) {
            edituraDB.setCarteDBCollection(new ArrayList<CarteDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<CarteDB> attachedCarteDBCollection = new ArrayList<CarteDB>();
            for (CarteDB carteDBCollectionCarteDBToAttach : edituraDB.getCarteDBCollection()) {
                carteDBCollectionCarteDBToAttach = em.getReference(carteDBCollectionCarteDBToAttach.getClass(), carteDBCollectionCarteDBToAttach.getId());
                attachedCarteDBCollection.add(carteDBCollectionCarteDBToAttach);
            }
            edituraDB.setCarteDBCollection(attachedCarteDBCollection);
            em.persist(edituraDB);
            for (CarteDB carteDBCollectionCarteDB : edituraDB.getCarteDBCollection()) {
                EdituraDB oldEdituraOfCarteDBCollectionCarteDB = carteDBCollectionCarteDB.getEditura();
                carteDBCollectionCarteDB.setEditura(edituraDB);
                carteDBCollectionCarteDB = em.merge(carteDBCollectionCarteDB);
                if (oldEdituraOfCarteDBCollectionCarteDB != null) {
                    oldEdituraOfCarteDBCollectionCarteDB.getCarteDBCollection().remove(carteDBCollectionCarteDB);
                    oldEdituraOfCarteDBCollectionCarteDB = em.merge(oldEdituraOfCarteDBCollectionCarteDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EdituraDB edituraDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EdituraDB persistentEdituraDB = em.find(EdituraDB.class, edituraDB.getId());
            Collection<CarteDB> carteDBCollectionOld = persistentEdituraDB.getCarteDBCollection();
            Collection<CarteDB> carteDBCollectionNew = edituraDB.getCarteDBCollection();
            List<String> illegalOrphanMessages = null;
            for (CarteDB carteDBCollectionOldCarteDB : carteDBCollectionOld) {
                if (!carteDBCollectionNew.contains(carteDBCollectionOldCarteDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CarteDB " + carteDBCollectionOldCarteDB + " since its editura field is not nullable.");
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
            edituraDB.setCarteDBCollection(carteDBCollectionNew);
            edituraDB = em.merge(edituraDB);
            for (CarteDB carteDBCollectionNewCarteDB : carteDBCollectionNew) {
                if (!carteDBCollectionOld.contains(carteDBCollectionNewCarteDB)) {
                    EdituraDB oldEdituraOfCarteDBCollectionNewCarteDB = carteDBCollectionNewCarteDB.getEditura();
                    carteDBCollectionNewCarteDB.setEditura(edituraDB);
                    carteDBCollectionNewCarteDB = em.merge(carteDBCollectionNewCarteDB);
                    if (oldEdituraOfCarteDBCollectionNewCarteDB != null && !oldEdituraOfCarteDBCollectionNewCarteDB.equals(edituraDB)) {
                        oldEdituraOfCarteDBCollectionNewCarteDB.getCarteDBCollection().remove(carteDBCollectionNewCarteDB);
                        oldEdituraOfCarteDBCollectionNewCarteDB = em.merge(oldEdituraOfCarteDBCollectionNewCarteDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = edituraDB.getId();
                if (findEdituraDB(id) == null) {
                    throw new NonexistentEntityException("The edituraDB with id " + id + " no longer exists.");
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
            EdituraDB edituraDB;
            try {
                edituraDB = em.getReference(EdituraDB.class, id);
                edituraDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The edituraDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CarteDB> carteDBCollectionOrphanCheck = edituraDB.getCarteDBCollection();
            for (CarteDB carteDBCollectionOrphanCheckCarteDB : carteDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EdituraDB (" + edituraDB + ") cannot be destroyed since the CarteDB " + carteDBCollectionOrphanCheckCarteDB + " in its carteDBCollection field has a non-nullable editura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(edituraDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EdituraDB> findEdituraDBEntities() {
        return findEdituraDBEntities(true, -1, -1);
    }

    public List<EdituraDB> findEdituraDBEntities(int maxResults, int firstResult) {
        return findEdituraDBEntities(false, maxResults, firstResult);
    }

    private List<EdituraDB> findEdituraDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EdituraDB.class));
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

    public EdituraDB findEdituraDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EdituraDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getEdituraDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EdituraDB> rt = cq.from(EdituraDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public EdituraDB findEdituraByNume(String nume){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("EdituraDB.findByNume");
        q.setParameter("nume", nume);
        
        try{
            return (EdituraDB)q.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
    public List<EdituraDB> findEdituriByComenzi(){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT e.* "
                                        +"FROM comenzi com, carti c, comanda_has_carte chc, edituri e "
                                        +"WHERE com.id IN (SELECT com2.id "
                                                          +"FROM comenzi com2 "
                                                          +"WHERE com2.status LIKE 'IN DESFASURARE') && " 
                                        +"chc.comanda = com.id &&"
                                        +"chc.carte = c.id && "
                                        +"c.editura = e.id ",EdituraDB.class);
        return q.getResultList();
    }
    
}
