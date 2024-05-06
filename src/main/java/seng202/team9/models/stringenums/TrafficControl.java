package seng202.team9.models.stringenums;

/**
 * An enum that holds all the relevant input values for traffic control
 *
 * @author ist46
 */
public enum TrafficControl {
    /** Give way sign */
    GIVE_WAY("Give way"),

    /** Grouping of various traffic signals */
    TRAFFIC_SIGNALS("Traffic Signals"),

    /** Stop sign */
    STOP("Stop");

    /** String containing the expected value for the TrafficControl field */
    public final String printForm;

    TrafficControl(String printForm) {
        this.printForm = printForm;
    }
}
