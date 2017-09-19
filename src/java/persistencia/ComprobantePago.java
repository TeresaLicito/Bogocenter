/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "COMPROBANTE_PAGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprobantePago.findAll", query = "SELECT c FROM ComprobantePago c")
    , @NamedQuery(name = "ComprobantePago.findByNumeroComp", query = "SELECT c FROM ComprobantePago c WHERE c.numeroComp = :numeroComp")
    , @NamedQuery(name = "ComprobantePago.findByFecha", query = "SELECT c FROM ComprobantePago c WHERE c.fecha = :fecha")})
public class ComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "NUMERO_COMP")
    private String numeroComp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FECHA")
    private String fecha;
    @JoinColumn(name = "NUMERO_PED", referencedColumnName = "NUMERO_PED")
    @ManyToOne(optional = false)
    private NotaPedido numeroPed;
    @JoinColumn(name = "ID_CODIGO", referencedColumnName = "ID_CODIGO")
    @ManyToOne(optional = false)
    private Usuario idCodigo;

    public ComprobantePago() {
    }

    public ComprobantePago(String numeroComp) {
        this.numeroComp = numeroComp;
    }

    public ComprobantePago(String numeroComp, String fecha) {
        this.numeroComp = numeroComp;
        this.fecha = fecha;
    }

    public String getNumeroComp() {
        return numeroComp;
    }

    public void setNumeroComp(String numeroComp) {
        this.numeroComp = numeroComp;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public NotaPedido getNumeroPed() {
        return numeroPed;
    }

    public void setNumeroPed(NotaPedido numeroPed) {
        this.numeroPed = numeroPed;
    }

    public Usuario getIdCodigo() {
        return idCodigo;
    }

    public void setIdCodigo(Usuario idCodigo) {
        this.idCodigo = idCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroComp != null ? numeroComp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprobantePago)) {
            return false;
        }
        ComprobantePago other = (ComprobantePago) object;
        if ((this.numeroComp == null && other.numeroComp != null) || (this.numeroComp != null && !this.numeroComp.equals(other.numeroComp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.ComprobantePago[ numeroComp=" + numeroComp + " ]";
    }
    
}
