

Feature: Can the user search for a specific board game?
  Scenario: The user searches for monopoly
    Given The user is on the search page
    When The user enters "monopoly" into the search bar
    Then The user should be able to find all monopoly games