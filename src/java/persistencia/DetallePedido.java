/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "DETALLE_PEDIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallePedido.findAll", query = "SELECT d FROM DetallePedido d")
    , @NamedQuery(name = "DetallePedido.findByNumeroPed", query = "SELECT d FROM DetallePedido d WHERE d.detallePedidoPK.numeroPed = :numeroPed")
    , @NamedQuery(name = "DetallePedido.findByIdProducto", query = "SELECT d FROM DetallePedido d WHERE d.detallePedidoPK.idProducto = :idProducto")
    , @NamedQuery(name = "DetallePedido.findByCantidad", query = "SELECT d FROM DetallePedido d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetallePedido.findBySubTotal", query = "SELECT d FROM DetallePedido d WHERE d.subTotal = :subTotal")})
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallePedidoPK detallePedidoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    @JoinColumn(name = "NUMERO_PED", referencedColumnName = "NUMERO_PED", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private NotaPedido notaPedido;
    @JoinColumn(name = "ID_PRODUCTO", referencedColumnName = "ID_PRODUCTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(DetallePedidoPK detallePedidoPK) {
        this.detallePedidoPK = detallePedidoPK;
    }

    public DetallePedido(DetallePedidoPK detallePedidoPK, int cantidad, BigDecimal subTotal) {
        this.detallePedidoPK = detallePedidoPK;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
    }

    public DetallePedido(String numeroPed, String idProducto) {
        this.detallePedidoPK = new DetallePedidoPK(numeroPed, idProducto);
    }

    public DetallePedidoPK getDetallePedidoPK() {
        return detallePedidoPK;
    }

    public void setDetallePedidoPK(DetallePedidoPK detallePedidoPK) {
        this.detallePedidoPK = detallePedidoPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public NotaPedido getNotaPedido() {
        return notaPedido;
    }

    public void setNotaPedido(NotaPedido notaPedido) {
        this.notaPedido = notaPedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallePedidoPK != null ? detallePedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePedido)) {
            return false;
        }
        DetallePedido other = (DetallePedido) object;
        if ((this.detallePedidoPK == null && other.detallePedidoPK != null) || (this.detallePedidoPK != null && !this.detallePedidoPK.equals(other.detallePedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.DetallePedido[ detallePedidoPK=" + detallePedidoPK + " ]";
    }
    
}
