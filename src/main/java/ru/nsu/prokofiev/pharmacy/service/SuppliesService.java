package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Supplies;
import ru.nsu.prokofiev.pharmacy.repository.SuppliesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuppliesService {

    private final SuppliesRepository suppliesRepository;

    public SuppliesService(SuppliesRepository suppliesRepository) {
        this.suppliesRepository = suppliesRepository;
    }

    public List<Supplies> findAll() {
        return suppliesRepository.findAll();
    }

    public Optional<Supplies> findById(Long id) {
        return suppliesRepository.findById(id);
    }

    public Supplies save(Supplies supply) {
        return suppliesRepository.save(supply);
    }

    public void deleteById(Long id) {
        suppliesRepository.deleteById(id);
    }
}
