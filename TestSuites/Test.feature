@API @VinTicket_API_UAT @GetOrder
Feature: Get order

  @GetOrder_S1
  Scenario: Get order
    Given I login to app with phone number 0111555123 and  otp 123456
    And I get an order
    Then I verify response data






