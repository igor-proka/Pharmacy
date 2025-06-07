package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.WaitingMedicaments;
import ru.nsu.prokofiev.pharmacy.model.WaitingMedicamentsId;

public interface WaitingMedicamentsRepository extends JpaRepository<WaitingMedicaments, WaitingMedicamentsId> {
}
