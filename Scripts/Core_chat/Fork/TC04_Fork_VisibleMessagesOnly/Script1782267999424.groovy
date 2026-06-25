import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC04: Fork Test - Visible Messages Only
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Fork menu button of the last message
 * 5. Click "Visible messages only" option
 * 6. Verify fork success message displayed
 */

WebUI.comment('=== TC04: Fork Test (Visible Messages Only) ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )

    // Step 3: Open existing chat from history
    WebUI.comment('Step 3: Opening existing chat from history...')
    String chatName = CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    WebUI.comment('Opened chat: ' + chatName)

    // Step 4: Click Fork menu button of the last message
    WebUI.comment('Step 4: Clicking Fork menu button of the last message...')
    
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

    // Step 5: Click "Visible messages only" option
    WebUI.comment('Step 5: Clicking "Visible messages only" option...')
    
    TestObject visibleMessagesOption = new TestObject('dynamic_visible_messages_option')
    visibleMessagesOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Visible messages only']")
    
    WebUI.waitForElementVisible(visibleMessagesOption, 5)
    WebUI.click(visibleMessagesOption)
    WebUI.comment('"Visible messages only" option clicked')

    // Step 6: Verify fork success message displayed
    WebUI.comment('Step 6: Verifying fork success message...')
    
    TestObject forkSuccessMessage = new TestObject('dynamic_fork_success_message')
    forkSuccessMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(text(),'Successfully forked')]")
    
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC04_Fork_VisibleMessagesOnly_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC04 PASSED')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Fork_VisibleMessagesOnly_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}