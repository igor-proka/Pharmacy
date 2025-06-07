package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.MedicamentTypes;

public interface MedicamentTypesRepository extends JpaRepository<MedicamentTypes, Long> {
    boolean existsByName(String name);
}
