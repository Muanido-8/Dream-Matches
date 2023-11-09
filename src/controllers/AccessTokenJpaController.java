/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.AccessToken;
import models.User;

/**
 *
 * @author Conta 2
 */
public class AccessTokenJpaController implements Serializable {

    public AccessTokenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccessToken accessToken) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userId = accessToken.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                accessToken.setUserId(userId);
            }
            em.persist(accessToken);
            if (userId != null) {
                userId.getAccessTokenCollection().add(accessToken);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccessToken accessToken) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessToken persistentAccessToken = em.find(AccessToken.class, accessToken.getId());
            User userIdOld = persistentAccessToken.getUserId();
            User userIdNew = accessToken.getUserId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                accessToken.setUserId(userIdNew);
            }
            accessToken = em.merge(accessToken);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getAccessTokenCollection().remove(accessToken);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getAccessTokenCollection().add(accessToken);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accessToken.getId();
                if (findAccessToken(id) == null) {
                    throw new NonexistentEntityException("The accessToken with id " + id + " no longer exists.");
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
            AccessToken accessToken;
            try {
                accessToken = em.getReference(AccessToken.class, id);
                accessToken.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accessToken with id " + id + " no longer exists.", enfe);
            }
            User userId = accessToken.getUserId();
            if (userId != null) {
                userId.getAccessTokenCollection().remove(accessToken);
                userId = em.merge(userId);
            }
            em.remove(accessToken);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccessToken> findAccessTokenEntities() {
        return findAccessTokenEntities(true, -1, -1);
    }

    public List<AccessToken> findAccessTokenEntities(int maxResults, int firstResult) {
        return findAccessTokenEntities(false, maxResults, firstResult);
    }

    private List<AccessToken> findAccessTokenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccessToken.class));
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

    public AccessToken findAccessToken(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccessToken.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccessTokenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccessToken> rt = cq.from(AccessToken.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
