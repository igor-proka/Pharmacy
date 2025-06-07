package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Powders;
import ru.nsu.prokofiev.pharmacy.repository.PowdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PowdersService {

    private final PowdersRepository repository;

    public PowdersService(PowdersRepository repository) {
        this.repository = repository;
    }

    public List<Powders> findAll() {
        return repository.findAll();
    }

    public Optional<Powders> findById(Long id) {
        return repository.findById(id);
    }

    public Powders save(Powders entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
