package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.WaysOfUse;
import ru.nsu.prokofiev.pharmacy.repository.WaysOfUseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WaysOfUseService {

    private final WaysOfUseRepository repository;

    public WaysOfUseService(WaysOfUseRepository repository) {
        this.repository = repository;
    }

    public List<WaysOfUse> findAll() {
        return repository.findAll();
    }

    public Optional<WaysOfUse> findById(Long id) {
        return repository.findById(id);
    }

    public WaysOfUse save(WaysOfUse item) {
        return repository.save(item);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
