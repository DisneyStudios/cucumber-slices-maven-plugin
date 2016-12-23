Feature: Searching Google Using Scenario Outline

  @regression
  Scenario Outline: The search for <keyword>
    Given I am on Google home page
    When I enter the keyword of "<keyword>"
    And click the Submit button
    Then the page title returned is <page title>

    Examples: Searching Google for Disney Stuff
      | keyword      | page title                   |
      | "$Mickey Mouse" | "$Mickey Mouse - Google Search" |
      | Donald Duck  | Donald Duck - Google Search  |
      | Walt Disney  | Walt Disney - Google Search  |

  @smoke
  Scenario Outline: The search for <keyword>
    Given I am on Google home page
    When I enter the keyword of "<keyword>"
    And click the Submit button
    Then the page title returned is "<page title>"

    Examples: Searching Google for Disney Stuff
      | keyword      | page title                   |
      | Goofy        | Goofy - Google Search        |
      | Minnie Mouse | Minnie Mouse - Google Search |
      | Pluto        | Pluto - Google Search        |