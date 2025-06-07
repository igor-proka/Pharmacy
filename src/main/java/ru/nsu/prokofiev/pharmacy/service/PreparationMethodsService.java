package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.PreparationMethods;
import ru.nsu.prokofiev.pharmacy.repository.PreparationMethodsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PreparationMethodsService {

    private final PreparationMethodsRepository repository;

    public PreparationMethodsService(PreparationMethodsRepository repository) {
        this.repository = repository;
    }

    public List<PreparationMethods> findAll() {
        return repository.findAll();
    }

    public Optional<PreparationMethods> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<PreparationMethods> findByName(String name) {
        return repository.findByName(name);
    }

    public PreparationMethods save(PreparationMethods method) {
        return repository.save(method);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
