
package negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NotaPedidoObj {
   private DetallePedidoObj detaPed;
   private double total;
   private int cantidad;
   private UsuarioObj Usuario;
   private String NumPedido, fecha, hora, cancelarPedido;
   private List cesta=new ArrayList();
   
    public DetallePedidoObj getDetaPed() {
        return detaPed;
    }

    public void setDetaPed(DetallePedidoObj detaPed) {
        this.detaPed = detaPed;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public UsuarioObj getUsuario() {
        return Usuario;
    }

    public void setUsuario(UsuarioObj Usuario) {
        this.Usuario = Usuario;
    }

    public String getNumPedido() {
        return NumPedido;
    }

    public void setNumPedido(String NumPedido) {
        this.NumPedido = NumPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCancelarPedido() {
        return cancelarPedido;
    }

    public void setCancelarPedido(String cancelarPedido) {
        this.cancelarPedido = cancelarPedido;
    }
   
}
