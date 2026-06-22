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

String anyPassword = "Test@123456"

WebUI.comment('=== Testing: Leave email empty ===')

// Leave email empty (do not set text)
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), '')

// Enter any password
WebUI.setText(findTestObject('Page_Login/input_Signin_password'), anyPassword)

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))
WebUI.delay(2)

// Check error message
String expectedError = "Email is required"
boolean isErrorDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message_email', [('text') : expectedError]), 
    5, FailureHandling.OPTIONAL)

// Check still on login page
String currentUrl = WebUI.getUrl()
boolean isStillOnLoginPage = currentUrl.contains('/login') || 
    (currentUrl.contains('chat.nufi.me') && !currentUrl.contains('/c/new'))

boolean isPassed = isErrorDisplayed && isStillOnLoginPage

if (isPassed) {
    WebUI.comment('TC09 PASSED - Để trống email bị từ chối đúng cách')
    WebUI.takeScreenshot('TC09_Passed.png')
} else if (isErrorDisplayed) {
    WebUI.comment('TC09 FAILED - Hiển thị error message nhưng không ở trang login: ' + currentUrl)
    WebUI.takeScreenshot('TC09_Failed_UnexpectedUrl.png')
} else if (isStillOnLoginPage) {
    WebUI.comment('TC09 FAILED - Không hiển thị error message dù để trống email')
    WebUI.takeScreenshot('TC09_Failed_NoError.png')
} else {
    WebUI.comment('TC09 FAILED - Redirect dù để trống email: ' + currentUrl)
    WebUI.takeScreenshot('TC09_Failed_Redirect.png')
}
assert isPassed : 'TC09 failed - expected error message and stay on login page, actual URL: ' + currentUrl

WebUI.delay(2)
WebUI.closeBrowser()