package com.example.httpclients.repositories;

import com.example.httpclients.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
