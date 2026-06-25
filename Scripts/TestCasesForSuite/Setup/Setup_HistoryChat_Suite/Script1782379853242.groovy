import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * Setup_HistoryChat_Suite
 * 
 * Setup for test cases that need to work with existing chat from history.
 * Performs:
 * 1. Open browser
 * 2. Login
 * 3. Open a random chat from history
 * 
 * This setup runs once before ALL test cases in the suite.
 */

WebUI.comment('=== SETUP: History Chat Suite ===')

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

    // Step 3: Open random chat from history
    WebUI.comment('Opening random chat from history...')
    CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    
    WebUI.comment('✓ Setup completed - ready for test cases with existing chat')

} catch (Exception e) {
    WebUI.comment('✗ SETUP FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('Setup_HistoryChat_Suite_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}