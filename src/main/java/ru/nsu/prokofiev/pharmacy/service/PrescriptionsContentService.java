package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.PrescriptionsContent;
import ru.nsu.prokofiev.pharmacy.model.PrescriptionsContentId;
import ru.nsu.prokofiev.pharmacy.repository.PrescriptionsContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionsContentService {

    private final PrescriptionsContentRepository repository;

    public PrescriptionsContentService(PrescriptionsContentRepository repository) {
        this.repository = repository;
    }

    public List<PrescriptionsContent> findAll() {
        return repository.findAll();
    }

    public Optional<PrescriptionsContent> findById(PrescriptionsContentId id) {
        return repository.findById(id);
    }

    public PrescriptionsContent save(PrescriptionsContent entity) {
        return repository.save(entity);
    }

    public void delete(PrescriptionsContentId id) {
        repository.deleteById(id);
    }
}
