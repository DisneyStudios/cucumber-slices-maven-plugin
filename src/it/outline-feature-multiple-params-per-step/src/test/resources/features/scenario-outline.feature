Feature: Addition

  @regression
  Scenario Outline: Adding 2 numbers
    Given I have 2 numbers
    When I enter <first number> + <second number>
    Then the result is <third number>

    Examples: Numbers
      | first number | second number | third number |
      | 2            | 3             | 5            |

  @regression
  Scenario Outline: Adding 3 numbers
    Given I have 3 numbers
    When I enter <first number> + <second number> + <third number>
    Then the result is <fourth number>

    Examples: Numbers
      | first number | second number | third number | fourth number |
      | 2            | 3             | 5            | 10            |