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

// ========== BƯỚC 1: Đăng nhập lần đầu bằng Google OAuth ==========
WebUI.openBrowser('')

WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')

//WebUI.delay(3)
String googleEmail = 'Luna@dudaji.com' // Email dùng để đăng nhập Google

String googlePassword = 'Lieulunlieule'

// Click nút Google Login
WebUI.click(findTestObject('Page_Login/a_Continue with Google'))

//WebUI.delay(5)
// Nhập email Google
WebUI.waitForElementPresent(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), 10, FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), googleEmail)

WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Next'))

//WebUI.delay(5)
// Nhập password Google
WebUI.waitForElementPresent(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), 3, FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), googlePassword)

WebUI.click(findTestObject('Object Repository/GG/Page_Welcome/btn_Next'))

//WebUI.delay(5)
// Xử lý Allow permissions nếu được yêu cầu
if (WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'), 3, FailureHandling.OPTIONAL)) {
    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'))

    WebUI.delay(3)
}

// Kiểm tra đăng nhập Google thành công
String currentUrl = WebUI.getUrl()

if (currentUrl.contains('/c/new')) {
    WebUI.comment('Bước 1: Đăng nhập Google OAuth thành công (lần đầu)')

    WebUI.takeScreenshot('TC04_Step1_GoogleLogin_Success.png')
} else {
    WebUI.comment('Bước 1: Đăng nhập Google OAuth thất bại - URL: ' + currentUrl)
}

// ========== BƯỚC 2: Logout ==========
WebUI.click(findTestObject('Object Repository/Page_Nufi Chat/Side_bar/img_avatar'))

WebUI.click(findTestObject('Object Repository/Page_Nufi Chat/Side_bar/btn_Log out'))

WebUI.delay(2)

// ========== BƯỚC 3: Thử đăng nhập bằng email/password với cùng email ==========
// Nhập email (chính là email đã dùng để đăng nhập Google)
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), googleEmail)

WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '12345678')

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))

WebUI.waitForElementVisible(findTestObject('Page_Login/error_Message'), 2)

WebUI.closeBrowser()

