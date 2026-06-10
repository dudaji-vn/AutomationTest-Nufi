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

WebUI.comment('=== Đang test: Để trống password ===')

// Nhập email hợp lệ
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), validEmail)

// Để trống password (không set text)
WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '')

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))
WebUI.delay(2)

// Kiểm tra error message
String expectedError = "Password is required"
boolean isErrorDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message_pass', [('text') : expectedError]), 
    5, FailureHandling.OPTIONAL)

// Kiểm tra vẫn ở trang login
String currentUrl = WebUI.getUrl()
boolean isStillOnLoginPage = currentUrl.contains('/login') 

if (isErrorDisplayed && isStillOnLoginPage) {
    WebUI.comment('TC08 PASSED - Để trống password bị từ chối đúng cách')
    WebUI.takeScreenshot('TC08_Passed.png')
} else if (isErrorDisplayed) {
    WebUI.comment('TC08 PASSED - Hiển thị error message')
} else if (isStillOnLoginPage) {
    WebUI.comment('TC08 FAILED - Không hiển thị error message dù để trống password')
    WebUI.takeScreenshot('TC08_Failed_NoError.png')
} else {
    WebUI.comment('TC08 FAILED - Redirect dù để trống password: ' + currentUrl)
    WebUI.takeScreenshot('TC08_Failed_Redirect.png')
}

WebUI.delay(2)
WebUI.closeBrowser()