/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
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
@Table(name = "USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByIdCodigo", query = "SELECT u FROM Usuario u WHERE u.idCodigo = :idCodigo")
    , @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Usuario.findByApellido", query = "SELECT u FROM Usuario u WHERE u.apellido = :apellido")
    , @NamedQuery(name = "Usuario.findByDireccion", query = "SELECT u FROM Usuario u WHERE u.direccion = :direccion")
    , @NamedQuery(name = "Usuario.findByTelefono", query = "SELECT u FROM Usuario u WHERE u.telefono = :telefono")
    , @NamedQuery(name = "Usuario.findByNick", query = "SELECT u FROM Usuario u WHERE u.nick = :nick")
    , @NamedQuery(name = "Usuario.findByPassword", query = "SELECT u FROM Usuario u WHERE u.password = :password")
    , @NamedQuery(name = "Usuario.findBySexo", query = "SELECT u FROM Usuario u WHERE u.sexo = :sexo")
    , @NamedQuery(name = "Usuario.findByFechaNac", query = "SELECT u FROM Usuario u WHERE u.fechaNac = :fechaNac")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "ID_CODIGO")
    private String idCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "APELLIDO")
    private String apellido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "TELEFONO")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NICK")
    private String nick;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEXO")
    private Character sexo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FECHA_NAC")
    private String fechaNac;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCodigo")
    private List<ComprobantePago> comprobantePagoList;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private TipoUsuario idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCodigo")
    private List<NotaPedido> notaPedidoList;

    public Usuario() {
    }

    public Usuario(String idCodigo) {
        this.idCodigo = idCodigo;
    }

    public Usuario(String idCodigo, String nombre, String apellido, String direccion, String telefono, String nick, String password, Character sexo, String fechaNac) {
        this.idCodigo = idCodigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
        this.fechaNac = fechaNac;
    }

    public String getIdCodigo() {
        return idCodigo;
    }

    public void setIdCodigo(String idCodigo) {
        this.idCodigo = idCodigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    @XmlTransient
    public List<ComprobantePago> getComprobantePagoList() {
        return comprobantePagoList;
    }

    public void setComprobantePagoList(List<ComprobantePago> comprobantePagoList) {
        this.comprobantePagoList = comprobantePagoList;
    }

    public TipoUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(TipoUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<NotaPedido> getNotaPedidoList() {
        return notaPedidoList;
    }

    public void setNotaPedidoList(List<NotaPedido> notaPedidoList) {
        this.notaPedidoList = notaPedidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCodigo != null ? idCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idCodigo == null && other.idCodigo != null) || (this.idCodigo != null && !this.idCodigo.equals(other.idCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistencia.Usuario[ idCodigo=" + idCodigo + " ]";
    }
    
}
