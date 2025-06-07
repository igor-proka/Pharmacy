package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Prescriptions;
import ru.nsu.prokofiev.pharmacy.repository.PrescriptionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionsService {

    private final PrescriptionsRepository repository;

    public PrescriptionsService(PrescriptionsRepository repository) {
        this.repository = repository;
    }

    public List<Prescriptions> findAll() {
        return repository.findAll();
    }

    public Optional<Prescriptions> findById(Long id) {
        return repository.findById(id);
    }

    public Prescriptions save(Prescriptions entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
