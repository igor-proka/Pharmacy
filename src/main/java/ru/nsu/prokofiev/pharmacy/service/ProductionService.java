package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.Production;
import ru.nsu.prokofiev.pharmacy.repository.ProductionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionService {

    private final ProductionRepository repository;

    public ProductionService(ProductionRepository repository) {
        this.repository = repository;
    }

    public List<Production> findAll() {
        return repository.findAll();
    }

    public Optional<Production> findById(Long id) {
        return repository.findById(id);
    }

    public Production save(Production production) {
        return repository.save(production);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
