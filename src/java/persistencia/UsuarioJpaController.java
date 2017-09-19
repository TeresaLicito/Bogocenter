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
public class UsuarioJpaController implements Serializable {
    
    public UsuarioJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuario.getComprobantePagoList() == null) {
            usuario.setComprobantePagoList(new ArrayList<ComprobantePago>());
        }
        if (usuario.getNotaPedidoList() == null) {
            usuario.setNotaPedidoList(new ArrayList<NotaPedido>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoUsuario idUsuario = usuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                usuario.setIdUsuario(idUsuario);
            }
            List<ComprobantePago> attachedComprobantePagoList = new ArrayList<ComprobantePago>();
            for (ComprobantePago comprobantePagoListComprobantePagoToAttach : usuario.getComprobantePagoList()) {
                comprobantePagoListComprobantePagoToAttach = em.getReference(comprobantePagoListComprobantePagoToAttach.getClass(), comprobantePagoListComprobantePagoToAttach.getNumeroComp());
                attachedComprobantePagoList.add(comprobantePagoListComprobantePagoToAttach);
            }
            usuario.setComprobantePagoList(attachedComprobantePagoList);
            List<NotaPedido> attachedNotaPedidoList = new ArrayList<NotaPedido>();
            for (NotaPedido notaPedidoListNotaPedidoToAttach : usuario.getNotaPedidoList()) {
                notaPedidoListNotaPedidoToAttach = em.getReference(notaPedidoListNotaPedidoToAttach.getClass(), notaPedidoListNotaPedidoToAttach.getNumeroPed());
                attachedNotaPedidoList.add(notaPedidoListNotaPedidoToAttach);
            }
            usuario.setNotaPedidoList(attachedNotaPedidoList);
            em.persist(usuario);
            if (idUsuario != null) {
                idUsuario.getUsuarioList().add(usuario);
                idUsuario = em.merge(idUsuario);
            }
            for (ComprobantePago comprobantePagoListComprobantePago : usuario.getComprobantePagoList()) {
                Usuario oldIdCodigoOfComprobantePagoListComprobantePago = comprobantePagoListComprobantePago.getIdCodigo();
                comprobantePagoListComprobantePago.setIdCodigo(usuario);
                comprobantePagoListComprobantePago = em.merge(comprobantePagoListComprobantePago);
                if (oldIdCodigoOfComprobantePagoListComprobantePago != null) {
                    oldIdCodigoOfComprobantePagoListComprobantePago.getComprobantePagoList().remove(comprobantePagoListComprobantePago);
                    oldIdCodigoOfComprobantePagoListComprobantePago = em.merge(oldIdCodigoOfComprobantePagoListComprobantePago);
                }
            }
            for (NotaPedido notaPedidoListNotaPedido : usuario.getNotaPedidoList()) {
                Usuario oldIdCodigoOfNotaPedidoListNotaPedido = notaPedidoListNotaPedido.getIdCodigo();
                notaPedidoListNotaPedido.setIdCodigo(usuario);
                notaPedidoListNotaPedido = em.merge(notaPedidoListNotaPedido);
                if (oldIdCodigoOfNotaPedidoListNotaPedido != null) {
                    oldIdCodigoOfNotaPedidoListNotaPedido.getNotaPedidoList().remove(notaPedidoListNotaPedido);
                    oldIdCodigoOfNotaPedidoListNotaPedido = em.merge(oldIdCodigoOfNotaPedidoListNotaPedido);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuario(usuario.getIdCodigo()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdCodigo());
            TipoUsuario idUsuarioOld = persistentUsuario.getIdUsuario();
            TipoUsuario idUsuarioNew = usuario.getIdUsuario();
            List<ComprobantePago> comprobantePagoListOld = persistentUsuario.getComprobantePagoList();
            List<ComprobantePago> comprobantePagoListNew = usuario.getComprobantePagoList();
            List<NotaPedido> notaPedidoListOld = persistentUsuario.getNotaPedidoList();
            List<NotaPedido> notaPedidoListNew = usuario.getNotaPedidoList();
            List<String> illegalOrphanMessages = null;
            for (ComprobantePago comprobantePagoListOldComprobantePago : comprobantePagoListOld) {
                if (!comprobantePagoListNew.contains(comprobantePagoListOldComprobantePago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ComprobantePago " + comprobantePagoListOldComprobantePago + " since its idCodigo field is not nullable.");
                }
            }
            for (NotaPedido notaPedidoListOldNotaPedido : notaPedidoListOld) {
                if (!notaPedidoListNew.contains(notaPedidoListOldNotaPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain NotaPedido " + notaPedidoListOldNotaPedido + " since its idCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                usuario.setIdUsuario(idUsuarioNew);
            }
            List<ComprobantePago> attachedComprobantePagoListNew = new ArrayList<ComprobantePago>();
            for (ComprobantePago comprobantePagoListNewComprobantePagoToAttach : comprobantePagoListNew) {
                comprobantePagoListNewComprobantePagoToAttach = em.getReference(comprobantePagoListNewComprobantePagoToAttach.getClass(), comprobantePagoListNewComprobantePagoToAttach.getNumeroComp());
                attachedComprobantePagoListNew.add(comprobantePagoListNewComprobantePagoToAttach);
            }
            comprobantePagoListNew = attachedComprobantePagoListNew;
            usuario.setComprobantePagoList(comprobantePagoListNew);
            List<NotaPedido> attachedNotaPedidoListNew = new ArrayList<NotaPedido>();
            for (NotaPedido notaPedidoListNewNotaPedidoToAttach : notaPedidoListNew) {
                notaPedidoListNewNotaPedidoToAttach = em.getReference(notaPedidoListNewNotaPedidoToAttach.getClass(), notaPedidoListNewNotaPedidoToAttach.getNumeroPed());
                attachedNotaPedidoListNew.add(notaPedidoListNewNotaPedidoToAttach);
            }
            notaPedidoListNew = attachedNotaPedidoListNew;
            usuario.setNotaPedidoList(notaPedidoListNew);
            usuario = em.merge(usuario);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getUsuarioList().remove(usuario);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getUsuarioList().add(usuario);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (ComprobantePago comprobantePagoListNewComprobantePago : comprobantePagoListNew) {
                if (!comprobantePagoListOld.contains(comprobantePagoListNewComprobantePago)) {
                    Usuario oldIdCodigoOfComprobantePagoListNewComprobantePago = comprobantePagoListNewComprobantePago.getIdCodigo();
                    comprobantePagoListNewComprobantePago.setIdCodigo(usuario);
                    comprobantePagoListNewComprobantePago = em.merge(comprobantePagoListNewComprobantePago);
                    if (oldIdCodigoOfComprobantePagoListNewComprobantePago != null && !oldIdCodigoOfComprobantePagoListNewComprobantePago.equals(usuario)) {
                        oldIdCodigoOfComprobantePagoListNewComprobantePago.getComprobantePagoList().remove(comprobantePagoListNewComprobantePago);
                        oldIdCodigoOfComprobantePagoListNewComprobantePago = em.merge(oldIdCodigoOfComprobantePagoListNewComprobantePago);
                    }
                }
            }
            for (NotaPedido notaPedidoListNewNotaPedido : notaPedidoListNew) {
                if (!notaPedidoListOld.contains(notaPedidoListNewNotaPedido)) {
                    Usuario oldIdCodigoOfNotaPedidoListNewNotaPedido = notaPedidoListNewNotaPedido.getIdCodigo();
                    notaPedidoListNewNotaPedido.setIdCodigo(usuario);
                    notaPedidoListNewNotaPedido = em.merge(notaPedidoListNewNotaPedido);
                    if (oldIdCodigoOfNotaPedidoListNewNotaPedido != null && !oldIdCodigoOfNotaPedidoListNewNotaPedido.equals(usuario)) {
                        oldIdCodigoOfNotaPedidoListNewNotaPedido.getNotaPedidoList().remove(notaPedidoListNewNotaPedido);
                        oldIdCodigoOfNotaPedidoListNewNotaPedido = em.merge(oldIdCodigoOfNotaPedidoListNewNotaPedido);
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
                String id = usuario.getIdCodigo();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ComprobantePago> comprobantePagoListOrphanCheck = usuario.getComprobantePagoList();
            for (ComprobantePago comprobantePagoListOrphanCheckComprobantePago : comprobantePagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ComprobantePago " + comprobantePagoListOrphanCheckComprobantePago + " in its comprobantePagoList field has a non-nullable idCodigo field.");
            }
            List<NotaPedido> notaPedidoListOrphanCheck = usuario.getNotaPedidoList();
            for (NotaPedido notaPedidoListOrphanCheckNotaPedido : notaPedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the NotaPedido " + notaPedidoListOrphanCheckNotaPedido + " in its notaPedidoList field has a non-nullable idCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoUsuario idUsuario = usuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getUsuarioList().remove(usuario);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
