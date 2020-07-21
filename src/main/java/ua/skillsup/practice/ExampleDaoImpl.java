package ua.skillsup.practice;

import java.util.*;
import java.time.*;
import java.util.stream.Collectors;

public class ExampleDaoImpl implements ExampleDao {
    private Map<LocalDate, List<ExampleEntity>> mapOfEntityLists;

    public ExampleDaoImpl() {
        mapOfEntityLists = new HashMap<>();
    }

    @Override
    public boolean store(ExampleEntity entity) throws ExampleNetworkException {
//      check that the entity passed in the parameter is not null and belongs to the correct type:
        if (entity == null || entity.getClass() != ExampleEntity.class) {
            return false;
        }
//      check for the presence of a parameter in the DB map:
        if (mapOfEntityLists.values().stream().flatMap(dayList -> dayList.stream().map(ExampleEntity::getTitle)).
                anyMatch(title -> entity.getTitle().equals(title))) {
            System.out.println("Adding of item is impossible - You try to enter the item with duplicate title!");
            return false;
        }
//      adding a new entity to the list of entities stored on the corresponding date:
        List<ExampleEntity> dateListToAddEntity = mapOfEntityLists.get(entity.getLocalDateInFromInstant());
        if (dateListToAddEntity == null) dateListToAddEntity = new ArrayList<>();
        dateListToAddEntity.add(entity);
        mapOfEntityLists.put(entity.getLocalDateInFromInstant(), dateListToAddEntity);
        return true;
    }

    @Override
    public List<ExampleEntity> findAll() throws ExampleNetworkException {
        return mapOfEntityLists.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}