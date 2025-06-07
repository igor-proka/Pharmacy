package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Tinctures;
import ru.nsu.prokofiev.pharmacy.repository.TincturesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TincturesService {

    private final TincturesRepository repository;

    public TincturesService(TincturesRepository repository) {
        this.repository = repository;
    }

    public List<Tinctures> findAll() {
        return repository.findAll();
    }

    public Optional<Tinctures> findById(Long id) {
        return repository.findById(id);
    }

    public Tinctures save(Tinctures entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
