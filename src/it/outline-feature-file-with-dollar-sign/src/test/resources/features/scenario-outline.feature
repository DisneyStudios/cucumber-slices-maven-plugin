Feature: Searching Google Using Scenario Outline With An Embedded Dollar Sign In Examples

  @regression
  Scenario Outline: The search for <keyword>
    Given I am on Google home page
    When I enter the keyword of <keyword>
    And click the Submit button
    Then the page title returned is <page title>

    Examples: Searching Google for Disney Stuff
      | keyword         | page title                      |
      | "$Mickey Mouse" | "$Mickey Mouse - Google Search" |
      | Donald Duck     | Donald Duck - Google Search     |
      | Walt Disney     | Walt Disney - Google Search     |