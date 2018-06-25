/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import db.ClientDB;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import db.ComandaDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ionut
 */
public class ClientDAO implements Serializable {

    public ClientDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClientDB clientDB) {
        if (clientDB.getComandaDBCollection() == null) {
            clientDB.setComandaDBCollection(new ArrayList<ComandaDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ComandaDB> attachedComandaDBCollection = new ArrayList<ComandaDB>();
            for (ComandaDB comandaDBCollectionComandaDBToAttach : clientDB.getComandaDBCollection()) {
                comandaDBCollectionComandaDBToAttach = em.getReference(comandaDBCollectionComandaDBToAttach.getClass(), comandaDBCollectionComandaDBToAttach.getId());
                attachedComandaDBCollection.add(comandaDBCollectionComandaDBToAttach);
            }
            clientDB.setComandaDBCollection(attachedComandaDBCollection);
            em.persist(clientDB);
            for (ComandaDB comandaDBCollectionComandaDB : clientDB.getComandaDBCollection()) {
                ClientDB oldClientOfComandaDBCollectionComandaDB = comandaDBCollectionComandaDB.getClient();
                comandaDBCollectionComandaDB.setClient(clientDB);
                comandaDBCollectionComandaDB = em.merge(comandaDBCollectionComandaDB);
                if (oldClientOfComandaDBCollectionComandaDB != null) {
                    oldClientOfComandaDBCollectionComandaDB.getComandaDBCollection().remove(comandaDBCollectionComandaDB);
                    oldClientOfComandaDBCollectionComandaDB = em.merge(oldClientOfComandaDBCollectionComandaDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClientDB clientDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientDB persistentClientDB = em.find(ClientDB.class, clientDB.getId());
            Collection<ComandaDB> comandaDBCollectionOld = persistentClientDB.getComandaDBCollection();
            Collection<ComandaDB> comandaDBCollectionNew = clientDB.getComandaDBCollection();
            List<String> illegalOrphanMessages = null;
            for (ComandaDB comandaDBCollectionOldComandaDB : comandaDBCollectionOld) {
                if (!comandaDBCollectionNew.contains(comandaDBCollectionOldComandaDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComandaDB " + comandaDBCollectionOldComandaDB + " since its client field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ComandaDB> attachedComandaDBCollectionNew = new ArrayList<ComandaDB>();
            for (ComandaDB comandaDBCollectionNewComandaDBToAttach : comandaDBCollectionNew) {
                comandaDBCollectionNewComandaDBToAttach = em.getReference(comandaDBCollectionNewComandaDBToAttach.getClass(), comandaDBCollectionNewComandaDBToAttach.getId());
                attachedComandaDBCollectionNew.add(comandaDBCollectionNewComandaDBToAttach);
            }
            comandaDBCollectionNew = attachedComandaDBCollectionNew;
            clientDB.setComandaDBCollection(comandaDBCollectionNew);
            clientDB = em.merge(clientDB);
            for (ComandaDB comandaDBCollectionNewComandaDB : comandaDBCollectionNew) {
                if (!comandaDBCollectionOld.contains(comandaDBCollectionNewComandaDB)) {
                    ClientDB oldClientOfComandaDBCollectionNewComandaDB = comandaDBCollectionNewComandaDB.getClient();
                    comandaDBCollectionNewComandaDB.setClient(clientDB);
                    comandaDBCollectionNewComandaDB = em.merge(comandaDBCollectionNewComandaDB);
                    if (oldClientOfComandaDBCollectionNewComandaDB != null && !oldClientOfComandaDBCollectionNewComandaDB.equals(clientDB)) {
                        oldClientOfComandaDBCollectionNewComandaDB.getComandaDBCollection().remove(comandaDBCollectionNewComandaDB);
                        oldClientOfComandaDBCollectionNewComandaDB = em.merge(oldClientOfComandaDBCollectionNewComandaDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientDB.getId();
                if (findClientDB(id) == null) {
                    throw new NonexistentEntityException("The clientDB with id " + id + " no longer exists.");
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
            ClientDB clientDB;
            try {
                clientDB = em.getReference(ClientDB.class, id);
                clientDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ComandaDB> comandaDBCollectionOrphanCheck = clientDB.getComandaDBCollection();
            for (ComandaDB comandaDBCollectionOrphanCheckComandaDB : comandaDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ClientDB (" + clientDB + ") cannot be destroyed since the ComandaDB " + comandaDBCollectionOrphanCheckComandaDB + " in its comandaDBCollection field has a non-nullable client field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clientDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClientDB> findClientDBEntities() {
        return findClientDBEntities(true, -1, -1);
    }

    public List<ClientDB> findClientDBEntities(int maxResults, int firstResult) {
        return findClientDBEntities(false, maxResults, firstResult);
    }

    private List<ClientDB> findClientDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClientDB.class));
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

    public ClientDB findClientDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClientDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClientDB> rt = cq.from(ClientDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public ClientDB findClientByCNP(String cnp){
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("ClientDB.findByCnp");
        q.setParameter("cnp", cnp);
        
        try{
            return (ClientDB) q.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
}
