# Spring Testing (with Spring Boot)

## Intro

Testing is an integral part of any software development project, be it an enterprise scale solution, or just a pet project. Good, conscious testing provides a lot of benefits. It  improves the quality of code, makes finding bugs easier and faster,  simplifies integration and reduces costs, just to name a few. The Spring framework has exceptional support for both unit and integration testing.

### Unit testing

A unit test covers a single _unit_ of  our application, where a unit is most commonly a single class.  Unit tests drive a method or a chain of methods in isolation, to ensure that they produce the expected output, (or side effect)  given the appropriate inputs. Unit tests runs in milliseconds, as they  don't need an application context. This greatly benefits the "test / code / test” flow TDD.

### Integration testing

An integration test is one which covers multiple _units_, and test the interaction between two or more clusters of cohesive classes. In general, integration tests cover the whole path through an application. In these tests, we send a request to the application and  check that it responds correctly. Integration tests need an application context (or a slice of it) to work, and as such, they take longer to run. 

### Testing by layers

We can also distinguish our tests by application layer, and we can either write unit tests checking the functionality of one layer in isolation, or we can create integration tests to check the interaction between two or more layers, which are: 

- Persistence layer
- Service layer
- Controller layer

### Folder and file structure

In case of maven, `src/test/java` is designed to contain test classes. You could have both unit and integration tests inside this directory and make the difference between them with a suffix: `Test` for unit tests and `IT` for integration tests, by convention. 
Lets see an example with an `OrcService` class. 

1. We place the test classes in the `src/test/java` directory, inside the `com.progmasters.mordor.service` package.
2. We create an `OrcServiceTest` class for the unit tests.
3. We create an `OrcServiceIT` class for the integration tests.
   So a unit testing class for an OrsService class would be called 

As you can see, it is also a good practice to create a package structure that matches the one under `src/main/java`, and also necessary to reach package-private fields and methods.

### JUnit

JUnit is the Java testing framework which lays the foundation for our tests. It provides an easy-to-use toolset to write and run our test. The latest major version is JUnit 5, and it is included in `spring-boot-starter-test` from version 2.2.x, so we don't have to manually add it to our _pom.xml_. JUnit 5 requires Java 8 or higher. 

### Anatomy of a test class

After we create a test class, we can start writing tests, and there is a conventional way to do that. 

- First we add a field holding an instance of the class we are going to test, and we also need to provide the classes this class depends on. (Either by @Autowiring or mocking them - more on this later)

- Tests methods are **public** and **void**, and they are named after the method they are meant to test, with a `test` prefix. Eg.: `testListOrcs`

  - There are some other conventions, that can be followed, which ever you choose, just stay consistent throughout your application:

    https://dzone.com/articles/7-popular-unit-test-naming 

- Test methods are annotated with the `@Test` annotation

- Initialization and teardown logic is placed in lifecycle methods called `@BeforeEach` and `@AfterEach`.

## Service and persistence integration testing

This time, we are going to see how to create tests to check the interaction between the `service` and `persistence` layers of our application, but before we dive into the details, lets go over some of the basics. 

### Example 1 - Integration tests for Service and Repository layers - OrcServiceIT.java

Let's see an example in action. 

- We add an `@Autowired` to OrcService itself:

```java
@SpringBootTest
@Transaction
@Rollback
public class OrcServiceIT {

    @Autowired
    private OrcService orcService;

}
```


```java
    @Test
    public void testListOrcs(){
        //given
        OrcDetails firstOrcDetails = new OrcDetails();
        firstOrcDetails.setName("tibork");
        firstOrcDetails.setKillCount(50L);
        firstOrcDetails.setRaceType("MOUNTAIN");
        firstOrcDetails.setWeapons(List.of("KNIFE", "BOW"));

        OrcDetails secondOrcDetails = new OrcDetails();
        secondOrcDetails.setName("orkaresz");
        secondOrcDetails.setKillCount(10L);
        secondOrcDetails.setRaceType("URUK");
        secondOrcDetails.setWeapons(List.of("SWORD"));

        orcService.saveOrc(firstOrcDetails);
        orcService.saveOrc(secondOrcDetails);

        //when
        List<OrcListItem> orcList = orcService.listOrcs();

        //then
         assertEquals(2, orcList.size());

        assertEquals("tibork",orcList.get(0).getName());
        assertEquals(50L, orcList.get(0).getKillCount());
        assertEquals("Mountain",orcList.get(0).getOrcRaceType());
        assertEquals(2,orcList.get(0).getWeapons().size());
        assertTrue(orcList.get(0).getWeapons().contains("Knife"));

        assertEquals("orkaresz",orcList.get(1).getName());
        assertEquals(10L, orcList.get(1).getKillCount());
        assertEquals("Uruk",orcList.get(1).getOrcRaceType());
        assertEquals(1,orcList.get(1).getWeapons().size());
        assertTrue(orcList.get(1).getWeapons().contains("Sword"));
    }
```

- The test method is annotated with `@Test`, and it consists of three parts:
    - `given` : This is where we set up our tests, create the required DTO instances, etc...
    - `when`: This is where we invoke the function that we wish to test
    - `then`: This is where we examine the result and side effects of the invoked function and make assertions

- The `assertion` functions decide if the test passes or fails. They are provided by the JUnit framework.

#### In-memory database

You probably don't want to pollute, or risk your own database while running tests against it, so it might be a better solution, to use any in-memory-database. The backside of this can be, that it might use a different SQL language/syntax, different rule-set than the one you use in production. Always keep this in mind, when running your tests. See: [Don't use in-memory db in tests](https://phauer.com/2017/dont-use-in-memory-databases-tests-h2/) 

Using Spring Auto Configuration:

- Simply add the following annotation to your test class, this will tell Springboot to create an In-Memory H2 Database for your tests:

  ```java
  @AutoConfigureTestDatabase
  ```

Using test-profile configuration:

 - Checkout the second yml file under /src/**test**/resources/application.yml
 - Database settings are overriden and H2 in-memory database system is used. It only exists during the test execution.

````yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
````

Note, that you should add maven dependency as well for both approaches:

````xml
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.197</version>
        </dependency>
````

### Example 2 - Test only JPA Repository layer - OrcRepositoryTest.java

- The `@ExtendWith(SpringExtension.class)` provides full integration between the Spring TestContext and the JUnit framework.
- The `@DataJpaTest` annotation comes from Spring and initializes only a slice of the whole application context, which is responsible for data persistence.
    - By default this annotation creates an empty, in-memory database, and makes sure each test method runs in its own transaction, which is rolled back after the method. 
    - This in-memory database however requires a dependency to work, so the 'H2-database' dependency has to be added to the _pom.xml_

## Service Layer Unit testing

Writing these test should be self-explanatory. These should always be unit tests, with all dependency being mocked. Keep in mind, that you need to keep the mocked dependencies “under-control”. Always specifying what result you expect to be returned, otherwise it will return null-s. 

If you specify more than you actually use in your test method, an **UnneccessaryStubbingException** will occur. Also note, the option to check invocation of any methods with Mockito-s verify() and verifyNoMoreInteractions() methods. These tools can come very handy, if you want to spot any unexpected method invocations.

````java
@ExtendWith(MockitoExtension.class)
class OrcServiceTest {

   @Mock
   private OrcRepository orcRepositoryMock;

   private OrcService orcService;

   @BeforeEach
   void init() {
       this.orcServicee = new OrcService(orcRepositoryMock);
   }

   @Test
   void testCreateJob_validCommand_returns() { 				   	when(orcRepositoryMock.save(any(Orc.class))).then(returnsFirstArg());
    OrcCreateCommand orcCreateCommand = new OrcCreateCommand("Urugha");
    assertDoesNotThrow(() -> orcService.saveOrc(orcCreateCommand));
    verify(orcRepositoryMock, times(1)).save(any());
    verifyNoMoreInteractions(orcRepositoryMock);
   }

````

With the addition of @ExtendedWith(MockitoExtension.class) annotation, you may use the @Mock annotation in test classes. 

All this will do, is give you a dummy instance of that particular class. You may still invoke it's methods, however if you don't specify the answer, or behaviour it will return nulls for you. This can be very useful, in case you already tested, and trust the mocked parts, and you want to run your tests in a more isolated environment.

## Repository Layer Testing

You may run simple tests against your repository with just using ```@DataJpaTest``` annotation on your test class. This will give you an in-memory-database to work with. Note that the H2-database dependency is required once again.

````java
@DataJpaTest
public class OrcRepositoryTest {

    @Autowired
    private OrcRepository orcRepository;

    @Test
    public void testSaveAndFindAll() {
        // given
        Orc orc = new Orc();
        orc.setName("Varag");
        orc.setOrcRaceType(OrcRaceType.MOUNTAIN);
        orc.setWeapons(Arrays.asList(WeaponType.KNIFE, WeaponType.SHIELD));
        orc.setKillCount(1000L);
        orcRepository.save(orc);
        // when
        List<Orc> orcs = orcRepository.findAll();
        // then
        assertEquals(1, orcs.size());
    }
}
````

Keep in mind, that there is no point to test such queries and methods provided by the framework itself. However it might come handy, to write tests against your own custom queries. 

# See also

- [Short, useful and up-to-date documentation about Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)
- [Mockito When/Then Cookbook](https://www.baeldung.com/mockito-behavior)
- [Mockito Verify Cookbook](https://www.baeldung.com/mockito-verify)
- [Don't use in-memory db in tests](https://phauer.com/2017/dont-use-in-memory-databases-tests-h2/) 

