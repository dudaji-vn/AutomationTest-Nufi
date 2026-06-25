import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC24: Dislike Response - Other issue with feedback
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open existing chat from history
 * 4. Click Dislike button of the last message
 * 5. Select "Other issue" reason
 * 6. Enter feedback in textarea
 * 7. Click Save button
 * 8. Verify feedback submitted successfully
 */

WebUI.comment('=== TC24: Dislike Response (Other issue with feedback) ===')

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

    // Step 5: Select "Other issue" reason - FIXED XPATH
    WebUI.comment('Step 5: Selecting "Other issue" reason...')
    TestObject popup = new TestObject('dynamic_dislike_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    
    // Use more flexible XPath
    TestObject reasonOption = new TestObject('dynamic_dislike_other_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and contains(text(), 'Other')]")
    
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Other issue" reason selected')

    // Step 6: Wait for feedback popup
    WebUI.comment('Step 6: Waiting for feedback popup...')
    TestObject feedbackPopup = new TestObject('dynamic_feedback_popup')
    feedbackPopup.addProperty('xpath', ConditionType.EQUALS, 
        "//*[contains(text(), 'Provide additional feedback')]")
    WebUI.waitForElementVisible(feedbackPopup, 5)
    WebUI.comment('Feedback popup displayed')

    // Step 7: Enter feedback
    WebUI.comment('Step 7: Entering feedback...')
    TestObject feedbackTextarea = new TestObject('dynamic_feedback_textarea')
    feedbackTextarea.addProperty('xpath', ConditionType.EQUALS, 
        "//textarea[@placeholder='Provide additional feedback']")
    WebUI.waitForElementVisible(feedbackTextarea, 5)
    
    String feedbackMessage = 'This is automated test feedback for Other issue'
    WebUI.setText(feedbackTextarea, feedbackMessage)
    WebUI.comment('Feedback entered: ' + feedbackMessage)

    // Step 8: Click Save button
    WebUI.comment('Step 8: Clicking Save button...')
    TestObject saveButton = new TestObject('dynamic_dislike_save_button')
    saveButton.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and contains(text(), 'Save')]")
    WebUI.waitForElementClickable(saveButton, 5)
    WebUI.click(saveButton)
    WebUI.comment('Save button clicked')

    // Step 9: Verify feedback popup closed
    WebUI.comment('Step 9: Verifying feedback popup closed...')
    WebUI.waitForElementNotVisible(feedbackPopup, 5)
    WebUI.comment('Feedback popup closed successfully')
    
    WebUI.delay(2)
    WebUI.takeScreenshot('TC24_Dislike_OtherIssue_Success.png')

    // Step 10: Close browser
    WebUI.comment('Step 10: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC24 PASSED')

} catch (Exception e) {
    WebUI.comment('TC24 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC24_Dislike_OtherIssue_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}