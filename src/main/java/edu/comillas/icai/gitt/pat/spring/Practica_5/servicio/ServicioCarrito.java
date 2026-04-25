package edu.comillas.icai.gitt.pat.spring.Practica_5.servicio;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio.RepoLineaCarrito;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicioCarrito {
    @Autowired
    RepoCarrito repoCarrito;
    @Autowired
    RepoLineaCarrito repoLineaCarrito;

    @Autowired
    private ServicioPedido servicioPedido;

    //CREAR CARRITO
    public Carrito crea(Carrito carritoNuevo){
        if (repoCarrito.existsByIdCarrito(carritoNuevo.getIdCarrito())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Carrito ya existe");
        }
        carritoNuevo.setTotalPrecio(0.0);
        return repoCarrito.save(carritoNuevo);
    }
    //LEER CARRITO
    public Carrito lee(String idCarrito){
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
        if (carrito == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        return carrito;
    }
    //Añadir linea al carrito
    @Transactional
    public LineaCarrito anadirLinea(String idCarrito, String idArticulo, double precioUnitario, int unidades){
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
        if (carrito == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        if (unidades <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La unidades debe ser mayor a 0");
        }
        LineaCarrito lineaCarritoExistente = repoLineaCarrito.findByCarrito_IdAndIdArticulo(carrito.getId(), idArticulo)
                .orElse(null);

        LineaCarrito lineaCarrito;
        if (lineaCarritoExistente != null){
            //si ya existe, actualizamos unidades
            lineaCarrito = lineaCarritoExistente;
            lineaCarrito.setNumeroUnidades(
                    lineaCarrito.getNumeroUnidades() + unidades);

        }
        else{
            //crea nueva linea
            lineaCarrito = new LineaCarrito();
            lineaCarrito.setCarrito(carrito);
            lineaCarrito.setIdArticulo(idArticulo);
            lineaCarrito.setNumeroUnidades(unidades);
            lineaCarrito.setPrecioUnitario(precioUnitario);
        }
        //calcular coste linea
        lineaCarrito.setCosteLinea(
                lineaCarrito.getPrecioUnitario() * lineaCarrito.getNumeroUnidades()
        );
        repoLineaCarrito.save(lineaCarrito);

        //Recalcular total carrito
        List<LineaCarrito>  lineaCarritos = repoLineaCarrito.findByCarrito_Id(carrito.getId());

        double total = 0.0;
        for (LineaCarrito l : lineaCarritos){
            total += l.getCosteLinea();
        }
        carrito.setTotalPrecio(total);
        repoCarrito.save(carrito);
        return lineaCarrito;
    }
    //Borra Carrito
    @Transactional
    public void borra(String idCarrito){
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        List<LineaCarrito> lineas = repoLineaCarrito.findByCarrito_Id(carrito.getId());
        repoLineaCarrito.deleteAll(lineas);

        repoCarrito.delete(carrito);
    }
    public List<LineaCarrito> listarLineas(String idCarrito) {
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        return repoLineaCarrito.findByCarrito_Id(carrito.getId());
    }
    @Transactional
    public void borrarLinea(String idCarrito, String idArticulo) {
        Carrito carrito = repoCarrito.findByIdCarrito(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        LineaCarrito linea = repoLineaCarrito.findByCarrito_IdAndIdArticulo(carrito.getId(), idArticulo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea no encontrada"));

        repoLineaCarrito.delete(linea);

        List<LineaCarrito> lineasRestantes = repoLineaCarrito.findByCarrito_Id(carrito.getId());

        double total = 0.0;
        for (LineaCarrito l : lineasRestantes) {
            total += l.getCosteLinea();
        }

        carrito.setTotalPrecio(total);
        repoCarrito.save(carrito);
    }

}