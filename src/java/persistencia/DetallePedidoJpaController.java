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
public class DetallePedidoJpaController implements Serializable {
    
    public DetallePedidoJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public DetallePedidoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetallePedido detallePedido) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (detallePedido.getDetallePedidoPK() == null) {
            detallePedido.setDetallePedidoPK(new DetallePedidoPK());
        }
        detallePedido.getDetallePedidoPK().setNumeroPed(detallePedido.getNotaPedido().getNumeroPed());
        detallePedido.getDetallePedidoPK().setIdProducto(detallePedido.getProducto().getIdProducto());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NotaPedido notaPedido = detallePedido.getNotaPedido();
            if (notaPedido != null) {
                notaPedido = em.getReference(notaPedido.getClass(), notaPedido.getNumeroPed());
                detallePedido.setNotaPedido(notaPedido);
            }
            Producto producto = detallePedido.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getIdProducto());
                detallePedido.setProducto(producto);
            }
            em.persist(detallePedido);
            if (notaPedido != null) {
                notaPedido.getDetallePedidoList().add(detallePedido);
                notaPedido = em.merge(notaPedido);
            }
            if (producto != null) {
                producto.getDetallePedidoList().add(detallePedido);
                producto = em.merge(producto);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallePedido(detallePedido.getDetallePedidoPK()) != null) {
                throw new PreexistingEntityException("DetallePedido " + detallePedido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetallePedido detallePedido) throws NonexistentEntityException, RollbackFailureException, Exception {
        detallePedido.getDetallePedidoPK().setNumeroPed(detallePedido.getNotaPedido().getNumeroPed());
        detallePedido.getDetallePedidoPK().setIdProducto(detallePedido.getProducto().getIdProducto());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetallePedido persistentDetallePedido = em.find(DetallePedido.class, detallePedido.getDetallePedidoPK());
            NotaPedido notaPedidoOld = persistentDetallePedido.getNotaPedido();
            NotaPedido notaPedidoNew = detallePedido.getNotaPedido();
            Producto productoOld = persistentDetallePedido.getProducto();
            Producto productoNew = detallePedido.getProducto();
            if (notaPedidoNew != null) {
                notaPedidoNew = em.getReference(notaPedidoNew.getClass(), notaPedidoNew.getNumeroPed());
                detallePedido.setNotaPedido(notaPedidoNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getIdProducto());
                detallePedido.setProducto(productoNew);
            }
            detallePedido = em.merge(detallePedido);
            if (notaPedidoOld != null && !notaPedidoOld.equals(notaPedidoNew)) {
                notaPedidoOld.getDetallePedidoList().remove(detallePedido);
                notaPedidoOld = em.merge(notaPedidoOld);
            }
            if (notaPedidoNew != null && !notaPedidoNew.equals(notaPedidoOld)) {
                notaPedidoNew.getDetallePedidoList().add(detallePedido);
                notaPedidoNew = em.merge(notaPedidoNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDetallePedidoList().remove(detallePedido);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDetallePedidoList().add(detallePedido);
                productoNew = em.merge(productoNew);
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
                DetallePedidoPK id = detallePedido.getDetallePedidoPK();
                if (findDetallePedido(id) == null) {
                    throw new NonexistentEntityException("The detallePedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetallePedidoPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetallePedido detallePedido;
            try {
                detallePedido = em.getReference(DetallePedido.class, id);
                detallePedido.getDetallePedidoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallePedido with id " + id + " no longer exists.", enfe);
            }
            NotaPedido notaPedido = detallePedido.getNotaPedido();
            if (notaPedido != null) {
                notaPedido.getDetallePedidoList().remove(detallePedido);
                notaPedido = em.merge(notaPedido);
            }
            Producto producto = detallePedido.getProducto();
            if (producto != null) {
                producto.getDetallePedidoList().remove(detallePedido);
                producto = em.merge(producto);
            }
            em.remove(detallePedido);
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

    public List<DetallePedido> findDetallePedidoEntities() {
        return findDetallePedidoEntities(true, -1, -1);
    }

    public List<DetallePedido> findDetallePedidoEntities(int maxResults, int firstResult) {
        return findDetallePedidoEntities(false, maxResults, firstResult);
    }

    private List<DetallePedido> findDetallePedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallePedido.class));
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

    public DetallePedido findDetallePedido(DetallePedidoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetallePedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallePedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetallePedido> rt = cq.from(DetallePedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
