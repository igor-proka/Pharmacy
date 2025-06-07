package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.WaitingMedicaments;
import ru.nsu.prokofiev.pharmacy.model.WaitingMedicamentsId;
import ru.nsu.prokofiev.pharmacy.repository.WaitingMedicamentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WaitingMedicamentsService {

    private final WaitingMedicamentsRepository repository;

    public WaitingMedicamentsService(WaitingMedicamentsRepository repository) {
        this.repository = repository;
    }

    public List<WaitingMedicaments> findAll() {
        return repository.findAll();
    }

    public Optional<WaitingMedicaments> findById(WaitingMedicamentsId id) {
        return repository.findById(id);
    }

    public WaitingMedicaments save(WaitingMedicaments wm) {
        return repository.save(wm);
    }

    public void deleteById(WaitingMedicamentsId id) {
        repository.deleteById(id);
    }
}
