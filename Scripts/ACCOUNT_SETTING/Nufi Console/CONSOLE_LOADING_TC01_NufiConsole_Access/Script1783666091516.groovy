import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC01: Nufi Console - Access from Chat
 * 
 * Test Flow:
 * 1. Open browser and login
 * 2. Check screen width
 * 3. If width < 760px, ensure navbar is open
 * 4. Click Account Settings button
 * 5. Click Console menu item
 * 6. Verify new tab opens to console.nufi.me
 */

WebUI.comment('=== TC01: Nufi Console Access ===')

try {
    // Step 1: Open browser and login
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Check screen width and ensure navbar is open if needed
    WebUI.comment('Step 3: Checking screen width...')
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    WebUI.comment('Screen width: ' + screenWidth + 'px')
    
    if (screenWidth <= 768) {
        WebUI.comment('Screen width <= 768px, ensuring navbar is open...')
        
        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        
        String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
        
        if (ariaHidden == 'true') {
            WebUI.comment('Navbar is closed, opening...')
            TestObject openButton = new TestObject('openButton')
            openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
            WebUI.waitForElementClickable(openButton, 5)
            WebUI.click(openButton)
            WebUI.delay(1)
            WebUI.comment('Navbar opened')
        } else {
            WebUI.comment('Navbar already open')
        }
    } else {
        WebUI.comment('Screen width >= 760px, skipping navbar check')
    }

    // Step 4: Click Account Settings button
    WebUI.comment('Step 4: Clicking Account Settings button...')
    TestObject accountSettingsButton = new TestObject('accountSettingsButton')
    accountSettingsButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Account Settings']")
    WebUI.waitForElementClickable(accountSettingsButton, 10)
    WebUI.click(accountSettingsButton)
    WebUI.comment('Account Settings button clicked')
    WebUI.delay(1)

    // Step 5: Click Console menu item
    WebUI.comment('Step 5: Clicking Console menu item...')
    TestObject consoleMenuItem = new TestObject('consoleMenuItem')
    consoleMenuItem.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@role='menuitem' and contains(text(), 'Console')]")
    WebUI.waitForElementClickable(consoleMenuItem, 10)
    WebUI.click(consoleMenuItem)
    WebUI.comment('Console menu item clicked')
    WebUI.delay(2)

    // Step 6: Verify new tab opens to console.nufi.me
    WebUI.comment('Step 6: Verifying new tab opens...')
    
    // Get WebDriver instance
    WebDriver driver = DriverFactory.getWebDriver()
    
    // Get all window handles
    Set<String> windowHandles = driver.getWindowHandles()
    WebUI.comment('Number of windows/tabs: ' + windowHandles.size())
    
    // Get current window handle
    String originalWindow = driver.getWindowHandle()
    WebUI.comment('Original window: ' + originalWindow)
    
    // Switch to new tab (the last one)
    String consoleUrl = ''
    for (String handle : windowHandles) {
        driver.switchTo().window(handle)
        String currentUrl = driver.getCurrentUrl()
        WebUI.comment('Current URL: ' + currentUrl)
        
        if (currentUrl.contains('console.nufi.me')) {
            consoleUrl = currentUrl
            WebUI.comment('Found Console tab: ' + consoleUrl)
            break
        }
    }
    
    // Verify Console URL
    WebUI.comment('Console URL: ' + consoleUrl)
    
    if (consoleUrl.contains('console.nufi.me')) {
        WebUI.comment('Successfully navigated to NUFI Console')
    } else {
        WebUI.comment('Current URL does not contain console.nufi.me: ' + consoleUrl)
    }
    
    WebUI.takeScreenshot('TC01_NufiConsole_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC01 PASSED')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_NufiConsole_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}