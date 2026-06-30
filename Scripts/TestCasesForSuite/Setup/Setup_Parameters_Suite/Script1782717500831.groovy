import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

/**
 * Setup_Parameters_Suite
 * 
 * Setup for Parameters test suite.
 * Performs:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Open Parameters panel
 * 
 * This setup runs once before ALL test cases in the suite.
 */

WebUI.comment('=== SETUP: Parameters Suite ===')

try {
    // Step 1: Open browser
    WebUI.comment('Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // Step 2: Login
    WebUI.comment('Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )

    // Step 3: Open new conversation
    WebUI.comment('Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(
        GlobalVariable.Base_URL
    )

    // Step 4: Select endpoint + model
    WebUI.comment('Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'(
//        'Gemini',
//        'gemini'
//		  'Nufi',
//		  'Qwen2.5-0.5B'
		  'sys_test_npu',
		  'llama-3-1-8b-v1-rngd'
    )
    WebUI.delay(2)

    // Step 5: Open Parameters panel
    WebUI.comment('Opening Parameters panel...')
    
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
        WebUI.comment('Sidebar opened')
    }
    
    // Click Parameters button
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters'),
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters'))
    WebUI.delay(2)
    WebUI.comment('Parameters panel opened')

    WebUI.comment('✓ Setup completed - ready for Parameters test cases')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_Parameters_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}