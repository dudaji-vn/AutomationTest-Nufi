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
	WebUI.comment('Opening browser...')
	CustomKeywords.'keywords.ChatKeywords.openBrowser'(
		GlobalVariable.Base_URL
	)

	WebUI.comment('Logging in...')
	CustomKeywords.'keywords.ChatKeywords.loginChat'(
		GlobalVariable.email,
		GlobalVariable.password
//		"luilui@lui.lui","luilui@lui.lui"
	)
    
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