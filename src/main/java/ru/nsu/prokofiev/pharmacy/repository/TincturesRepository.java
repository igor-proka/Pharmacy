package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Tinctures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TincturesRepository extends JpaRepository<Tinctures, Long> {
}
