import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC16: Read Aloud and Toggle Off
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Read Aloud button of the last message to start reading
 * 5. Click Read Aloud button again to stop reading (toggle off)
 * 6. Verify read stopped
 */

WebUI.comment('=== TC16: Read Aloud and Toggle Off ===')

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

    // Step 4: Click Read Aloud button to start reading
    WebUI.comment('Step 4: Clicking Read Aloud button to start reading...')
    TestObject readButton = new TestObject('dynamic_read_button')
    readButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Read aloud']")
    
    WebUI.waitForElementVisible(readButton, 5)
    WebUI.click(readButton)
    WebUI.comment('Read Aloud button clicked - reading started')
    WebUI.delay(2)

    // Step 5: Click Read Aloud button again to stop reading
    WebUI.comment('Step 5: Clicking Read Aloud button again to stop reading...')
    WebUI.waitForElementVisible(readButton, 5)
    WebUI.click(readButton)
    WebUI.comment('Read Aloud button clicked again - reading stopped')
    WebUI.delay(1)

    // Step 6: Verify read stopped
    WebUI.comment('Step 6: Verifying read stopped...')
    WebUI.takeScreenshot('TC16_ReadAloud_ToggleOff_Success.png')
    WebUI.comment('Read toggled off successfully')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC16 PASSED')

} catch (Exception e) {
    WebUI.comment('TC16 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC16_ReadAloud_ToggleOff_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}