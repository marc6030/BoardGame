

Feature: Search for boardgames
  Scenario: The user searches for monopoly
    Given The user searches for a board game
    When The users enters his search
    Then The search results is returned
