package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.repository.MedicamentPackagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentPackagesService {

    private final MedicamentPackagesRepository repository;

    public MedicamentPackagesService(MedicamentPackagesRepository repository) {
        this.repository = repository;
    }

    public List<MedicamentPackages> findAll() {
        return repository.findAll();
    }

    public Optional<MedicamentPackages> findById(Long id) {
        return repository.findById(id);
    }

    public MedicamentPackages save(MedicamentPackages entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
