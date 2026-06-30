import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// List of invalid emails to test
List<String> invalidEmails = ['test@', 'test.com', 'test@com', 'test@@example.com']

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(3)

for (String invalidEmail in invalidEmails) {
    WebUI.comment('=== Testing with email: ' + invalidEmail + ' ===')
    
    // Enter invalid email
    WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), invalidEmail)
    
    // Enter any password (non-empty)
    WebUI.setText(findTestObject('Page_Login/input_Signin_password'), 'anypassword123')
    
    // Click Continue
    WebUI.click(findTestObject('Page_Login/button_Continue'))
    WebUI.delay(2)
    
    // Check error message
    String expectedError = "You must enter a valid email address"
    boolean isErrorDisplayed = WebUI.verifyElementPresent(
        findTestObject('Page_Login/error_Message_email', [('text') : expectedError]), 
        5, FailureHandling.OPTIONAL)
    
    if (isErrorDisplayed) {
        WebUI.comment('TC05 PASSED - Email: ' + invalidEmail + ' - Correct error message displayed')
    } else {
        WebUI.comment('TC05 FAILED - Email: ' + invalidEmail + ' - No error message displayed')
        WebUI.takeScreenshot('TC05_Failed_' + invalidEmail.replace('@', '_') + '.png')
        assert isErrorDisplayed : 'TC05 failed - Email: ' + invalidEmail + ' - No error message displayed'
    }
    
    // Reload the page before testing the next email (avoid redirect issues)
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
    WebUI.delay(2)
}

WebUI.delay(2)
WebUI.closeBrowser()