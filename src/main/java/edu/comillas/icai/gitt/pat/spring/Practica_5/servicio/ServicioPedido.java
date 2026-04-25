package edu.comillas.icai.gitt.pat.spring.Practica_5.servicio;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Pedido;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaPedido;
import edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio.RepoLineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio.RepoPedido;
import edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio.RepoLineaPedido;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicioPedido {

    @Autowired
    RepoPedido repoPedido;

    @Autowired
    RepoLineaPedido repoLineaPedido;

    @Autowired
    RepoCarrito repoCarrito;

    @Autowired
    RepoLineaCarrito repoLineaCarrito;

    @Transactional
    public Pedido crearPedidoDesdeCarrito(String idCarrito, String nombreCliente, String correoCliente) {
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        List<LineaCarrito> lineasCarrito = repoLineaCarrito.findByCarrito_Id(carrito.getId());

        if (lineasCarrito.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede crear un pedido con carrito vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setIdCarrito(idCarrito);
        pedido.setNombreCliente(nombreCliente);
        pedido.setCorreoCliente(correoCliente);
        pedido.setTotalPedido(carrito.getTotalPrecio());
        pedido.setFechaPedido(LocalDateTime.now());

        Pedido pedidoGuardado = repoPedido.save(pedido);

        for (LineaCarrito lineaCarrito : lineasCarrito) {
            LineaPedido lineaPedido = new LineaPedido();
            lineaPedido.setPedido(pedidoGuardado);
            lineaPedido.setIdArticulo(lineaCarrito.getIdArticulo());
            lineaPedido.setPrecioUnitario(lineaCarrito.getPrecioUnitario());
            lineaPedido.setNumeroUnidades(lineaCarrito.getNumeroUnidades());
            lineaPedido.setCosteLinea(lineaCarrito.getCosteLinea());

            repoLineaPedido.save(lineaPedido);
        }

        repoLineaCarrito.deleteAll(lineasCarrito);
        carrito.setTotalPrecio(0.0);
        repoCarrito.save(carrito);

        return pedidoGuardado;
    }

    public List<Pedido> listarPedidosAdmin(String adminKey) {
        if (!"clave-admin-2027".equals(adminKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }

        return repoPedido.findAll();
    }

    public List<Pedido> listarPedidos() {
        return repoPedido.findAll();
    }
}
