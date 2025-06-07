package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientsRepository extends JpaRepository<Patients, Long> {
}
