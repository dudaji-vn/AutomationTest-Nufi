import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// 1. Open browser (ONLY OPEN ONCE)
WebUI.openBrowser('')

// 2. Navigate to URL
WebUI.navigateToUrl(GlobalVariable.Base_URL)

// 3. Wait for page to load
WebUI.delay(3)

// 4. Log in
WebUI.setText(findTestObject('Page_Login/input_Sign in_email'), 'lieucao16122003@gmail.com')

WebUI.setText(findTestObject('Page_Login/input_Signin_password'), 'lieucao16122003')

WebUI.click(findTestObject('Page_Login/button_Continue'))

// 5. Check login success
String currentUrl = WebUI.getUrl()

WebUI.comment('Setup - Đăng nhập thành công, URL hiện tại: ' + currentUrl)

// 6. IMPORTANT: DO NOT close the browser
// WebUI.closeBrowser() <--- REMOVED THIS LINE
// 7. (Optional) Save token/JWT to Global Variable if using API
// GlobalVariable.authToken = "token_from_response"
WebUI.comment('=== SETUP HOÀN TẤT, TRÌNH DUYỆT ĐANG MỞ SẴN ===')

