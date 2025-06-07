package ru.nsu.prokofiev.pharmacy.repository;

import ru.nsu.prokofiev.pharmacy.model.PrescriptionsContent;
import ru.nsu.prokofiev.pharmacy.model.PrescriptionsContentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionsContentRepository extends JpaRepository<PrescriptionsContent, PrescriptionsContentId> {
}
