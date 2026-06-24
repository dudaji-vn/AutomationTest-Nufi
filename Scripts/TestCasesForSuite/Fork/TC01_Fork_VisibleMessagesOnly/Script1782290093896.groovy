import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC01: Fork - Visible Messages Only
 * 
 * Test Flow:
 * 1. Click Fork menu button
 * 2. Click "Visible messages only" option
 * 3. Verify fork success
 */

WebUI.comment('=== TC01: Fork (Visible Messages Only) ===')

try {
    WebUI.comment('Clicking Fork menu button...')
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

    WebUI.comment('Clicking "Visible messages only" option...')
    TestObject visibleMessagesOption = new TestObject('dynamic_visible_messages_option')
    visibleMessagesOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Visible messages only']")
    
    WebUI.waitForElementVisible(visibleMessagesOption, 5)
    WebUI.click(visibleMessagesOption)
    WebUI.comment('"Visible messages only" option clicked')

    WebUI.comment('Verifying fork success...')
    TestObject forkSuccessMessage = findTestObject('Object Repository/Core Chat/Action/Fork/Fork Test Message')

    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed')
    WebUI.takeScreenshot('TC01_Fork_VisibleMessagesOnly_Success.png')

    WebUI.comment('TC01 PASSED')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Fork_VisibleMessagesOnly_Error.png')
    throw e
}