package org.example.apitests.testutil;

import org.example.apitests.controller.StudioController.StudioInput;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public class StudioFactory {
    public static StudioInput valid() {
        return Instancio.of(StudioInput.class)
                .set(field(StudioInput::getName), "Studio_" + System.nanoTime())
                .set(field(StudioInput::getCountry), "Japan")
                .create();
    }
    public static StudioInput withName(String name) {
        StudioInput input = valid();
        input.setName(name);
        return input;
    }
    public static StudioInput withCountry(String country) {
        StudioInput input = valid();
        input.setCountry(country);
        return input;
    }
    public static StudioInput empty() {
        return new StudioInput();
    }
}
