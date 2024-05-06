package seng202.team9.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.models.Attribute;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.models.QueryCriterion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that interacts with the database to execute the built query
 *
 * @author ist46
 */
public class Query {

    private static final Logger log = LogManager.getLogger(Query.class);
    private final DatabaseManager databaseManager;

    /**
     * The list of QueryCriterion built up
     */
    private final List<QueryCriterion> criteria;

    /**
     * Creates a Query, a container for a list of individual QueryCriterion comparisons.
     * @param criteria List of QueryCriterion objects that are summed to create the query
     */
    public Query(List<QueryCriterion> criteria) {
        this.criteria = criteria;
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Combines all the strings from the queries to form the where clause for the query
     *
     * @return the where clause for the sql statement
     */
    @Override
    public String toString() {
        String string = "";
        int index = 1;
        for (QueryCriterion criterion : criteria) {
            string += criterion.toString();
            if(index != criteria.size()) {
                string += " AND ";
            }
            index++;
        }
        return string;
    }

    /**
     * Generates an SQL script based on the queries chosen by the user and fetches the data
     * @param tableName table to be included in the SQL script
     * @return the list of crashes from the query
     */
    public List<Crash> generateSQL(String tableName) {
        List<Crash> crashes = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE " + this + ";";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ArrayList<Attribute> data = new ArrayList<>();
                AttributeType[] enumValues = AttributeType.values();
                boolean isCustom = rs.getString(1).charAt(0) == 'c';
                if (isCustom) {
                    data.add(new Attribute(AttributeType.OBJECTID, rs.getString(1).substring(1)));
                } else {
                    data.add(new Attribute(AttributeType.OBJECTID, rs.getString(1)));
                }
                for (int i = 1; i < enumValues.length; i++) {
                    data.add(new Attribute(enumValues[i], rs.getString(i+1)));
                }
                crashes.add(new Crash(data, isCustom));
            }
        } catch (SQLException e) {
            log.error("There was an error executing the query from the DB");
            log.error(e.getMessage());
        }
        return crashes;
    }


}
