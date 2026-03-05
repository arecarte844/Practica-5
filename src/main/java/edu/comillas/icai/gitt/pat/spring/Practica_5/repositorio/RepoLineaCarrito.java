package edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoLineaCarrito extends JpaRepository<LineaCarrito, Long> {

    List<LineaCarrito> findByCarrito_Id(Long carritoId);

    Optional<LineaCarrito> findByCarrito_IdAndIdArticulo(Long carritoId, String idArticulo);
}