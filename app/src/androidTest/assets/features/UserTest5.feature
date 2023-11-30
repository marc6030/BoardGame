Feature: Add user rating

  Scenario: User wants to rate a game
    Given The user is logged in and has selected a game
    When the user adds a rating to a game
    Then the users rating for that game should change