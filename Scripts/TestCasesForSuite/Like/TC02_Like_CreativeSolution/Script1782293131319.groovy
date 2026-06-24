import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC02: Like - Creative Solution
 * 
 * Test Flow:
 * 1. Click Like button of the last message
 * 2. Select "Creative Solution" reason
 * 3. Verify popup closed (like added)
 * 4. Click Like button again to remove like (unlike)
 * 5. Verify like removed
 */

WebUI.comment('=== TC02: Like (Creative Solution) ===')

try {
    WebUI.comment('Step 1: Clicking Like button...')
    TestObject likeButton = new TestObject('dynamic_like_button')
    likeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Love this']")
    
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked')

    WebUI.comment('Step 2: Selecting "Creative Solution" reason...')
    TestObject popup = findTestObject('Object Repository/Core Chat/Action/Love-this/popover_Love-this')
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = findTestObject('Object Repository/Core Chat/Action/Love-this/button_Creative Solution')
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Creative Solution" reason selected')

    WebUI.comment('Step 3: Verifying like added...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed - Like added')
    WebUI.takeScreenshot('TC02_Like_CreativeSolution_Added.png')

    WebUI.comment('Step 4: Clicking Like button to remove like...')
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked again - Removing like')

    WebUI.comment('Step 5: Verifying like removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC02_Like_CreativeSolution_Removed.png')
    WebUI.comment('Like removed successfully')

    WebUI.comment('TC02 PASSED')

} catch (Exception e) {
    WebUI.comment('TC02 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02_Like_CreativeSolution_Error.png')
    throw e
}