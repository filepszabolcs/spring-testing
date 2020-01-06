# Spring Testing (with  Spring Boot)

Testing is an integral part of any software development project, be it an enterprise scale solution, or just a pet project.
Good, conscious testing provides a lot of benefits. It  improves the quality of code, makes finding bugs easier and faster, simplifies integration and reduces the costs, just to name a few.
The Spring framework has exceptional support for both unit and integration testing.

## Unit testing

A unit test covers a single _unit_ of  our application, where a unit is most commonly a single class. 
Unit tests drive a method or a chain of methods in isolation, to ensure that they produce the expected output, (or side effect) given the appropriate inputs.
Unit tests runs in milliseconds, as they  don't need an application context. This greatly benefits the "test / code / test” flow TDD.

## Integration testing

##Integration testing

An integration test is one which covers multiple _units_,and test the interaction between two or more clusters of cohesive classes.
In general, integration tests cover the whole path through an application. In these tests, we send a request to the application and check that it responds correctly
Integration tests need an application context (or a slice of it) to work, and as such, they take longer to run. 

## Testing by MVC layers

We can also distinguish our tests by application layer, and we can either write unit tests checking the functionality of one layer in isolation,
or we can create integration tests to check the interaction between two or more layers, which are: 

- Persistence layer
- Service layer
- Controller layer

## Service and persistence integration testing

This time, we are going to see how to create tests to check the interaction between the `service` and `persistence` layers of our application, but before we dive into the details,
lets go over some of the basics. 

### Folder and file structure

Spring and Spring Boot don't have specific recommendations about the test classes layout. So instead, you should follow the conventions of your build tool.
In case of maven, `src/test/java` is designed to contain test classes. You could have both unit and integration tests inside this directory and make the difference between them with a suffix
: `Test` for unit tests and `IT` for integration tests, by convention. Lets see an example with an `OrcService` class. 

1. We place the test classes in the `src/test/java` directory, inside the `com.progmasters.mordor.service` package.
2. We create an `OrcServiceTest` class for the unit tests.
3. We create an `OrcServiceIT` class for the integration tests.
So a unit testing  class for an OrsService class would be called 

As you can see, it is also a good practice to create a package structure that matches the one under `src/main/java`

### JUnit

 JUnit is the Java testing framework which lays the foundation for our tests. It provides an easy-to-use toolset to write and run our test.
 The latest major version is JUnit 5, and it is included in `spring-boot-starter-test` from version 2.2.x, so we don't have to manually add it to our _pom.xml_.
 JUnit 5 requires Java 8 or higher. 
 
 ### Anatomy of a test class

After we create a test class, we can start writing test, and there is a conventional way to do that. 

- First we add a field holding an instance of the class we are going to test, and we also need to provide the classes this class depends on.(Either by autowiring or mocking them - more on this later)
- Tests methods are public and void, and they are named after the method they are meant to test, with a `test` prefix. Eg.: `testListOrcs`
- Test methods are annotated with the `@Test` annotation
- Initialization or teardown logic is placed in lifecycle methods with  `@BeforeEach` and `@AfterEach`.

###Example
Let's see an example in action. 

```java
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrcServiceIT {

    @Autowired
    OrcRepository orcRepository;
    private OrcService orcService;

    @BeforeEach
    public void init() {
        this.orcService = new OrcService(orcRepository);
    }

}
```

- The `@ExtendWith(SpringExtension.class)` provides full integration between the Spring TestContext and the JUnit framework.
- The `@DataJpaTest` annotation comes from Spring and initializes only a slice of the whole application context, which is responsible for data persistence.
    - By default this annotation creates an empty, in memory database, and makes sure each test method runs in its own transaction, which is rolled back after the method. 
    - This in memory database however requires a dependency to work, so the following has to be added to the _pom.xml_
    
````xml
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.197</version>
        </dependency>
````

- We add an `@Autowired` OrcRepository field and the OrcService itself, which is instantiated in the _init()_ method, annotated with `@BeforeEach`

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
    - `given` : This is where we set up our tests, create the required dto instances, etc...
    - `when`: This is where we invoke the function that we wish to test
    - `then`: This is where we examine the result and side effects of the invoked function and make assertions
    
- The `assertion` functions decide if the test passes or fails. They are provided by the JUnit framework.
