package edu.comillas.icai.gitt.pat.spring.Practica_5.controlador;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Pedido;
import edu.comillas.icai.gitt.pat.spring.Practica_5.servicio.ServicioPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import edu.comillas.icai.gitt.pat.spring.Practica_5.servicio.ServicioCarrito;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ControladorCarrito {
    @Autowired
    private ServicioCarrito servicioCarrito;
    @Autowired
    private ServicioPedido servicioPedido;

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

    @GetMapping("/api/carritos/{idCarrito}/lineas")
    public java.util.List<LineaCarrito> listarLineas(@PathVariable String idCarrito) {
        return servicioCarrito.listarLineas(idCarrito);
    }

    @DeleteMapping("/api/carritos/{idCarrito}/lineas/{idArticulo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrarLinea(@PathVariable String idCarrito, @PathVariable String idArticulo) {
        servicioCarrito.borrarLinea(idCarrito, idArticulo);
    }

    public static class PedidoRequest {
        public String nombreCliente;
        public String correoCliente;
    }
    @GetMapping("/api/admin/pedidos")
    public List<Pedido> listarPedidos(@RequestHeader("X-Admin-Key") String adminKey) {
        if (!"clave-admin-2027".equals(adminKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }

        return servicioPedido.listarPedidos();
    }

    @PostMapping("/api/pedidos/{idCarrito}")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crearPedido(@PathVariable String idCarrito, @RequestBody PedidoRequest req) {
        return servicioPedido.crearPedidoDesdeCarrito(idCarrito, req.nombreCliente, req.correoCliente);
    }

}
