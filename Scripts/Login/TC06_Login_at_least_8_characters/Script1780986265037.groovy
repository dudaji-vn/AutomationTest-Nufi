import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// List of passwords shorter than 8 characters (each case < 8 chars)
List<String> shortPasswords = ['Abc@12', '123', 'a', 'P@ss', '1234567']

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(3)

// Valid email but does not need to exist in the system because validation happens client-side
String validEmail = "test@example.com"

for (String shortPassword in shortPasswords) {
    WebUI.comment('=== Đang test với password: "' + shortPassword + '" (độ dài: ' + shortPassword.length() + ') ===')
    
    // Enter a valid email
    WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), validEmail)
    
    // Enter short password
    WebUI.setText(findTestObject('Page_Login/input_Signin_password'), shortPassword)
    
    // Click Continue
    WebUI.click(findTestObject('Page_Login/button_Continue'))
    WebUI.delay(2)
    
    // Check error message
    String expectedError = "Password must be at least 8 characters"
    boolean isErrorDisplayed = WebUI.verifyElementPresent(
        findTestObject('Page_Login/error_Message_pass', [('text') : expectedError]), 
        5, FailureHandling.OPTIONAL)
    
    if (isErrorDisplayed) {
        WebUI.comment('TC06 PASSED - Password: "' + shortPassword + '" - Hiển thị đúng error message')
    } else {
        WebUI.comment('TC06 FAILED - Password: "' + shortPassword + '" - Không hiển thị error message')
        WebUI.takeScreenshot('TC06_Failed_pw_' + shortPassword + '.png')
        assert isErrorDisplayed : 'TC06 failed - Password: "' + shortPassword + '" - Không hiển thị error message'
    }
    
    // Clear form before next test (do not reload to keep email)
    WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '')
    WebUI.delay(1)
}

WebUI.delay(2)
WebUI.closeBrowser()