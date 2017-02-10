Feature: A Feature File With Tags
  As a casual user
  I want the ability to use Google's search feature
  So that I can retrieve useful and accurate information

  @test-tag1
  Scenario: The search for cheese
    Given I am on Google home page
    When I enter the keyword of "Cheese"
    And click the Submit button
    Then the page title returned is "Cheese - Google Search"

  @test-tag2
  Scenario: The search for star wars
    Given I am on Google home page
    When I enter the keyword of "Star Wars"
    And click the Submit button
    Then the page title returned is "Star Wars - Google Search"

  Scenario: The search for avengers
    Given I am on Google home page
    When I enter the keyword of "Avengers Age of Ultron"
    And click the Submit button
    Then the page title returned is "Avengers Age of Ultron - Google Search"

  @test-tag2
  Scenario: The search for Mickey Mouse
    Given I am on Google home page
    When I enter the keyword of "Mickey Mouse"
    And click the Submit button
    Then the page title returned is "Mickey Mouse - Google Search"

  @test-tag1 @test-tag2
  Scenario: The search for Donald Duck
    Given I am on Google home page
    When I enter the keyword of "Donald Duck"
    And click the Submit button
    Then the page title returned is "Donald Duck - Google Search"