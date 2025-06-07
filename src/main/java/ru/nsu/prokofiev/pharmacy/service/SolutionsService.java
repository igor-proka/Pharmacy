package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Solutions;
import ru.nsu.prokofiev.pharmacy.repository.SolutionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionsService {

    private final SolutionsRepository repository;

    public SolutionsService(SolutionsRepository repository) {
        this.repository = repository;
    }

    public List<Solutions> findAll() {
        return repository.findAll();
    }

    public Optional<Solutions> findById(Long id) {
        return repository.findById(id);
    }

    public Solutions save(Solutions entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
