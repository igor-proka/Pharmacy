package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.MedicamentTypes;
import ru.nsu.prokofiev.pharmacy.repository.MedicamentTypesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentTypesService {

    private final MedicamentTypesRepository repository;

    public MedicamentTypesService(MedicamentTypesRepository repository) {
        this.repository = repository;
    }

    public List<MedicamentTypes> findAll() {
        return repository.findAll();
    }

    public Optional<MedicamentTypes> findById(Long id) {
        return repository.findById(id);
    }

    public MedicamentTypes save(MedicamentTypes medicamentType) {
        return repository.save(medicamentType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

