package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.MedicamentUse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentUseRepository extends JpaRepository<MedicamentUse, MedicamentUse.MedicamentUseId> {
}
