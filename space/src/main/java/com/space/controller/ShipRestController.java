package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipRestController {

    private ShipService shipService;

    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/ships")
    public List<Ship> findAll(@RequestParam(required = false) String name, @RequestParam(required = false) String planet, @RequestParam(required = false) ShipType shipType,
                              @RequestParam(required = false) Long after, @RequestParam(required = false) Long before, @RequestParam(required = false) Boolean isUsed,
                              @RequestParam(required = false) Double minSpeed, @RequestParam(required = false) Double maxSpeed, @RequestParam(required = false) Integer minCrewSize,
                              @RequestParam(required = false) Integer maxCrewSize, @RequestParam(required = false) Double minRating, @RequestParam(required = false) Double maxRating,
                              @RequestParam(defaultValue = "0") Integer pageNumber,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              @RequestParam(defaultValue = "ID") ShipOrder order) {
        return shipService.findAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, pageNumber, pageSize, order);
    }

    @GetMapping("/ships/{id}")
    public Ship findById(@PathVariable Long id) throws Exception {
        if (id <= 0) {
            throw new Exception();
        }
        Ship ship = shipService.findById(id);
        if (ship == null) {
            throw new ShipNotFoundException();
        }
        return ship;
    }

    @GetMapping("/ships/count")
    public Integer count(@RequestParam(required = false) String name, @RequestParam(required = false) String planet, @RequestParam(required = false) ShipType shipType,
                         @RequestParam(required = false) Long after, @RequestParam(required = false) Long before, @RequestParam(required = false) Boolean isUsed,
                         @RequestParam(required = false) Double minSpeed, @RequestParam(required = false) Double maxSpeed, @RequestParam(required = false) Integer minCrewSize,
                         @RequestParam(required = false) Integer maxCrewSize, @RequestParam(required = false) Double minRating, @RequestParam(required = false) Double maxRating) {
        return shipService.count(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @PostMapping("/ships")
    public Ship save(@RequestBody Ship ship) throws Exception {

        if (ship.getName().length() > 50 || ship.getPlanet().length() > 50 || ship.getName().trim().length() == 0 || ship.getPlanet().trim().length() == 0 || ship.getCrewSize() < 1
                || ship.getCrewSize() > 9999 || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99 || ship.getProdDate().getTime() < 0
                || (ship.getProdDate().getYear() + 1900) < 2800 || (ship.getProdDate().getYear() + 1900) > 3019) {
            throw new Exception();
        }

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }

        if (ship.getUsed()) {
            ship.setRating((double) Math.round(((80 * (Math.round(ship.getSpeed() * 100.0) / 100.0) * 0.5) / (3019 - (ship.getProdDate().getYear() + 1900) + 1)) * 100.0) / 100.0);
        } else {
            ship.setRating((double) Math.round(((80 * (Math.round(ship.getSpeed() * 100.0) / 100.0) * 1) / (3019 - (ship.getProdDate().getYear() + 1900) + 1)) * 100.0) / 100.0);
        }

        ship.setId(0L);

        return shipService.save(ship);
    }

    @PostMapping("/ships/{id}")
    public Ship update(@RequestBody Ship ship, @PathVariable Long id) throws Exception {
        if (id <= 0) {
            throw new Exception();
        }

        Ship theShip = shipService.findById(id);

        if (theShip == null) {
            throw new ShipNotFoundException();
        }

        if (ship.getName() == null && ship.getPlanet() == null && ship.getShipType() == null && ship.getProdDate() == null && ship.getUsed() == null
        && ship.getSpeed() == null && ship.getCrewSize() == null) {
            return shipService.save(theShip);
        }

        if (ship.getName() != null) {
            if (ship.getName().length() > 50 || ship.getName().trim().length() == 0) {
                throw new Exception();
            }
            theShip.setName(ship.getName());
        }

        if (ship.getPlanet() != null) {
            if (ship.getPlanet().length() > 50 || ship.getPlanet().trim().length() == 0) {
                throw new Exception();
            }
            theShip.setPlanet(ship.getPlanet());
        }

        if (ship.getShipType() != null) {
            theShip.setShipType(ship.getShipType());
        }

        if (ship.getProdDate() != null) {
            if (ship.getProdDate().getTime() < 0 || (ship.getProdDate().getYear() + 1900) < 2800 || (ship.getProdDate().getYear() + 1900) > 3019) {
                throw new Exception();
            }
            theShip.setProdDate(ship.getProdDate());
        }

        if (ship.getUsed() != null) {
            theShip.setUsed(ship.getUsed());
        }

        if (ship.getSpeed() != null) {
            if (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) {
                throw new Exception();
            }
            theShip.setSpeed(Math.round(ship.getSpeed() * 100.0) / 100.0);
        }

        if (ship.getCrewSize() != null) {
            if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) {
                throw new Exception();
            }
            theShip.setCrewSize(ship.getCrewSize());
        }

        if (theShip.getUsed()) {
            theShip.setRating((double) Math.round(((80 * (Math.round(theShip.getSpeed() * 100.0) / 100.0) * 0.5) / (3019 - (theShip.getProdDate().getYear() + 1900) + 1)) * 100.0) / 100.0);
        } else {
            theShip.setRating((double) Math.round(((80 * (Math.round(theShip.getSpeed() * 100.0) / 100.0) * 1) / (3019 - (theShip.getProdDate().getYear() + 1900) + 1)) * 100.0) / 100.0);
        }

        return shipService.save(theShip);
    }

    @DeleteMapping("/ships/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        if (id <= 0) {
            throw new Exception();
        }
        Ship ship = shipService.findById(id);
        if (ship == null) {
            throw new ShipNotFoundException();
        }
        shipService.deleteById(id);
    }
}
