import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory
import java.util.Random

/**
 * Setup_Console_Suite
 * 
 * Setup for Nufi Console test suite.
 * Creates a new user, logs in, and opens Console tab.
 * Browser stays open for all test cases.
 */

WebUI.comment('=== SETUP: Nufi Console Suite ===')

try {
    // Generate random user data
    long timestamp = System.currentTimeMillis()
    Random random = new Random()
    int randomNum = random.nextInt(10000)
    
    String randomName = 'Console Test ' + timestamp
    String randomUsername = 'console_user_' + timestamp + randomNum
    String randomEmail = 'console_test_' + timestamp + randomNum + '@example.com'
    String randomPassword = 'Test@' + timestamp + '!aB'
    
    // Store in GlobalVariable for test cases
    GlobalVariable.consoleUserEmail = randomEmail
    GlobalVariable.consoleUserPassword = randomPassword
    GlobalVariable.consoleUserName = randomName
    GlobalVariable.consoleUserUsername = randomUsername
    
    WebUI.comment('Creating new user: ' + randomEmail)
    
    // Step 1: Open browser
    WebUI.comment('Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)
    
    // Step 2: Register
    WebUI.comment('Navigating to register page...')
    WebUI.navigateToUrl(GlobalVariable.Base_URL + '/register')
    WebUI.delay(2)
    
    WebUI.comment('Filling registration form...')
    
    TestObject nameField = new TestObject('nameField')
    nameField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='name']")
    WebUI.setText(nameField, randomName)
    
    TestObject usernameField = new TestObject('usernameField')
    usernameField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='username']")
    WebUI.setText(usernameField, randomUsername)
    
    TestObject emailField = new TestObject('emailField')
    emailField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='email']")
    WebUI.setText(emailField, randomEmail)
    
    TestObject passwordField = new TestObject('passwordField')
    passwordField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='password']")
    WebUI.setText(passwordField, randomPassword)
    
    TestObject confirmPasswordField = new TestObject('confirmPasswordField')
    confirmPasswordField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='password_confirm']")
    WebUI.setText(confirmPasswordField, randomPassword)
    
    TestObject continueButton = new TestObject('continueButton')
    continueButton.addProperty('xpath', ConditionType.EQUALS, "//button[contains(text(),'Continue')]")
    WebUI.click(continueButton)
    WebUI.delay(3)
    
    String currentUrl = WebUI.getUrl()
    if (currentUrl.contains('/login')) {
        WebUI.comment('Registration successful')
    }
    
    // Step 3: Login with new user
    WebUI.comment('Logging in with new user...')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.delay(2)
    
    TestObject loginEmailField = new TestObject('loginEmailField')
    loginEmailField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='email']")
    WebUI.setText(loginEmailField, randomEmail)
    
    TestObject loginPasswordField = new TestObject('loginPasswordField')
    loginPasswordField.addProperty('xpath', ConditionType.EQUALS, "//input[@id='password']")
    WebUI.setText(loginPasswordField, randomPassword)
    
    TestObject loginContinueButton = new TestObject('loginContinueButton')
    loginContinueButton.addProperty('xpath', ConditionType.EQUALS, "//button[contains(text(),'Continue')]")
    WebUI.click(loginContinueButton)
    WebUI.delay(3)
    
    WebUI.comment('Login successful')
    
    // Step 4: Open Console tab (but keep both tabs open)
    WebUI.comment('Opening Console tab...')
    
    // Check screen width
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    
    if (screenWidth < 760) {
        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        if (ariaHidden == 'true') {
            TestObject openButton = new TestObject('openButton')
            openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
            WebUI.click(openButton)
            WebUI.delay(1)
        }
    }
    
    TestObject accountSettingsButton = new TestObject('accountSettingsButton')
    accountSettingsButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Account Settings']")
    WebUI.waitForElementClickable(accountSettingsButton, 10)
    WebUI.click(accountSettingsButton)
    WebUI.delay(1)
    
    TestObject consoleMenuItem = new TestObject('consoleMenuItem')
    consoleMenuItem.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@role='menuitem' and contains(text(), 'Console')]")
    WebUI.waitForElementClickable(consoleMenuItem, 10)
    WebUI.click(consoleMenuItem)
    WebUI.delay(3)
    
    // Switch to Console tab and stay there
    WebDriver driver = DriverFactory.getWebDriver()
    Set<String> windowHandles = driver.getWindowHandles()
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String url = driver.getCurrentUrl()
        if (url.contains('console.nufi.me')) {
            WebUI.comment('Switched to Console tab: ' + url)
            break
        }
    }
    
    WebUI.takeScreenshot('Setup_Console_Suite_Success.png')
    WebUI.comment('✓ Setup completed - Console tab ready for test cases')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_Console_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}