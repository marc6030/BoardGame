Feature: Trending games

  Scenario: User wants to see the trending board games
    Given the user is on the front page
    When the user selects a game from trending section
    Then the game details are obtained