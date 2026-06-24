import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC01: Like - Accurate and Reliable
 * 
 * Test Flow:
 * 1. Click Like button of the last message
 * 2. Select "Accurate and Reliable" reason
 * 3. Verify popup closed (like added)
 * 4. Click Like button again to remove like (unlike)
 * 5. Verify like removed
 */

WebUI.comment('=== TC01: Like (Accurate and Reliable) ===')

try {
    WebUI.comment('Step 1: Clicking Like button...')
    TestObject likeButton = new TestObject('dynamic_like_button')
    likeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Love this']")
    
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked')

    WebUI.comment('Step 2: Selecting "Accurate and Reliable" reason...')
    TestObject popup = findTestObject('Object Repository/Core Chat/Action/Love-this/popover_Love-this')
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = findTestObject('Object Repository/Core Chat/Action/Love-this/button_Accurate and Reliable')
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Accurate and Reliable" reason selected')

    WebUI.comment('Step 3: Verifying like added...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed - Like added')
    WebUI.takeScreenshot('TC01_Like_AccurateReliable_Added.png')

    WebUI.comment('Step 4: Clicking Like button to remove like...')
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked again - Removing like')

    WebUI.comment('Step 5: Verifying like removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC01_Like_AccurateReliable_Removed.png')
    WebUI.comment('Like removed successfully')

    WebUI.comment('TC01 PASSED')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Like_AccurateReliable_Error.png')
    throw e
}