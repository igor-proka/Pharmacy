package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
