import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('')

WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')

long timestamp = System.currentTimeMillis()

Random random = new Random()

String randomPassword = ('Test@' + timestamp) + '!aB'

String randomName = 'Test User ' + timestamp

String existingEmail = 'Lunatest@test.com'

WebUI.setText(findTestObject('Page_Signup/input_name'), randomName)

WebUI.setText(findTestObject('Page_Signup/input_username'), 'testuser_' + System.currentTimeMillis())

WebUI.setText(findTestObject('Page_Signup/input_email'), existingEmail)

WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)

WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), randomPassword)

WebUI.click(findTestObject('Page_Signup/button_Continue'))

// ========== CHECK ERROR MESSAGE ==========
WebUI.waitForElementPresent(findTestObject('Page_Signup/Registration_Message'), 5)

String errorMessage = WebUI.getText(findTestObject('Page_Signup/Registration_Message'))
WebUI.comment('Error message displayed: "' + errorMessage + '"')

// Verify error message contains expected text
boolean isExpectedError = errorMessage.contains('Email already exists') || 
                          errorMessage.contains('Account already registered with this email') ||
                          errorMessage.contains('Too many accounts created') ||
                          errorMessage.contains('There was an error attempting to register your account')

if (isExpectedError) {
    WebUI.comment('PASSED: Correct error message displayed or registration blocked due to account limits')
} else {
    WebUI.comment('FAILED: Expected an existing-email or account-limit registration error, but got: ' + errorMessage)
}

// ========== CHECK USER STAYS ON REGISTER PAGE (NOT REDIRECTED TO LOGIN) ==========
WebUI.delay(2)
String currentUrl = WebUI.getUrl()
boolean isStillOnRegisterPage = currentUrl.contains('/register')

if (isStillOnRegisterPage) {
    WebUI.comment('PASSED: User stays on register page, not redirected to login')
} else {
    WebUI.comment('FAILED: User was redirected to: ' + currentUrl)
}

boolean overallPassed = isExpectedError && isStillOnRegisterPage
assert overallPassed : 'TC03 failed - expected existing email registration error and stay on register page, actual message: ' + errorMessage + ', actual URL: ' + currentUrl + '\n'

WebUI.closeBrowser()