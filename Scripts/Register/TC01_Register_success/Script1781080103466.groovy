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

// ========== CREATE MULTIPLE RANDOM ACCOUNTS ==========
int numberOfAccounts = 1 // Number of accounts to register

List<Map> registeredAccounts = []

for (int i = 1; i <= numberOfAccounts; i++) {
    WebUI.comment(('========== ĐANG ĐĂNG KÝ TÀI KHOẢN THỨ ' + i) + ' ==========')

    // Generate random data for each account
    long timestamp = System.currentTimeMillis() + i

    Random random = new Random()

    String randomName = 'Test User ' + timestamp

    String randomUsername = ('user_' + timestamp) + random.nextInt(10000)

    String randomEmail = (('test_' + timestamp) + random.nextInt(10000)) + '@example.com'

    String randomPassword = ('Test@' + timestamp) + '!aB'

    // Save for verification later
    Map<String, String> account = new HashMap()

    account.put('name', randomName)

    account.put('username', randomUsername)

    account.put('email', randomEmail)

    account.put('password', randomPassword)

    registeredAccounts.add(account)

    // Open browser for each registration
    WebUI.openBrowser('')

    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')

    WebUI.delay(2)

    // Enter information
    WebUI.setText(findTestObject('Page_Signup/input_name'), randomName)

    WebUI.setText(findTestObject('Page_Signup/input_username'), randomUsername)

    WebUI.setText(findTestObject('Page_Signup/input_email'), randomEmail)

    WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)

    WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), randomPassword)

    // Submit
    WebUI.click(findTestObject('Page_Signup/button_Continue'))

    WebUI.delay(3)

    // Check results
    // Get text from message element
    //	String actualMessage = WebUI.getText(findTestObject('Page_Signup/Registration_Message'))
    //	
    //	// Check if it contains "Registration successful"
    //	if (actualMessage.contains('Registration successful')) {
    //		WebUI.comment('✓ PASSED: Message contains "Registration successful"')
    //	} else {
    //		WebUI.comment('✗ FAILED: Message not contains "Registration successful". Actual: ' + actualMessage)
    //	}
    //	
    boolean hasSuccessMessage = WebUI.verifyElementPresent(findTestObject('Page_Signup/Registration_Message'), 5, 
        FailureHandling.OPTIONAL)

    String currentUrl = WebUI.getUrl()

    boolean isRedirectedToLogin = currentUrl.contains('/login')

    if (hasSuccessMessage && isRedirectedToLogin) {
        WebUI.comment((('✓ Tài khoản ' + i) + ' registered successfully: ') + randomEmail)
    } else {
        WebUI.comment(('✗ Tài khoản ' + i) + ' registration failed')
        WebUI.takeScreenshot(('Signup_Failed_' + i) + '.png')
        assert false : 'TC01 failed - expected successful registration and redirect to login for account ' + randomEmail + ', actual URL: ' + currentUrl
    }
    
    WebUI.closeBrowser()

    WebUI.delay(1)
}

//    WebUI.openBrowser('')
//	WebUI.navigateToUrl(GlobalVariable.Base_URL + 'register')
//
//    WebUI.setText(findTestObject('Page_Signup/input_name'), "Lunatest")
//
//    WebUI.setText(findTestObject('Page_Signup/input_username'), "Lunatest")
//
//    WebUI.setText(findTestObject('Page_Signup/input_email'), "Lunatest@test.com")
//
//    WebUI.setText(findTestObject('Page_Signup/input_password'), "Lunatest")
//
//    WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), "Lunatest")
//	boolean hasSuccessMessage = WebUI.verifyElementPresent(findTestObject('Page_Signup/Registration_Message'), 5,
//		FailureHandling.OPTIONAL)
//
//	String currentUrl = WebUI.getUrl()
//
//	boolean isRedirectedToLogin = currentUrl.contains('/login')
//
//	if (hasSuccessMessage && isRedirectedToLogin) {
//		WebUI.comment('✓ Tài khoản registered successfully: ') 
//	} else {
//		WebUI.comment('✗ Tài khoản  registration failed')
//	}
//
//    WebUI.closeBrowser()


