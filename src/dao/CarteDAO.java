/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import db.AutorDB;
import db.CarteDB;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.EdituraDB;
import db.FurnizorDB;
import db.ComandaHasCarteDB;
import java.util.ArrayList;
import java.util.Collection;
import db.CarteHasAutorDB;
import db.ComandaDB;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.*;

/**
 *
 * @author Ionut
 */
public class CarteDAO implements Serializable {

    public CarteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CarteDB carteDB) {
        if (carteDB.getComandaHasCarteDBCollection() == null) {
            carteDB.setComandaHasCarteDBCollection(new ArrayList<ComandaHasCarteDB>());
        }
        if (carteDB.getCarteHasAutorDBCollection() == null) {
            carteDB.setCarteHasAutorDBCollection(new ArrayList<CarteHasAutorDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EdituraDB editura = carteDB.getEditura();
            if (editura != null) {
                editura = em.getReference(editura.getClass(), editura.getId());
                carteDB.setEditura(editura);
            }
            FurnizorDB furnizor = carteDB.getFurnizor();
            if (furnizor != null) {
                furnizor = em.getReference(furnizor.getClass(), furnizor.getId());
                carteDB.setFurnizor(furnizor);
            }
            Collection<ComandaHasCarteDB> attachedComandaHasCarteDBCollection = new ArrayList<ComandaHasCarteDB>();
            for (ComandaHasCarteDB comandaHasCarteDBCollectionComandaHasCarteDBToAttach : carteDB.getComandaHasCarteDBCollection()) {
                comandaHasCarteDBCollectionComandaHasCarteDBToAttach = em.getReference(comandaHasCarteDBCollectionComandaHasCarteDBToAttach.getClass(), comandaHasCarteDBCollectionComandaHasCarteDBToAttach.getId());
                attachedComandaHasCarteDBCollection.add(comandaHasCarteDBCollectionComandaHasCarteDBToAttach);
            }
            carteDB.setComandaHasCarteDBCollection(attachedComandaHasCarteDBCollection);
            Collection<CarteHasAutorDB> attachedCarteHasAutorDBCollection = new ArrayList<CarteHasAutorDB>();
            for (CarteHasAutorDB carteHasAutorDBCollectionCarteHasAutorDBToAttach : carteDB.getCarteHasAutorDBCollection()) {
                carteHasAutorDBCollectionCarteHasAutorDBToAttach = em.getReference(carteHasAutorDBCollectionCarteHasAutorDBToAttach.getClass(), carteHasAutorDBCollectionCarteHasAutorDBToAttach.getId());
                attachedCarteHasAutorDBCollection.add(carteHasAutorDBCollectionCarteHasAutorDBToAttach);
            }
            carteDB.setCarteHasAutorDBCollection(attachedCarteHasAutorDBCollection);
            em.persist(carteDB);
            if (editura != null) {
                editura.getCarteDBCollection().add(carteDB);
                editura = em.merge(editura);
            }
            if (furnizor != null) {
                furnizor.getCarteDBCollection().add(carteDB);
                furnizor = em.merge(furnizor);
            }
            for (ComandaHasCarteDB comandaHasCarteDBCollectionComandaHasCarteDB : carteDB.getComandaHasCarteDBCollection()) {
                CarteDB oldCarteOfComandaHasCarteDBCollectionComandaHasCarteDB = comandaHasCarteDBCollectionComandaHasCarteDB.getCarte();
                comandaHasCarteDBCollectionComandaHasCarteDB.setCarte(carteDB);
                comandaHasCarteDBCollectionComandaHasCarteDB = em.merge(comandaHasCarteDBCollectionComandaHasCarteDB);
                if (oldCarteOfComandaHasCarteDBCollectionComandaHasCarteDB != null) {
                    oldCarteOfComandaHasCarteDBCollectionComandaHasCarteDB.getComandaHasCarteDBCollection().remove(comandaHasCarteDBCollectionComandaHasCarteDB);
                    oldCarteOfComandaHasCarteDBCollectionComandaHasCarteDB = em.merge(oldCarteOfComandaHasCarteDBCollectionComandaHasCarteDB);
                }
            }
            for (CarteHasAutorDB carteHasAutorDBCollectionCarteHasAutorDB : carteDB.getCarteHasAutorDBCollection()) {
                CarteDB oldCarteOfCarteHasAutorDBCollectionCarteHasAutorDB = carteHasAutorDBCollectionCarteHasAutorDB.getCarte();
                carteHasAutorDBCollectionCarteHasAutorDB.setCarte(carteDB);
                carteHasAutorDBCollectionCarteHasAutorDB = em.merge(carteHasAutorDBCollectionCarteHasAutorDB);
                if (oldCarteOfCarteHasAutorDBCollectionCarteHasAutorDB != null) {
                    oldCarteOfCarteHasAutorDBCollectionCarteHasAutorDB.getCarteHasAutorDBCollection().remove(carteHasAutorDBCollectionCarteHasAutorDB);
                    oldCarteOfCarteHasAutorDBCollectionCarteHasAutorDB = em.merge(oldCarteOfCarteHasAutorDBCollectionCarteHasAutorDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CarteDB carteDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CarteDB persistentCarteDB = em.find(CarteDB.class, carteDB.getId());
            EdituraDB edituraOld = persistentCarteDB.getEditura();
            EdituraDB edituraNew = carteDB.getEditura();
            FurnizorDB furnizorOld = persistentCarteDB.getFurnizor();
            FurnizorDB furnizorNew = carteDB.getFurnizor();
            Collection<ComandaHasCarteDB> comandaHasCarteDBCollectionOld = persistentCarteDB.getComandaHasCarteDBCollection();
            Collection<ComandaHasCarteDB> comandaHasCarteDBCollectionNew = carteDB.getComandaHasCarteDBCollection();
            Collection<CarteHasAutorDB> carteHasAutorDBCollectionOld = persistentCarteDB.getCarteHasAutorDBCollection();
            Collection<CarteHasAutorDB> carteHasAutorDBCollectionNew = carteDB.getCarteHasAutorDBCollection();
            List<String> illegalOrphanMessages = null;
            for (ComandaHasCarteDB comandaHasCarteDBCollectionOldComandaHasCarteDB : comandaHasCarteDBCollectionOld) {
                if (!comandaHasCarteDBCollectionNew.contains(comandaHasCarteDBCollectionOldComandaHasCarteDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComandaHasCarteDB " + comandaHasCarteDBCollectionOldComandaHasCarteDB + " since its carte field is not nullable.");
                }
            }
            for (CarteHasAutorDB carteHasAutorDBCollectionOldCarteHasAutorDB : carteHasAutorDBCollectionOld) {
                if (!carteHasAutorDBCollectionNew.contains(carteHasAutorDBCollectionOldCarteHasAutorDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CarteHasAutorDB " + carteHasAutorDBCollectionOldCarteHasAutorDB + " since its carte field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (edituraNew != null) {
                edituraNew = em.getReference(edituraNew.getClass(), edituraNew.getId());
                carteDB.setEditura(edituraNew);
            }
            if (furnizorNew != null) {
                furnizorNew = em.getReference(furnizorNew.getClass(), furnizorNew.getId());
                carteDB.setFurnizor(furnizorNew);
            }
            Collection<ComandaHasCarteDB> attachedComandaHasCarteDBCollectionNew = new ArrayList<ComandaHasCarteDB>();
            for (ComandaHasCarteDB comandaHasCarteDBCollectionNewComandaHasCarteDBToAttach : comandaHasCarteDBCollectionNew) {
                comandaHasCarteDBCollectionNewComandaHasCarteDBToAttach = em.getReference(comandaHasCarteDBCollectionNewComandaHasCarteDBToAttach.getClass(), comandaHasCarteDBCollectionNewComandaHasCarteDBToAttach.getId());
                attachedComandaHasCarteDBCollectionNew.add(comandaHasCarteDBCollectionNewComandaHasCarteDBToAttach);
            }
            comandaHasCarteDBCollectionNew = attachedComandaHasCarteDBCollectionNew;
            carteDB.setComandaHasCarteDBCollection(comandaHasCarteDBCollectionNew);
            Collection<CarteHasAutorDB> attachedCarteHasAutorDBCollectionNew = new ArrayList<CarteHasAutorDB>();
            for (CarteHasAutorDB carteHasAutorDBCollectionNewCarteHasAutorDBToAttach : carteHasAutorDBCollectionNew) {
                carteHasAutorDBCollectionNewCarteHasAutorDBToAttach = em.getReference(carteHasAutorDBCollectionNewCarteHasAutorDBToAttach.getClass(), carteHasAutorDBCollectionNewCarteHasAutorDBToAttach.getId());
                attachedCarteHasAutorDBCollectionNew.add(carteHasAutorDBCollectionNewCarteHasAutorDBToAttach);
            }
            carteHasAutorDBCollectionNew = attachedCarteHasAutorDBCollectionNew;
            carteDB.setCarteHasAutorDBCollection(carteHasAutorDBCollectionNew);
            carteDB = em.merge(carteDB);
            if (edituraOld != null && !edituraOld.equals(edituraNew)) {
                edituraOld.getCarteDBCollection().remove(carteDB);
                edituraOld = em.merge(edituraOld);
            }
            if (edituraNew != null && !edituraNew.equals(edituraOld)) {
                edituraNew.getCarteDBCollection().add(carteDB);
                edituraNew = em.merge(edituraNew);
            }
            if (furnizorOld != null && !furnizorOld.equals(furnizorNew)) {
                furnizorOld.getCarteDBCollection().remove(carteDB);
                furnizorOld = em.merge(furnizorOld);
            }
            if (furnizorNew != null && !furnizorNew.equals(furnizorOld)) {
                furnizorNew.getCarteDBCollection().add(carteDB);
                furnizorNew = em.merge(furnizorNew);
            }
            for (ComandaHasCarteDB comandaHasCarteDBCollectionNewComandaHasCarteDB : comandaHasCarteDBCollectionNew) {
                if (!comandaHasCarteDBCollectionOld.contains(comandaHasCarteDBCollectionNewComandaHasCarteDB)) {
                    CarteDB oldCarteOfComandaHasCarteDBCollectionNewComandaHasCarteDB = comandaHasCarteDBCollectionNewComandaHasCarteDB.getCarte();
                    comandaHasCarteDBCollectionNewComandaHasCarteDB.setCarte(carteDB);
                    comandaHasCarteDBCollectionNewComandaHasCarteDB = em.merge(comandaHasCarteDBCollectionNewComandaHasCarteDB);
                    if (oldCarteOfComandaHasCarteDBCollectionNewComandaHasCarteDB != null && !oldCarteOfComandaHasCarteDBCollectionNewComandaHasCarteDB.equals(carteDB)) {
                        oldCarteOfComandaHasCarteDBCollectionNewComandaHasCarteDB.getComandaHasCarteDBCollection().remove(comandaHasCarteDBCollectionNewComandaHasCarteDB);
                        oldCarteOfComandaHasCarteDBCollectionNewComandaHasCarteDB = em.merge(oldCarteOfComandaHasCarteDBCollectionNewComandaHasCarteDB);
                    }
                }
            }
            for (CarteHasAutorDB carteHasAutorDBCollectionNewCarteHasAutorDB : carteHasAutorDBCollectionNew) {
                if (!carteHasAutorDBCollectionOld.contains(carteHasAutorDBCollectionNewCarteHasAutorDB)) {
                    CarteDB oldCarteOfCarteHasAutorDBCollectionNewCarteHasAutorDB = carteHasAutorDBCollectionNewCarteHasAutorDB.getCarte();
                    carteHasAutorDBCollectionNewCarteHasAutorDB.setCarte(carteDB);
                    carteHasAutorDBCollectionNewCarteHasAutorDB = em.merge(carteHasAutorDBCollectionNewCarteHasAutorDB);
                    if (oldCarteOfCarteHasAutorDBCollectionNewCarteHasAutorDB != null && !oldCarteOfCarteHasAutorDBCollectionNewCarteHasAutorDB.equals(carteDB)) {
                        oldCarteOfCarteHasAutorDBCollectionNewCarteHasAutorDB.getCarteHasAutorDBCollection().remove(carteHasAutorDBCollectionNewCarteHasAutorDB);
                        oldCarteOfCarteHasAutorDBCollectionNewCarteHasAutorDB = em.merge(oldCarteOfCarteHasAutorDBCollectionNewCarteHasAutorDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carteDB.getId();
                if (findCarteDB(id) == null) {
                    throw new NonexistentEntityException("The carteDB with id " + id + " no longer exists.");
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
            CarteDB carteDB;
            try {
                carteDB = em.getReference(CarteDB.class, id);
                carteDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carteDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ComandaHasCarteDB> comandaHasCarteDBCollectionOrphanCheck = carteDB.getComandaHasCarteDBCollection();
            for (ComandaHasCarteDB comandaHasCarteDBCollectionOrphanCheckComandaHasCarteDB : comandaHasCarteDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CarteDB (" + carteDB + ") cannot be destroyed since the ComandaHasCarteDB " + comandaHasCarteDBCollectionOrphanCheckComandaHasCarteDB + " in its comandaHasCarteDBCollection field has a non-nullable carte field.");
            }
            Collection<CarteHasAutorDB> carteHasAutorDBCollectionOrphanCheck = carteDB.getCarteHasAutorDBCollection();
            for (CarteHasAutorDB carteHasAutorDBCollectionOrphanCheckCarteHasAutorDB : carteHasAutorDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CarteDB (" + carteDB + ") cannot be destroyed since the CarteHasAutorDB " + carteHasAutorDBCollectionOrphanCheckCarteHasAutorDB + " in its carteHasAutorDBCollection field has a non-nullable carte field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EdituraDB editura = carteDB.getEditura();
            if (editura != null) {
                editura.getCarteDBCollection().remove(carteDB);
                editura = em.merge(editura);
            }
            FurnizorDB furnizor = carteDB.getFurnizor();
            if (furnizor != null) {
                furnizor.getCarteDBCollection().remove(carteDB);
                furnizor = em.merge(furnizor);
            }
            em.remove(carteDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CarteDB> findCarteDBEntities() {
        return findCarteDBEntities(true, -1, -1);
    }

    public List<CarteDB> findCarteDBEntities(int maxResults, int firstResult) {
        return findCarteDBEntities(false, maxResults, firstResult);
    }

    private List<CarteDB> findCarteDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CarteDB.class));
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

    public CarteDB findCarteDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CarteDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarteDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CarteDB> rt = cq.from(CarteDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<CarteDB> findCartiByAutor(AutorDB autor){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT c.* FROM autori a, carte_has_autor cha, carti c WHERE "
                                        +"c.id = cha.carte AND "
                                        +"cha.autor = a.id AND "
                                        +"a.id = ?", CarteDB.class);
        q.setParameter(1, autor.getId());
        return q.getResultList();
    }
    
    public CarteDB findCarteByISBN(String isbn){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("CarteDB.findByIsbn");
        q.setParameter("isbn", isbn);
        
        try{
            return (CarteDB) q.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
    public List<CarteDB> findCartiByComanda(ComandaDB comanda){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT carti.* FROM comenzi, comanda_has_carte chc, carti WHERE "
                                                    +"carti.id = chc.carte && "
                                                    +"chc.comanda = comenzi.id && "
                                                    +"comenzi.id = ?",CarteDB.class);
        q.setParameter(1, comanda.getId());
        return q.getResultList();
    }
    
    public int findCantitateTotala(CarteDB carte){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT SUM(cantitate) FROM comanda_has_carte cha, comenzi "
                                            +"WHERE comenzi.id IN(SELECT com2.id "
                                                                +"FROM comenzi com2 "
                                                                +"WHERE com2.status LIKE 'IN DESFASURARE') && " 
                                            +"cha.comanda = comenzi.id && "
                                            +"cha.carte = ?");
        q.setParameter(1, carte.getId());
        
        BigDecimal cantitate = (BigDecimal)q.getSingleResult();
        
        try{
            return cantitate.intValueExact();
        }catch(Exception e){
            return 0;
        } 
    }
    
    public List<CarteDB> findCartiByEditura(EdituraDB editura){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT carti.* FROM carti, edituri WHERE "
                                            +"carti.editura = edituri.id &&"
                                            +"edituri.id = ?",CarteDB.class);
        q.setParameter(1, editura.getId());
        return q.getResultList();
    }
    
    public List<CarteDB> findCartiByFurnizor(FurnizorDB furnizor){
        EntityManager em = getEntityManager();
        Query q = em.createNativeQuery("SELECT carti.* FROM carti, furnizori WHERE "
                                            +"carti.furnizor = furnizori.id &&"
                                            +"furnizori.id = ?",CarteDB.class);
        q.setParameter(1, furnizor.getId());
        return q.getResultList();
    }
}
