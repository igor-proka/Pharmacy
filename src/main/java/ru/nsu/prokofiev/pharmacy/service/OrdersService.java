package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.Orders;
import ru.nsu.prokofiev.pharmacy.repository.OrdersRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepository repository;

    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }

    public List<Orders> findAll() {
        return repository.findAll();
    }

    public Optional<Orders> findById(Long id) {
        return repository.findById(id);
    }

    public Orders save(Orders order) {
        return repository.save(order);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
