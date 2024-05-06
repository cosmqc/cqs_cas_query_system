package seng202.team9.models.stringenums;

/**
 * An enum that holds all the possible input values for region
 *
 * @author ist46
 */
public enum Region {
    /** Region representing Auckland */
    AUCKLAND("Auckland"),

    /** Region representing Waikato */
    WAIKATO("Waikato"),

    /** Region representing Canterbury */
    CANTERBURY("Canterbury"),

    /** Region representing Wellington */
    WELLINGTON("Wellington"),

    /** Region representing Bay of Plenty */
    BAY_OF_PLENTY("Bay of Plenty"),

    /** Region representing Manawatū-Whanganui */
    MANAWATU_WHANGANUI("Manawatū-Whanganui"),

    /** Region representing Otago */
    OTAGO("Otago"),

    /** Region representing Northland */
    NORTHLAND("Northland"),

    /** Region representing Hawke's Bay */
    HAWKES_BAY("Hawke's Bay"),

    /** Region representing Southland */
    SOUTHLAND("Southland"),

    /** Region representing Taranaki */
    TARANAKI("Taranaki"),

    /** Region representing Gisbourne */
    GISBORNE("Gisborne"),

    /** Region representing Marlborough */
    MARLBOROUGH("Marlborough"),

    /** Region representing Nelson */
    NELSON("Nelson"),

    /** Region representing Tasman */
    TASMAN("Tasman"),

    /** Region representing West Coast */
    WEST_COAST("West Coast");

    /** String containing the expected database value of each Region value */
    public final String printForm;

    Region(String printForm) {
        this.printForm = printForm;
    }
}
