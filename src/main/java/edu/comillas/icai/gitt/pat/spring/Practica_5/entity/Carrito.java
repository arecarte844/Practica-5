package edu.comillas.icai.gitt.pat.spring.Practica_5.entity;

import jakarta.persistence.*;

@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String idCarrito;

    private String idUsuario;
    private String correoUsuario;

    private double totalPrecio;
    public Long getId() { return id; }

    public String getIdCarrito() { return idCarrito; }
    public void setIdCarrito(String idCarrito) { this.idCarrito = idCarrito; }

    public double getTotalPrecio() { return totalPrecio; }
    public void setTotalPrecio(double totalPrecio) { this.totalPrecio = totalPrecio; }

}
