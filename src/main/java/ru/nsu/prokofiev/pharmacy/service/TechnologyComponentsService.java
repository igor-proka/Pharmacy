package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.TechnologyComponents;
import ru.nsu.prokofiev.pharmacy.model.TechnologyComponentsId;
import ru.nsu.prokofiev.pharmacy.repository.TechnologyComponentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologyComponentsService {

    private final TechnologyComponentsRepository repository;

    public TechnologyComponentsService(TechnologyComponentsRepository repository) {
        this.repository = repository;
    }

    public List<TechnologyComponents> findAll() {
        return repository.findAll();
    }

    public Optional<TechnologyComponents> findById(TechnologyComponentsId id) {
        return repository.findById(id);
    }

    public TechnologyComponents save(TechnologyComponents component) {
        return repository.save(component);
    }

    public void deleteById(TechnologyComponentsId id) {
        repository.deleteById(id);
    }
}
