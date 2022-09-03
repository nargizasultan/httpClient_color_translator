package com.example.httpclients.services;

import com.example.httpclients.models.Car;
import com.example.httpclients.repositories.CarRepository;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;


    @Value("${google.translate.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public record CarSaveRequest(String name, String color) {
    }

    public record CarSaveResponse(Long id, String name, String color) {
    }

    public record ColorResponse(String paletteTitle) {
    }

    public List<CarSaveResponse> save(CarSaveRequest carSaveRequest) throws Exception {

        List<CarSaveResponse> list = new ArrayList<>();
        ColorResponse color = restTemplate.getForObject("https://api.color.pizza/v1/?values=" + carSaveRequest.color, ColorResponse.class);

        Translate translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();

        Translation translation = translate.translate(color.paletteTitle, Translate.TranslateOption.targetLanguage("ru"));

        String translatedText = translation.getTranslatedText();

        Car car = new Car(carSaveRequest.name, translatedText);
        carRepository.save(car);
        List<Car> all = carRepository.findAll();
        for (Car c : all) {
            list.add(new CarSaveResponse(c.getId(), c.getName(), c.getColor()));
        }
        return list;
    }

}
