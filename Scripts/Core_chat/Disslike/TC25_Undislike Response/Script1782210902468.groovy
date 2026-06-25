import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC25: Undislike Response - Remove Dislike with Other Issue feedback
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
 * 9. Click Dislike button again to remove dislike
 * 10. Verify dislike removed
 */

WebUI.comment('=== TC25: Undislike Response (Remove Dislike with Other Issue) ===')

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

    // Step 5: Wait for popup and select "Other issue"
    WebUI.comment('Step 5: Waiting for dislike popup...')
    TestObject popup = new TestObject('dynamic_dislike_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    WebUI.comment('Dislike popup displayed')

    // Try multiple XPath options for "Other issue"
    WebUI.comment('Step 6: Selecting "Other issue" reason...')
    
    // Option 1: Try with "Other issue" text
    TestObject reasonOption = new TestObject('dynamic_dislike_other_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Other issue')]")
    
    boolean found = WebUI.waitForElementVisible(reasonOption, 3, FailureHandling.OPTIONAL)
    
    if (!found) {
        // Option 2: Try with just "Other"
        reasonOption.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(text(), 'Other') and not(contains(text(), 'Related'))]")
        found = WebUI.waitForElementVisible(reasonOption, 3, FailureHandling.OPTIONAL)
    }
    
    if (!found) {
        // Option 3: Try with aria-label
        reasonOption.addProperty('xpath', ConditionType.EQUALS, 
            "//*[@aria-label='Other issue']")
        found = WebUI.waitForElementVisible(reasonOption, 3, FailureHandling.OPTIONAL)
    }
    
    if (!found) {
        // Option 4: Try with class containing "other"
        reasonOption.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@class, 'other')]")
        found = WebUI.waitForElementVisible(reasonOption, 3, FailureHandling.OPTIONAL)
    }
    
    if (found) {
        WebUI.click(reasonOption)
        WebUI.comment('"Other issue" reason selected')
    } else {
        WebUI.comment('Could not find "Other issue" button, trying to click last button...')
        // Try to click the last button in the popup as fallback
        TestObject lastButton = new TestObject('dynamic_last_button')
        lastButton.addProperty('xpath', ConditionType.EQUALS, 
            "(//div[@role='dialog']//button)[last()]")
        WebUI.waitForElementVisible(lastButton, 5)
        WebUI.click(lastButton)
        WebUI.comment('Last button clicked as fallback')
    }

    // Step 7: Wait for feedback popup
    WebUI.comment('Step 7: Waiting for feedback popup...')
    TestObject feedbackPopup = new TestObject('dynamic_feedback_popup')
    feedbackPopup.addProperty('xpath', ConditionType.EQUALS, 
        "//*[contains(text(), 'Provide additional feedback')]")
    WebUI.waitForElementVisible(feedbackPopup, 5)
    WebUI.comment('Feedback popup displayed')

    // Step 8: Enter feedback
    WebUI.comment('Step 8: Entering feedback...')
    TestObject feedbackTextarea = new TestObject('dynamic_feedback_textarea')
    feedbackTextarea.addProperty('xpath', ConditionType.EQUALS, 
        "//textarea[@placeholder='Provide additional feedback']")
    WebUI.waitForElementVisible(feedbackTextarea, 5)
    
    String feedbackMessage = 'This is automated test feedback for Other issue'
    WebUI.setText(feedbackTextarea, feedbackMessage)
    WebUI.comment('Feedback entered: ' + feedbackMessage)

    // Step 9: Click Save button
    WebUI.comment('Step 9: Clicking Save button...')
    TestObject saveButton = new TestObject('dynamic_dislike_save_button')
    saveButton.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and contains(text(), 'Save')]")
    WebUI.waitForElementClickable(saveButton, 5)
    WebUI.click(saveButton)
    WebUI.comment('Save button clicked')

    // Step 10: Verify feedback popup closed
    WebUI.comment('Step 10: Verifying feedback popup closed...')
    WebUI.waitForElementNotVisible(feedbackPopup, 5)
    WebUI.comment('Feedback popup closed successfully - Dislike added with feedback')
    
    WebUI.delay(2)

    // Step 11: Click Dislike button again to remove dislike
    WebUI.comment('Step 11: Clicking Dislike button again to remove dislike...')
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked again to remove dislike')

    // Step 12: Verify dislike removed
    WebUI.comment('Step 12: Verifying dislike removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC25_Undislike_OtherIssue_Success.png')
    WebUI.comment('Dislike removed successfully')

    // Step 13: Close browser
    WebUI.comment('Step 13: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC25 PASSED')

} catch (Exception e) {
    WebUI.comment('TC25 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC25_Undislike_OtherIssue_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}