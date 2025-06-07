package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.Technologies;

public interface TechnologiesRepository extends JpaRepository<Technologies, Long> {
}
