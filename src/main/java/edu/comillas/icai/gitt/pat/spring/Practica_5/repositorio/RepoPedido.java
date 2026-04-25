package edu.comillas.icai.gitt.pat.spring.Practica_5.repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica_5.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoPedido extends JpaRepository<Pedido, Long> {
}
