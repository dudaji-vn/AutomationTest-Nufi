import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC05: Fork Test - Include Related Branches
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Fork menu button of the last message
 * 5. Click "Include related branches" option
 * 6. Verify fork success message displayed
 */

WebUI.comment('=== TC05: Fork Test (Include Related Branches) ===')

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

    // Step 5: Click "Include related branches" option
    WebUI.comment('Step 5: Clicking "Include related branches" option...')
    
    TestObject includeRelatedOption = new TestObject('dynamic_include_related_option')
    includeRelatedOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Include related branches']")
    
    WebUI.waitForElementVisible(includeRelatedOption, 5)
    WebUI.click(includeRelatedOption)
    WebUI.comment('"Include related branches" option clicked')

    // Step 6: Verify fork success message displayed
    WebUI.comment('Step 6: Verifying fork success message...')
    
    TestObject forkSuccessMessage = new TestObject('dynamic_fork_success_message')
    forkSuccessMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(text(),'Successfully forked')]")
    
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC05_Fork_IncludeRelated_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC05 PASSED')

} catch (Exception e) {
    WebUI.comment('TC05 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC05_Fork_IncludeRelated_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}