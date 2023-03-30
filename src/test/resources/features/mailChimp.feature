Feature: Mailchimp

  Scenario Outline: Register user
    Given I have opened "<browser>"
    Given I enter the email "<email>"
    Given My random username is <usernameLength>
    Given I enter the password "<password>"
    When I click the signup button
    Then I want the creation of the account to "<created>"
    Examples:
      | browser | email               | usernameLength | password   | created |
      | chrome  | ellinor86@gurka.com | 9              | Ellinor12! | succeed |
      | edge    | ellinor86@gurka.com | 101            | Ellinor12! | fail    |
      | chrome  | ellinor86@gurka.com | 1              | Ellinor12! | fail    |
      | edge    |                     | 10             | Ellinor12! | fail    |
    