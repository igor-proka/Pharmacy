package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Medicaments;
import ru.nsu.prokofiev.pharmacy.repository.MedicamentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentsService {

    private final MedicamentsRepository repository;

    public MedicamentsService(MedicamentsRepository repository) {
        this.repository = repository;
    }

    public List<Medicaments> findAll() {
        return repository.findAll();
    }

    public Optional<Medicaments> findById(Long id) {
        return repository.findById(id);
    }

    public Medicaments save(Medicaments medicament) {
        return repository.save(medicament);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
