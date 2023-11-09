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
import models.Coach;
import models.Team;

/**
 *
 * @author Conta 2
 */
public class CoachJpaController implements Serializable {

    public CoachJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Coach coach) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Team teamId = coach.getTeamId();
            if (teamId != null) {
                teamId = em.getReference(teamId.getClass(), teamId.getId());
                coach.setTeamId(teamId);
            }
            em.persist(coach);
            if (teamId != null) {
                teamId.getCoachCollection().add(coach);
                teamId = em.merge(teamId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Coach coach) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Coach persistentCoach = em.find(Coach.class, coach.getId());
            Team teamIdOld = persistentCoach.getTeamId();
            Team teamIdNew = coach.getTeamId();
            if (teamIdNew != null) {
                teamIdNew = em.getReference(teamIdNew.getClass(), teamIdNew.getId());
                coach.setTeamId(teamIdNew);
            }
            coach = em.merge(coach);
            if (teamIdOld != null && !teamIdOld.equals(teamIdNew)) {
                teamIdOld.getCoachCollection().remove(coach);
                teamIdOld = em.merge(teamIdOld);
            }
            if (teamIdNew != null && !teamIdNew.equals(teamIdOld)) {
                teamIdNew.getCoachCollection().add(coach);
                teamIdNew = em.merge(teamIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = coach.getId();
                if (findCoach(id) == null) {
                    throw new NonexistentEntityException("The coach with id " + id + " no longer exists.");
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
            Coach coach;
            try {
                coach = em.getReference(Coach.class, id);
                coach.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The coach with id " + id + " no longer exists.", enfe);
            }
            Team teamId = coach.getTeamId();
            if (teamId != null) {
                teamId.getCoachCollection().remove(coach);
                teamId = em.merge(teamId);
            }
            em.remove(coach);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Coach> findCoachEntities() {
        return findCoachEntities(true, -1, -1);
    }

    public List<Coach> findCoachEntities(int maxResults, int firstResult) {
        return findCoachEntities(false, maxResults, firstResult);
    }

    private List<Coach> findCoachEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Coach.class));
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

    public Coach findCoach(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Coach.class, id);
        } finally {
            em.close();
        }
    }

    public int getCoachCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Coach> rt = cq.from(Coach.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
