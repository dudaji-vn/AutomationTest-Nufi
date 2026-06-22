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

// Leave Full name blank
WebUI.setText(findTestObject('Page_Signup/input_name'), '')

// Enter valid Email
long timestamp = System.currentTimeMillis()
String randomEmail = "test_" + timestamp + "@example.com"
WebUI.setText(findTestObject('Page_Signup/input_email'), randomEmail)

// Enter valid Password
String randomPassword = "Test@123456"
WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)

// Enter same Password in Confirm password
WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), randomPassword)

// Click "Continue" button
WebUI.click(findTestObject('Page_Signup/button_Continue'))
WebUI.delay(2)

// Check error message: "Full name is required"
WebUI.waitForElementPresent(findTestObject('Object Repository/Page_Signup/Message_error/Message_error_Name'), 5)
String errorMessage = WebUI.getText(findTestObject('Object Repository/Page_Signup/Message_error/Message_error_Name'))
WebUI.comment('Error message: "' + errorMessage + '"')

boolean isExpectedError = errorMessage.contains('Full name is required') || 
                          errorMessage.contains('Name is required') ||
                          errorMessage.toLowerCase().contains('name is required')

if (isExpectedError) {
    WebUI.comment('PASSED: Correct error message displayed')
} else {
    WebUI.comment('FAILED: Expected "Full name is required", but got: ' + errorMessage)
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
assert overallPassed : 'TC05 failed - expected full name required error and stay on register page, actual message: ' + errorMessage + ', actual URL: ' + currentUrl + '\n'

WebUI.delay(2)
WebUI.closeBrowser()