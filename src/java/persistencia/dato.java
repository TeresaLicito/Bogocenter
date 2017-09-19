/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

/**
 *
 * @author Usuario
 */
public interface dato {
//nota pedido
public String grabarNotaPedido(String numero,String fecha,double importe,String usuario);
public String grabaDetallePedido(String numero,String producto, int cantidad, double subtotal);
public NotaPedido buscarNotaPedido(String numero);

//mantenimiento producto
public String grabarProducto(String codigo,String nombre,String fecha_ven,int stock,double precio,String descripion,String tipo);
public Producto buscarProducto(String codigo);
public String actualizarProducto(String codigo,String nombre,String fecha_ven,int stock,double precio,String descripion,String tipo);

//comprobante pago
public String grabarComprobante(String numero,String fecha,double total,String pedido,String usuario);

//mantenimiento usuario
public String grabarUsuario(String codigo,String nombre,String apellido, String direccion,String telefono,String nick,String password,char sexo,String fecha_nac,String tipo);
public Usuario buscarUsuario(String codigo);
public String actualizarUsuario(String codigo,String nombre,String apellido, String direccion,String telefono,String nick,String password,char sexo,String fecha_nac,String tipo);
public Usuario acceso(String nick,String password);

}
