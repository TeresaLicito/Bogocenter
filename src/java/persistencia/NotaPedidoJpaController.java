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
public class NotaPedidoJpaController implements Serializable {

    public NotaPedidoJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public NotaPedidoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NotaPedido notaPedido) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (notaPedido.getDetallePedidoList() == null) {
            notaPedido.setDetallePedidoList(new ArrayList<DetallePedido>());
        }
        if (notaPedido.getComprobantePagoList() == null) {
            notaPedido.setComprobantePagoList(new ArrayList<ComprobantePago>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario idCodigo = notaPedido.getIdCodigo();
            if (idCodigo != null) {
                idCodigo = em.getReference(idCodigo.getClass(), idCodigo.getIdCodigo());
                notaPedido.setIdCodigo(idCodigo);
            }
            List<DetallePedido> attachedDetallePedidoList = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoListDetallePedidoToAttach : notaPedido.getDetallePedidoList()) {
                detallePedidoListDetallePedidoToAttach = em.getReference(detallePedidoListDetallePedidoToAttach.getClass(), detallePedidoListDetallePedidoToAttach.getDetallePedidoPK());
                attachedDetallePedidoList.add(detallePedidoListDetallePedidoToAttach);
            }
            notaPedido.setDetallePedidoList(attachedDetallePedidoList);
            List<ComprobantePago> attachedComprobantePagoList = new ArrayList<ComprobantePago>();
            for (ComprobantePago comprobantePagoListComprobantePagoToAttach : notaPedido.getComprobantePagoList()) {
                comprobantePagoListComprobantePagoToAttach = em.getReference(comprobantePagoListComprobantePagoToAttach.getClass(), comprobantePagoListComprobantePagoToAttach.getNumeroComp());
                attachedComprobantePagoList.add(comprobantePagoListComprobantePagoToAttach);
            }
            notaPedido.setComprobantePagoList(attachedComprobantePagoList);
            em.persist(notaPedido);
            if (idCodigo != null) {
                idCodigo.getNotaPedidoList().add(notaPedido);
                idCodigo = em.merge(idCodigo);
            }
            for (DetallePedido detallePedidoListDetallePedido : notaPedido.getDetallePedidoList()) {
                NotaPedido oldNotaPedidoOfDetallePedidoListDetallePedido = detallePedidoListDetallePedido.getNotaPedido();
                detallePedidoListDetallePedido.setNotaPedido(notaPedido);
                detallePedidoListDetallePedido = em.merge(detallePedidoListDetallePedido);
                if (oldNotaPedidoOfDetallePedidoListDetallePedido != null) {
                    oldNotaPedidoOfDetallePedidoListDetallePedido.getDetallePedidoList().remove(detallePedidoListDetallePedido);
                    oldNotaPedidoOfDetallePedidoListDetallePedido = em.merge(oldNotaPedidoOfDetallePedidoListDetallePedido);
                }
            }
            for (ComprobantePago comprobantePagoListComprobantePago : notaPedido.getComprobantePagoList()) {
                NotaPedido oldNumeroPedOfComprobantePagoListComprobantePago = comprobantePagoListComprobantePago.getNumeroPed();
                comprobantePagoListComprobantePago.setNumeroPed(notaPedido);
                comprobantePagoListComprobantePago = em.merge(comprobantePagoListComprobantePago);
                if (oldNumeroPedOfComprobantePagoListComprobantePago != null) {
                    oldNumeroPedOfComprobantePagoListComprobantePago.getComprobantePagoList().remove(comprobantePagoListComprobantePago);
                    oldNumeroPedOfComprobantePagoListComprobantePago = em.merge(oldNumeroPedOfComprobantePagoListComprobantePago);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findNotaPedido(notaPedido.getNumeroPed()) != null) {
                throw new PreexistingEntityException("NotaPedido " + notaPedido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NotaPedido notaPedido) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NotaPedido persistentNotaPedido = em.find(NotaPedido.class, notaPedido.getNumeroPed());
            Usuario idCodigoOld = persistentNotaPedido.getIdCodigo();
            Usuario idCodigoNew = notaPedido.getIdCodigo();
            List<DetallePedido> detallePedidoListOld = persistentNotaPedido.getDetallePedidoList();
            List<DetallePedido> detallePedidoListNew = notaPedido.getDetallePedidoList();
            List<ComprobantePago> comprobantePagoListOld = persistentNotaPedido.getComprobantePagoList();
            List<ComprobantePago> comprobantePagoListNew = notaPedido.getComprobantePagoList();
            List<String> illegalOrphanMessages = null;
            for (DetallePedido detallePedidoListOldDetallePedido : detallePedidoListOld) {
                if (!detallePedidoListNew.contains(detallePedidoListOldDetallePedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetallePedido " + detallePedidoListOldDetallePedido + " since its notaPedido field is not nullable.");
                }
            }
            for (ComprobantePago comprobantePagoListOldComprobantePago : comprobantePagoListOld) {
                if (!comprobantePagoListNew.contains(comprobantePagoListOldComprobantePago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComprobantePago " + comprobantePagoListOldComprobantePago + " since its numeroPed field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCodigoNew != null) {
                idCodigoNew = em.getReference(idCodigoNew.getClass(), idCodigoNew.getIdCodigo());
                notaPedido.setIdCodigo(idCodigoNew);
            }
            List<DetallePedido> attachedDetallePedidoListNew = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoListNewDetallePedidoToAttach : detallePedidoListNew) {
                detallePedidoListNewDetallePedidoToAttach = em.getReference(detallePedidoListNewDetallePedidoToAttach.getClass(), detallePedidoListNewDetallePedidoToAttach.getDetallePedidoPK());
                attachedDetallePedidoListNew.add(detallePedidoListNewDetallePedidoToAttach);
            }
            detallePedidoListNew = attachedDetallePedidoListNew;
            notaPedido.setDetallePedidoList(detallePedidoListNew);
            List<ComprobantePago> attachedComprobantePagoListNew = new ArrayList<ComprobantePago>();
            for (ComprobantePago comprobantePagoListNewComprobantePagoToAttach : comprobantePagoListNew) {
                comprobantePagoListNewComprobantePagoToAttach = em.getReference(comprobantePagoListNewComprobantePagoToAttach.getClass(), comprobantePagoListNewComprobantePagoToAttach.getNumeroComp());
                attachedComprobantePagoListNew.add(comprobantePagoListNewComprobantePagoToAttach);
            }
            comprobantePagoListNew = attachedComprobantePagoListNew;
            notaPedido.setComprobantePagoList(comprobantePagoListNew);
            notaPedido = em.merge(notaPedido);
            if (idCodigoOld != null && !idCodigoOld.equals(idCodigoNew)) {
                idCodigoOld.getNotaPedidoList().remove(notaPedido);
                idCodigoOld = em.merge(idCodigoOld);
            }
            if (idCodigoNew != null && !idCodigoNew.equals(idCodigoOld)) {
                idCodigoNew.getNotaPedidoList().add(notaPedido);
                idCodigoNew = em.merge(idCodigoNew);
            }
            for (DetallePedido detallePedidoListNewDetallePedido : detallePedidoListNew) {
                if (!detallePedidoListOld.contains(detallePedidoListNewDetallePedido)) {
                    NotaPedido oldNotaPedidoOfDetallePedidoListNewDetallePedido = detallePedidoListNewDetallePedido.getNotaPedido();
                    detallePedidoListNewDetallePedido.setNotaPedido(notaPedido);
                    detallePedidoListNewDetallePedido = em.merge(detallePedidoListNewDetallePedido);
                    if (oldNotaPedidoOfDetallePedidoListNewDetallePedido != null && !oldNotaPedidoOfDetallePedidoListNewDetallePedido.equals(notaPedido)) {
                        oldNotaPedidoOfDetallePedidoListNewDetallePedido.getDetallePedidoList().remove(detallePedidoListNewDetallePedido);
                        oldNotaPedidoOfDetallePedidoListNewDetallePedido = em.merge(oldNotaPedidoOfDetallePedidoListNewDetallePedido);
                    }
                }
            }
            for (ComprobantePago comprobantePagoListNewComprobantePago : comprobantePagoListNew) {
                if (!comprobantePagoListOld.contains(comprobantePagoListNewComprobantePago)) {
                    NotaPedido oldNumeroPedOfComprobantePagoListNewComprobantePago = comprobantePagoListNewComprobantePago.getNumeroPed();
                    comprobantePagoListNewComprobantePago.setNumeroPed(notaPedido);
                    comprobantePagoListNewComprobantePago = em.merge(comprobantePagoListNewComprobantePago);
                    if (oldNumeroPedOfComprobantePagoListNewComprobantePago != null && !oldNumeroPedOfComprobantePagoListNewComprobantePago.equals(notaPedido)) {
                        oldNumeroPedOfComprobantePagoListNewComprobantePago.getComprobantePagoList().remove(comprobantePagoListNewComprobantePago);
                        oldNumeroPedOfComprobantePagoListNewComprobantePago = em.merge(oldNumeroPedOfComprobantePagoListNewComprobantePago);
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
                String id = notaPedido.getNumeroPed();
                if (findNotaPedido(id) == null) {
                    throw new NonexistentEntityException("The notaPedido with id " + id + " no longer exists.");
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
            NotaPedido notaPedido;
            try {
                notaPedido = em.getReference(NotaPedido.class, id);
                notaPedido.getNumeroPed();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notaPedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetallePedido> detallePedidoListOrphanCheck = notaPedido.getDetallePedidoList();
            for (DetallePedido detallePedidoListOrphanCheckDetallePedido : detallePedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NotaPedido (" + notaPedido + ") cannot be destroyed since the DetallePedido " + detallePedidoListOrphanCheckDetallePedido + " in its detallePedidoList field has a non-nullable notaPedido field.");
            }
            List<ComprobantePago> comprobantePagoListOrphanCheck = notaPedido.getComprobantePagoList();
            for (ComprobantePago comprobantePagoListOrphanCheckComprobantePago : comprobantePagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NotaPedido (" + notaPedido + ") cannot be destroyed since the ComprobantePago " + comprobantePagoListOrphanCheckComprobantePago + " in its comprobantePagoList field has a non-nullable numeroPed field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idCodigo = notaPedido.getIdCodigo();
            if (idCodigo != null) {
                idCodigo.getNotaPedidoList().remove(notaPedido);
                idCodigo = em.merge(idCodigo);
            }
            em.remove(notaPedido);
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

    public List<NotaPedido> findNotaPedidoEntities() {
        return findNotaPedidoEntities(true, -1, -1);
    }

    public List<NotaPedido> findNotaPedidoEntities(int maxResults, int firstResult) {
        return findNotaPedidoEntities(false, maxResults, firstResult);
    }

    private List<NotaPedido> findNotaPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NotaPedido.class));
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

    public NotaPedido findNotaPedido(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NotaPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NotaPedido> rt = cq.from(NotaPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
