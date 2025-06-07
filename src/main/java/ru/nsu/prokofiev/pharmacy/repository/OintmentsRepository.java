package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Ointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OintmentsRepository extends JpaRepository<Ointments, Long> {
}
