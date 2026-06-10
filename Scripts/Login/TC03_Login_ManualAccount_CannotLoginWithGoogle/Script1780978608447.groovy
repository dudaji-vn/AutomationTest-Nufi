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

String manualEmail = "sun@dudaji.com"  
String manualPassword = "12345678"

WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), manualEmail)
WebUI.setText(findTestObject('Page_Login/input_Signin_password'), manualPassword)
WebUI.click(findTestObject('Page_Login/button_Continue'))
//WebUI.delay(5)

String currentUrl = WebUI.getUrl()
if (currentUrl.contains('/c/new')) {
    WebUI.comment('Bước 1: Đăng nhập thủ công thành công')
} else {
    WebUI.comment('Bước 1: Đăng nhập thủ công thất bại')
}

WebUI.click(findTestObject('Object Repository/Page_Nufi Chat/Side_bar/img_avatar'))  // Click avatar
//WebUI.delay(2)
WebUI.click(findTestObject('Object Repository/Page_Nufi Chat/Side_bar/btn_Log out'))
WebUI.delay(3)

WebUI.click(findTestObject('Page_Login/a_Continue with Google'))
WebUI.delay(2)

// Nếu cần nhập email
if (WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), 5, FailureHandling.OPTIONAL)) {
    WebUI.setText(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), manualEmail)
    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Next'))
//    WebUI.delay(3)
    
    WebUI.setText(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), manualPassword)
    WebUI.click(findTestObject('Object Repository/GG/Page_Welcome/btn_Next'))
//    WebUI.delay(5)
} 
//else {
//    // Nếu tài khoản đã hiển thị trong danh sách
//    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/account_ByEmail', 
//        [('email') : manualEmail]))
////    WebUI.delay(3)
//}
//
//// Xử lý Allow permissions nếu được yêu cầu
//if (WebUI.verifyElementPresent(
//    findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'), 
//    2, FailureHandling.OPTIONAL)) {
//    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'))
////    WebUI.delay(3)
//}


// Kiểm tra xem có thông báo lỗi xuất hiện không
currentUrl = WebUI.getUrl()
boolean isErrorMessageDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message'), 
    10, FailureHandling.OPTIONAL)

boolean isStillOnLoginPage = currentUrl.contains('/login') || currentUrl.contains('chat.nufi.me') && !currentUrl.contains('/c/new')

if (isErrorMessageDisplayed || isStillOnLoginPage) {
    WebUI.comment('TC03 PASSED: User đăng ký thủ công KHÔNG thể đăng nhập bằng Google OAuth')
    WebUI.takeScreenshot('TC03_Passed.png')
} else if (currentUrl.contains('/c/new')) {
    WebUI.comment('TC03 FAILED: User đã đăng nhập được bằng Google OAuth dù đã đăng ký thủ công')
    WebUI.takeScreenshot('TC03_Failed.png')
} else {
    WebUI.comment('TC03 - Cần kiểm tra thêm: URL = ' + currentUrl)
}

WebUI.delay(3)
WebUI.closeBrowser()