package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Ointments;
import ru.nsu.prokofiev.pharmacy.repository.OintmentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OintmentsService {

    private final OintmentsRepository repository;

    public OintmentsService(OintmentsRepository repository) {
        this.repository = repository;
    }

    public List<Ointments> findAll() {
        return repository.findAll();
    }

    public Optional<Ointments> findById(Long id) {
        return repository.findById(id);
    }

    public Ointments save(Ointments entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
