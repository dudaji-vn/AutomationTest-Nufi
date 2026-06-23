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
 * 3. Open new conversation
 * 4. Select Gemini endpoint + model
 * 5. Send a message and get response
 * 6. Click Fork menu button of the last message
 * 7. Click "Visible messages only" option
 * 8. Verify fork success message displayed
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

    // Step 3: Open new conversation
    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(
        GlobalVariable.Base_URL
    )

    // Step 4: Select endpoint + model
    WebUI.comment('Step 4: Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'(
        'Nufi',
        'Qwen2.5-0.5B'
    )

    // Step 5: Send message and get response
    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Fork test message - visible only'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    // Step 6: Click Fork menu button of the last message
    WebUI.comment('Step 6: Clicking Fork menu button of the last message...')
    
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

    // Step 7: Click "Visible messages only" option
    WebUI.comment('Step 7: Clicking "Visible messages only" option...')
    
    TestObject visibleMessagesOption = new TestObject('dynamic_visible_messages_option')
    visibleMessagesOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Visible messages only']")
    
    WebUI.waitForElementVisible(visibleMessagesOption, 5)
    WebUI.click(visibleMessagesOption)
    WebUI.comment('"Visible messages only" option clicked')

    // Step 8: Verify fork success message displayed
    WebUI.comment('Step 8: Verifying fork success message...')
    
    TestObject forkSuccessMessage = findTestObject('Object Repository/Core Chat/Page_Fork Test Message/div_Successfully forked conversation')
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC04_Fork_VisibleMessagesOnly_Success.png')

    // Step 9: Close browser
    WebUI.comment('Step 9: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC04 PASSED')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Fork_VisibleMessagesOnly_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}