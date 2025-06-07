package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.Supplies;

public interface SuppliesRepository extends JpaRepository<Supplies, Long> {
}
