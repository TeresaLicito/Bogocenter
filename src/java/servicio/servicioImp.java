
package servicio;

import negocio.*;
import persistencia.*;

public class servicioImp implements servicio{
private dato dat;

    public void setDat(dato dat) {
        this.dat = dat;
    }
    @Override
    public UsuarioObj acceso(String nick, String password) {
     UsuarioObj usu= new UsuarioObj();
     Usuario us=dat.acceso(nick, password);
     usu.setApeUsuario(us.getApellido());
     usu.setCodUsuario(us.getIdCodigo());
     usu.setDirUsuario(us.getDireccion());
     usu.setFechaNac(us.getFechaNac());
     usu.setNickUsuario(us.getNick());
     usu.setNomUsuario(us.getNombre());
     usu.setPasUsuario(us.getPassword());
     
     return usu;
    }

    @Override
    public NotaPedidoObj nuevo_pedido() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarNotaPedido(String numero, String fecha, double importe, String usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarDetalle_Pedido(String numero, String producto, int cantidad, double sub_total) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String agregar(String producto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductoObj nuevoProducto() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarProducto(String codigo, String nombre, String fecha_ven, int stock, double precio, String descripcion, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductoObj buscarProducto(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarproducto(String codigo, String nombre, String fecha_ven, int stock, double precio, String descripcion, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComprobantePagoObj nuevoComprobantePago() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarComprobantePago(String numero, String fecha, double total, String pedido, String usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NotaPedidoObj buscarNotaPedido(String numero_ped) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UsuarioObj nuevousuario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarUsuario(String codigo, String nombre, String apellido, String direccion, String telefono, String nick, String password, char sexo, String fecha_nac, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UsuarioObj buscarUsuario(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarUsuario(String codigo, String nombre, String apellido, String direccion, String telefono, String nick, String password, char sexo, String fecha_nac, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
