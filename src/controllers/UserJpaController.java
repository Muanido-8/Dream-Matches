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
import models.AccessToken;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Campeonato;
import models.User;

/**
 *
 * @author Conta 2
 */
public class UserJpaController implements Serializable {
    public static String lastMessage;
    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getAccessTokenCollection() == null) {
            user.setAccessTokenCollection(new ArrayList<AccessToken>());
        }
        if (user.getCampeonatoCollection() == null) {
            user.setCampeonatoCollection(new ArrayList<Campeonato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<AccessToken> attachedAccessTokenCollection = new ArrayList<AccessToken>();
            for (AccessToken accessTokenCollectionAccessTokenToAttach : user.getAccessTokenCollection()) {
                accessTokenCollectionAccessTokenToAttach = em.getReference(accessTokenCollectionAccessTokenToAttach.getClass(), accessTokenCollectionAccessTokenToAttach.getId());
                attachedAccessTokenCollection.add(accessTokenCollectionAccessTokenToAttach);
            }
            user.setAccessTokenCollection(attachedAccessTokenCollection);
            Collection<Campeonato> attachedCampeonatoCollection = new ArrayList<Campeonato>();
            for (Campeonato campeonatoCollectionCampeonatoToAttach : user.getCampeonatoCollection()) {
                campeonatoCollectionCampeonatoToAttach = em.getReference(campeonatoCollectionCampeonatoToAttach.getClass(), campeonatoCollectionCampeonatoToAttach.getId());
                attachedCampeonatoCollection.add(campeonatoCollectionCampeonatoToAttach);
            }
            user.setCampeonatoCollection(attachedCampeonatoCollection);
            em.persist(user);
            for (AccessToken accessTokenCollectionAccessToken : user.getAccessTokenCollection()) {
                User oldUserIdOfAccessTokenCollectionAccessToken = accessTokenCollectionAccessToken.getUserId();
                accessTokenCollectionAccessToken.setUserId(user);
                accessTokenCollectionAccessToken = em.merge(accessTokenCollectionAccessToken);
                if (oldUserIdOfAccessTokenCollectionAccessToken != null) {
                    oldUserIdOfAccessTokenCollectionAccessToken.getAccessTokenCollection().remove(accessTokenCollectionAccessToken);
                    oldUserIdOfAccessTokenCollectionAccessToken = em.merge(oldUserIdOfAccessTokenCollectionAccessToken);
                }
            }
            for (Campeonato campeonatoCollectionCampeonato : user.getCampeonatoCollection()) {
                User oldUserIdOfCampeonatoCollectionCampeonato = campeonatoCollectionCampeonato.getUserId();
                campeonatoCollectionCampeonato.setUserId(user);
                campeonatoCollectionCampeonato = em.merge(campeonatoCollectionCampeonato);
                if (oldUserIdOfCampeonatoCollectionCampeonato != null) {
                    oldUserIdOfCampeonatoCollectionCampeonato.getCampeonatoCollection().remove(campeonatoCollectionCampeonato);
                    oldUserIdOfCampeonatoCollectionCampeonato = em.merge(oldUserIdOfCampeonatoCollectionCampeonato);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Collection<AccessToken> accessTokenCollectionOld = persistentUser.getAccessTokenCollection();
            Collection<AccessToken> accessTokenCollectionNew = user.getAccessTokenCollection();
            Collection<Campeonato> campeonatoCollectionOld = persistentUser.getCampeonatoCollection();
            Collection<Campeonato> campeonatoCollectionNew = user.getCampeonatoCollection();
            List<String> illegalOrphanMessages = null;
            for (AccessToken accessTokenCollectionOldAccessToken : accessTokenCollectionOld) {
                if (!accessTokenCollectionNew.contains(accessTokenCollectionOldAccessToken)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccessToken " + accessTokenCollectionOldAccessToken + " since its userId field is not nullable.");
                }
            }
            for (Campeonato campeonatoCollectionOldCampeonato : campeonatoCollectionOld) {
                if (!campeonatoCollectionNew.contains(campeonatoCollectionOldCampeonato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Campeonato " + campeonatoCollectionOldCampeonato + " since its userId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AccessToken> attachedAccessTokenCollectionNew = new ArrayList<AccessToken>();
            for (AccessToken accessTokenCollectionNewAccessTokenToAttach : accessTokenCollectionNew) {
                accessTokenCollectionNewAccessTokenToAttach = em.getReference(accessTokenCollectionNewAccessTokenToAttach.getClass(), accessTokenCollectionNewAccessTokenToAttach.getId());
                attachedAccessTokenCollectionNew.add(accessTokenCollectionNewAccessTokenToAttach);
            }
            accessTokenCollectionNew = attachedAccessTokenCollectionNew;
            user.setAccessTokenCollection(accessTokenCollectionNew);
            Collection<Campeonato> attachedCampeonatoCollectionNew = new ArrayList<Campeonato>();
            for (Campeonato campeonatoCollectionNewCampeonatoToAttach : campeonatoCollectionNew) {
                campeonatoCollectionNewCampeonatoToAttach = em.getReference(campeonatoCollectionNewCampeonatoToAttach.getClass(), campeonatoCollectionNewCampeonatoToAttach.getId());
                attachedCampeonatoCollectionNew.add(campeonatoCollectionNewCampeonatoToAttach);
            }
            campeonatoCollectionNew = attachedCampeonatoCollectionNew;
            user.setCampeonatoCollection(campeonatoCollectionNew);
            user = em.merge(user);
            for (AccessToken accessTokenCollectionNewAccessToken : accessTokenCollectionNew) {
                if (!accessTokenCollectionOld.contains(accessTokenCollectionNewAccessToken)) {
                    User oldUserIdOfAccessTokenCollectionNewAccessToken = accessTokenCollectionNewAccessToken.getUserId();
                    accessTokenCollectionNewAccessToken.setUserId(user);
                    accessTokenCollectionNewAccessToken = em.merge(accessTokenCollectionNewAccessToken);
                    if (oldUserIdOfAccessTokenCollectionNewAccessToken != null && !oldUserIdOfAccessTokenCollectionNewAccessToken.equals(user)) {
                        oldUserIdOfAccessTokenCollectionNewAccessToken.getAccessTokenCollection().remove(accessTokenCollectionNewAccessToken);
                        oldUserIdOfAccessTokenCollectionNewAccessToken = em.merge(oldUserIdOfAccessTokenCollectionNewAccessToken);
                    }
                }
            }
            for (Campeonato campeonatoCollectionNewCampeonato : campeonatoCollectionNew) {
                if (!campeonatoCollectionOld.contains(campeonatoCollectionNewCampeonato)) {
                    User oldUserIdOfCampeonatoCollectionNewCampeonato = campeonatoCollectionNewCampeonato.getUserId();
                    campeonatoCollectionNewCampeonato.setUserId(user);
                    campeonatoCollectionNewCampeonato = em.merge(campeonatoCollectionNewCampeonato);
                    if (oldUserIdOfCampeonatoCollectionNewCampeonato != null && !oldUserIdOfCampeonatoCollectionNewCampeonato.equals(user)) {
                        oldUserIdOfCampeonatoCollectionNewCampeonato.getCampeonatoCollection().remove(campeonatoCollectionNewCampeonato);
                        oldUserIdOfCampeonatoCollectionNewCampeonato = em.merge(oldUserIdOfCampeonatoCollectionNewCampeonato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AccessToken> accessTokenCollectionOrphanCheck = user.getAccessTokenCollection();
            for (AccessToken accessTokenCollectionOrphanCheckAccessToken : accessTokenCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the AccessToken " + accessTokenCollectionOrphanCheckAccessToken + " in its accessTokenCollection field has a non-nullable userId field.");
            }
            Collection<Campeonato> campeonatoCollectionOrphanCheck = user.getCampeonatoCollection();
            for (Campeonato campeonatoCollectionOrphanCheckCampeonato : campeonatoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Campeonato " + campeonatoCollectionOrphanCheckCampeonato + " in its campeonatoCollection field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public User findByEmail(String email) {
        Query query = getEntityManager().createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        
        User user;
        try {
            user = (User) query.getSingleResult();
        } catch(Exception e) {
            user = null;
            System.out.println(e.getMessage());
        }
        return user;
    }
    
    public boolean login(String email, String hashedPassword) {
        User user = this.findByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(hashedPassword)) {
                UserJpaController.lastMessage = "Bem vindo " + user.getName();
                return true;
            } else {
                UserJpaController.lastMessage = "Senha errada";
            }
        } else {
            UserJpaController.lastMessage = "Usuario nao encontrado";
        }
        return false;
    }
}
