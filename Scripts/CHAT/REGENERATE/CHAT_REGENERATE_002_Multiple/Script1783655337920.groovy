import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC14: Regenerate Response Multiple Times
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Regenerate button 3 times
 * 5. Verify each regeneration completes
 */

WebUI.comment('=== TC14: Regenerate Response Multiple Times ===')

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

    // Step 5: Create dynamic objects
    WebUI.comment('Step 5: Creating dynamic objects...')
    TestObject regenerateButton = new TestObject('dynamic_regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    TestObject thinkingIndicator = new TestObject('dynamic_thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")

    // Step 6: Regenerate multiple times
    int regenerateCount = 3
    for (int i = 1; i <= regenerateCount; i++) {
        WebUI.comment('Regeneration attempt ' + i + ' of ' + regenerateCount)
        
        WebUI.waitForElementVisible(regenerateButton, 5)
        WebUI.click(regenerateButton)
        WebUI.comment('Regenerate button clicked - attempt ' + i)
        
        WebUI.waitForElementVisible(thinkingIndicator, 5)
        WebUI.comment('Thinking indicator appeared - regenerating...')
        
        WebUI.waitForElementNotVisible(thinkingIndicator, 30)
        WebUI.comment('Thinking indicator disappeared - new response received')
        
        WebUI.waitForElementVisible(lastMessage, 5)
        String newResponse = WebUI.getText(lastMessage)
        WebUI.comment('Response ' + i + ' generated: ' + (newResponse.length() > 80 ? newResponse.substring(0, 80) + '...' : newResponse))
        
        WebUI.delay(1)
    }

    WebUI.comment('All ' + regenerateCount + ' regenerations completed successfully')
    WebUI.takeScreenshot('TC14_Regenerate_Multiple_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC14 PASSED')

} catch (Exception e) {
    WebUI.comment('TC14 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC14_Regenerate_Multiple_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}