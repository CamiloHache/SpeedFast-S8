package modelo;

public class Pedido {
    private int id;
    private String direccionEntrega;
    private TipoPedido tipo;
    private EstadoPedido estado;

    public Pedido(String direccionEntrega, TipoPedido tipo, EstadoPedido estado) {
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Pedido(int id, String direccionEntrega, TipoPedido tipo, EstadoPedido estado) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public void setTipo(TipoPedido tipo) {
        this.tipo = tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return id + " - " + direccionEntrega;
    }
}
