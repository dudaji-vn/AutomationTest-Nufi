import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// ========== TẠO DỮ LIỆU NGẪU NHIÊN ==========
long timestamp = System.currentTimeMillis()
Random random = new Random()

String randomName = "Test User " + timestamp
String randomUsername = "user_" + timestamp
String randomEmail = "test_" + timestamp + "@example.com"
String randomPassword = "Abc@123456"
String wrongConfirmPassword = "Abc@123457"  // Khác với password

WebUI.comment('=== TC04: Đăng ký thất bại - Password và Confirm không khớp ===')
WebUI.comment('Password: ' + randomPassword)
WebUI.comment('Confirm Password: ' + wrongConfirmPassword)

// ========== MỞ TRÌNH DUYỆT ==========
WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
WebUI.delay(3)

// ========== CLICK "Sign up" LINK ==========
WebUI.click(findTestObject('Object Repository/Page_Login/a_Sign up'))
WebUI.delay(2)

// ========== NHẬP DỮ LIỆU ĐĂNG KÝ ==========
WebUI.setText(findTestObject('Page_Signup/input_name'), randomName)
WebUI.setText(findTestObject('Page_Signup/input_username'), randomUsername)
WebUI.setText(findTestObject('Page_Signup/input_email'), randomEmail)
WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)
WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), wrongConfirmPassword)

// Chụp ảnh trước khi submit
WebUI.takeScreenshot('TC04_Before_Submit.png')

// ========== CLICK CONTINUE ==========
//WebUI.click(findTestObject('Page_Signup/button_Continue'))
WebUI.delay(3)

// ========== KIỂM TRA ERROR MESSAGE "Passwords do not match" ==========
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
    // Thử tìm message error tổng quát
    boolean generalError = WebUI.verifyElementPresent(
        findTestObject('Page_Signup/Message_error'), 
        3, FailureHandling.OPTIONAL)
    
    if (generalError) {
        String generalMsg = WebUI.getText(findTestObject('Page_Signup/Message_error'))
        WebUI.comment('General error message: "' + generalMsg + '"')
        if (generalMsg.contains('match') || generalMsg.contains('password')) {
            WebUI.comment('✓ PASSED: Có error message liên quan đến password')
        }
    } else {
        WebUI.comment('✗ FAILED: Không hiển thị error message cho password không khớp')
    }
}

// ========== KIỂM TRA VẪN Ở TRANG REGISTER ==========
String currentUrl = WebUI.getUrl()
boolean isStillOnRegisterPage = currentUrl.contains('/register') || currentUrl.contains('/signup')

if (isStillOnRegisterPage) {
    WebUI.comment('✓ PASSED: Vẫn ở trang register, không bị redirect')
} else {
    WebUI.comment('✗ FAILED: Đã bị redirect đến: ' + currentUrl)
}

// ========== KIỂM TRA KHÔNG CÓ MESSAGE THÀNH CÔNG ==========
boolean hasSuccessMessage = WebUI.verifyElementPresent(
    findTestObject('Page_Signup/Registration_Message'), 
    3, FailureHandling.OPTIONAL)

if (!hasSuccessMessage) {
    WebUI.comment('✓ PASSED: Không hiển thị message thành công')
} else {
    WebUI.comment('✗ FAILED: Vẫn hiển thị message thành công')
}

// ========== KẾT LUẬN ==========
if (isErrorDisplayed && isStillOnRegisterPage && !hasSuccessMessage) {
    WebUI.comment('=== TC04 PASSED - Password không khớp bị từ chối đúng cách ===')
    WebUI.takeScreenshot('TC04_Passed.png')
} else {
    WebUI.comment('=== TC04 FAILED ===')
    WebUI.takeScreenshot('TC04_Failed.png')
}

WebUI.delay(3)
WebUI.closeBrowser()