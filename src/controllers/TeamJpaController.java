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
import models.Campeonato;
import models.Coach;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Play;
import models.Player;
import models.Team;

/**
 *
 * @author Conta 2
 */
public class TeamJpaController implements Serializable {

    public TeamJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Team team) {
        if (team.getCoachCollection() == null) {
            team.setCoachCollection(new ArrayList<Coach>());
        }
        if (team.getPlayCollection() == null) {
            team.setPlayCollection(new ArrayList<Play>());
        }
        if (team.getPlayCollection1() == null) {
            team.setPlayCollection1(new ArrayList<Play>());
        }
        if (team.getPlayerCollection() == null) {
            team.setPlayerCollection(new ArrayList<Player>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Campeonato campeonatoId = team.getCampeonatoId();
            if (campeonatoId != null) {
                campeonatoId = em.getReference(campeonatoId.getClass(), campeonatoId.getId());
                team.setCampeonatoId(campeonatoId);
            }
            Collection<Coach> attachedCoachCollection = new ArrayList<Coach>();
            for (Coach coachCollectionCoachToAttach : team.getCoachCollection()) {
                coachCollectionCoachToAttach = em.getReference(coachCollectionCoachToAttach.getClass(), coachCollectionCoachToAttach.getId());
                attachedCoachCollection.add(coachCollectionCoachToAttach);
            }
            team.setCoachCollection(attachedCoachCollection);
            Collection<Play> attachedPlayCollection = new ArrayList<Play>();
            for (Play playCollectionPlayToAttach : team.getPlayCollection()) {
                playCollectionPlayToAttach = em.getReference(playCollectionPlayToAttach.getClass(), playCollectionPlayToAttach.getId());
                attachedPlayCollection.add(playCollectionPlayToAttach);
            }
            team.setPlayCollection(attachedPlayCollection);
            Collection<Play> attachedPlayCollection1 = new ArrayList<Play>();
            for (Play playCollection1PlayToAttach : team.getPlayCollection1()) {
                playCollection1PlayToAttach = em.getReference(playCollection1PlayToAttach.getClass(), playCollection1PlayToAttach.getId());
                attachedPlayCollection1.add(playCollection1PlayToAttach);
            }
            team.setPlayCollection1(attachedPlayCollection1);
            Collection<Player> attachedPlayerCollection = new ArrayList<Player>();
            for (Player playerCollectionPlayerToAttach : team.getPlayerCollection()) {
                playerCollectionPlayerToAttach = em.getReference(playerCollectionPlayerToAttach.getClass(), playerCollectionPlayerToAttach.getId());
                attachedPlayerCollection.add(playerCollectionPlayerToAttach);
            }
            team.setPlayerCollection(attachedPlayerCollection);
            em.persist(team);
            if (campeonatoId != null) {
                campeonatoId.getTeamCollection().add(team);
                campeonatoId = em.merge(campeonatoId);
            }
            for (Coach coachCollectionCoach : team.getCoachCollection()) {
                Team oldTeamIdOfCoachCollectionCoach = coachCollectionCoach.getTeamId();
                coachCollectionCoach.setTeamId(team);
                coachCollectionCoach = em.merge(coachCollectionCoach);
                if (oldTeamIdOfCoachCollectionCoach != null) {
                    oldTeamIdOfCoachCollectionCoach.getCoachCollection().remove(coachCollectionCoach);
                    oldTeamIdOfCoachCollectionCoach = em.merge(oldTeamIdOfCoachCollectionCoach);
                }
            }
            for (Play playCollectionPlay : team.getPlayCollection()) {
                Team oldTeamHomeOfPlayCollectionPlay = playCollectionPlay.getTeamHome();
                playCollectionPlay.setTeamHome(team);
                playCollectionPlay = em.merge(playCollectionPlay);
                if (oldTeamHomeOfPlayCollectionPlay != null) {
                    oldTeamHomeOfPlayCollectionPlay.getPlayCollection().remove(playCollectionPlay);
                    oldTeamHomeOfPlayCollectionPlay = em.merge(oldTeamHomeOfPlayCollectionPlay);
                }
            }
            for (Play playCollection1Play : team.getPlayCollection1()) {
                Team oldTeamVisitorOfPlayCollection1Play = playCollection1Play.getTeamVisitor();
                playCollection1Play.setTeamVisitor(team);
                playCollection1Play = em.merge(playCollection1Play);
                if (oldTeamVisitorOfPlayCollection1Play != null) {
                    oldTeamVisitorOfPlayCollection1Play.getPlayCollection1().remove(playCollection1Play);
                    oldTeamVisitorOfPlayCollection1Play = em.merge(oldTeamVisitorOfPlayCollection1Play);
                }
            }
            for (Player playerCollectionPlayer : team.getPlayerCollection()) {
                Team oldTeamIdOfPlayerCollectionPlayer = playerCollectionPlayer.getTeamId();
                playerCollectionPlayer.setTeamId(team);
                playerCollectionPlayer = em.merge(playerCollectionPlayer);
                if (oldTeamIdOfPlayerCollectionPlayer != null) {
                    oldTeamIdOfPlayerCollectionPlayer.getPlayerCollection().remove(playerCollectionPlayer);
                    oldTeamIdOfPlayerCollectionPlayer = em.merge(oldTeamIdOfPlayerCollectionPlayer);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Team team) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Team persistentTeam = em.find(Team.class, team.getId());
            Campeonato campeonatoIdOld = persistentTeam.getCampeonatoId();
            Campeonato campeonatoIdNew = team.getCampeonatoId();
            Collection<Coach> coachCollectionOld = persistentTeam.getCoachCollection();
            Collection<Coach> coachCollectionNew = team.getCoachCollection();
            Collection<Play> playCollectionOld = persistentTeam.getPlayCollection();
            Collection<Play> playCollectionNew = team.getPlayCollection();
            Collection<Play> playCollection1Old = persistentTeam.getPlayCollection1();
            Collection<Play> playCollection1New = team.getPlayCollection1();
            Collection<Player> playerCollectionOld = persistentTeam.getPlayerCollection();
            Collection<Player> playerCollectionNew = team.getPlayerCollection();
            List<String> illegalOrphanMessages = null;
            for (Coach coachCollectionOldCoach : coachCollectionOld) {
                if (!coachCollectionNew.contains(coachCollectionOldCoach)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Coach " + coachCollectionOldCoach + " since its teamId field is not nullable.");
                }
            }
            for (Play playCollectionOldPlay : playCollectionOld) {
                if (!playCollectionNew.contains(playCollectionOldPlay)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Play " + playCollectionOldPlay + " since its teamHome field is not nullable.");
                }
            }
            for (Play playCollection1OldPlay : playCollection1Old) {
                if (!playCollection1New.contains(playCollection1OldPlay)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Play " + playCollection1OldPlay + " since its teamVisitor field is not nullable.");
                }
            }
            for (Player playerCollectionOldPlayer : playerCollectionOld) {
                if (!playerCollectionNew.contains(playerCollectionOldPlayer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Player " + playerCollectionOldPlayer + " since its teamId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (campeonatoIdNew != null) {
                campeonatoIdNew = em.getReference(campeonatoIdNew.getClass(), campeonatoIdNew.getId());
                team.setCampeonatoId(campeonatoIdNew);
            }
            Collection<Coach> attachedCoachCollectionNew = new ArrayList<Coach>();
            for (Coach coachCollectionNewCoachToAttach : coachCollectionNew) {
                coachCollectionNewCoachToAttach = em.getReference(coachCollectionNewCoachToAttach.getClass(), coachCollectionNewCoachToAttach.getId());
                attachedCoachCollectionNew.add(coachCollectionNewCoachToAttach);
            }
            coachCollectionNew = attachedCoachCollectionNew;
            team.setCoachCollection(coachCollectionNew);
            Collection<Play> attachedPlayCollectionNew = new ArrayList<Play>();
            for (Play playCollectionNewPlayToAttach : playCollectionNew) {
                playCollectionNewPlayToAttach = em.getReference(playCollectionNewPlayToAttach.getClass(), playCollectionNewPlayToAttach.getId());
                attachedPlayCollectionNew.add(playCollectionNewPlayToAttach);
            }
            playCollectionNew = attachedPlayCollectionNew;
            team.setPlayCollection(playCollectionNew);
            Collection<Play> attachedPlayCollection1New = new ArrayList<Play>();
            for (Play playCollection1NewPlayToAttach : playCollection1New) {
                playCollection1NewPlayToAttach = em.getReference(playCollection1NewPlayToAttach.getClass(), playCollection1NewPlayToAttach.getId());
                attachedPlayCollection1New.add(playCollection1NewPlayToAttach);
            }
            playCollection1New = attachedPlayCollection1New;
            team.setPlayCollection1(playCollection1New);
            Collection<Player> attachedPlayerCollectionNew = new ArrayList<Player>();
            for (Player playerCollectionNewPlayerToAttach : playerCollectionNew) {
                playerCollectionNewPlayerToAttach = em.getReference(playerCollectionNewPlayerToAttach.getClass(), playerCollectionNewPlayerToAttach.getId());
                attachedPlayerCollectionNew.add(playerCollectionNewPlayerToAttach);
            }
            playerCollectionNew = attachedPlayerCollectionNew;
            team.setPlayerCollection(playerCollectionNew);
            team = em.merge(team);
            if (campeonatoIdOld != null && !campeonatoIdOld.equals(campeonatoIdNew)) {
                campeonatoIdOld.getTeamCollection().remove(team);
                campeonatoIdOld = em.merge(campeonatoIdOld);
            }
            if (campeonatoIdNew != null && !campeonatoIdNew.equals(campeonatoIdOld)) {
                campeonatoIdNew.getTeamCollection().add(team);
                campeonatoIdNew = em.merge(campeonatoIdNew);
            }
            for (Coach coachCollectionNewCoach : coachCollectionNew) {
                if (!coachCollectionOld.contains(coachCollectionNewCoach)) {
                    Team oldTeamIdOfCoachCollectionNewCoach = coachCollectionNewCoach.getTeamId();
                    coachCollectionNewCoach.setTeamId(team);
                    coachCollectionNewCoach = em.merge(coachCollectionNewCoach);
                    if (oldTeamIdOfCoachCollectionNewCoach != null && !oldTeamIdOfCoachCollectionNewCoach.equals(team)) {
                        oldTeamIdOfCoachCollectionNewCoach.getCoachCollection().remove(coachCollectionNewCoach);
                        oldTeamIdOfCoachCollectionNewCoach = em.merge(oldTeamIdOfCoachCollectionNewCoach);
                    }
                }
            }
            for (Play playCollectionNewPlay : playCollectionNew) {
                if (!playCollectionOld.contains(playCollectionNewPlay)) {
                    Team oldTeamHomeOfPlayCollectionNewPlay = playCollectionNewPlay.getTeamHome();
                    playCollectionNewPlay.setTeamHome(team);
                    playCollectionNewPlay = em.merge(playCollectionNewPlay);
                    if (oldTeamHomeOfPlayCollectionNewPlay != null && !oldTeamHomeOfPlayCollectionNewPlay.equals(team)) {
                        oldTeamHomeOfPlayCollectionNewPlay.getPlayCollection().remove(playCollectionNewPlay);
                        oldTeamHomeOfPlayCollectionNewPlay = em.merge(oldTeamHomeOfPlayCollectionNewPlay);
                    }
                }
            }
            for (Play playCollection1NewPlay : playCollection1New) {
                if (!playCollection1Old.contains(playCollection1NewPlay)) {
                    Team oldTeamVisitorOfPlayCollection1NewPlay = playCollection1NewPlay.getTeamVisitor();
                    playCollection1NewPlay.setTeamVisitor(team);
                    playCollection1NewPlay = em.merge(playCollection1NewPlay);
                    if (oldTeamVisitorOfPlayCollection1NewPlay != null && !oldTeamVisitorOfPlayCollection1NewPlay.equals(team)) {
                        oldTeamVisitorOfPlayCollection1NewPlay.getPlayCollection1().remove(playCollection1NewPlay);
                        oldTeamVisitorOfPlayCollection1NewPlay = em.merge(oldTeamVisitorOfPlayCollection1NewPlay);
                    }
                }
            }
            for (Player playerCollectionNewPlayer : playerCollectionNew) {
                if (!playerCollectionOld.contains(playerCollectionNewPlayer)) {
                    Team oldTeamIdOfPlayerCollectionNewPlayer = playerCollectionNewPlayer.getTeamId();
                    playerCollectionNewPlayer.setTeamId(team);
                    playerCollectionNewPlayer = em.merge(playerCollectionNewPlayer);
                    if (oldTeamIdOfPlayerCollectionNewPlayer != null && !oldTeamIdOfPlayerCollectionNewPlayer.equals(team)) {
                        oldTeamIdOfPlayerCollectionNewPlayer.getPlayerCollection().remove(playerCollectionNewPlayer);
                        oldTeamIdOfPlayerCollectionNewPlayer = em.merge(oldTeamIdOfPlayerCollectionNewPlayer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = team.getId();
                if (findTeam(id) == null) {
                    throw new NonexistentEntityException("The team with id " + id + " no longer exists.");
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
            Team team;
            try {
                team = em.getReference(Team.class, id);
                team.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The team with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Coach> coachCollectionOrphanCheck = team.getCoachCollection();
            for (Coach coachCollectionOrphanCheckCoach : coachCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Team (" + team + ") cannot be destroyed since the Coach " + coachCollectionOrphanCheckCoach + " in its coachCollection field has a non-nullable teamId field.");
            }
            Collection<Play> playCollectionOrphanCheck = team.getPlayCollection();
            for (Play playCollectionOrphanCheckPlay : playCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Team (" + team + ") cannot be destroyed since the Play " + playCollectionOrphanCheckPlay + " in its playCollection field has a non-nullable teamHome field.");
            }
            Collection<Play> playCollection1OrphanCheck = team.getPlayCollection1();
            for (Play playCollection1OrphanCheckPlay : playCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Team (" + team + ") cannot be destroyed since the Play " + playCollection1OrphanCheckPlay + " in its playCollection1 field has a non-nullable teamVisitor field.");
            }
            Collection<Player> playerCollectionOrphanCheck = team.getPlayerCollection();
            for (Player playerCollectionOrphanCheckPlayer : playerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Team (" + team + ") cannot be destroyed since the Player " + playerCollectionOrphanCheckPlayer + " in its playerCollection field has a non-nullable teamId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Campeonato campeonatoId = team.getCampeonatoId();
            if (campeonatoId != null) {
                campeonatoId.getTeamCollection().remove(team);
                campeonatoId = em.merge(campeonatoId);
            }
            em.remove(team);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Team> findTeamEntities() {
        return findTeamEntities(true, -1, -1);
    }

    public List<Team> findTeamEntities(int maxResults, int firstResult) {
        return findTeamEntities(false, maxResults, firstResult);
    }

    private List<Team> findTeamEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Team.class));
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

    public Team findTeam(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Team.class, id);
        } finally {
            em.close();
        }
    }

    public int getTeamCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Team> rt = cq.from(Team.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
