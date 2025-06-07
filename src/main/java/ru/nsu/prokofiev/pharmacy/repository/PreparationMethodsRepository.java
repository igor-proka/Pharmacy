package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.PreparationMethods;

import java.util.Optional;

public interface PreparationMethodsRepository extends JpaRepository<PreparationMethods, Long> {
    Optional<PreparationMethods> findByName(String name);
}
