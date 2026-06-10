import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.Base_URL + '/login')
//WebUI.maximizeWindow()
WebUI.delay(3)

WebUI.click(findTestObject('Page_Login/a_Continue with Google'))
WebUI.delay(5)


WebUI.waitForElementPresent(
    findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), 
    10, FailureHandling.STOP_ON_FAILURE)
WebUI.setText(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), 'Luna@dudaji.com')
WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Next'))
WebUI.delay(5)



WebUI.waitForElementPresent(
	findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), 
    10, FailureHandling.STOP_ON_FAILURE)
WebUI.setText(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), 'Lieulunlieule')
WebUI.click(findTestObject('Object Repository/GG/Page_Welcome/btn_Next'))
WebUI.delay(5)


String currentUrl = WebUI.getUrl()
WebUI.comment('URL hiện tại: ' + currentUrl)

// Kiểm tra đã quay lại chat.nufi.me chưa
if (currentUrl.contains('/c/new')) {
    WebUI.comment('Đăng nhập thành công!')
    WebUI.takeScreenshot('Login_Success.png')
} else {
    WebUI.comment('Đang ở trang: ' + currentUrl)
    WebUI.takeScreenshot('Current_Page.png')
}

WebUI.delay(3)
WebUI.closeBrowser()