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

String googleEmail = "sun@dudaji.com"  
String googlePassword = "12345678"

WebUI.comment('TC03 Step 1: Attempt Google OAuth login for manual account')
WebUI.click(findTestObject('Page_Login/a_Continue with Google'))
WebUI.waitForPageLoad(15)
WebUI.delay(3)

boolean emailPagePresent = WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), 10, FailureHandling.OPTIONAL)
boolean accountChooserPresent = WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Sign in/account_ByEmail', 
    [('email') : googleEmail]), 10, FailureHandling.OPTIONAL)

if (emailPagePresent) {
    WebUI.comment('TC03: Google login email page displayed')
    WebUI.setText(findTestObject('Object Repository/GG/Page_Sign in/txt_Email'), googleEmail)
    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Next'))
    WebUI.waitForPageLoad(15)
    WebUI.delay(3)
} else if (accountChooserPresent) {
    WebUI.comment('TC03: Google account chooser displayed')
    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/account_ByEmail', 
        [('email') : googleEmail]))
    WebUI.waitForPageLoad(15)
    WebUI.delay(3)
} else {
    WebUI.comment('TC03 WARNING: Google email field or account chooser not found')
    WebUI.delay(3)
}

boolean passwordPagePresent = WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), 10, FailureHandling.OPTIONAL)
if (passwordPagePresent) {
    WebUI.comment('TC03: Google password page displayed')
    WebUI.setText(findTestObject('Object Repository/GG/Page_Welcome/txt_Password'), googlePassword)
    WebUI.click(findTestObject('Object Repository/GG/Page_Welcome/btn_Next'))
    WebUI.waitForPageLoad(15)
    WebUI.delay(3)
} else {
    WebUI.comment('TC03: Google password page not shown, continuing')
}

if (WebUI.verifyElementPresent(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'), 10, FailureHandling.OPTIONAL)) {
    WebUI.comment('TC03: Google consent Allow button displayed')
    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'))
    WebUI.waitForPageLoad(15)
    WebUI.delay(3)
}
//else {
//    // Nếu tài khoản đã hiển thị trong danh sách
//    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/account_ByEmail', 
//        [('email') : manualEmail]))
////    WebUI.delay(3)
//}
//
//// Handle Allow permissions if requested
//if (WebUI.verifyElementPresent(
//    findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'), 
//    2, FailureHandling.OPTIONAL)) {
//    WebUI.click(findTestObject('Object Repository/GG/Page_Sign in/btn_Allow'))
////    WebUI.delay(3)
//}


// Check whether an error message is displayed
currentUrl = WebUI.getUrl()
boolean isErrorMessageDisplayed = WebUI.verifyElementPresent(
    findTestObject('Page_Login/error_Message'), 
    10, FailureHandling.OPTIONAL)

boolean isStillOnLoginPage = currentUrl.contains('/login') || currentUrl.contains('chat.nufi.me') && !currentUrl.contains('/c/new')
boolean isPassed = !currentUrl.contains('/c/new') && (isErrorMessageDisplayed || isStillOnLoginPage)

if (isPassed) {
    WebUI.comment('TC03 PASSED: Manual account user CANNOT login with Google OAuth')
    WebUI.takeScreenshot('TC03_Passed.png')
} else {
    WebUI.comment('TC03 FAILED: User logged in with Google OAuth despite manual registration or incorrect error notice')
    WebUI.takeScreenshot('TC03_Failed.png')
}
assert isPassed : 'TC03 failed - expected no Google OAuth login for manual account, actual URL: ' + currentUrl + ', isErrorMessageDisplayed=' + isErrorMessageDisplayed + ', isStillOnLoginPage=' + isStillOnLoginPage + '\n'

WebUI.delay(3)
WebUI.closeBrowser()