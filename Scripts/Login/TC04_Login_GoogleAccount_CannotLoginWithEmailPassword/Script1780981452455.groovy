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

// ========== STEP 1: First login with Google OAuth ==========
WebUI.openBrowser('')

WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')

//WebUI.delay(3)
String googleEmail = 'Luna@dudaji.com' // Email used to login to Google

String googlePassword = 'Lieulunlieule'

// Click Google Login button
WebUI.click(findTestObject('Page_Login/a_Continue with Google'))

// Enter Google email
WebUI.waitForElementPresent(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), 10, FailureHandling.STOP_ON_FAILURE)
WebUI.setText(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), googleEmail)
WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Next'))

// Enter Google password
WebUI.waitForElementPresent(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), 10, FailureHandling.STOP_ON_FAILURE)
WebUI.setText(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), googlePassword)
WebUI.click(findTestObject('Object Repository/GG/Page_Welcome/btn_Next'))

WebUI.waitForPageLoad(15)
WebUI.delay(5)

// Handle Allow permissions if requested
if (WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'), 10, FailureHandling.OPTIONAL)) {
    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'))
    WebUI.waitForPageLoad(15)
    WebUI.delay(5)
}

// Check Google login success
String currentUrl = WebUI.getUrl()

boolean googleLoginSuccess = currentUrl.contains('/c/new') || currentUrl.contains('/oauth') || currentUrl.contains('chat.nufi.me') && !currentUrl.contains('/login')
if (googleLoginSuccess) {
    WebUI.comment('Bước 1: Đăng nhập Google OAuth thành công (lần đầu)')
    WebUI.takeScreenshot('TC04_Step1_GoogleLogin_Success.png')
} else {
    WebUI.comment('TC04 FAILED - Bước 1: Đăng nhập Google OAuth thất bại - URL: ' + currentUrl)
    assert false : 'TC04 failed at step 1 - Google OAuth login không thành công, actual URL: ' + currentUrl
}

// ========== STEP 2: Logout ==========
WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Account Settings'))

WebUI.click(findTestObject('Object Repository/Core Chat/nav/Account_Settings/btn_Log out'))

WebUI.delay(2)

// ========== STEP 3: Try login with email/password using same email ==========
// Enter email (same used for Google login)
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), googleEmail)

WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '12345678')

// Click Continue
WebUI.click(findTestObject('Page_Login/button_Continue'))

WebUI.waitForElementVisible(findTestObject('Page_Login/error_Message'), 10, FailureHandling.OPTIONAL)
boolean isErrorDisplayed = WebUI.verifyElementVisible(findTestObject('Page_Login/error_Message'), FailureHandling.OPTIONAL)
String finalUrl = WebUI.getUrl()
boolean isStillOnLoginPage = finalUrl.contains('/login') || finalUrl.contains('chat.nufi.me') && !finalUrl.contains('/c/new')

if (isErrorDisplayed && isStillOnLoginPage) {
    WebUI.comment('TC04 PASSED - Không thể đăng nhập bằng email/password với cùng email Google')
} else {
    WebUI.comment('TC04 FAILED - Expected error message and stay on login page, actual URL: ' + finalUrl)
    WebUI.takeScreenshot('TC04_Failed.png')
}
assert isErrorDisplayed && isStillOnLoginPage : 'TC04 failed - expected error message and stay on login page, actual URL: ' + finalUrl

WebUI.closeBrowser()

