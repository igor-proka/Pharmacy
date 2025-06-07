package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.ReservedMedicaments;
import ru.nsu.prokofiev.pharmacy.model.ReservedMedicamentsId;

public interface ReservedMedicamentsRepository extends JpaRepository<ReservedMedicaments, ReservedMedicamentsId> {
}
