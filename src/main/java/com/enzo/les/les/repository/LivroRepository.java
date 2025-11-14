package com.enzo.les.les.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enzo.les.les.model.entities.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    

    // MÉTODO QUE ESTAVA FALTANDO/DANDO ERRO
    /**
     * Busca todos os livros que estão na lista de IDs fornecida
     * E que possuem estoque maior que zero.
     */
    @Query("SELECT l FROM Livro l WHERE l.id IN :ids AND l.saldoEstoque.quantidade > 0")
List<Livro> findAllByIdInAndEstoqueGreaterThanZero(@Param("ids") Collection<Long> ids);
    
    /**
     * Retorna os N livros mais vendidos para serem usados como catálogo base pela IA.
     * Esta é uma SIMULAÇÃO que você deve substituir por uma QUERY REAL de vendas.
     * @return Lista dos 20 Livros mais vendidos (simulado).
     */
    @Query(value = "SELECT * FROM livros ORDER BY RANDOM() LIMIT 20", nativeQuery = true)
    List<Livro> findTop20MaisVendidos();

    /**
     * Busca todos os livros cujos IDs estão contidos no Set fornecido.
     * Método necessário para buscar os LivroDTOs após a IA retornar as recomendações.
     * @param ids Set de IDs de livros recomendados pela IA.
     * @return Lista de Livros encontrados.
     */
    List<Livro> findByIdIn(Set<Long> ids);
}