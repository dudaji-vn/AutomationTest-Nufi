import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC25: Undislike Response (Remove Dislike)
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Dislike button of the last message
 * 7. Select "Other issue" reason
 * 8. Verify dislike success
 * 9. Click Dislike button again to remove dislike
 * 10. Verify dislike removed
 */

WebUI.comment('=== TC25: Undislike Response (Remove Dislike) ===')

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
    String testMessage = 'Undislike test message'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    WebUI.comment('Step 6: Clicking Dislike button of the last message...')
    TestObject dislikeButton = new TestObject('dynamic_dislike_button')
    dislikeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
    
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked')

    WebUI.comment('Step 7: Selecting "Other issue" reason...')
    TestObject popup = findTestObject('Object Repository/Core Chat/Needs improvement/popover_Needs improvement')
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = findTestObject('Object Repository/Core Chat/Needs improvement/button_Other issue')
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Other issue" reason selected')

    WebUI.comment('Step 8: Verifying popup closed...')
    WebUI.waitForElementNotVisible(popup, 5)
    WebUI.comment('Popup closed successfully - Dislike added')

    WebUI.comment('Step 9: Clicking Dislike button again to remove dislike...')
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked again to remove dislike')

    WebUI.comment('Step 10: Verifying dislike removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC25_Undislike_Success.png')
    WebUI.comment('Dislike removed successfully')

    WebUI.comment('Step 11: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC25 PASSED')

} catch (Exception e) {
    WebUI.comment('TC25 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC25_Undislike_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}