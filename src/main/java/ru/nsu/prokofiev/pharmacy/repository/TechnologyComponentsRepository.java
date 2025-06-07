package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.prokofiev.pharmacy.model.TechnologyComponents;
import ru.nsu.prokofiev.pharmacy.model.TechnologyComponentsId;

public interface TechnologyComponentsRepository extends JpaRepository<TechnologyComponents, TechnologyComponentsId> {
}
