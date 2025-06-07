package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.Production;

public interface ProductionRepository extends JpaRepository<Production, Long> {
}
