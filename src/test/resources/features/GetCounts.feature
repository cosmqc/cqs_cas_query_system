Feature: The backend for the graphing functionality which gets the counts of each attribute for a chosen category
  Scenario Outline: Get the counts of each attribute for a category
    Given the category is <category>
    And the list of attributes is:
      | <attribute1>     |
      | <attribute2>     |
      | <attribute3>     |
      | <attribute4>     |
    And the database contains the table "countsTest"
    When I get the counts of each attribute
    Then the respective counts should be:
      | <count1> |
      | <count2> |
      | <count3> |
      | <count4> |

    Examples:
      | category          | attribute1         | attribute2           | attribute3  | attribute4     | count1  | count2 | count3 | count4 |
      | "Severity"        | Minor Crash        | Non-Injury Crash     | Fatal Crash | Serious Crash  | 2       | 3      | 0      | 1      |
      | "Participants"    | bicycle            | car or station wagon | pedestrian  | taxi           | 2       | 6      | 0      | 1      |
      | "Obstacles"       | cliff or bank      | body of water        | guard rail  | parked vehicle | 3       | 4      | 0      | 5      |
      | "Light"           | Bright sun         | Overcast             | Dark        | Twilight       | 2       | 3      | 1      | 0      |
      | "Holiday"         | Christmas New Year | Queens Birthday      | Easter      | Labour Weekend | 3       | 0      | 3      | 0      |
      | "Region"          | Hawke's Bay        | Manawatū-Whanganui   | West Coast  | Waikato        | 2       | 1      | 1      | 1      |
      | "Traffic Control" | Stop               | Traffic Signals      | Give way    | Nil            | 2       | 1      | 0      | 3      |
      | "Weather"         | Fine               | Strong wind          | Snow        | Heavy rain     | 1       | 2      | 2      | 1      |
      | "Year"            | 2000               | 2020                 | 2013        | 2004           | 1       | 0      | 2      | 1      |




