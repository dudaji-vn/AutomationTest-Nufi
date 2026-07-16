import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * Setup_RAG_Suite
 * 
 * Setup for RAG (Retrieval-Augmented Generation) test suite.
 * Performs:
 * 1. Open browser
 * 2. Login
 * 3. Switch to Advanced interface (required for Agent Builder button to display)
 * 4. Navigate to Agent Builder page
 * 
 * This setup runs once before ALL test cases in the suite.
 */

WebUI.comment('=== SETUP: RAG Suite ===')

try {
    // ================================================================
    // Step 1: Open browser
    // ================================================================
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // ================================================================
    // Step 2: Login
    // ================================================================
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )
    WebUI.delay(2)
    WebUI.comment('✓ Login successful')

    // ================================================================
    // Step 3: Switch to Advanced interface
    // ================================================================
    WebUI.comment('Step 3: Switching to Advanced interface...')
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.comment('✓ Advanced interface enabled')

    // ================================================================
    // Step 4: Navigate to Agent Builder
    // ================================================================
    WebUI.comment('Step 4: Navigating to Agent Builder...')
    
    // Check screen width - if <= 768, open sidebar first
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    WebUI.comment('Screen width: ' + screenWidth + 'px')
    
    if (screenWidth <= 768) {
        WebUI.comment('Screen width <= 768px, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('✓ Sidebar opened')
    }
    
    // Click Agent Builder button
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Agent Builder'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Agent Builder'))
    WebUI.delay(3)
    WebUI.comment('✓ Agent Builder page opened')

    WebUI.comment('✓ Setup completed - ready for RAG test cases')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_RAG_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}