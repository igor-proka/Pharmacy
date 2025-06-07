package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.UnitsOfMeasurement;
import ru.nsu.prokofiev.pharmacy.repository.UnitsOfMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitsOfMeasurementService {

    private final UnitsOfMeasurementRepository repository;

    public UnitsOfMeasurementService(UnitsOfMeasurementRepository repository) {
        this.repository = repository;
    }

    public List<UnitsOfMeasurement> findAll() {
        return repository.findAll();
    }

    public Optional<UnitsOfMeasurement> findById(Long id) {
        return repository.findById(id);
    }

    public UnitsOfMeasurement save(UnitsOfMeasurement unit) {
        return repository.save(unit);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
