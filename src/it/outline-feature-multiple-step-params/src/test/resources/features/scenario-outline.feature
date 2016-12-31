Feature: Math

  @regression
  Scenario Outline: Adding 2 numbers
    Given I want to add 2 numbers
    When I enter <first number> + <second number>
    Then the result is 5

    Examples: Searching Google for Disney Stuff
      | first number | second number |
      | 2            | 3             |
