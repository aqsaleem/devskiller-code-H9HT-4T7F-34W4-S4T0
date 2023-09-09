package com.devskiller.library.service;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceLoaderTest {

    @Test
    void readExistingProperty() {
        assertEquals(Optional.of("30"), ResourceLoader.getProperty("loan.period.days"));
    }

}
