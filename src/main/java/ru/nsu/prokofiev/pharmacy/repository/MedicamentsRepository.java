package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Medicaments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentsRepository extends JpaRepository<Medicaments, Long> {
}
