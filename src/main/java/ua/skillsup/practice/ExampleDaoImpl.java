package ua.skillsup.practice;

import java.math.BigDecimal;
import java.util.*;
import java.time.*;
import java.util.stream.Collectors;

public class ExampleDaoImpl implements ExampleDao {
    private static ExampleDaoImpl exampleDaoInstance;
    private Map<LocalDate, List<ExampleEntity>> mapOfEntityLists;

    private ExampleDaoImpl() {
    }

    public static ExampleDaoImpl getInstance() {
        if (exampleDaoInstance == null) {
            exampleDaoInstance = new ExampleDaoImpl();
        }
        return exampleDaoInstance;
    }

    @Override
    public boolean store(ExampleEntity entity) throws ExampleNetworkException {
//      check that the entity passed in the parameter is not null and belongs to the correct type:
        if (entity == null || entity.getClass() != ExampleEntity.class) {
            return false;
        }
//      check of the existence of a DB map:
        if (mapOfEntityLists == null) mapOfEntityLists = new HashMap<>();
//      check for the presence of a parameter in the DB map:
        if (mapOfEntityLists.values().stream().anyMatch(lst -> lst.contains(entity))) {
            return false;
        }
//      adding a new entity to the list of entities stored on the corresponding date:
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate entityLocalDate = entity.getDateIn().atZone(defaultZoneId).toLocalDate();
        List<ExampleEntity> dateListToAddEntity = mapOfEntityLists.get(entityLocalDate);
        if (dateListToAddEntity == null) dateListToAddEntity = new ArrayList<>();
        dateListToAddEntity.add(entity);
        mapOfEntityLists.put(entityLocalDate, dateListToAddEntity);
        System.out.println("store");
        return true;
    }

    @Override
    public List<ExampleEntity> findAll() throws ExampleNetworkException {
        if (mapOfEntityLists == null) mapOfEntityLists = new HashMap<>();
        return mapOfEntityLists.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public Map<LocalDate, List<ExampleEntity>> getDB() {
        return Map.copyOf(mapOfEntityLists);
    }
}