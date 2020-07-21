package ua.skillsup.practice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleServiceImplTest {
    public static final String NO_TITLE = null;
    public static final BigDecimal SOME_PRICE = BigDecimal.valueOf(53.18);
    public static final String SOME_TITLE = "TITLE";
    public static final BigDecimal NO_PRICE = null;
    public static final String TOO_SHORT_TITLE = "Ti";
    public static final String TOO_LONG_TITLE = "TitleTitleTitleTitleTitle";
    public static final BigDecimal TOO_SMALL_PRICE = BigDecimal.TEN;

    //  Сначала 6 юнит-тестов:
    @Test
    public void shouldNotSaveItemWithNullTitle() {
        ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
        ExampleServiceImpl exampleService = new ExampleServiceImpl(mockedExampleDao);
        exampleService.addNewItem(NO_TITLE, SOME_PRICE);
        verify(mockedExampleDao, times(0)).store(any());
    }

    @Test
    public void shouldNotSaveItemWithTitleShorterThanThreeSymbols() {
        ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
        ExampleServiceImpl exampleService = new ExampleServiceImpl(mockedExampleDao);
        exampleService.addNewItem(TOO_SHORT_TITLE, SOME_PRICE);
        verify(mockedExampleDao, times(0)).store(any());
    }

    @Test
    public void shouldNotSaveItemWithTitleLongerThanTwentySymbols() {
        ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
        ExampleServiceImpl exampleService = new ExampleServiceImpl(mockedExampleDao);
        exampleService.addNewItem(TOO_LONG_TITLE, SOME_PRICE);
        verify(mockedExampleDao, times(0)).store(any());
    }

    @Test
    public void shouldNotSaveItemWithNullPrice() {
        ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
        ExampleServiceImpl exampleService = new ExampleServiceImpl(mockedExampleDao);
        exampleService.addNewItem(SOME_TITLE, NO_PRICE);
        verify(mockedExampleDao, times(0)).store(any());
    }

    @Test
    public void shouldNotSaveItemWithPriceLessThanFifteen() {
        ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
        ExampleServiceImpl exampleService = new ExampleServiceImpl(mockedExampleDao);
        exampleService.addNewItem(SOME_TITLE, TOO_SMALL_PRICE);
        verify(mockedExampleDao, times(0)).store(any());
    }

    private Answer<List<ExampleEntity>> answerForFindAll = invocation -> List.of(
            new ExampleEntity("Fruit", Instant.parse("2020-06-12T10:15:30.00Z"), BigDecimal.valueOf(18)),
            new ExampleEntity("Beaf", Instant.parse("2020-06-12T12:45:33.00Z"), BigDecimal.valueOf(38)));

    @Test
    public void shouldReceiveMapWithSizeOneAndKeyTwelveSixTwentyAndValueTwentyEight() {
        ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
        ExampleServiceImpl exampleService = new ExampleServiceImpl(mockedExampleDao);
        when(mockedExampleDao.findAll()).thenAnswer(answerForFindAll);// now method "findAll" will return the list contains the two items
        Map<LocalDate, BigDecimal> statisticMap = exampleService.getStatistic();
        verify(mockedExampleDao, times(1)).findAll(); // check that method "findAll" was invoked
        verifyNoMoreInteractions(mockedExampleDao); // check that there were no more calls to ExampleDao
        assertEquals(1, statisticMap.size()); //check that the size of the reseived map is one
        assertTrue(statisticMap.containsKey(LocalDate.of(2020, 06, 12))); //check that the key of the reseived map is 2020-06-12
        assertEquals(BigDecimal.valueOf(28.00).setScale(2), statisticMap.get(LocalDate.of(2020, 6, 12)));
    }

    //  И два интеграционных теста:
    @Test
    public void shouldNotSaveItemWithDuplicateTitle() {
        ExampleDaoImpl exampleDao = new ExampleDaoImpl();
        ExampleServiceImpl exampleService = new ExampleServiceImpl(exampleDao);
        exampleService.addNewItem("Fish", BigDecimal.valueOf(22));   // correct item to enter
        exampleService.addNewItem("Fruit", BigDecimal.valueOf(33));  // another correct item to enter
        exampleService.addNewItem("Meat", BigDecimal.valueOf(44));   // and one more correct item to enter
        exampleService.addNewItem("Fruit", BigDecimal.valueOf(22.48)); // attempting to enter the item with duplicate title
        assertEquals(3, exampleDao.findAll().size()); //check that the size of the received list is three
    }

    @Test
    public void shouldReturnMapWithKeyTodayAndValueThirtyThree() {
        ExampleDaoImpl exampleDao = new ExampleDaoImpl();
        ExampleServiceImpl exampleService = new ExampleServiceImpl(exampleDao);
        exampleService.addNewItem("Fish", BigDecimal.valueOf(22));   // correct item to enter
        exampleService.addNewItem("Fruit", BigDecimal.valueOf(33));  // another correct item to enter
        exampleService.addNewItem("Meat", BigDecimal.valueOf(44));   // and one more correct item to enter
        assertEquals(Set.of(LocalDate.now()), exampleService.getStatistic().keySet()); //check that the received map has one correct key - today
        //check that the reseived map has one correct value = 33:
        assertEquals(BigDecimal.valueOf(33.00).setScale(2), exampleService.getStatistic().get(LocalDate.now()));
    }
}
