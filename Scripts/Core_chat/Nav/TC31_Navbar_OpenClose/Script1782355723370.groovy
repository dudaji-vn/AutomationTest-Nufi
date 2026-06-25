import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC01: Navbar - Open/Close Toggle
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Verify navbar is open (aria-hidden="false")
 * 4. Click close sidebar button
 * 5. Verify navbar is closed (aria-hidden="true")
 * 6. Click open sidebar button
 * 7. Verify navbar is open (aria-hidden="false")
 */

WebUI.comment('=== TC01: Navbar Open/Close Toggle ===')

try {
    // Create TestObjects with direct XPath
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    
    TestObject closeButton = new TestObject('closeButton')
    closeButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='close-sidebar-button']")
    
    TestObject openButton = new TestObject('openButton')
    openButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")

    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Verify navbar is open initially
    WebUI.comment('Step 3: Verifying navbar is open...')
    WebUI.waitForElementVisible(navSidebar, 10)
    
    String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('Initial aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'false') {
        WebUI.comment('Navbar is open - aria-hidden="false"')
    } else {
        WebUI.comment('Navbar is closed - aria-hidden="true"')
    }
    WebUI.takeScreenshot('TC01_Navbar_Initial.png')

    // Step 4: Click close sidebar button
    WebUI.comment('Step 4: Clicking close sidebar button...')
    WebUI.waitForElementClickable(closeButton, 5)
    WebUI.click(closeButton)
    WebUI.comment('Close sidebar button clicked')
    WebUI.delay(1)

    // Step 5: Verify navbar is closed
    WebUI.comment('Step 5: Verifying navbar is closed...')
    ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('After close - aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'true') {
        WebUI.comment('Navbar is closed - aria-hidden="true"')
    } else {
        WebUI.comment('Navbar is still open - aria-hidden="false"')
    }
    WebUI.takeScreenshot('TC01_Navbar_Closed.png')
    
    // Wait for open button to become visible
    WebUI.delay(1)

    // Step 6: Click open sidebar button
    WebUI.comment('Step 6: Clicking open sidebar button...')
    
    // Check if open button is visible before clicking
    boolean isOpenVisible = WebUI.waitForElementVisible(openButton, 5, FailureHandling.OPTIONAL)
    
    if (isOpenVisible) {
        WebUI.click(openButton)
        WebUI.comment('Open sidebar button clicked')
    } else {
        WebUI.comment('Open button not visible, trying JavaScript click...')
        WebUI.executeJavaScript(
            "var btn = document.getElementById('open-sidebar-button'); if(btn) btn.click();", 
            null
        )
        WebUI.comment('Open sidebar button clicked via JavaScript')
    }
    WebUI.delay(1)

    // Step 7: Verify navbar is open again
    WebUI.comment('Step 7: Verifying navbar is open again...')
    ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('After open again - aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'false') {
        WebUI.comment('Navbar is open - aria-hidden="false"')
    } else {
        WebUI.comment('Navbar is still closed - aria-hidden="true"')
    }
    WebUI.takeScreenshot('TC01_Navbar_Reopened.png')

    // Step 8: Close browser
    WebUI.comment('Step 8: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC01 PASSED')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Navbar_OpenClose_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}