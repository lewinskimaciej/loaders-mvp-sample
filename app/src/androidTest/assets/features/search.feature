Feature: Search
  Scenario: User searches for Pokemon by it's ID

    When I add text "1" to "number_input"
      And I click "button"
      And I wait for "5" seconds
    Then I should see view with id "pokemon_container"
