/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;
import persistencia.exceptions.RollbackFailureException;

/**
 *
 * @author Usuario
 */
public class ComprobantePagoJpaController implements Serializable {
    public ComprobantePagoJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public ComprobantePagoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComprobantePago comprobantePago) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NotaPedido numeroPed = comprobantePago.getNumeroPed();
            if (numeroPed != null) {
                numeroPed = em.getReference(numeroPed.getClass(), numeroPed.getNumeroPed());
                comprobantePago.setNumeroPed(numeroPed);
            }
            Usuario idCodigo = comprobantePago.getIdCodigo();
            if (idCodigo != null) {
                idCodigo = em.getReference(idCodigo.getClass(), idCodigo.getIdCodigo());
                comprobantePago.setIdCodigo(idCodigo);
            }
            em.persist(comprobantePago);
            if (numeroPed != null) {
                numeroPed.getComprobantePagoList().add(comprobantePago);
                numeroPed = em.merge(numeroPed);
            }
            if (idCodigo != null) {
                idCodigo.getComprobantePagoList().add(comprobantePago);
                idCodigo = em.merge(idCodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findComprobantePago(comprobantePago.getNumeroComp()) != null) {
                throw new PreexistingEntityException("ComprobantePago " + comprobantePago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComprobantePago comprobantePago) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComprobantePago persistentComprobantePago = em.find(ComprobantePago.class, comprobantePago.getNumeroComp());
            NotaPedido numeroPedOld = persistentComprobantePago.getNumeroPed();
            NotaPedido numeroPedNew = comprobantePago.getNumeroPed();
            Usuario idCodigoOld = persistentComprobantePago.getIdCodigo();
            Usuario idCodigoNew = comprobantePago.getIdCodigo();
            if (numeroPedNew != null) {
                numeroPedNew = em.getReference(numeroPedNew.getClass(), numeroPedNew.getNumeroPed());
                comprobantePago.setNumeroPed(numeroPedNew);
            }
            if (idCodigoNew != null) {
                idCodigoNew = em.getReference(idCodigoNew.getClass(), idCodigoNew.getIdCodigo());
                comprobantePago.setIdCodigo(idCodigoNew);
            }
            comprobantePago = em.merge(comprobantePago);
            if (numeroPedOld != null && !numeroPedOld.equals(numeroPedNew)) {
                numeroPedOld.getComprobantePagoList().remove(comprobantePago);
                numeroPedOld = em.merge(numeroPedOld);
            }
            if (numeroPedNew != null && !numeroPedNew.equals(numeroPedOld)) {
                numeroPedNew.getComprobantePagoList().add(comprobantePago);
                numeroPedNew = em.merge(numeroPedNew);
            }
            if (idCodigoOld != null && !idCodigoOld.equals(idCodigoNew)) {
                idCodigoOld.getComprobantePagoList().remove(comprobantePago);
                idCodigoOld = em.merge(idCodigoOld);
            }
            if (idCodigoNew != null && !idCodigoNew.equals(idCodigoOld)) {
                idCodigoNew.getComprobantePagoList().add(comprobantePago);
                idCodigoNew = em.merge(idCodigoNew);
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
                String id = comprobantePago.getNumeroComp();
                if (findComprobantePago(id) == null) {
                    throw new NonexistentEntityException("The comprobantePago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ComprobantePago comprobantePago;
            try {
                comprobantePago = em.getReference(ComprobantePago.class, id);
                comprobantePago.getNumeroComp();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprobantePago with id " + id + " no longer exists.", enfe);
            }
            NotaPedido numeroPed = comprobantePago.getNumeroPed();
            if (numeroPed != null) {
                numeroPed.getComprobantePagoList().remove(comprobantePago);
                numeroPed = em.merge(numeroPed);
            }
            Usuario idCodigo = comprobantePago.getIdCodigo();
            if (idCodigo != null) {
                idCodigo.getComprobantePagoList().remove(comprobantePago);
                idCodigo = em.merge(idCodigo);
            }
            em.remove(comprobantePago);
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

    public List<ComprobantePago> findComprobantePagoEntities() {
        return findComprobantePagoEntities(true, -1, -1);
    }

    public List<ComprobantePago> findComprobantePagoEntities(int maxResults, int firstResult) {
        return findComprobantePagoEntities(false, maxResults, firstResult);
    }

    private List<ComprobantePago> findComprobantePagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComprobantePago.class));
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

    public ComprobantePago findComprobantePago(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComprobantePago.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprobantePagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComprobantePago> rt = cq.from(ComprobantePago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
