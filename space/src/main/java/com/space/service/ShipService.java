package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import java.util.List;

public interface ShipService {

    List<Ship> findAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                       Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                       Double maxRating, Integer pageNumber, Integer pageSize, ShipOrder order);

    Ship findById(Long id);

    Ship save(Ship ship);

    void deleteById(Long id);

    Integer count(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
               Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
               Double maxRating);
}
