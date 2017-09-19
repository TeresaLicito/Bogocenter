/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.util.List;

public class datoImp implements dato{
private Usuario us;
private UsuarioJpaController usCon;
    @Override
    public String grabarNotaPedido(String numero, String fecha, double importe, String usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabaDetallePedido(String numero, String producto, int cantidad, double subtotal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NotaPedido buscarNotaPedido(String numero) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarProducto(String codigo, String nombre, String fecha_ven, int stock, double precio, String descripion, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Producto buscarProducto(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarProducto(String codigo, String nombre, String fecha_ven, int stock, double precio, String descripion, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarComprobante(String numero, String fecha, double total, String pedido, String usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String grabarUsuario(String codigo, String nombre, String apellido, String direccion, String telefono, String nick, String password, char sexo, String fecha_nac, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario buscarUsuario(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String actualizarUsuario(String codigo, String nombre, String apellido, String direccion, String telefono, String nick, String password, char sexo, String fecha_nac, String tipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario acceso(String nick, String password) {
        List lista=usCon.findUsuarioEntities();
        for(int i=0;i<lista.size();i++){
            us=(Usuario) lista.get(i);
            if (us.getNick().equalsIgnoreCase(nick) && us.getPassword().equalsIgnoreCase(password)) {
                return us;
            }
        }
        return null;
    }
  
}
