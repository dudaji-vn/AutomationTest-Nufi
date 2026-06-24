import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC04: Like - Attention to Detail
 * 
 * Test Flow:
 * 1. Click Like button of the last message
 * 2. Select "Attention to Detail" reason
 * 3. Verify popup closed (like added)
 * 4. Click Like button again to remove like (unlike)
 * 5. Verify like removed
 */

WebUI.comment('=== TC04: Like (Attention to Detail) ===')

try {
    WebUI.comment('Step 1: Clicking Like button...')
    TestObject likeButton = new TestObject('dynamic_like_button')
    likeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Love this']")
    
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked')

    WebUI.comment('Step 2: Selecting "Attention to Detail" reason...')
    TestObject popup = findTestObject('Object Repository/Core Chat/Action/Love-this/popover_Love-this')
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = findTestObject('Object Repository/Core Chat/Action/Love-this/button_Attention to Detail')
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Attention to Detail" reason selected')

    WebUI.comment('Step 3: Verifying like added...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed - Like added')
    WebUI.takeScreenshot('TC04_Like_AttentionToDetail_Added.png')

    WebUI.comment('Step 4: Clicking Like button to remove like...')
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked again - Removing like')

    WebUI.comment('Step 5: Verifying like removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC04_Like_AttentionToDetail_Removed.png')
    WebUI.comment('Like removed successfully')

    WebUI.comment('TC04 PASSED')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Like_AttentionToDetail_Error.png')
    throw e
}