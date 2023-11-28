

Feature: Search for Board Games

  Scenario: The user searches for Monopoly
    Given the board game search functionality is available
    When the user submits a search for "Monopoly"
    Then the search results should include "Monopoly" among the returned games

