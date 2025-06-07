package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Mixtures;
import ru.nsu.prokofiev.pharmacy.repository.MixturesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MixturesService {

    private final MixturesRepository repository;

    public MixturesService(MixturesRepository repository) {
        this.repository = repository;
    }

    public List<Mixtures> findAll() {
        return repository.findAll();
    }

    public Optional<Mixtures> findById(Long id) {
        return repository.findById(id);
    }

    public Mixtures save(Mixtures entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
