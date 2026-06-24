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
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Regenerate button of the last message
 * 7. Wait for new response
 * 8. Verify new response is generated
 */

WebUI.comment('=== TC13: Regenerate Response ===')

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
    String testMessage = 'Regenerate test message'
    String originalResponse = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Original response: ' + (originalResponse.length() > 100 ? originalResponse.substring(0, 100) + '...' : originalResponse))

    WebUI.comment('Step 6: Clicking Regenerate button of the last message...')
    TestObject regenerateButton = new TestObject('dynamic_regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    WebUI.waitForElementVisible(regenerateButton, 5)
    WebUI.click(regenerateButton)
    WebUI.comment('Regenerate button clicked')

    WebUI.comment('Step 7: Waiting for new response...')
    TestObject thinkingIndicator = findTestObject('Object Repository/Core Chat/Chatting/thinking')
    WebUI.waitForElementVisible(thinkingIndicator, 5)
    WebUI.comment('Thinking indicator appeared - regenerating...')
    
    WebUI.waitForElementNotVisible(thinkingIndicator, 30)
    WebUI.comment('Thinking indicator disappeared - new response received')

    WebUI.comment('Step 8: Verifying new response...')
    TestObject lastMessage = findTestObject('Object Repository/Core Chat/Chatting/message-content')
    WebUI.waitForElementVisible(lastMessage, 5)
    
    String newResponse = WebUI.getText(lastMessage)
    WebUI.comment('New response: ' + (newResponse.length() > 100 ? newResponse.substring(0, 100) + '...' : newResponse))
    
    if (newResponse != null && !newResponse.trim().isEmpty()) {
        WebUI.comment('New response generated successfully')
        WebUI.takeScreenshot('TC13_Regenerate_Success.png')
    } else {
        throw new Exception('New response is empty')
    }

    WebUI.comment('Step 9: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC13 PASSED')

} catch (Exception e) {
    WebUI.comment('TC13 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC13_Regenerate_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}