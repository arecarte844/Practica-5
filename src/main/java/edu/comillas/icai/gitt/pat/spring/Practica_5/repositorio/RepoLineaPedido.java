package edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.LineaPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoLineaPedido extends JpaRepository<LineaPedido, Long> {
}
