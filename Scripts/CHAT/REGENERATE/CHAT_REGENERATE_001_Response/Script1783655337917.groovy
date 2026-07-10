import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC13: Regenerate Response
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Regenerate button of the last message
 * 5. Wait for new response
 * 6. Verify new response is generated
 */

WebUI.comment('=== TC13: Regenerate Response ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Open existing chat from history
    WebUI.comment('Step 3: Opening existing chat from history...')
    String chatName = CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    WebUI.comment('Opened chat: ' + chatName)

    // Step 4: Get original response
    WebUI.comment('Step 4: Getting original response...')
    TestObject lastMessage = new TestObject('dynamic_last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]")
    WebUI.waitForElementVisible(lastMessage, 5)
    String originalResponse = WebUI.getText(lastMessage)
    WebUI.comment('Original response: ' + (originalResponse.length() > 100 ? originalResponse.substring(0, 100) + '...' : originalResponse))

    // Step 5: Click Regenerate button of the last message
    WebUI.comment('Step 5: Clicking Regenerate button of the last message...')
    TestObject regenerateButton = new TestObject('dynamic_regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    WebUI.waitForElementVisible(regenerateButton, 5)
    WebUI.click(regenerateButton)
    WebUI.comment('Regenerate button clicked')

    // Step 6: Wait for new response
    WebUI.comment('Step 6: Waiting for new response...')
    TestObject thinkingIndicator = new TestObject('dynamic_thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")
    WebUI.waitForElementVisible(thinkingIndicator, 5)
    WebUI.comment('Thinking indicator appeared - regenerating...')
    
    WebUI.waitForElementNotVisible(thinkingIndicator, 30)
    WebUI.comment('Thinking indicator disappeared - new response received')

    // Step 7: Verify new response
    WebUI.comment('Step 7: Verifying new response...')
    WebUI.waitForElementVisible(lastMessage, 5)
    
    String newResponse = WebUI.getText(lastMessage)
    WebUI.comment('New response: ' + (newResponse.length() > 100 ? newResponse.substring(0, 100) + '...' : newResponse))
    
    if (newResponse != null && !newResponse.trim().isEmpty()) {
        WebUI.comment('New response generated successfully')
        WebUI.takeScreenshot('TC13_Regenerate_Success.png')
    } else {
        throw new Exception('New response is empty')
    }

    // Step 8: Close browser
    WebUI.comment('Step 8: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC13 PASSED')

} catch (Exception e) {
    WebUI.comment('TC13 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC13_Regenerate_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}