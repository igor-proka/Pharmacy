package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Prescriptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionsRepository extends JpaRepository<Prescriptions, Long> {
}
