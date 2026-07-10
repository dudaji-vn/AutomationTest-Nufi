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

String validEmail = "test@example.com"

WebUI.comment('=== Testing: Leave password empty ===')

// Enter valid email
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), validEmail)

// Leave password empty (do not set text)
WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '')

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))
WebUI.delay(2)

// Check error message
String expectedError = "Password is required"
boolean isErrorDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message_pass', [('text') : expectedError]), 
    5, FailureHandling.OPTIONAL)

// Check still on login page
String currentUrl = WebUI.getUrl()
boolean isStillOnLoginPage = currentUrl.contains('/login') 

boolean isPassed = isErrorDisplayed && isStillOnLoginPage

if (isPassed) {
    WebUI.comment('TC08 PASSED - Empty password was rejected correctly')
    WebUI.takeScreenshot('TC08_Passed.png')
} else if (isErrorDisplayed) {
    WebUI.comment('TC08 FAILED - Error message shown but not on login page: ' + currentUrl)
    WebUI.takeScreenshot('TC08_Failed_UnexpectedUrl.png')
} else if (isStillOnLoginPage) {
    WebUI.comment('TC08 FAILED - No error message shown despite empty password')
    WebUI.takeScreenshot('TC08_Failed_NoError.png')
} else {
    WebUI.comment('TC08 FAILED - Redirected despite empty password: ' + currentUrl)
    WebUI.takeScreenshot('TC08_Failed_Redirect.png')
}
assert isPassed : 'TC08 failed - expected error message and stay on login page, actual URL: ' + currentUrl

WebUI.delay(2)
WebUI.closeBrowser()