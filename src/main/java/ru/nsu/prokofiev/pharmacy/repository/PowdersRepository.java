package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Powders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowdersRepository extends JpaRepository<Powders, Long> {
}
