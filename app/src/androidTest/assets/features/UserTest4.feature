Feature: Add to favorites

  Scenario: User wants to add a game to favorites list
    Given The user is logged in
    When the user adds a game to favorites
    Then the game should appear in the users favorites list