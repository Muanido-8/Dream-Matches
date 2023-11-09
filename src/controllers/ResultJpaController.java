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
import models.Play;
import models.Result;

/**
 *
 * @author Conta 2
 */
public class ResultJpaController implements Serializable {

    public ResultJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Result result) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Play playId = result.getPlayId();
            if (playId != null) {
                playId = em.getReference(playId.getClass(), playId.getId());
                result.setPlayId(playId);
            }
            em.persist(result);
            if (playId != null) {
                playId.getResultCollection().add(result);
                playId = em.merge(playId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Result result) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Result persistentResult = em.find(Result.class, result.getId());
            Play playIdOld = persistentResult.getPlayId();
            Play playIdNew = result.getPlayId();
            if (playIdNew != null) {
                playIdNew = em.getReference(playIdNew.getClass(), playIdNew.getId());
                result.setPlayId(playIdNew);
            }
            result = em.merge(result);
            if (playIdOld != null && !playIdOld.equals(playIdNew)) {
                playIdOld.getResultCollection().remove(result);
                playIdOld = em.merge(playIdOld);
            }
            if (playIdNew != null && !playIdNew.equals(playIdOld)) {
                playIdNew.getResultCollection().add(result);
                playIdNew = em.merge(playIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = result.getId();
                if (findResult(id) == null) {
                    throw new NonexistentEntityException("The result with id " + id + " no longer exists.");
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
            Result result;
            try {
                result = em.getReference(Result.class, id);
                result.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The result with id " + id + " no longer exists.", enfe);
            }
            Play playId = result.getPlayId();
            if (playId != null) {
                playId.getResultCollection().remove(result);
                playId = em.merge(playId);
            }
            em.remove(result);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Result> findResultEntities() {
        return findResultEntities(true, -1, -1);
    }

    public List<Result> findResultEntities(int maxResults, int firstResult) {
        return findResultEntities(false, maxResults, firstResult);
    }

    private List<Result> findResultEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Result.class));
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

    public Result findResult(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Result.class, id);
        } finally {
            em.close();
        }
    }

    public int getResultCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Result> rt = cq.from(Result.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
