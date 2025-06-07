package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.MedicamentUse;
import ru.nsu.prokofiev.pharmacy.repository.MedicamentUseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentUseService {

    private final MedicamentUseRepository repository;

    public MedicamentUseService(MedicamentUseRepository repository) {
        this.repository = repository;
    }

    public List<MedicamentUse> findAll() {
        return repository.findAll();
    }

    public MedicamentUse save(MedicamentUse medicamentUse) {
        if (medicamentUse.getTypeId() == null || medicamentUse.getUseId() == null) {
            throw new IllegalArgumentException("Type and Use cannot be null.");
        }

        MedicamentUse.MedicamentUseId id = new MedicamentUse.MedicamentUseId(
                medicamentUse.getTypeId(),
                medicamentUse.getUseId()
        );

        if (repository.existsById(id)) {
            throw new IllegalStateException("Such a combination already exists.");
        }

        return repository.save(medicamentUse);
    }

    public void delete(Long typeId, Long useId) {
        repository.deleteById(new MedicamentUse.MedicamentUseId(typeId, useId));
    }
}
