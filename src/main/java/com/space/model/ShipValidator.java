package com.space.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("shipValidator")
public class ShipValidator implements Validator {
    private static final int MAX_NAME_FIELD_LENGTH = 50;
    private static final int MAX_PLANET_FIELD_LENGTH = 50;
    private static final int MIN_PROD_DATE_YEAR = 2800 - 1900; // - 1900 cause we use Date.getYear
    private static final int MAX_PROD_DATE_YEAR = 3019 - 1900;
    private static final double MIN_SPEED = 0.01;
    private static final double MAX_SPEED = 0.99;
    private static final int MIN_CREW_SIZE = 1;
    private static final int MAX_CREW_SIZE = 9999;
    @Override
    public boolean supports(Class<?> clazz) {
        return Ship.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name can't be blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "planet", "planet can't be blank");
        Ship checkedShip = (Ship) target;
        if (checkedShip.getName() != null && checkedShip.getName().length() > MAX_NAME_FIELD_LENGTH) {
            errors.rejectValue("name", "name should be less or equal than " + MAX_NAME_FIELD_LENGTH +
                    " characters");
        }
        if (checkedShip.getPlanet() != null && checkedShip.getPlanet().length() > MAX_PLANET_FIELD_LENGTH) {
            errors.rejectValue("planet", "planet should be less or equal than " +
                    MAX_PLANET_FIELD_LENGTH + " characters");
        }

        if (checkedShip.getShipType() == null) {
            errors.rejectValue("shipType", "shipType is required");
        }

        if (checkedShip.getProdDate() == null) {
            errors.rejectValue("prodDate", "prodDate is required");
        } else if (checkedShip.getProdDate().getYear() < MIN_PROD_DATE_YEAR) {
            errors.rejectValue("prodDate", "prodDate should be greater or equal than 2800");
        } else if (checkedShip.getProdDate().getYear() > MAX_PROD_DATE_YEAR) {
            errors.rejectValue("prodDate", "prodDate should be less or equal than 3019");
        }

        if (checkedShip.getSpeed() == null) {
            errors.rejectValue("speed", "speed is required");
        } else if (checkedShip.getSpeed() < MIN_SPEED) {
            errors.rejectValue("speed", "speed should be greater or equal than " + MIN_SPEED);
        } else if (checkedShip.getSpeed() > MAX_SPEED) {
            errors.rejectValue("speed", "speed should be less or equal than " + MAX_SPEED);
        }

        if (checkedShip.getCrewSize() == null) {
            errors.rejectValue("crewSize", "crewSize is required");
        } else if (checkedShip.getCrewSize() < MIN_CREW_SIZE) {
            errors.rejectValue("crewSize", "crewSize should be greater or equal than " + MIN_CREW_SIZE);
        } else if (checkedShip.getCrewSize() > MAX_CREW_SIZE) {
            errors.rejectValue("crewSize", "crewSize should be less or equal than " + MAX_CREW_SIZE);
        }
    }
}
