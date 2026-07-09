import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

/**
 * Setup_Teams_Suite
 * 
 * Setup for Teams test suite.
 * Performs:
 * 1. Open browser
 * 2. Login with existing user
 * 3. Navigate to Teams page
 */

WebUI.comment('=== SETUP: Teams Suite ===')

try {
    // Step 1: Open browser
    WebUI.comment('Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // Step 2: Login with existing user
    WebUI.comment('Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )

    // Step 3: Navigate to Teams page
    WebUI.comment('Navigating to Teams page...')
    
    // Check screen width for mobile responsive
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    
    if (screenWidth < 760) {
        WebUI.comment('Screen width < 760px, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    }
    
    // Click Teams button on navbar
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    // Verify navigated to Teams page
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.contains('/teams')) {
        throw new Exception('Failed to navigate to Teams page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Navigated to Teams page: ' + currentUrl)
    
    WebUI.takeScreenshot('Setup_Teams_Suite_Success.png')
    WebUI.comment('✓ Setup completed - ready for Teams test cases')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_Teams_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}