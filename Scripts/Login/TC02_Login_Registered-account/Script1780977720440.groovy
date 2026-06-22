import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('')

WebUI.navigateToUrl(GlobalVariable.Base_URL)

//WebUI.maximizeWindow()
WebUI.delay(3)

WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), 'sun@dudaji.com')

WebUI.setText(findTestObject('Page_Login/input_Signin_password'), '12345678')

WebUI.click(findTestObject('Page_Login/button_Continue'))
WebUI.waitForPageLoad(10)
WebUI.delay(3)

String currentUrl = WebUI.getUrl()

WebUI.comment('Current Url after login attempt: ' + currentUrl)

boolean loginSuccess = currentUrl.contains('/c/new') || currentUrl.contains('/chat')
if (loginSuccess) {
    WebUI.comment('TC02 PASSED - Login tài khoản đăng ký thành công')
    WebUI.takeScreenshot('Login_Success.png')
} else {
    WebUI.comment('TC02 FAILED - Không quay về trang chat, actual URL: ' + currentUrl)
    WebUI.takeScreenshot('Current_Page.png')
}
assert loginSuccess : 'TC02 failed - expected login success, actual URL: ' + currentUrl

WebUI.delay(3)

WebUI.closeBrowser()

