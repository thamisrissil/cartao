package com.cartoes.api.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.cartoes.api.entities.Cartao;
public interface CartaoRepository extends JpaRepository<Cartao, Integer> {
    @Transactional(readOnly = true)
    @Query("SELECT ca FROM Cartao ca WHERE ca.cliente.id = :clienteId")
 List<Cartao> findByClienteId(@Param("clienteId") int clienteId);
    @Transactional(readOnly = true)
    Optional<Cartao> findByNumero(String numero);
}