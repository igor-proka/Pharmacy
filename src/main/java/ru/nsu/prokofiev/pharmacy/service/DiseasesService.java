package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Diseases;
import ru.nsu.prokofiev.pharmacy.repository.DiseasesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiseasesService {

    private final DiseasesRepository repository;

    public DiseasesService(DiseasesRepository repository) {
        this.repository = repository;
    }

    public List<Diseases> findAll() {
        return repository.findAll();
    }

    public Optional<Diseases> findById(Long id) {
        return repository.findById(id);
    }

    public Diseases save(Diseases entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
