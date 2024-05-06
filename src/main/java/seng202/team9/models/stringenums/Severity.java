package seng202.team9.models.stringenums;

/**
 * An enum that holds the possible input values for crash severity.
 * This is determined by the worst injury sustained in the crash at the time of entry.
 * Ordered by severity so can do comparisons between severity values if needed.
 * @author ist46
 */
public enum Severity {
    /** Value representing a crash where no-one was injured */
    NON_INJURY_CRASH("Non-Injury Crash"),

    /** Value representing a crash where there were only minor injuries */
    MINOR_CRASH("Minor Crash"),

    /** Value representing a crash where there were serious injuries */
    SERIOUS_CRASH("Serious Crash"),

    /** Value representing a crash where someone died */
    FATAL_CRASH("Fatal Crash");


    /** String containing the expected database value of each Severity value */
    public String printForm;

    Severity(String printForm) {
        this.printForm = printForm;
    }
}
