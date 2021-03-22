#Author: suresh.madhasu@cigna.com
#Keywords Summary :
#Feature: List of scenarios
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios

@PARSANPAPI
Feature: User posts and gets response using REST API web services

  Scenario Outline: Price and Validate PAR SANP Claims
    Given User posts the xml Request "<request>" to the SANP restapi endpoint
    Then User validates the response for given xml payload

    Examples: 
      | request |
#@DataFeedPlaceHolder

