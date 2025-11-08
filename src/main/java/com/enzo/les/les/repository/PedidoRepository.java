package com.enzo.les.les.repository;

import com.enzo.les.les.dtos.SalesAnalyticsDTO;
import com.enzo.les.les.model.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
        SELECT COALESCE(SUM(pi.subtotal), 0.0)
        FROM Pedido p
        JOIN p.itens pi
        WHERE p.dataPedido BETWEEN :inicio AND :fim
    """)
    Double getVolumeVendasPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    // ✅ Query corrigida para PRODUTO - iniciando de PedidoItem
    @Query("""
        SELECT new com.enzo.les.les.dtos.SalesAnalyticsDTO(
            CAST(p.dataPedido AS LocalDate),
            CAST(SUM(pi.quantidade) AS Long),
            SUM(pi.subtotal)
        )
        FROM PedidoItem pi
        JOIN pi.pedido p
        WHERE pi.livro.id = :produtoId
        AND CAST(p.dataPedido AS LocalDate) BETWEEN :dataInicio AND :dataFim
        GROUP BY CAST(p.dataPedido AS LocalDate)
        ORDER BY CAST(p.dataPedido AS LocalDate)
    """)
    List<SalesAnalyticsDTO> getVendasPorProduto(
            @Param("produtoId") Long produtoId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    // ✅ Query corrigida para CATEGORIA - usando o relacionamento ManyToMany correto
    @Query("""
        SELECT new com.enzo.les.les.dtos.SalesAnalyticsDTO(
            CAST(p.dataPedido AS LocalDate),
            CAST(SUM(pi.quantidade) AS Long),
            SUM(pi.subtotal)
        )
        FROM PedidoItem pi
        JOIN pi.pedido p
        JOIN pi.livro l
        JOIN l.categorias c
        WHERE c.id = :categoriaId
        AND CAST(p.dataPedido AS LocalDate) BETWEEN :dataInicio AND :dataFim
        GROUP BY CAST(p.dataPedido AS LocalDate)
        ORDER BY CAST(p.dataPedido AS LocalDate)
    """)
    List<SalesAnalyticsDTO> getVendasPorCategoria(
            @Param("categoriaId") Long categoriaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}