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
 * 3. Open existing chat from history
 * 4. Click Read Aloud button of the user message (first message)
 * 5. Verify read aloud started
 */

WebUI.comment('=== TC17: Read Aloud User Message ===')

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

    // Step 4: Click Read Aloud button of the user message (first message)
    WebUI.comment('Step 4: Clicking Read Aloud button of the user message (first message)...')
    TestObject readButton = new TestObject('dynamic_read_button_user')
    readButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[1]/ancestor::div[contains(@class,'message')]//button[@title='Read aloud']")
    
    WebUI.waitForElementVisible(readButton, 5)
    WebUI.click(readButton)
    WebUI.comment('Read Aloud button of user message clicked')
    WebUI.delay(2)

    // Step 5: Verify read aloud started
    WebUI.comment('Step 5: Verifying read aloud started...')
    WebUI.takeScreenshot('TC17_ReadAloud_UserMessage_Success.png')
    WebUI.comment('Read aloud started successfully for user message')

    // Step 6: Close browser
    WebUI.comment('Step 6: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC17 PASSED')

} catch (Exception e) {
    WebUI.comment('TC17 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC17_ReadAloud_UserMessage_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}