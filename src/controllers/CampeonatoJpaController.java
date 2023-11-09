/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.User;
import models.Play;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Campeonato;
import models.Team;

/**
 *
 * @author Conta 2
 */
public class CampeonatoJpaController implements Serializable {

    public CampeonatoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Campeonato campeonato) {
        if (campeonato.getPlayCollection() == null) {
            campeonato.setPlayCollection(new ArrayList<Play>());
        }
        if (campeonato.getTeamCollection() == null) {
            campeonato.setTeamCollection(new ArrayList<Team>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userId = campeonato.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                campeonato.setUserId(userId);
            }
            Collection<Play> attachedPlayCollection = new ArrayList<Play>();
            for (Play playCollectionPlayToAttach : campeonato.getPlayCollection()) {
                playCollectionPlayToAttach = em.getReference(playCollectionPlayToAttach.getClass(), playCollectionPlayToAttach.getId());
                attachedPlayCollection.add(playCollectionPlayToAttach);
            }
            campeonato.setPlayCollection(attachedPlayCollection);
            Collection<Team> attachedTeamCollection = new ArrayList<Team>();
            for (Team teamCollectionTeamToAttach : campeonato.getTeamCollection()) {
                teamCollectionTeamToAttach = em.getReference(teamCollectionTeamToAttach.getClass(), teamCollectionTeamToAttach.getId());
                attachedTeamCollection.add(teamCollectionTeamToAttach);
            }
            campeonato.setTeamCollection(attachedTeamCollection);
            em.persist(campeonato);
            if (userId != null) {
                userId.getCampeonatoCollection().add(campeonato);
                userId = em.merge(userId);
            }
            for (Play playCollectionPlay : campeonato.getPlayCollection()) {
                Campeonato oldCampeonatoIdOfPlayCollectionPlay = playCollectionPlay.getCampeonatoId();
                playCollectionPlay.setCampeonatoId(campeonato);
                playCollectionPlay = em.merge(playCollectionPlay);
                if (oldCampeonatoIdOfPlayCollectionPlay != null) {
                    oldCampeonatoIdOfPlayCollectionPlay.getPlayCollection().remove(playCollectionPlay);
                    oldCampeonatoIdOfPlayCollectionPlay = em.merge(oldCampeonatoIdOfPlayCollectionPlay);
                }
            }
            for (Team teamCollectionTeam : campeonato.getTeamCollection()) {
                Campeonato oldCampeonatoIdOfTeamCollectionTeam = teamCollectionTeam.getCampeonatoId();
                teamCollectionTeam.setCampeonatoId(campeonato);
                teamCollectionTeam = em.merge(teamCollectionTeam);
                if (oldCampeonatoIdOfTeamCollectionTeam != null) {
                    oldCampeonatoIdOfTeamCollectionTeam.getTeamCollection().remove(teamCollectionTeam);
                    oldCampeonatoIdOfTeamCollectionTeam = em.merge(oldCampeonatoIdOfTeamCollectionTeam);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Campeonato campeonato) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Campeonato persistentCampeonato = em.find(Campeonato.class, campeonato.getId());
            User userIdOld = persistentCampeonato.getUserId();
            User userIdNew = campeonato.getUserId();
            Collection<Play> playCollectionOld = persistentCampeonato.getPlayCollection();
            Collection<Play> playCollectionNew = campeonato.getPlayCollection();
            Collection<Team> teamCollectionOld = persistentCampeonato.getTeamCollection();
            Collection<Team> teamCollectionNew = campeonato.getTeamCollection();
            List<String> illegalOrphanMessages = null;
            for (Play playCollectionOldPlay : playCollectionOld) {
                if (!playCollectionNew.contains(playCollectionOldPlay)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Play " + playCollectionOldPlay + " since its campeonatoId field is not nullable.");
                }
            }
            for (Team teamCollectionOldTeam : teamCollectionOld) {
                if (!teamCollectionNew.contains(teamCollectionOldTeam)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Team " + teamCollectionOldTeam + " since its campeonatoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                campeonato.setUserId(userIdNew);
            }
            Collection<Play> attachedPlayCollectionNew = new ArrayList<Play>();
            for (Play playCollectionNewPlayToAttach : playCollectionNew) {
                playCollectionNewPlayToAttach = em.getReference(playCollectionNewPlayToAttach.getClass(), playCollectionNewPlayToAttach.getId());
                attachedPlayCollectionNew.add(playCollectionNewPlayToAttach);
            }
            playCollectionNew = attachedPlayCollectionNew;
            campeonato.setPlayCollection(playCollectionNew);
            Collection<Team> attachedTeamCollectionNew = new ArrayList<Team>();
            for (Team teamCollectionNewTeamToAttach : teamCollectionNew) {
                teamCollectionNewTeamToAttach = em.getReference(teamCollectionNewTeamToAttach.getClass(), teamCollectionNewTeamToAttach.getId());
                attachedTeamCollectionNew.add(teamCollectionNewTeamToAttach);
            }
            teamCollectionNew = attachedTeamCollectionNew;
            campeonato.setTeamCollection(teamCollectionNew);
            campeonato = em.merge(campeonato);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getCampeonatoCollection().remove(campeonato);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getCampeonatoCollection().add(campeonato);
                userIdNew = em.merge(userIdNew);
            }
            for (Play playCollectionNewPlay : playCollectionNew) {
                if (!playCollectionOld.contains(playCollectionNewPlay)) {
                    Campeonato oldCampeonatoIdOfPlayCollectionNewPlay = playCollectionNewPlay.getCampeonatoId();
                    playCollectionNewPlay.setCampeonatoId(campeonato);
                    playCollectionNewPlay = em.merge(playCollectionNewPlay);
                    if (oldCampeonatoIdOfPlayCollectionNewPlay != null && !oldCampeonatoIdOfPlayCollectionNewPlay.equals(campeonato)) {
                        oldCampeonatoIdOfPlayCollectionNewPlay.getPlayCollection().remove(playCollectionNewPlay);
                        oldCampeonatoIdOfPlayCollectionNewPlay = em.merge(oldCampeonatoIdOfPlayCollectionNewPlay);
                    }
                }
            }
            for (Team teamCollectionNewTeam : teamCollectionNew) {
                if (!teamCollectionOld.contains(teamCollectionNewTeam)) {
                    Campeonato oldCampeonatoIdOfTeamCollectionNewTeam = teamCollectionNewTeam.getCampeonatoId();
                    teamCollectionNewTeam.setCampeonatoId(campeonato);
                    teamCollectionNewTeam = em.merge(teamCollectionNewTeam);
                    if (oldCampeonatoIdOfTeamCollectionNewTeam != null && !oldCampeonatoIdOfTeamCollectionNewTeam.equals(campeonato)) {
                        oldCampeonatoIdOfTeamCollectionNewTeam.getTeamCollection().remove(teamCollectionNewTeam);
                        oldCampeonatoIdOfTeamCollectionNewTeam = em.merge(oldCampeonatoIdOfTeamCollectionNewTeam);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = campeonato.getId();
                if (findCampeonato(id) == null) {
                    throw new NonexistentEntityException("The campeonato with id " + id + " no longer exists.");
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
            Campeonato campeonato;
            try {
                campeonato = em.getReference(Campeonato.class, id);
                campeonato.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The campeonato with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Play> playCollectionOrphanCheck = campeonato.getPlayCollection();
            for (Play playCollectionOrphanCheckPlay : playCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Campeonato (" + campeonato + ") cannot be destroyed since the Play " + playCollectionOrphanCheckPlay + " in its playCollection field has a non-nullable campeonatoId field.");
            }
            Collection<Team> teamCollectionOrphanCheck = campeonato.getTeamCollection();
            for (Team teamCollectionOrphanCheckTeam : teamCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Campeonato (" + campeonato + ") cannot be destroyed since the Team " + teamCollectionOrphanCheckTeam + " in its teamCollection field has a non-nullable campeonatoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userId = campeonato.getUserId();
            if (userId != null) {
                userId.getCampeonatoCollection().remove(campeonato);
                userId = em.merge(userId);
            }
            em.remove(campeonato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Campeonato> findCampeonatoEntities() {
        return findCampeonatoEntities(true, -1, -1);
    }

    public List<Campeonato> findCampeonatoEntities(int maxResults, int firstResult) {
        return findCampeonatoEntities(false, maxResults, firstResult);
    }

    private List<Campeonato> findCampeonatoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Campeonato.class));
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

    public Campeonato findCampeonato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Campeonato.class, id);
        } finally {
            em.close();
        }
    }

    public int getCampeonatoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Campeonato> rt = cq.from(Campeonato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
