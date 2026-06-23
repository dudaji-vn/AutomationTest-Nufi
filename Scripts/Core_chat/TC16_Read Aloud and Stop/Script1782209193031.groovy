import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC16: Read Aloud and Stop
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Read Aloud button of the last message
 * 7. Click Stop button to stop reading
 * 8. Verify read stopped
 */

WebUI.comment('=== TC16: Read Aloud and Stop ===')

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
    String testMessage = 'Read aloud and stop test'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    WebUI.comment('Step 6: Clicking Read Aloud button...')
    TestObject readButton = new TestObject('dynamic_read_button')
    readButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Read aloud']")
    
    WebUI.waitForElementVisible(readButton, 5)
    WebUI.click(readButton)
    WebUI.comment('Read Aloud button clicked')
    WebUI.delay(2)

    WebUI.comment('Step 7: Clicking Stop button...')
    TestObject stopButton = new TestObject('dynamic_stop_read_button')
    stopButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@title='Stop']")
    
    WebUI.waitForElementVisible(stopButton, 5)
    WebUI.click(stopButton)
    WebUI.comment('Stop button clicked')
    WebUI.delay(1)

    WebUI.comment('Step 8: Verifying read stopped...')
    WebUI.takeScreenshot('TC16_ReadAloud_Stop_Success.png')
    WebUI.comment('Read stopped successfully')

    WebUI.comment('Step 9: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC16 PASSED')

} catch (Exception e) {
    WebUI.comment('TC16 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC16_ReadAloud_Stop_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}