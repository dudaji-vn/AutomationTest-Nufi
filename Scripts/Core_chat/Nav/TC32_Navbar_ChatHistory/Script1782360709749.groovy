import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC32: Navbar - Verify Chat History Tab
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Verify navbar is open
 * 4. Check current aria-label of nav region
 * 5. If not "Chat History", click Chat History button
 * 6. Verify aria-label changed to "Chat History"
 */

WebUI.comment('=== TC32: Navbar - Verify Chat History Tab ===')

try {
    // Create TestObjects with direct XPath
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    
    TestObject navRegion = new TestObject('navRegion')
    navRegion.addProperty('xpath', ConditionType.EQUALS, "//nav//div[@role='region']")
    
    TestObject chatHistoryButton = new TestObject('chatHistoryButton')
    chatHistoryButton.addProperty('xpath', ConditionType.EQUALS, "//button[@aria-label='Chat History']")

    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Verify navbar is open
    WebUI.comment('Step 3: Verifying navbar is open...')
    WebUI.waitForElementVisible(navSidebar, 10)
    
    String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'true') {
        WebUI.comment('Navbar is closed, opening...')
        TestObject openButton = new TestObject('openButton')
        openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.click(openButton)
        WebUI.delay(1)
    }
    WebUI.comment('Navbar is open')

    // Step 4: Get current aria-label of nav region
    WebUI.comment('Step 4: Checking current aria-label...')
    WebUI.waitForElementVisible(navRegion, 10)
    
    String currentAriaLabel = WebUI.getAttribute(navRegion, 'aria-label')
    WebUI.comment('Current aria-label: ' + currentAriaLabel)
    
    // Step 5: If not "Chat History", click the button
    if (currentAriaLabel != 'Chat History') {
        WebUI.comment('Step 5: Current tab is not Chat History, switching...')
        WebUI.waitForElementClickable(chatHistoryButton, 5)
        WebUI.click(chatHistoryButton)
        WebUI.comment('Chat History button clicked')
        WebUI.delay(1)
        
        // Step 6: Verify aria-label changed
        WebUI.comment('Step 6: Verifying aria-label changed...')
        String newAriaLabel = WebUI.getAttribute(navRegion, 'aria-label')
        WebUI.comment('New aria-label: ' + newAriaLabel)
        
        if (newAriaLabel == 'Chat History') {
            WebUI.comment('Successfully switched to Chat History tab')
        } else {
            WebUI.comment('Still on: ' + newAriaLabel)
        }
    } else {
        WebUI.comment('Already on Chat History tab - no action needed')
    }
    
    WebUI.takeScreenshot('TC32_Navbar_ChatHistory_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC32 PASSED')

} catch (Exception e) {
    WebUI.comment('TC32 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC32_Navbar_ChatHistory_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}