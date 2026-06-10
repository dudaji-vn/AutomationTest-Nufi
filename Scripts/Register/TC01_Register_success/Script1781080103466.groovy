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

// ========== TẠO MULTIPLE TÀI KHOẢN NGẪU NHIÊN ==========
int numberOfAccounts = 1 // Số lượng tài khoản cần đăng ký

List<Map> registeredAccounts = []

for (int i = 1; i <= numberOfAccounts; i++) {
    WebUI.comment(('========== ĐANG ĐĂNG KÝ TÀI KHOẢN THỨ ' + i) + ' ==========')

    // Tạo dữ liệu ngẫu nhiên cho mỗi tài khoản
    long timestamp = System.currentTimeMillis() + i

    Random random = new Random()

    String randomName = 'Test User ' + timestamp

    String randomUsername = ('user_' + timestamp) + random.nextInt(10000)

    String randomEmail = (('test_' + timestamp) + random.nextInt(10000)) + '@example.com'

    String randomPassword = ('Test@' + timestamp) + '!aB'

    // Lưu lại để kiểm tra sau
    Map<String, String> account = new HashMap()

    account.put('name', randomName)

    account.put('username', randomUsername)

    account.put('email', randomEmail)

    account.put('password', randomPassword)

    registeredAccounts.add(account)

    // Mở trình duyệt cho mỗi lần đăng ký
    WebUI.openBrowser('')

    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')

    WebUI.delay(2)

    // Nhập thông tin
    WebUI.setText(findTestObject('Page_Signup/input_name'), randomName)

    WebUI.setText(findTestObject('Page_Signup/input_username'), randomUsername)

    WebUI.setText(findTestObject('Page_Signup/input_email'), randomEmail)

    WebUI.setText(findTestObject('Page_Signup/input_password'), randomPassword)

    WebUI.setText(findTestObject('Page_Signup/input_Password_confirm_password'), randomPassword)

    // Submit
    WebUI.click(findTestObject('Page_Signup/button_Continue'))

    WebUI.delay(3)

    // Kiểm tra kết quả
    // Lấy text từ element message
    //	String actualMessage = WebUI.getText(findTestObject('Page_Signup/Registration_Message'))
    //	
    //	// Kiểm tra có chứa "Registration successful" không
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
        WebUI.comment((('✓ Tài khoản ' + i) + ' đăng ký thành công: ') + randomEmail)
    } else {
        WebUI.comment(('✗ Tài khoản ' + i) + ' đăng ký thất bại')

        WebUI.takeScreenshot(('Signup_Failed_' + i) + '.png')
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
//		WebUI.comment('✓ Tài khoản đăng ký thành công: ') 
//	} else {
//		WebUI.comment('✗ Tài khoản  đăng ký thất bại')
//	}
//
//    WebUI.closeBrowser()


