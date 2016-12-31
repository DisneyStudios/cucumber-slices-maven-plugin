Feature: A Feature File With Tags
  As a casual user
  I want the ability to use Google's search feature
  So that I can retrieve useful and accurate information

  @test-tag
  Scenario: The search for cheese
    Given I am on Google home page
    When I enter the keyword of "Cheese"
    And click the Submit button
    Then the page title returned is "Cheese - Google Search"