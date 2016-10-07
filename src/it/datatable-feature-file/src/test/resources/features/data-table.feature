Feature: Searching Google Using Data Table

  @regression
  Scenario: The search for star wars
    Given I am on Google home page
    When I enter the keyword of "Star Wars"
    And click the Submit button
    Then the following page elements shall be displayed
      | News   |
      | Images |
      | Videos |