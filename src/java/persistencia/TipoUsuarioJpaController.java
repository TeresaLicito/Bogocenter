/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;
import persistencia.exceptions.RollbackFailureException;

/**
 *
 * @author Usuario
 */
public class TipoUsuarioJpaController implements Serializable {
    
    public TipoUsuarioJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public TipoUsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoUsuario tipoUsuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoUsuario.getUsuarioList() == null) {
            tipoUsuario.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : tipoUsuario.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdCodigo());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            tipoUsuario.setUsuarioList(attachedUsuarioList);
            em.persist(tipoUsuario);
            for (Usuario usuarioListUsuario : tipoUsuario.getUsuarioList()) {
                TipoUsuario oldIdUsuarioOfUsuarioListUsuario = usuarioListUsuario.getIdUsuario();
                usuarioListUsuario.setIdUsuario(tipoUsuario);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdUsuarioOfUsuarioListUsuario != null) {
                    oldIdUsuarioOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdUsuarioOfUsuarioListUsuario = em.merge(oldIdUsuarioOfUsuarioListUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoUsuario(tipoUsuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("TipoUsuario " + tipoUsuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoUsuario tipoUsuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoUsuario persistentTipoUsuario = em.find(TipoUsuario.class, tipoUsuario.getIdUsuario());
            List<Usuario> usuarioListOld = persistentTipoUsuario.getUsuarioList();
            List<Usuario> usuarioListNew = tipoUsuario.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdCodigo());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            tipoUsuario.setUsuarioList(usuarioListNew);
            tipoUsuario = em.merge(tipoUsuario);
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    TipoUsuario oldIdUsuarioOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdUsuario();
                    usuarioListNewUsuario.setIdUsuario(tipoUsuario);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdUsuarioOfUsuarioListNewUsuario != null && !oldIdUsuarioOfUsuarioListNewUsuario.equals(tipoUsuario)) {
                        oldIdUsuarioOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdUsuarioOfUsuarioListNewUsuario = em.merge(oldIdUsuarioOfUsuarioListNewUsuario);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tipoUsuario.getIdUsuario();
                if (findTipoUsuario(id) == null) {
                    throw new NonexistentEntityException("The tipoUsuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoUsuario tipoUsuario;
            try {
                tipoUsuario = em.getReference(TipoUsuario.class, id);
                tipoUsuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoUsuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usuario> usuarioListOrphanCheck = tipoUsuario.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoUsuario (" + tipoUsuario + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoUsuario);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoUsuario> findTipoUsuarioEntities() {
        return findTipoUsuarioEntities(true, -1, -1);
    }

    public List<TipoUsuario> findTipoUsuarioEntities(int maxResults, int firstResult) {
        return findTipoUsuarioEntities(false, maxResults, firstResult);
    }

    private List<TipoUsuario> findTipoUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoUsuario.class));
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

    public TipoUsuario findTipoUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoUsuario> rt = cq.from(TipoUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
