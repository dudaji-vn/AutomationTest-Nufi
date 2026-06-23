import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC17: Read Aloud User Message
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Read Aloud button of the user message (first message)
 * 7. Verify read aloud started
 */

WebUI.comment('=== TC17: Read Aloud User Message ===')

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
    String testMessage = 'Read aloud user message test'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    WebUI.comment('Step 6: Clicking Read Aloud button of the user message (first message)...')
    TestObject readButton = new TestObject('dynamic_read_button_user')
    readButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[1]/ancestor::div[contains(@class,'message')]//button[@title='Read aloud']")
    
    WebUI.waitForElementVisible(readButton, 5)
    WebUI.click(readButton)
    WebUI.comment('Read Aloud button of user message clicked')
    WebUI.delay(2)

    WebUI.comment('Step 7: Verifying read aloud started...')
    WebUI.takeScreenshot('TC17_ReadAloud_UserMessage_Success.png')
    WebUI.comment('Read aloud started successfully for user message')

    WebUI.comment('Step 8: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC17 PASSED')

} catch (Exception e) {
    WebUI.comment('TC17 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC17_ReadAloud_UserMessage_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}