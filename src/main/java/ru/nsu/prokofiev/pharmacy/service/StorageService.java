package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.Storage;
import ru.nsu.prokofiev.pharmacy.repository.StorageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    private final StorageRepository repository;

    public StorageService(StorageRepository repository) {
        this.repository = repository;
    }

    public List<Storage> findAll() {
        return repository.findAll();
    }

    public Optional<Storage> findById(Long id) {
        return repository.findById(id);
    }

    public Storage save(Storage storage) {
        return repository.save(storage);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
