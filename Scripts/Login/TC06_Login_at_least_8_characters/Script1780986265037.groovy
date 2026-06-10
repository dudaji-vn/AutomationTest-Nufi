import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Danh sách password ít hơn 8 ký tự (mỗi case đều < 8 chars)
List<String> shortPasswords = ['Abc@12', '123', 'a', 'P@ss', '1234567']

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(3)

// Email hợp lệ nhưng không cần tồn tại trong hệ thống vì validation xảy ra trước
String validEmail = "test@example.com"

for (String shortPassword in shortPasswords) {
    WebUI.comment('=== Đang test với password: "' + shortPassword + '" (độ dài: ' + shortPassword.length() + ') ===')
    
    // Nhập email hợp lệ
    WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), validEmail)
    
    // Nhập password ngắn
    WebUI.setText(findTestObject('Page_Login/input_Signin_password'), shortPassword)
    
    // Click Continue
    WebUI.click(findTestObject('Page_Login/button_Continue'))
    WebUI.delay(2)
    
    // Kiểm tra error message
    String expectedError = "Password must be at least 8 characters"
    boolean isErrorDisplayed = WebUI.verifyElementPresent(
        findTestObject('Page_Login/error_Message_pass', [('text') : expectedError]), 
        5, FailureHandling.OPTIONAL)
    
    if (isErrorDisplayed) {
        WebUI.comment('TC06 PASSED - Password: "' + shortPassword + '" - Hiển thị đúng error message')
    } else {
        WebUI.comment('TC06 FAILED - Password: "' + shortPassword + '" - Không hiển thị error message')
        WebUI.takeScreenshot('TC06_Failed_pw_' + shortPassword + '.png')
    }
    
    // Clear form trước khi test tiếp (không reload để giữ email)
    WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '')
    WebUI.delay(1)
}

WebUI.delay(2)
WebUI.closeBrowser()