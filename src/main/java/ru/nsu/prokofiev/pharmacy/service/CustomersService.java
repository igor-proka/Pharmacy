package ru.nsu.prokofiev.pharmacy.service;

import ru.nsu.prokofiev.pharmacy.model.Customers;
import ru.nsu.prokofiev.pharmacy.repository.CustomersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomersService {

    private final CustomersRepository repository;

    public CustomersService(CustomersRepository repository) {
        this.repository = repository;
    }

    public List<Customers> findAll() {
        return repository.findAll();
    }

    public Optional<Customers> findById(Long id) {
        return repository.findById(id);
    }

    public Customers save(Customers customer) {
        return repository.save(customer);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
