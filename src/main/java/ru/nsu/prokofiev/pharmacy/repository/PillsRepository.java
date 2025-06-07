package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Pills;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PillsRepository extends JpaRepository<Pills, Long> {
}
