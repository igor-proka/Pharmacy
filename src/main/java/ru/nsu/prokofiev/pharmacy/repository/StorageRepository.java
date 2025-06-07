package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.prokofiev.pharmacy.model.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
}
