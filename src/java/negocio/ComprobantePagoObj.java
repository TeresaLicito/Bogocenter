
package negocio;

public class ComprobantePagoObj {
    private NotaPedidoObj notpedi;
    private double total;
    private String NumComprobante, fecha;
    private UsuarioObj Usuario;

    public NotaPedidoObj getNotpedi() {
        return notpedi;
    }

    public void setNotpedi(NotaPedidoObj notpedi) {
        this.notpedi = notpedi;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNumComprobante() {
        return NumComprobante;
    }

    public void setNumComprobante(String NumComprobante) {
        this.NumComprobante = NumComprobante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public UsuarioObj getUsuario() {
        return Usuario;
    }

    public void setUsuario(UsuarioObj Usuario) {
        this.Usuario = Usuario;
    }
    
}
