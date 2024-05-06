Feature: Load CAS Data from file
  Scenario Outline: Load to existing table
    Given An empty table exists
    And The file <file> exists
    When The file is uploaded
    Then The table contains <numLoaded> files

    Examples:
      |                  file | numLoaded |
      |        "full_2_2.csv" |         2 |
      |        "full_1_2.csv" |         1 |
      |        "full_0_2.csv" |         0 |
      | "missing-col_1_2.csv" |         1 |
      | "missing-col_0_2.csv" |         0 |
      |   "extra-col_1_2.csv" |         1 |
      |   "extra-col_0_2.csv" |         0 |