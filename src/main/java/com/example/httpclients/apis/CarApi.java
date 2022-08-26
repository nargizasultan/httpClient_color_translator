package com.example.httpclients.apis;

import com.example.httpclients.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarApi {
    private final CarService carService;
    @PostMapping
    public List<CarService.CarSaveResponse> save(@RequestBody CarService.CarSaveRequest carSaveRequest) throws Exception {
       return carService.save(carSaveRequest);
    }
}
