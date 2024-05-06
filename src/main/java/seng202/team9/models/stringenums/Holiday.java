package seng202.team9.models.stringenums;

/**
 * An enum that holds all the possible input values for holiday
 *
 * @author ist46
 */
public enum Holiday {

    /**
     * Christmas and New Year holiday.
     */
    CHRISTMAS_NEW_YEAR("Christmas New Year"),

    /**
     * Easter holiday.
     */
    EASTER("Easter"),

    /**
     * Queen's Birthday holiday.
     */
    QUEENS_BIRTHDAY("Queen's Birthday"),

    /**
     * Labour Weekend holiday.
     */
    LABOUR_WEEKEND("Labour Weekend");

    /**
     * The printable form of the holiday name.
     */
    public String printForm;

    /**
     * Constructs a HolidayType enum with the specified print form.
     *
     * @param printForm The printable form of the holiday name.
     */
    Holiday(String printForm) {
        this.printForm = printForm;
    }
}
