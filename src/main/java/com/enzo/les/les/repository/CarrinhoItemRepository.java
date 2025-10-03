package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.CarrinhoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Long> {

}
