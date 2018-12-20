import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.jupiter.api.condition.JRE.*;
import static org.junit.jupiter.api.condition.OS.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FirstTest {

    public FirstTest() {
        System.out.println("==========> Constructor");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("==========> Before All");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("==========> Before Each");
    }

    @Test
    @DisplayName("assertEquals")
    void firstTest() {
        System.out.println("First");
        final String one = "first";
        final String two = "first";
        assertEquals(one, two);
    }

    @Test
    @DisplayName("assertEquals and a message in case of failure")
    @Disabled("assertEquals --disabled-- on purpose")
    void secondTest() {
        System.out.println("First");
        final String one = "first";
        final String two = "first2";
        assertEquals(one, two, () -> "The content of the strings should have been equal!");
    }

    @Test
    @DisplayName("assertThrows")
    void testThrowWithLambdaAndException() {
        assertThrows(Exception.class, () -> {
            throw new Exception("Exception!");
        });
    }

    @Test
    @DisplayName("assertThrows and Executable")
    void exceptionTesting() {
        Exception ex = assertThrows(Exception.class, this::throwAnException);
        assertEquals("Parsing problem!", ex.getMessage());
    }

    @Test
    @Disabled("Test disabled")
    @DisplayName("Test disabled --disabled-- on purpose")
    void disabledTest() {
        //bla bla
    }

    @Test
    @DisplayName("assertAll grouped assertions")
    void groupedAssertions() {
        Person person = new Person("John", "Smith", LocalDate.now());
        // In a grouped assertion all assertions are executed, and any
        // failures will be reported together.
        assertAll("person",
                () -> assertEquals("John", person.getFirst()),
                //() -> assertEquals("Doe", person.getLast())
                () -> assertEquals("Smith", person.getLast())
        );
    }

    @Test
    @DisplayName("assertAll dependent/nested assertions")
    void dependentAssertions() {
        Person person = new Person("John", "Smith", LocalDate.now());
        //Person person = new Person(null, "Smith", LocalDate.now());
        // Within a code block, if an assertion fails the
        // subsequent code in the same block will be skipped.
        assertAll("properties",
                () -> {
                    String firstName = person.getFirst();
                    assertNotNull(firstName);

                    // Executed only if the previous assertion is valid.
                    assertAll("first name",
                            () -> assertTrue(firstName.startsWith("J")),
                            () -> assertTrue(firstName.endsWith("n"))
                    );
                },
                () -> {
                    // Grouped assertion, so processed independently
                    // of results of first name assertions.
                    String lastName = person.getLast();
                    assertNotNull(lastName);

                    // Executed only if the previous assertion is valid.
                    assertAll("last name",
                            () -> assertTrue(lastName.startsWith("S")),
                            () -> assertTrue(lastName.endsWith("h"))
                    );
                }
        );
    }

    @Test
    @DisplayName("assertTimeout")
    void timeoutNotExceeded() {
        // The following assertion succeeds.
        assertTimeout(Duration.ofMinutes(2), () -> {
            // Perform task that takes less than 2 minutes.
        });
    }

    @Test
    @DisplayName("assertTimeout with result")
    void timeoutNotExceededWithResult() {
        // The following assertion succeeds, and returns the supplied object.
        String actualResult = assertTimeout(Duration.ofMinutes(2), () -> {
            return "a result";
        });
        assertEquals("a result", actualResult);
    }

    @Test
    @DisplayName("assertTimeout with method call")
    void timeoutNotExceededWithMethod() {
        // The following assertion invokes a method reference and returns an object.
        String actualGreeting = assertTimeout(Duration.ofMinutes(2), this::greeting);
        assertEquals("Hello, World!", actualGreeting);
    }

    @Test
    @DisplayName("assertTimeout exceeded")
    @Disabled("assertTimeout exceeded --disabled-- on purpose")
    void timeoutExceeded() {
        // The following assertion fails with an error message similar to:
        // execution exceeded timeout of 10 ms by 91 ms
        assertTimeout(Duration.ofMillis(10), () -> {
            // Simulate task that takes more than 10 ms.
            Thread.sleep(100);
        });
    }

    @Test
    @DisplayName("assertTimeoutPreemptively exceeded")
    @Disabled("assertTimeoutPreemptively exceeded --disabled-- on purpose")
    void timeoutExceededWithPreemptiveTermination() {
        // The following assertion fails with an error message similar to:
        // execution timed out after 10 ms
        assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
            // Simulate task that takes more than 10 ms.
            Thread.sleep(100);
        });
    }

    @Test
    @DisplayName("assumeTrue")
    void testOnlyOnCiServer() {
        assumeTrue("CI".equals(System.getenv("ENV")));
        // remainder of test
    }

    @Test
    @DisplayName("assumeTrue with message")
    void testOnlyOnDeveloperWorkstation() {
        assumeTrue("DEV".equals(System.getenv("ENV")),
                () -> "Aborting test: not on developer workstation");
        // remainder of test
    }

    @Test
    @DisplayName("assumeThat and assertEquals")
    void testInAllEnvironments() {
        assumingThat("CI".equals(System.getenv("ENV")),
                () -> {
                    // perform these assertions only on the CI server
                    assertEquals(2, 2);
                });

        // perform these assertions in all environments
        assertEquals("a string", "a string");
    }

    private String greeting() {
        return "Hello, World!";
    }


    private void throwAnException() throws Exception {
        String[] strings = "".split(" ");
        if (strings.length != 2) {
            throw new Exception("Parsing problem!");
        }
    }

}
