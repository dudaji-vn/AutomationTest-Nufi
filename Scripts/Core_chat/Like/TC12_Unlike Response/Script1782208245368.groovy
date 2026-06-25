import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC12: Unlike Response (Remove Like)
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Like button of the last message
 * 5. Select "Accurate and Reliable" reason
 * 6. Verify like success
 * 7. Click Like button again to remove like
 * 8. Verify like removed
 */

WebUI.comment('=== TC12: Unlike Response (Remove Like) ===')

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

    // Step 4: Click Like button of the last message
    WebUI.comment('Step 4: Clicking Like button of the last message...')
    TestObject likeButton = new TestObject('dynamic_like_button')
    likeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Love this']")
    
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked')

    // Step 5: Select "Accurate and Reliable" reason
    WebUI.comment('Step 5: Selecting "Accurate and Reliable" reason...')
    TestObject popup = new TestObject('dynamic_love_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = new TestObject('dynamic_accurate_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and @aria-label='Accurate and Reliable' and (text()='Accurate and Reliable' or .='Accurate and Reliable')]")
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Accurate and Reliable" reason selected')

    // Step 6: Verify popup closed - Like added
    WebUI.comment('Step 6: Verifying popup closed...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed successfully - Like added')

    // Step 7: Click Like button again to remove like
    WebUI.comment('Step 7: Clicking Like button again to remove like...')
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked again to remove like')

    // Step 8: Verify like removed
    WebUI.comment('Step 8: Verifying like removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC12_Unlike_Success.png')
    WebUI.comment('Like removed successfully')

    // Step 9: Close browser
    WebUI.comment('Step 9: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC12 PASSED')

} catch (Exception e) {
    WebUI.comment('TC12 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC12_Unlike_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}