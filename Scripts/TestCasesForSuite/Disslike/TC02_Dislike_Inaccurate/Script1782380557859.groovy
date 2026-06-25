import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC02: Dislike - Inaccurate or incorrect answer
 * 
 * Test Flow:
 * 1. Click Dislike button of the last message
 * 2. Select "Inaccurate or incorrect answer" reason
 * 3. Verify popup closed (dislike added)
 * 4. Click Dislike button again to open popup
 * 5. Click Delete button to remove dislike
 * 6. Verify dislike removed
 */

WebUI.comment('=== TC02: Dislike (Inaccurate or incorrect answer) ===')

try {
    WebUI.comment('Step 1: Clicking Dislike button...')
    TestObject dislikeButton = new TestObject('dynamic_dislike_button')
    dislikeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
    
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked')

    WebUI.comment('Step 2: Selecting "Inaccurate or incorrect answer" reason...')
    TestObject popup = new TestObject('dynamic_dislike_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = new TestObject('dynamic_dislike_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and (text()='Inaccurate or incorrect answer' or .='Inaccurate or incorrect answer')]")
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Inaccurate or incorrect answer" reason selected')

    WebUI.comment('Step 3: Verifying dislike added...')
    WebUI.waitForElementNotVisible(popup, 10)
    WebUI.comment('Popup closed - Dislike added')
    WebUI.takeScreenshot('TC02_Dislike_Inaccurate_Added.png')
    
    WebUI.delay(1)

    WebUI.comment('Step 4: Clicking Dislike button to open popup...')
    WebUI.waitForElementClickable(dislikeButton, 10)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked - Popup opened')

    WebUI.comment('Step 5: Clicking Delete button to remove dislike...')
    TestObject deleteButton = findTestObject('Object Repository/Core Chat/Action/Needs improvement/button_Delete')
    WebUI.waitForElementClickable(deleteButton, 10)
    WebUI.click(deleteButton)
    WebUI.comment('Delete button clicked - Dislike removed')

    WebUI.comment('Step 6: Verifying dislike removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC02_Dislike_Inaccurate_Removed.png')
    WebUI.comment('Dislike removed successfully')

    WebUI.comment('TC02 PASSED')

} catch (Exception e) {
    WebUI.comment('TC02 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02_Dislike_Inaccurate_Error.png')
    throw e
}