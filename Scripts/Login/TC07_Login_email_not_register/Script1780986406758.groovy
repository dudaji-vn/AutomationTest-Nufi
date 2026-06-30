import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(3)

// Email not registered before
String unregisteredEmail = "nonexistent_" + System.currentTimeMillis() + "@example.com"
String anyPassword = "Test@123456"  // Password has at least 8 characters, valid format

WebUI.comment('=== Testing with unregistered email: ' + unregisteredEmail + ' ===')

// Enter unregistered email
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), unregisteredEmail)

// Enter password (any, at least 8 characters)
WebUI.setText(findTestObject('Page_Login/input_Signin_password'), anyPassword)

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))
WebUI.delay(3)

// Check error message
String expectedError = "Unable to login with the information provided. Please check your credentials and try again."
boolean isErrorDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message', [('text') : expectedError]), 
    8, FailureHandling.OPTIONAL)

// Check still on login page (not redirected)
String currentUrl = WebUI.getUrl()
boolean isStillOnLoginPage = currentUrl.contains('/login') 

boolean isPassed = isErrorDisplayed && isStillOnLoginPage

if (isPassed) {
    WebUI.comment('TC07 PASSED - Unregistered email was rejected correctly')
    WebUI.takeScreenshot('TC07_Passed.png')
} else if (isErrorDisplayed) {
    WebUI.comment('TC07 FAILED - Error message shown but not on login page: ' + currentUrl)
    WebUI.takeScreenshot('TC07_Failed_UnexpectedUrl.png')
} else if (isStillOnLoginPage) {
    WebUI.comment('TC07 FAILED - No error message shown while still on login page')
    WebUI.takeScreenshot('TC07_Failed_NoError.png')
} else {
    WebUI.comment('TC07 FAILED - Redirected to: ' + currentUrl)
    WebUI.takeScreenshot('TC07_Failed_Redirect.png')
}
assert isPassed : 'TC07 failed - expected error message and stay on login page, actual URL: ' + currentUrl

WebUI.delay(2)
WebUI.closeBrowser()