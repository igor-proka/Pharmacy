package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Diseases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseasesRepository extends JpaRepository<Diseases, Long> {
}
