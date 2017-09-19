/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Usuario
 */
@Embeddable
public class DetallePedidoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "NUMERO_PED")
    private String numeroPed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "ID_PRODUCTO")
    private String idProducto;

    public DetallePedidoPK() {
    }

    public DetallePedidoPK(String numeroPed, String idProducto) {
        this.numeroPed = numeroPed;
        this.idProducto = idProducto;
    }

    public String getNumeroPed() {
        return numeroPed;
    }

    public void setNumeroPed(String numeroPed) {
        this.numeroPed = numeroPed;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroPed != null ? numeroPed.hashCode() : 0);
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePedidoPK)) {
            return false;
        }
        DetallePedidoPK other = (DetallePedidoPK) object;
        if ((this.numeroPed == null && other.numeroPed != null) || (this.numeroPed != null && !this.numeroPed.equals(other.numeroPed))) {
            return false;
        }
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.DetallePedidoPK[ numeroPed=" + numeroPed + ", idProducto=" + idProducto + " ]";
    }
    
}
