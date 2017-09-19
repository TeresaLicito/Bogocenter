
package servicio;

import negocio.*;


public interface servicio {
//nota pedido
public UsuarioObj acceso(String nick,String password);
public NotaPedidoObj nuevo_pedido();
public String grabarNotaPedido(String numero,String fecha,double importe, String usuario);
public String grabarDetalle_Pedido(String numero,String producto,int cantidad, double sub_total);
public String agregar(String producto);

//mantenimiento producto
public ProductoObj nuevoProducto();
public String grabarProducto(String codigo,String nombre,String fecha_ven,int stock, double precio,String descripcion,String tipo);
public ProductoObj buscarProducto(String codigo);
public String actualizarproducto(String codigo,String nombre,String fecha_ven,int stock, double precio,String descripcion,String tipo);

//comprobante de pago
public ComprobantePagoObj nuevoComprobantePago();
public String grabarComprobantePago(String numero,String fecha,double total,String pedido,String usuario);
public NotaPedidoObj buscarNotaPedido(String numero_ped);

//mantenimiento ususario
public UsuarioObj nuevousuario();
public String grabarUsuario(String codigo,String nombre,String apellido, String direccion,String telefono,String nick,String password, char sexo, String fecha_nac,String tipo);
public UsuarioObj buscarUsuario(String codigo);
public String actualizarUsuario(String codigo,String nombre,String apellido, String direccion,String telefono,String nick,String password, char sexo, String fecha_nac,String tipo);

}
