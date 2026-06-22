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

// Enter valid Email
String randomEmail = "test_" + System.currentTimeMillis() + "@example.com"
WebUI.setText(findTestObject('Page_Signup/input_email'), randomEmail)

// Leave Password blank
WebUI.setText(findTestObject('Page_Signup/input_password'), '')

// Leave Confirm password blank
WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), '')

// Click "Continue" button
WebUI.click(findTestObject('Page_Signup/button_Continue'))
WebUI.delay(2)

// Check error message: "Password is required"
WebUI.waitForElementPresent(findTestObject('Object Repository/Page_Signup/Message_error/Message_error_Password'), 5)
String errorMessage = WebUI.getText(findTestObject('Object Repository/Page_Signup/Message_error/Message_error_Password'))
WebUI.comment('Error message: "' + errorMessage + '"')

boolean isExpectedError = errorMessage.contains('Password is required') || 
                          errorMessage.toLowerCase().contains('password is required') ||
                          errorMessage.contains('required')

if (isExpectedError) {
    WebUI.comment('PASSED: Correct error message displayed for missing password')
} else {
    WebUI.comment('FAILED: Expected "Password is required", but got: ' + errorMessage)
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
assert overallPassed : 'TC08 failed - expected missing password error and stay on register page, actual message: ' + errorMessage + ', actual URL: ' + currentUrl + '\n'

WebUI.delay(2)
WebUI.closeBrowser()