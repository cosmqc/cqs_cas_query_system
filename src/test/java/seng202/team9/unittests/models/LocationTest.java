package seng202.team9.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team9.models.Attribute;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Location;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains JUnit test cases for the Location class
 */
public class LocationTest {

    private Location location;
    private HashMap<AttributeType, Attribute> attributes;

    /**
     * makes a new hash map before every test
     */
    @BeforeEach
    public void setUp() {
        attributes = new HashMap<>();
    }

    /**
     * Test case for checking if the `getLocationString` method returns "Unknown"
     * when all fields are empty.
     */
    @Test
    public void testGetLocationString_AllFieldsEmpty() {
        attributes.put(AttributeType.CRASHLOCATIONONE, new Attribute(AttributeType.CRASHLOCATIONONE, ""));
        attributes.put(AttributeType.CRASHLOCATIONTWO, new Attribute(AttributeType.CRASHLOCATIONTWO, ""));
        attributes.put(AttributeType.TLANAME, new Attribute(AttributeType.TLANAME, ""));

        location = new Location(attributes);
        assertEquals("Unknown", location.getLocationString());
    }

    /**
     * Test case for checking if the `getLocationString` method returns only the first
     * field when only that is populated.
     */
    @Test
    public void testGetLocationString_OnlyFirstField() {
        attributes.put(AttributeType.CRASHLOCATIONONE, new Attribute(AttributeType.CRASHLOCATIONONE, "Street1"));
        attributes.put(AttributeType.CRASHLOCATIONTWO, new Attribute(AttributeType.CRASHLOCATIONTWO, ""));
        attributes.put(AttributeType.TLANAME, new Attribute(AttributeType.TLANAME, ""));

        location = new Location(attributes);
        assertEquals("Street1", location.getLocationString());
    }

    /**
     * Test case for checking if the `getLocationString` method returns only the second
     * field when only that is populated.
     */
    @Test
    public void testGetLocationString_OnlySecondField() {
        attributes.put(AttributeType.CRASHLOCATIONONE, new Attribute(AttributeType.CRASHLOCATIONONE, ""));
        attributes.put(AttributeType.CRASHLOCATIONTWO, new Attribute(AttributeType.CRASHLOCATIONTWO, "Street2"));
        attributes.put(AttributeType.TLANAME, new Attribute(AttributeType.TLANAME, ""));

        location = new Location(attributes);
        assertEquals(", Street2", location.getLocationString());
    }

    /**
     * Test case for checking if the `getLocationString` method returns the concatenated
     * first and second fields when both are populated.
     */
    @Test
    public void testGetLocationString_FirstAndSecondField() {
        attributes.put(AttributeType.CRASHLOCATIONONE, new Attribute(AttributeType.CRASHLOCATIONONE, "Street1"));
        attributes.put(AttributeType.CRASHLOCATIONTWO, new Attribute(AttributeType.CRASHLOCATIONTWO, "Street2"));
        attributes.put(AttributeType.TLANAME, new Attribute(AttributeType.TLANAME, ""));

        location = new Location(attributes);
        assertEquals("Street1, Street2", location.getLocationString());
    }

    /**
     * Test case for checking if the `getLocationString` method returns the concatenated
     * first and second fields when both are populated.
     */
    @Test
    public void testGetLocationString_OnlyThirdField() {
        attributes.put(AttributeType.CRASHLOCATIONONE, new Attribute(AttributeType.CRASHLOCATIONONE, ""));
        attributes.put(AttributeType.CRASHLOCATIONTWO, new Attribute(AttributeType.CRASHLOCATIONTWO, ""));
        attributes.put(AttributeType.TLANAME, new Attribute(AttributeType.TLANAME, "Territory1"));

        location = new Location(attributes);
        assertEquals(" (Territory1)", location.getLocationString());
    }

    /**
     * Test case for checking if the `getLocationString` method returns the full location
     * string when all fields are populated.
     */
    @Test
    public void testGetLocationString_AllFields() {
        attributes.put(AttributeType.CRASHLOCATIONONE, new Attribute(AttributeType.CRASHLOCATIONONE, "Street1"));
        attributes.put(AttributeType.CRASHLOCATIONTWO, new Attribute(AttributeType.CRASHLOCATIONTWO, "Street2"));
        attributes.put(AttributeType.TLANAME, new Attribute(AttributeType.TLANAME, "Territory1"));

        location = new Location(attributes);
        assertEquals("Street1, Street2 (Territory1)", location.getLocationString());
    }
}
