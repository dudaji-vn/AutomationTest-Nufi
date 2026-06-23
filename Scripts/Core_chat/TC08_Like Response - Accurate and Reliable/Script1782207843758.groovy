import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC08: Like Response - Accurate and Reliable
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Like button of the last message
 * 7. Select "Accurate and Reliable" reason
 * 8. Verify like success
 */

WebUI.comment('=== TC08: Like Response (Accurate and Reliable) ===')

try {
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(GlobalVariable.Base_URL)

    WebUI.comment('Step 4: Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'('Nufi', 'Qwen2.5-0.5B')

    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Like test message'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    WebUI.comment('Step 6: Clicking Like button of the last message...')
    TestObject likeButton = new TestObject('dynamic_like_button')
    likeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Love this']")
    
    WebUI.waitForElementVisible(likeButton, 5)
    WebUI.click(likeButton)
    WebUI.comment('Like button clicked')

    WebUI.comment('Step 7: Selecting "Accurate and Reliable" reason...')
    TestObject popup = findTestObject('Object Repository/Core Chat/Love-this/popup_Love-this')
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = findTestObject('Object Repository/Core Chat/Love-this/button_Accurate and Reliable')
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Accurate and Reliable" reason selected')

    WebUI.comment('Step 8: Verifying popup closed...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed successfully')
    WebUI.takeScreenshot('TC08_Like_AccurateReliable_Success.png')

    WebUI.comment('Step 9: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC08 PASSED')

} catch (Exception e) {
    WebUI.comment('TC08 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC08_Like_AccurateReliable_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}