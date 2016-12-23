Feature: Searching Google Using Background

  Background: Initial step
    Given I am on Google home page

  @test-tag
  Scenario: The search for Java
    When I enter the keyword of "Java"
    And click the Submit button
    Then the page title returned is "Java - Google Search"

  @test-tag
  Scenario: The search for Sauce Labs
    When I enter the keyword of "Sauce Labs"
    And click the Submit button
    Then the page title returned is "Sauce Labs - Google Search"