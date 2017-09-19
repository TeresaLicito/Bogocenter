/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "NOTA_PEDIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotaPedido.findAll", query = "SELECT n FROM NotaPedido n")
    , @NamedQuery(name = "NotaPedido.findByNumeroPed", query = "SELECT n FROM NotaPedido n WHERE n.numeroPed = :numeroPed")
    , @NamedQuery(name = "NotaPedido.findByFecha", query = "SELECT n FROM NotaPedido n WHERE n.fecha = :fecha")
    , @NamedQuery(name = "NotaPedido.findByImporte", query = "SELECT n FROM NotaPedido n WHERE n.importe = :importe")})
public class NotaPedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "NUMERO_PED")
    private String numeroPed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FECHA")
    private String fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMPORTE")
    private BigDecimal importe;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notaPedido")
    private List<DetallePedido> detallePedidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numeroPed")
    private List<ComprobantePago> comprobantePagoList;
    @JoinColumn(name = "ID_CODIGO", referencedColumnName = "ID_CODIGO")
    @ManyToOne(optional = false)
    private Usuario idCodigo;

    public NotaPedido() {
    }

    public NotaPedido(String numeroPed) {
        this.numeroPed = numeroPed;
    }

    public NotaPedido(String numeroPed, String fecha, BigDecimal importe) {
        this.numeroPed = numeroPed;
        this.fecha = fecha;
        this.importe = importe;
    }

    public String getNumeroPed() {
        return numeroPed;
    }

    public void setNumeroPed(String numeroPed) {
        this.numeroPed = numeroPed;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    @XmlTransient
    public List<DetallePedido> getDetallePedidoList() {
        return detallePedidoList;
    }

    public void setDetallePedidoList(List<DetallePedido> detallePedidoList) {
        this.detallePedidoList = detallePedidoList;
    }

    @XmlTransient
    public List<ComprobantePago> getComprobantePagoList() {
        return comprobantePagoList;
    }

    public void setComprobantePagoList(List<ComprobantePago> comprobantePagoList) {
        this.comprobantePagoList = comprobantePagoList;
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
        hash += (numeroPed != null ? numeroPed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaPedido)) {
            return false;
        }
        NotaPedido other = (NotaPedido) object;
        if ((this.numeroPed == null && other.numeroPed != null) || (this.numeroPed != null && !this.numeroPed.equals(other.numeroPed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.NotaPedido[ numeroPed=" + numeroPed + " ]";
    }
    
}
