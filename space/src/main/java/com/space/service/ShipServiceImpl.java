package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<Ship> findAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                              Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                              Double maxRating, Integer pageNumber, Integer pageSize, ShipOrder order) {
        Page<Ship> page = shipRepository.findAll(ShipSpecifications.findAll(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating), PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())));
        return page.getContent();
    }

    @Override
    public Ship findById(Long id) {
        Optional<Ship> result = shipRepository.findById(id);
        Ship ship = null;
        if (result.isPresent()) {
            ship = result.get();
        }
        return ship;
    }

    @Override
    public Ship save(Ship ship) {
        return shipRepository.save(ship);
    }

    @Override
    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    @Override
    public Integer count(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                      Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                      Double maxRating) {
        List<Ship> list = shipRepository.findAll(ShipSpecifications.findAll(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating));
        return list.size();
    }
}
