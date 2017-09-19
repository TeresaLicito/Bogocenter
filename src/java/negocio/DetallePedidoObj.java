
package negocio;


public class DetallePedidoObj {
    private ProductoObj pro;
    private String cantidad;
    private double importe, subtotal;

    public ProductoObj getPro() {
        return pro;
    }

    public void setPro(ProductoObj pro) {
        this.pro = pro;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
}
