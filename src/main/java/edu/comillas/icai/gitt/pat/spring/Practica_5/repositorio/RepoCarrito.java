package edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepoCarrito extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByIdCarrito(String idCarrito);
    // This is my "findBy non-primary key" requirement

    boolean existsByIdCarrito(String idCarrito);
}