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
import models.Team;
import models.Campeonato;
import models.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Play;

/**
 *
 * @author Conta 2
 */
public class PlayJpaController implements Serializable {

    public PlayJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Play play) {
        if (play.getResultCollection() == null) {
            play.setResultCollection(new ArrayList<Result>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Team teamHome = play.getTeamHome();
            if (teamHome != null) {
                teamHome = em.getReference(teamHome.getClass(), teamHome.getId());
                play.setTeamHome(teamHome);
            }
            Team teamVisitor = play.getTeamVisitor();
            if (teamVisitor != null) {
                teamVisitor = em.getReference(teamVisitor.getClass(), teamVisitor.getId());
                play.setTeamVisitor(teamVisitor);
            }
            Campeonato campeonatoId = play.getCampeonatoId();
            if (campeonatoId != null) {
                campeonatoId = em.getReference(campeonatoId.getClass(), campeonatoId.getId());
                play.setCampeonatoId(campeonatoId);
            }
            Collection<Result> attachedResultCollection = new ArrayList<Result>();
            for (Result resultCollectionResultToAttach : play.getResultCollection()) {
                resultCollectionResultToAttach = em.getReference(resultCollectionResultToAttach.getClass(), resultCollectionResultToAttach.getId());
                attachedResultCollection.add(resultCollectionResultToAttach);
            }
            play.setResultCollection(attachedResultCollection);
            em.persist(play);
            if (teamHome != null) {
                teamHome.getPlayCollection().add(play);
                teamHome = em.merge(teamHome);
            }
            if (teamVisitor != null) {
                teamVisitor.getPlayCollection().add(play);
                teamVisitor = em.merge(teamVisitor);
            }
            if (campeonatoId != null) {
                campeonatoId.getPlayCollection().add(play);
                campeonatoId = em.merge(campeonatoId);
            }
            for (Result resultCollectionResult : play.getResultCollection()) {
                Play oldPlayIdOfResultCollectionResult = resultCollectionResult.getPlayId();
                resultCollectionResult.setPlayId(play);
                resultCollectionResult = em.merge(resultCollectionResult);
                if (oldPlayIdOfResultCollectionResult != null) {
                    oldPlayIdOfResultCollectionResult.getResultCollection().remove(resultCollectionResult);
                    oldPlayIdOfResultCollectionResult = em.merge(oldPlayIdOfResultCollectionResult);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Play play) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Play persistentPlay = em.find(Play.class, play.getId());
            Team teamHomeOld = persistentPlay.getTeamHome();
            Team teamHomeNew = play.getTeamHome();
            Team teamVisitorOld = persistentPlay.getTeamVisitor();
            Team teamVisitorNew = play.getTeamVisitor();
            Campeonato campeonatoIdOld = persistentPlay.getCampeonatoId();
            Campeonato campeonatoIdNew = play.getCampeonatoId();
            Collection<Result> resultCollectionOld = persistentPlay.getResultCollection();
            Collection<Result> resultCollectionNew = play.getResultCollection();
            List<String> illegalOrphanMessages = null;
            for (Result resultCollectionOldResult : resultCollectionOld) {
                if (!resultCollectionNew.contains(resultCollectionOldResult)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Result " + resultCollectionOldResult + " since its playId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (teamHomeNew != null) {
                teamHomeNew = em.getReference(teamHomeNew.getClass(), teamHomeNew.getId());
                play.setTeamHome(teamHomeNew);
            }
            if (teamVisitorNew != null) {
                teamVisitorNew = em.getReference(teamVisitorNew.getClass(), teamVisitorNew.getId());
                play.setTeamVisitor(teamVisitorNew);
            }
            if (campeonatoIdNew != null) {
                campeonatoIdNew = em.getReference(campeonatoIdNew.getClass(), campeonatoIdNew.getId());
                play.setCampeonatoId(campeonatoIdNew);
            }
            Collection<Result> attachedResultCollectionNew = new ArrayList<Result>();
            for (Result resultCollectionNewResultToAttach : resultCollectionNew) {
                resultCollectionNewResultToAttach = em.getReference(resultCollectionNewResultToAttach.getClass(), resultCollectionNewResultToAttach.getId());
                attachedResultCollectionNew.add(resultCollectionNewResultToAttach);
            }
            resultCollectionNew = attachedResultCollectionNew;
            play.setResultCollection(resultCollectionNew);
            play = em.merge(play);
            if (teamHomeOld != null && !teamHomeOld.equals(teamHomeNew)) {
                teamHomeOld.getPlayCollection().remove(play);
                teamHomeOld = em.merge(teamHomeOld);
            }
            if (teamHomeNew != null && !teamHomeNew.equals(teamHomeOld)) {
                teamHomeNew.getPlayCollection().add(play);
                teamHomeNew = em.merge(teamHomeNew);
            }
            if (teamVisitorOld != null && !teamVisitorOld.equals(teamVisitorNew)) {
                teamVisitorOld.getPlayCollection().remove(play);
                teamVisitorOld = em.merge(teamVisitorOld);
            }
            if (teamVisitorNew != null && !teamVisitorNew.equals(teamVisitorOld)) {
                teamVisitorNew.getPlayCollection().add(play);
                teamVisitorNew = em.merge(teamVisitorNew);
            }
            if (campeonatoIdOld != null && !campeonatoIdOld.equals(campeonatoIdNew)) {
                campeonatoIdOld.getPlayCollection().remove(play);
                campeonatoIdOld = em.merge(campeonatoIdOld);
            }
            if (campeonatoIdNew != null && !campeonatoIdNew.equals(campeonatoIdOld)) {
                campeonatoIdNew.getPlayCollection().add(play);
                campeonatoIdNew = em.merge(campeonatoIdNew);
            }
            for (Result resultCollectionNewResult : resultCollectionNew) {
                if (!resultCollectionOld.contains(resultCollectionNewResult)) {
                    Play oldPlayIdOfResultCollectionNewResult = resultCollectionNewResult.getPlayId();
                    resultCollectionNewResult.setPlayId(play);
                    resultCollectionNewResult = em.merge(resultCollectionNewResult);
                    if (oldPlayIdOfResultCollectionNewResult != null && !oldPlayIdOfResultCollectionNewResult.equals(play)) {
                        oldPlayIdOfResultCollectionNewResult.getResultCollection().remove(resultCollectionNewResult);
                        oldPlayIdOfResultCollectionNewResult = em.merge(oldPlayIdOfResultCollectionNewResult);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = play.getId();
                if (findPlay(id) == null) {
                    throw new NonexistentEntityException("The play with id " + id + " no longer exists.");
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
            Play play;
            try {
                play = em.getReference(Play.class, id);
                play.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The play with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Result> resultCollectionOrphanCheck = play.getResultCollection();
            for (Result resultCollectionOrphanCheckResult : resultCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Play (" + play + ") cannot be destroyed since the Result " + resultCollectionOrphanCheckResult + " in its resultCollection field has a non-nullable playId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Team teamHome = play.getTeamHome();
            if (teamHome != null) {
                teamHome.getPlayCollection().remove(play);
                teamHome = em.merge(teamHome);
            }
            Team teamVisitor = play.getTeamVisitor();
            if (teamVisitor != null) {
                teamVisitor.getPlayCollection().remove(play);
                teamVisitor = em.merge(teamVisitor);
            }
            Campeonato campeonatoId = play.getCampeonatoId();
            if (campeonatoId != null) {
                campeonatoId.getPlayCollection().remove(play);
                campeonatoId = em.merge(campeonatoId);
            }
            em.remove(play);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Play> findPlayEntities() {
        return findPlayEntities(true, -1, -1);
    }

    public List<Play> findPlayEntities(int maxResults, int firstResult) {
        return findPlayEntities(false, maxResults, firstResult);
    }

    private List<Play> findPlayEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Play.class));
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

    public Play findPlay(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Play.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlayCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Play> rt = cq.from(Play.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
