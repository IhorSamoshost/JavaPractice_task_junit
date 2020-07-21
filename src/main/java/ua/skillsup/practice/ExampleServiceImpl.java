package ua.skillsup.practice;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExampleServiceImpl implements ExampleService {
    private ExampleDaoImpl exampleDao;

    public ExampleServiceImpl(ExampleDaoImpl exampleDao) {
        this.exampleDao = exampleDao;
    }

    @Override
    public void addNewItem(String title, BigDecimal price) {
        if (validateInputedTitle(title) && validateInputedPrice(price)) {
            exampleDao.store(new ExampleEntity(title, Instant.now(), price.setScale(2, RoundingMode.HALF_UP)));
        }
    }

    @Override
    public Map<LocalDate, BigDecimal> getStatistic() {
        Map<LocalDate, BigDecimal> statisticMap = new HashMap<>();
        Map<LocalDate, List<ExampleEntity>> temporalMap = exampleDao.findAll().stream()
                .collect(Collectors.groupingBy(ExampleEntity::getLocalDateInFromInstant));
        for (Map.Entry<LocalDate, List<ExampleEntity>> entry : temporalMap.entrySet()) {
            BigDecimal sumOfTodayList = BigDecimal.ZERO;
            for (ExampleEntity todayExampleEntity : entry.getValue()) {
                sumOfTodayList = sumOfTodayList.add(todayExampleEntity.getPrice());
            }
            BigDecimal todayAveragePrice = sumOfTodayList.divide(BigDecimal.valueOf(entry.getValue().size()), RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            statisticMap.put(entry.getKey(), todayAveragePrice);
        }
        return statisticMap;
    }

    private boolean validateInputedTitle(String title) {
        if (title == null || title.length() < 3 || title.length() > 20) {
            System.out.println("Adding of item is impossible - You try to enter incorrect entity title!");
            return false;
        }
        return true;
    }

    private boolean validateInputedPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.valueOf(15)) < 0) {
            System.out.println("Adding of item is impossible - You try to enter incorrect entity price!");
            return false;
        }
        return true;
    }
//-------------------------------------------------------------------------------------------
//    Это вспомогательные методы, которыми я пользовался в процессе написания кода:

//    public ExampleDao getExampleDao() {
//        return exampleDao;
//    }

//    public static void main(String[] args) {
//        ExampleServiceImpl exampleService = new ExampleServiceImpl(new ExampleDaoImpl());
////      Try to enter same different items for checking of method "addNewItem":
//        exampleService.addNewItem("Fish", BigDecimal.valueOf(25.44));               // correct item to enter
//        exampleService.addNewItem("Fruit", BigDecimal.valueOf(22.48));              // one else correct item to enter
//        exampleService.addNewItem("Meat", BigDecimal.valueOf(42.8));              // one else correct item to enter
//
//        exampleService.addNewItem("Fi", BigDecimal.valueOf(25));                     // title shorter than expected
//        exampleService.addNewItem("Fishhhhhhhhhhhhhhhhhhh", BigDecimal.valueOf(25)); // title longer than expected
//        exampleService.addNewItem("Fish", BigDecimal.valueOf(5));                    // price is less than expected
//        exampleService.addNewItem("Fruit", BigDecimal.valueOf(22.477));              // attempting to enter the item with duplicate title
//        System.out.println("DB contents after entering new data:");
////      Receive and print a list of all items contained in DB represented by Dtos:
//        exampleService.getExampleDao().findAll().stream().map(ExampleEntity::toDto).forEach(System.out::println);
////      Receive and print a map contains average prices for every date:
//        System.out.println("Printout of the map with average price statistics:");
//        new TreeMap<>(exampleService.getStatistic()).entrySet().forEach(System.out::println);
//    }
}