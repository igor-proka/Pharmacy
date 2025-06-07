package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.ReservedMedicaments;
import ru.nsu.prokofiev.pharmacy.model.ReservedMedicamentsId;
import ru.nsu.prokofiev.pharmacy.repository.ReservedMedicamentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservedMedicamentsService {

    private final ReservedMedicamentsRepository repository;

    public ReservedMedicamentsService(ReservedMedicamentsRepository repository) {
        this.repository = repository;
    }

    public List<ReservedMedicaments> findAll() {
        return repository.findAll();
    }

    public Optional<ReservedMedicaments> findById(ReservedMedicamentsId id) {
        return repository.findById(id);
    }

    public ReservedMedicaments save(ReservedMedicaments rm) {
        return repository.save(rm);
    }

    public void deleteById(ReservedMedicamentsId id) {
        repository.deleteById(id);
    }
}
