Feature: Editing a crash entry currently in a table
  Scenario Outline: Attempt to edit a ID to and already existing ID
    Given We have a table with an existing crash with an ID of <ID1>
    When We update a different crash in the table to <ID1>
    Then The edit modal rejects the change and the single entry <ID1> remains


    Examples:
      |  ID1 |
      |  "100" |
      |  "1"   |

