package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Pills;
import ru.nsu.prokofiev.pharmacy.repository.PillsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PillsService {

    private final PillsRepository repository;

    public PillsService(PillsRepository repository) {
        this.repository = repository;
    }

    public List<Pills> findAll() {
        return repository.findAll();
    }

    public Optional<Pills> findById(Long id) {
        return repository.findById(id);
    }

    public Pills save(Pills entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
