import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(2)

// Click on "Sign up" link
WebUI.click(findTestObject('Object Repository/Page_Login/a_Sign up'))
WebUI.delay(2)

// Enter valid Full name
String randomName = "Test User " + System.currentTimeMillis()
WebUI.setText(findTestObject('Page_Signup/input_name'), randomName)

// Enter invalid email (e.g., test@)
String invalidEmail = "test@"
WebUI.setText(findTestObject('Page_Signup/input_email'), invalidEmail)

// Enter valid Password
String randomPassword = "Test@123456"
WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)

// Enter same Password in Confirm password
WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), randomPassword)

// Click "Continue" button
//WebUI.click(findTestObject('Page_Signup/button_Continue'))
//WebUI.delay(2)

// Check error message: "Please enter a valid email address"
WebUI.waitForElementPresent(findTestObject('Object Repository/Page_Signup/Message_error/Message_error_Email'), 5)
String errorMessage = WebUI.getText(findTestObject('Object Repository/Page_Signup/Message_error/Message_error_Email'))
WebUI.comment('Error message: "' + errorMessage + '"')

boolean isExpectedError = errorMessage.contains('valid email address') || 
                          errorMessage.toLowerCase().contains('valid email') ||
                          errorMessage.contains('You must enter a valid email address')

if (isExpectedError) {
    WebUI.comment('PASSED: Correct error message displayed for invalid email: ' + invalidEmail)
} else {
    WebUI.comment('FAILED: Expected "Please enter a valid email address", but got: ' + errorMessage)
}

// Check user stays on register page (no redirect to login)
String currentUrl = WebUI.getUrl()
boolean isStillOnRegisterPage = currentUrl.contains('/register')

if (isStillOnRegisterPage) {
    WebUI.comment('PASSED: User stays on register page, not redirected to login')
} else {
    WebUI.comment('FAILED: User was redirected to: ' + currentUrl)
}

boolean overallPassed = isExpectedError && isStillOnRegisterPage
assert overallPassed : 'TC06 failed - expected invalid email error and stay on register page, actual message: ' + errorMessage + ', actual URL: ' + currentUrl + '\n'

WebUI.delay(2)
WebUI.closeBrowser()