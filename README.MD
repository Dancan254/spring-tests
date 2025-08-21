## Core Fundamantals
`@Test` - Marks a method as a test method  
`DisplayName` - custom test name  
`Diabled` - Disables a test  
`RepeatedTest(5)` - Runs a test 5 times   
`TestMethodOrder(MethodOrderer.OrderAnnotation.class)` - Order tests by annotation  
`@Order(1)` - Order tests by number  
`@BeforeAll` - Runs before all tests  
`@AfterAll` - Runs after all tests  
`@BeforeEach` - Runs before each test  
`@AfterEach` - Runs after each test  
`@ParameterizedTest` - Runs a test for each parameter  
`@ValueSource` - Provides values for a parameter  
`@EnumSource` - Provides values for a parameter from an enum  
`@CsvSource` - Provides values for a parameter from a CSV file  

## Assertions
```java
// Basic assertions
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);
assertTrue(condition);
assertFalse(condition);
assertNull(object);
assertNotNull(object);

// Collection assertions
assertIterableEquals(expectedList, actualList);
assertArrayEquals(expectedArray, actualArray);

// Exception assertions
assertThrows(IllegalArgumentException.class, () -> {
// code that should throw exception
});

// Multiple assertions (all executed even if one fails)
assertAll(
() -> assertEquals("John", student.getFirstname()),
() -> assertEquals("Doe", student.getLastname()),
() -> assertNotNull(student.getEmail())
);

// Custom assertion messages
assertEquals(expected, actual, "Custom failure message");
assertEquals(expected, actual, () -> "Expensive message computation");
```

## SpringBoot Testing Annotations
`@SpringBootTest` - Starts the Spring Boot application  
`@AutoConfigureMockMvc` - Configures the Spring MVC test framework  
`@WebMvcTest` - Configures the Spring MVC test framework  
`@MockBean` - Mocks a bean  
`@InjectMocks` - Injects mocks into a bean  
`@Mock` - Mocks a field  
`@Spy` - Spys a field  
`@DataJpaTest` - // JPA repository testing  
`@ExtendWith(MockitoExtension.class)` - Mockito extension for JUnit 5    
`@ExtendWith(SpringExtension.class)` - Spring extension for JUnit 5  