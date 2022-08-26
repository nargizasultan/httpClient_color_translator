package com.example.httpclients.services;

import com.example.httpclients.models.Car;
import com.example.httpclients.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final Translator translator;

    private final RestTemplate restTemplate = new RestTemplate();

    record SimpleResponse(String message) {
    }

    public record CarSaveRequest(String name, String color) {
    }

    public record CarSaveResponse(Long id, String name, String color) {
    }

    public record ColorResponse(String name) {
    }

    public List<CarSaveResponse> save(CarSaveRequest carSaveRequest) throws Exception {

        List<CarSaveResponse> list = new ArrayList<>();
        ColorResponse color = restTemplate.getForObject("https://colornames.org/search/json/?hex=" + carSaveRequest.color, ColorResponse.class);
        String translate = translator.translate("en", "ru", color.name);
        Car car = new Car(carSaveRequest.name, translate);
        carRepository.save(car);
        List<Car> all = carRepository.findAll();
        for (Car c : all) {
            list.add(new CarSaveResponse(c.getId(), c.getName(), c.getColor()));
        }
        return list;
    }

}
