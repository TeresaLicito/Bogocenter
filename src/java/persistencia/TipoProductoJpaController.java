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
public class TipoProductoJpaController implements Serializable {
    
    public TipoProductoJpaController(){
    emf=Persistence.createEntityManagerFactory("bogocentroPU");
    }
    
    public TipoProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoProducto tipoProducto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoProducto.getProductoList() == null) {
            tipoProducto.setProductoList(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : tipoProducto.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getIdProducto());
                attachedProductoList.add(productoListProductoToAttach);
            }
            tipoProducto.setProductoList(attachedProductoList);
            em.persist(tipoProducto);
            for (Producto productoListProducto : tipoProducto.getProductoList()) {
                TipoProducto oldCodigoOfProductoListProducto = productoListProducto.getCodigo();
                productoListProducto.setCodigo(tipoProducto);
                productoListProducto = em.merge(productoListProducto);
                if (oldCodigoOfProductoListProducto != null) {
                    oldCodigoOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldCodigoOfProductoListProducto = em.merge(oldCodigoOfProductoListProducto);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoProducto(tipoProducto.getCodigo()) != null) {
                throw new PreexistingEntityException("TipoProducto " + tipoProducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoProducto tipoProducto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoProducto persistentTipoProducto = em.find(TipoProducto.class, tipoProducto.getCodigo());
            List<Producto> productoListOld = persistentTipoProducto.getProductoList();
            List<Producto> productoListNew = tipoProducto.getProductoList();
            List<String> illegalOrphanMessages = null;
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoListOldProducto + " since its codigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getIdProducto());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            tipoProducto.setProductoList(productoListNew);
            tipoProducto = em.merge(tipoProducto);
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    TipoProducto oldCodigoOfProductoListNewProducto = productoListNewProducto.getCodigo();
                    productoListNewProducto.setCodigo(tipoProducto);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldCodigoOfProductoListNewProducto != null && !oldCodigoOfProductoListNewProducto.equals(tipoProducto)) {
                        oldCodigoOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldCodigoOfProductoListNewProducto = em.merge(oldCodigoOfProductoListNewProducto);
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
                String id = tipoProducto.getCodigo();
                if (findTipoProducto(id) == null) {
                    throw new NonexistentEntityException("The tipoProducto with id " + id + " no longer exists.");
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
            TipoProducto tipoProducto;
            try {
                tipoProducto = em.getReference(TipoProducto.class, id);
                tipoProducto.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoProducto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Producto> productoListOrphanCheck = tipoProducto.getProductoList();
            for (Producto productoListOrphanCheckProducto : productoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoProducto (" + tipoProducto + ") cannot be destroyed since the Producto " + productoListOrphanCheckProducto + " in its productoList field has a non-nullable codigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoProducto);
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

    public List<TipoProducto> findTipoProductoEntities() {
        return findTipoProductoEntities(true, -1, -1);
    }

    public List<TipoProducto> findTipoProductoEntities(int maxResults, int firstResult) {
        return findTipoProductoEntities(false, maxResults, firstResult);
    }

    private List<TipoProducto> findTipoProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoProducto.class));
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

    public TipoProducto findTipoProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoProducto> rt = cq.from(TipoProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
