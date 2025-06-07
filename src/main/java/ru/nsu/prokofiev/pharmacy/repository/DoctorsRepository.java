package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsRepository extends JpaRepository<Doctors, Long> {
}
