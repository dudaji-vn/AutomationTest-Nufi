import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC08: Undislike - Remove Dislike (Quick toggle)
 * 
 * Test Flow:
 * 1. Click Dislike button of the last message
 * 2. Select "Lacked useful information" reason
 * 3. Verify popup closed (dislike added)
 * 4. Click Dislike button again to remove dislike (undislike)
 * 5. Verify dislike removed
 */

WebUI.comment('=== TC08: Undislike (Remove Dislike) ===')

try {
    WebUI.comment('Step 1: Clicking Dislike button...')
    TestObject dislikeButton = new TestObject('dynamic_dislike_button')
    dislikeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
    
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked')

    WebUI.comment('Step 2: Selecting "Lacked useful information" reason...')
    TestObject popup = new TestObject('dynamic_dislike_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = new TestObject('dynamic_dislike_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and (text()='Lacked useful information' or .='Lacked useful information')]")
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Lacked useful information" reason selected')

    WebUI.comment('Step 3: Verifying dislike added...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed - Dislike added')
    WebUI.takeScreenshot('TC08_Undislike_Added.png')

    WebUI.comment('Step 4: Clicking Dislike button to remove dislike...')
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked again - Removing dislike')

    WebUI.comment('Step 5: Verifying dislike removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC08_Undislike_Removed.png')
    WebUI.comment('Dislike removed successfully')

    WebUI.comment('TC08 PASSED')

} catch (Exception e) {
    WebUI.comment('TC08 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC08_Undislike_Error.png')
    throw e
}