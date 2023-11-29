Feature: See Board Game Details

  Scenario: User wants to see the details of Anima
    Given the game is in the search results
    When the user selects Anima
    Then the details of the game should be accessible