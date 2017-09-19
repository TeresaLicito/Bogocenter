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
public class ProductoJpaController implements Serializable {
    
    public ProductoJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (producto.getDetallePedidoList() == null) {
            producto.setDetallePedidoList(new ArrayList<DetallePedido>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoProducto codigo = producto.getCodigo();
            if (codigo != null) {
                codigo = em.getReference(codigo.getClass(), codigo.getCodigo());
                producto.setCodigo(codigo);
            }
            List<DetallePedido> attachedDetallePedidoList = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoListDetallePedidoToAttach : producto.getDetallePedidoList()) {
                detallePedidoListDetallePedidoToAttach = em.getReference(detallePedidoListDetallePedidoToAttach.getClass(), detallePedidoListDetallePedidoToAttach.getDetallePedidoPK());
                attachedDetallePedidoList.add(detallePedidoListDetallePedidoToAttach);
            }
            producto.setDetallePedidoList(attachedDetallePedidoList);
            em.persist(producto);
            if (codigo != null) {
                codigo.getProductoList().add(producto);
                codigo = em.merge(codigo);
            }
            for (DetallePedido detallePedidoListDetallePedido : producto.getDetallePedidoList()) {
                Producto oldProductoOfDetallePedidoListDetallePedido = detallePedidoListDetallePedido.getProducto();
                detallePedidoListDetallePedido.setProducto(producto);
                detallePedidoListDetallePedido = em.merge(detallePedidoListDetallePedido);
                if (oldProductoOfDetallePedidoListDetallePedido != null) {
                    oldProductoOfDetallePedidoListDetallePedido.getDetallePedidoList().remove(detallePedidoListDetallePedido);
                    oldProductoOfDetallePedidoListDetallePedido = em.merge(oldProductoOfDetallePedidoListDetallePedido);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProducto(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            TipoProducto codigoOld = persistentProducto.getCodigo();
            TipoProducto codigoNew = producto.getCodigo();
            List<DetallePedido> detallePedidoListOld = persistentProducto.getDetallePedidoList();
            List<DetallePedido> detallePedidoListNew = producto.getDetallePedidoList();
            List<String> illegalOrphanMessages = null;
            for (DetallePedido detallePedidoListOldDetallePedido : detallePedidoListOld) {
                if (!detallePedidoListNew.contains(detallePedidoListOldDetallePedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetallePedido " + detallePedidoListOldDetallePedido + " since its producto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoNew != null) {
                codigoNew = em.getReference(codigoNew.getClass(), codigoNew.getCodigo());
                producto.setCodigo(codigoNew);
            }
            List<DetallePedido> attachedDetallePedidoListNew = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedidoListNewDetallePedidoToAttach : detallePedidoListNew) {
                detallePedidoListNewDetallePedidoToAttach = em.getReference(detallePedidoListNewDetallePedidoToAttach.getClass(), detallePedidoListNewDetallePedidoToAttach.getDetallePedidoPK());
                attachedDetallePedidoListNew.add(detallePedidoListNewDetallePedidoToAttach);
            }
            detallePedidoListNew = attachedDetallePedidoListNew;
            producto.setDetallePedidoList(detallePedidoListNew);
            producto = em.merge(producto);
            if (codigoOld != null && !codigoOld.equals(codigoNew)) {
                codigoOld.getProductoList().remove(producto);
                codigoOld = em.merge(codigoOld);
            }
            if (codigoNew != null && !codigoNew.equals(codigoOld)) {
                codigoNew.getProductoList().add(producto);
                codigoNew = em.merge(codigoNew);
            }
            for (DetallePedido detallePedidoListNewDetallePedido : detallePedidoListNew) {
                if (!detallePedidoListOld.contains(detallePedidoListNewDetallePedido)) {
                    Producto oldProductoOfDetallePedidoListNewDetallePedido = detallePedidoListNewDetallePedido.getProducto();
                    detallePedidoListNewDetallePedido.setProducto(producto);
                    detallePedidoListNewDetallePedido = em.merge(detallePedidoListNewDetallePedido);
                    if (oldProductoOfDetallePedidoListNewDetallePedido != null && !oldProductoOfDetallePedidoListNewDetallePedido.equals(producto)) {
                        oldProductoOfDetallePedidoListNewDetallePedido.getDetallePedidoList().remove(detallePedidoListNewDetallePedido);
                        oldProductoOfDetallePedidoListNewDetallePedido = em.merge(oldProductoOfDetallePedidoListNewDetallePedido);
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
                String id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetallePedido> detallePedidoListOrphanCheck = producto.getDetallePedidoList();
            for (DetallePedido detallePedidoListOrphanCheckDetallePedido : detallePedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the DetallePedido " + detallePedidoListOrphanCheckDetallePedido + " in its detallePedidoList field has a non-nullable producto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoProducto codigo = producto.getCodigo();
            if (codigo != null) {
                codigo.getProductoList().remove(producto);
                codigo = em.merge(codigo);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
