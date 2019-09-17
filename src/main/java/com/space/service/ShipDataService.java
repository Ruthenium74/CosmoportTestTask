package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ShipDataService {

    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> getAllShips()
    {
        return shipRepository.findAll();
    }

    public List<Ship> getAllShips(int pageNumber, int pageSize, ShipOrder order)
    {
        Sort sort = new Sort(Sort.Direction.ASC, order.getFieldName());
        return shipRepository.findAll(new PageRequest(pageNumber, pageSize, sort)).getContent();
    }

    public Page<Ship> findBy(String name, String planet, ShipType shipType, Long after, Long before,
                             Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                             Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order,
                             Integer pageNumber, Integer pageSize)
    {
        return shipRepository.findAll(new Specification<Ship>(){
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"),
                        "%" + name + "%")));
                if (planet != null) predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("planet"),
                        "%" + planet + "%")));
                if (shipType != null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shipType"),
                        shipType)));
                if (after != null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("prodDate"), new Date(after))));
                if (before != null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
                        root.get("prodDate"), new Date(before))));
                if (isUsed != null) predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isUsed"),
                        isUsed)));
                if (minSpeed != null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("speed"), minSpeed)));
                if (maxSpeed != null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
                        root.get("speed"), maxSpeed)));
                if (minCrewSize != null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("crewSize"), minCrewSize)));
                if (maxCrewSize != null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
                        root.get("crewSize"), maxCrewSize)));
                if (minRating != null) predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("rating"), minRating)));
                if (maxRating != null) predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
                        root.get("rating"), maxRating)));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.ASC, order.getFieldName())));
    }
}
