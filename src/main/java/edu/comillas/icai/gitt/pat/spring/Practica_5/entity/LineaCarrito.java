package edu.comillas.icai.gitt.pat.spring.Practica_5.entity;

import jakarta.persistence.*;

@Entity
public class LineaCarrito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false)
    @JoinColumn(name="carrito_id", nullable=false)
    private Carrito carrito;

    private String idArticulo;
    private double precioUnitario;
    private int numeroUnidades;
    private double costeLinea;

    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
    public Carrito getCarrito() { return carrito; }

    public String getIdArticulo() { return idArticulo; }
    public void setIdArticulo(String idArticulo) { this.idArticulo = idArticulo; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getNumeroUnidades() { return numeroUnidades; }
    public void setNumeroUnidades(int numeroUnidades) { this.numeroUnidades = numeroUnidades; }

    public double getCosteLinea() { return costeLinea; }
    public void setCosteLinea(double costeLinea) { this.costeLinea = costeLinea; }

}
