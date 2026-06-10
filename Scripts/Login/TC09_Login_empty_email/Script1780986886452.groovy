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

WebUI.comment('=== Đang test: Để trống email ===')

// Để trống email (không set text)
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), '')

// Nhập password bất kỳ
WebUI.setText(findTestObject('Page_Login/input_Signin_password'), anyPassword)

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))
WebUI.delay(2)

// Kiểm tra error message
String expectedError = "Email is required"
boolean isErrorDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message_email', [('text') : expectedError]), 
    5, FailureHandling.OPTIONAL)

// Kiểm tra vẫn ở trang login
String currentUrl = WebUI.getUrl()
boolean isStillOnLoginPage = currentUrl.contains('/login') || 
    (currentUrl.contains('chat.nufi.me') && !currentUrl.contains('/c/new'))

if (isErrorDisplayed && isStillOnLoginPage) {
    WebUI.comment('TC09 PASSED - Để trống email bị từ chối đúng cách')
    WebUI.takeScreenshot('TC09_Passed.png')
} else if (isErrorDisplayed) {
    WebUI.comment('TC09 PASSED - Hiển thị error message')
} else if (isStillOnLoginPage) {
    WebUI.comment('TC09 FAILED - Không hiển thị error message dù để trống email')
    WebUI.takeScreenshot('TC09_Failed_NoError.png')
} else {
    WebUI.comment('TC09 FAILED - Redirect dù để trống email: ' + currentUrl)
    WebUI.takeScreenshot('TC09_Failed_Redirect.png')
}

WebUI.delay(2)
WebUI.closeBrowser()