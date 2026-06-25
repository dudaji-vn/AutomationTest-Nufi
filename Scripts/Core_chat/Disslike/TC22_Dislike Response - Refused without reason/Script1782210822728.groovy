import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC22: Dislike Response - Refused without reason
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Dislike button of the last message
 * 5. Select "Refused without reason" reason
 * 6. Verify dislike success
 */

WebUI.comment('=== TC22: Dislike Response (Refused without reason) ===')

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

    // Step 4: Click Dislike button of the last message
    WebUI.comment('Step 4: Clicking Dislike button of the last message...')
    TestObject dislikeButton = new TestObject('dynamic_dislike_button')
    dislikeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
    
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked')

    // Step 5: Select "Refused without reason" reason
    WebUI.comment('Step 5: Selecting "Refused without reason" reason...')
    TestObject popup = new TestObject('dynamic_dislike_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = new TestObject('dynamic_dislike_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and (text()='Refused without reason' or .='Refused without reason')]")
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Refused without reason" reason selected')

    // Step 6: Verify popup closed
    WebUI.comment('Step 6: Verifying popup closed...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed successfully')
    WebUI.takeScreenshot('TC22_Dislike_RefusedWithoutReason_Success.png')

    // Step 7: Close browser
    WebUI.comment('Step 7: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC22 PASSED')

} catch (Exception e) {
    WebUI.comment('TC22 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC22_Dislike_RefusedWithoutReason_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}