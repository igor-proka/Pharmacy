package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.Technologies;
import ru.nsu.prokofiev.pharmacy.repository.TechnologiesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologiesService {

    private final TechnologiesRepository repository;

    public TechnologiesService(TechnologiesRepository repository) {
        this.repository = repository;
    }

    public List<Technologies> findAll() {
        return repository.findAll();
    }

    public Optional<Technologies> findById(Long id) {
        return repository.findById(id);
    }

    public Technologies save(Technologies technology) {
        return repository.save(technology);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
