package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Patients;
import ru.nsu.prokofiev.pharmacy.repository.PatientsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientsService {

    private final PatientsRepository repository;

    public PatientsService(PatientsRepository repository) {
        this.repository = repository;
    }

    public List<Patients> findAll() {
        return repository.findAll();
    }

    public Optional<Patients> findById(Long id) {
        return repository.findById(id);
    }

    public Patients save(Patients patient) {
        return repository.save(patient);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
