package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.model.ShipValidator;
import com.space.service.ShipDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class ShipController {

    @Autowired
    private ShipDataService shipDataService;

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public ResponseEntity<List<Ship>> ships(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
            )
    {

        return new ResponseEntity<>(shipDataService.findBy(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize).getContent(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public ResponseEntity<Integer> getShipsCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
    )
    {
        return new ResponseEntity<>((int) shipDataService.findBy(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, ShipOrder.ID, 0, 3)
                .getTotalElements(), HttpStatus.OK);
    }

    @PostMapping("/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship newShip)
    {
        if (newShip.isUsed() == null) newShip.setUsed(false);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(newShip, "newShip");
        new ShipValidator().validate(newShip, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newShip.calculateRatingAndSet();
        return new ResponseEntity<>(shipDataService.saveShip(newShip), HttpStatus.OK);
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable String id)
    {
        try {
            Long shipId = validationAndReturnId(id);
            Optional<Ship> optionalShip = shipDataService.getShipById(shipId);
            if (optionalShip.isPresent()){
                return new ResponseEntity<>(optionalShip.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/ships/{id}")
    public ResponseEntity<Ship> updateShip(@RequestBody Ship updatingShip, @PathVariable String id)
    {
        try {
            Long shipId = validationAndReturnId(id);
            Optional<Ship> optionalShip = shipDataService.getShipById(shipId);
            if (optionalShip.isPresent()) {
                Ship currentShipEntity = optionalShip.get();
                currentShipEntity.updateShip(updatingShip);
                BeanPropertyBindingResult result = new BeanPropertyBindingResult(currentShipEntity, "newShip");
                new ShipValidator().validate(currentShipEntity, result);
                if (result.hasErrors()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                currentShipEntity.calculateRatingAndSet();
                return new ResponseEntity<>(shipDataService.saveShip(currentShipEntity), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/ships/{id}")
    public ResponseEntity delete(@PathVariable String id)
    {
        try {
            Long shipId = validationAndReturnId(id);
            Optional<Ship> optionalShip = shipDataService.getShipById(shipId);
            if (optionalShip.isPresent()) {
                shipDataService.delete(shipId);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private Long validationAndReturnId(String idString) throws Exception
    {
        Long shipId = Long.parseLong(idString);
        if (shipId <= 0) throw new Exception();
        return shipId;
    }
}
