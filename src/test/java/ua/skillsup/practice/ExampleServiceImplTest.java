package ua.skillsup.practice;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class ExampleServiceImplTest {
    public static final String NO_TITLE = null;
    public static final BigDecimal SOME_PRICE = BigDecimal.valueOf(53.18);
    public static final String SOME_TITLE = "TITLE";
    public static final BigDecimal NO_PRICE = null;
    public static final String TOO_SHORT_TITLE = "Ti";
    public static final String TOO_LONG_TITLE = "TitleTitleTitleTitleTitle";
    public static final BigDecimal TOO_SMALL_PRICE = BigDecimal.TEN;

    private static ExampleServiceImpl exampleService = new ExampleServiceImpl();

    private ExampleDaoImpl getMockedExampleDao() throws NoSuchFieldException, IllegalAccessException {
        Field field = ExampleServiceImpl.class.getDeclaredField("exampleDao");
        field.setAccessible(true);
        return (ExampleDaoImpl) field.get(exampleService);
    }

    @BeforeClass
    public static void setup() {
        try {
            ExampleDaoImpl mockedExampleDao = mock(ExampleDaoImpl.class);
            Field field = ExampleServiceImpl.class.getDeclaredField("exampleDao");
            field.setAccessible(true);
            field.set(exampleService, mockedExampleDao);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should1_PassIfTitleIsAbsent() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            exampleService.addNewItem(NO_TITLE, SOME_PRICE);
            verify(mockedExampleDao, times(0)).store(any());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should2_PassIfTitleShorterThanThreeSymbols() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            exampleService.addNewItem(TOO_SHORT_TITLE, SOME_PRICE);
            verify(mockedExampleDao, times(0)).store(any());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should3_PassIfTitleLongerThanTwentySymbols() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            exampleService.addNewItem(TOO_LONG_TITLE, SOME_PRICE);
            verify(mockedExampleDao, times(0)).store(any());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should4_PassIfPriceIsAbsent() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            exampleService.addNewItem(SOME_TITLE, NO_PRICE);
            verify(mockedExampleDao, times(0)).store(any());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should5_PassIfPriceLessThanFifteen() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            exampleService.addNewItem(SOME_TITLE, TOO_SMALL_PRICE);
            verify(mockedExampleDao, times(0)).store(any());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Answer<List<ExampleEntity>> answerForFindAll = invocation -> List.of(new ExampleEntity(SOME_TITLE, Instant.now(), SOME_PRICE));

    @Test
    public void should6_StoreOneItemAndNotStoreSecondItemWithDuplicateTitle() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            exampleService.addNewItem(SOME_TITLE, SOME_PRICE);
            verify(mockedExampleDao).findAll();
            verify(mockedExampleDao).store(any());// entered correct data and storing has passed
            when(mockedExampleDao.findAll()).thenAnswer(answerForFindAll);// now method "findAll" will return the list contains the item with title "SOME_TITLE"
            exampleService.addNewItem(SOME_TITLE, SOME_PRICE);
            verify(mockedExampleDao, times(2)).findAll();
            verify(mockedExampleDao).store(any());
            verifyNoMoreInteractions(mockedExampleDao);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Answer<Map<LocalDate, List<ExampleEntity>>> answerForGetDB = invocation -> Map.of(
            LocalDate.of(2020, 06, 12),
            Arrays.asList(new ExampleEntity("Fruit", Instant.parse("2020-06-12T10:15:30.00Z"), BigDecimal.valueOf(18)),
                    new ExampleEntity("Beaf", Instant.parse("2020-06-12T12:45:33.00Z"), BigDecimal.valueOf(38))));

    @Test
    public void should7_ReceiveStatisticMapWithSize1key12_06_20andValue28() {
        try {
            ExampleDaoImpl mockedExampleDao = getMockedExampleDao();
            when(mockedExampleDao.getDB()).thenAnswer(answerForGetDB);// now method "getDB" will return the map contains the two items on key 12.06.20
            Map<LocalDate, BigDecimal> statisticMap = exampleService.getStatistic();
            verify(mockedExampleDao).getDB();
            verifyNoMoreInteractions(mockedExampleDao);
            assertEquals(1, statisticMap.size());
            assertTrue(statisticMap.containsKey(LocalDate.of(2020, 06, 12)));
            assertArrayEquals(new BigDecimal[]{BigDecimal.valueOf(28).setScale(2)}, statisticMap.values().toArray());
            assertEquals(BigDecimal.valueOf(28.00).setScale(2), statisticMap.get(LocalDate.of(2020, 6, 12)));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
