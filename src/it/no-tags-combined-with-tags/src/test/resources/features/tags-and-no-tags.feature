Feature: A Feature File Without Tags
  As a casual user
  I want the ability to use Google's search feature
  So that I can retrieve useful and accurate information

  Scenario: The search for cheese
    Given I am on Google home page
    When I enter the keyword of "Cheese"
    And click the Submit button
    Then the page title returned is "Cheese - Google Search"

  Scenario: The search for star wars
    Given I am on Google home page
    When I enter the keyword of "Star Wars"
    And click the Submit button
    Then the page title returned is "Star Wars - Google Search"

  @test-tag
  Scenario: The search for avengers
    Given I am on Google home page
    When I enter the keyword of "Avengers Age of Ultron"
    And click the Submit button
    Then the page title returned is "Avengers Age of Ultron - Google Search"
