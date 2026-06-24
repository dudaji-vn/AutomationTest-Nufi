import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * Setup_CoreChat_Suite
 * 
 * Single setup for ALL Core Chat test cases.
 * Performs:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send initial message and store response
 * 
 * This setup runs once before ALL test cases in the suite.
 */

WebUI.comment('=== SETUP: Core Chat Suite ===')

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
        'Nufi',
        'Qwen2.5-0.5B'
    )

    // Step 5: Send message and get response
    WebUI.comment('Sending initial test message...')
    String testMessage = 'Core Chat test message - shared across suite'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

//    // Store for test cases if needed
//    GlobalVariable.coreChatResponse = response
//    GlobalVariable.coreChatTestMessage = testMessage

    WebUI.comment('✓ Setup completed - ready for all Core Chat test cases')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_CoreChat_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}