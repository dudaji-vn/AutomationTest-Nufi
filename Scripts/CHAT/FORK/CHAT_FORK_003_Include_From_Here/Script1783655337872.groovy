import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC06: Fork Test - Include From Here
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Fork menu button of the last message
 * 5. Click "Include all to/from here" option
 * 6. Verify fork success message displayed
 */

WebUI.comment('=== TC06: Fork Test (Include From Here) ===')

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

    // Step 5: Click "Include all to/from here" option
    WebUI.comment('Step 5: Clicking "Include all to/from here" option...')
    
    TestObject includeFromHereOption = new TestObject('dynamic_include_from_here_option')
    includeFromHereOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Include all to/from here']")
    
    WebUI.waitForElementVisible(includeFromHereOption, 5)
    WebUI.click(includeFromHereOption)
    WebUI.comment('"Include all to/from here" option clicked')

    // Step 6: Verify fork success message displayed
    WebUI.comment('Step 6: Verifying fork success message...')
    
    TestObject forkSuccessMessage = new TestObject('dynamic_fork_success_message')
    forkSuccessMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(text(),'Successfully forked')]")
    
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC06_Fork_IncludeFromHere_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC06 PASSED')

} catch (Exception e) {
    WebUI.comment('TC06 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC06_Fork_IncludeFromHere_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}