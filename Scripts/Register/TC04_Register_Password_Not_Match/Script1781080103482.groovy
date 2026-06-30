import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// ========== GENERATE RANDOM DATA ==========
long timestamp = System.currentTimeMillis()
Random random = new Random()

String randomName = "Test User " + timestamp
String randomUsername = "user_" + timestamp
String randomEmail = "test_" + timestamp + "@example.com"
String randomPassword = "Abc@123456"
String wrongConfirmPassword = "Abc@123457"  // Different from password

WebUI.comment('=== TC04: Registration failed - Password and Confirm do not match ===')
WebUI.comment('Password: ' + randomPassword)
WebUI.comment('Confirm Password: ' + wrongConfirmPassword)

// ========== OPEN BROWSER ==========
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(3)

// ========== CLICK "Sign up" LINK ==========
WebUI.click(findTestObject('Object Repository/Page_Login/a_Sign up'))
WebUI.delay(2)

// ========== FILL REGISTRATION DATA ==========
WebUI.setText(findTestObject('Page_Signup/input_name'), randomName)
WebUI.setText(findTestObject('Page_Signup/input_username'), randomUsername)
WebUI.setText(findTestObject('Page_Signup/input_email'), randomEmail)
WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)
WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), wrongConfirmPassword)

// Take screenshot before submit
WebUI.takeScreenshot('TC04_Before_Submit.png')

// ========== CLICK CONTINUE ==========
//WebUI.click(findTestObject('Page_Signup/button_Continue'))
WebUI.delay(3)

// ========== CHECK ERROR MESSAGE "Passwords do not match" ==========
boolean isErrorDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Signup/Message_error_Confirm_Passwords'), 
    5, FailureHandling.OPTIONAL)

if (isErrorDisplayed) {
    String errorMessage = WebUI.getText(findTestObject('Page_Signup/Message_error_Confirm_Passwords'))
    WebUI.comment('Error message: "' + errorMessage + '"')
    
    if (errorMessage.contains('Passwords do not match') || errorMessage.contains('not match')) {
        WebUI.comment('✓ PASSED: Hiển thị đúng error message')
    } else {
        WebUI.comment('✗ FAILED: Error message sai nội dung')
    }
} else {
    // Try to find a general error message
    boolean generalError = WebUI.verifyElementPresent(
        findTestObject('Page_Signup/Message_error'), 
        3, FailureHandling.OPTIONAL)
    
    if (generalError) {
        String generalMsg = WebUI.getText(findTestObject('Page_Signup/Message_error'))
        WebUI.comment('General error message: "' + generalMsg + '"')
        if (generalMsg.contains('match') || generalMsg.contains('password')) {
            WebUI.comment('✓ PASSED: There are error message liên quan đến password')
        }
    } else {
        WebUI.comment('✗ FAILED: No error message displayed for password mismatch')
    }
}

// ========== CHECK STILL ON REGISTER PAGE ==========
String currentUrl = WebUI.getUrl()
boolean isStillOnRegisterPage = currentUrl.contains('/register') || currentUrl.contains('/signup')

if (isStillOnRegisterPage) {
    WebUI.comment('✓ PASSED: Vẫn ở trang register, không bị redirect')
} else {
    WebUI.comment('✗ FAILED: Đã bị redirect đến: ' + currentUrl)
}

// ========== CHECK NO SUCCESS MESSAGE ==========
boolean hasSuccessMessage = WebUI.verifyElementPresent(
    findTestObject('Page_Signup/Registration_Message'), 
    3, FailureHandling.OPTIONAL)

if (!hasSuccessMessage) {
    WebUI.comment('✓ PASSED: No success message displayed')
} else {
    WebUI.comment('✗ FAILED: Vẫn hiển thị message thành công')
}

// ========== CONCLUSION ==========
boolean overallPassed = isErrorDisplayed && isStillOnRegisterPage && !hasSuccessMessage
if (overallPassed) {
    WebUI.comment('=== TC04 PASSED - Password không khớp bị từ chối đúng cách ===')
    WebUI.takeScreenshot('TC04_Passed.png')
} else {
    WebUI.comment('=== TC04 FAILED ===')
    WebUI.takeScreenshot('TC04_Failed.png')
}
assert overallPassed : 'TC04 failed - expected password mismatch error, stay on register page, no success message. isErrorDisplayed=' + isErrorDisplayed + ', isStillOnRegisterPage=' + isStillOnRegisterPage + ', hasSuccessMessage=' + hasSuccessMessage + ', URL=' + currentUrl + '\n'

WebUI.delay(3)
WebUI.closeBrowser()