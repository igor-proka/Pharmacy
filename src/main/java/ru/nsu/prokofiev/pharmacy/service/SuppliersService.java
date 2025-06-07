package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Suppliers;
import ru.nsu.prokofiev.pharmacy.repository.SuppliersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuppliersService {

    private final SuppliersRepository repository;

    public SuppliersService(SuppliersRepository repository) {
        this.repository = repository;
    }

    public List<Suppliers> findAll() {
        return repository.findAll();
    }

    public Optional<Suppliers> findById(Long id) {
        return repository.findById(id);
    }

    public Suppliers save(Suppliers supplier) {
        return repository.save(supplier);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
