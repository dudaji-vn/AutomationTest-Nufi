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
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Regenerate button 3 times
 * 7. Verify each regeneration completes
 */

WebUI.comment('=== TC14: Regenerate Response Multiple Times ===')

try {
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(GlobalVariable.Base_URL)

    WebUI.comment('Step 4: Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'('Nufi', 'Qwen2.5-0.5B')

    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Regenerate multiple times test'
    String originalResponse = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Original response received')

    WebUI.comment('Step 6: Finding Regenerate button...')
    TestObject regenerateButton = new TestObject('dynamic_regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Chatting/thinking')
    TestObject lastMessage = findTestObject('Object Repository/Core Chat/Chatting/message-content')

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

    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC14 PASSED')

} catch (Exception e) {
    WebUI.comment('TC14 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC14_Regenerate_Multiple_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}