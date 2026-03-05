package edu.comillas.icai.gitt.pat.spring.Practica_5.controlador;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaCarrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import edu.comillas.icai.gitt.pat.spring.Practica_5.servicio.ServicioCarrito;


@RestController
public class ControladorCarrito {
    @Autowired
    private ServicioCarrito servicioCarrito;

    //Crear Carrito
    @PostMapping("/api/carritos")
    @ResponseStatus(HttpStatus.CREATED)
    public Carrito crea(@RequestBody Carrito carritoNuevo) {
        return servicioCarrito.crea(carritoNuevo);
    }
    //Leer carrito por idCarrito
    @GetMapping("/api/carritos/{idCarrito}")
    public Carrito lee(@PathVariable String idCarrito) {
        return servicioCarrito.lee(idCarrito);
    }
    //Añadir linea al carrito
    public static class LineaRequest {
        public String idArticulo;
        public double precioUnitario;
        public int numeroUnidades;
    }

    @PostMapping("/api/carritos/{idCarrito}/lineas")
    @ResponseStatus(HttpStatus.CREATED)
    public LineaCarrito anadirLinea(@PathVariable String idCarrito, @RequestBody LineaRequest req) {
    return servicioCarrito.anadirLinea(idCarrito, req.idArticulo, req.precioUnitario, req.numeroUnidades);

    }
    //Borrar carrito
    @DeleteMapping("/api/carritos/{idCarrito}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borra(@PathVariable String idCarrito) {
        servicioCarrito.borra(idCarrito);
    }
}
