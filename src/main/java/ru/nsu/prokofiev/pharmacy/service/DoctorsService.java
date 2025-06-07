package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Doctors;
import ru.nsu.prokofiev.pharmacy.repository.DoctorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorsService {

    private final DoctorsRepository repository;

    public DoctorsService(DoctorsRepository repository) {
        this.repository = repository;
    }

    public List<Doctors> findAll() {
        return repository.findAll();
    }

    public Optional<Doctors> findById(Long id) {
        return repository.findById(id);
    }

    public Doctors save(Doctors doctor) {
        return repository.save(doctor);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
