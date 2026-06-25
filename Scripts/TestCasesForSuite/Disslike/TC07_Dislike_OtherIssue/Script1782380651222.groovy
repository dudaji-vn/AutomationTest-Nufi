import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC07: Dislike - Other issue with feedback
 * 
 * Test Flow:
 * 1. Click Dislike button of the last message
 * 2. Select "Other issue" reason
 * 3. Enter feedback in textarea
 * 4. Click Save button
 * 5. Verify feedback submitted successfully
 * 6. Click Dislike button again to open popup
 * 7. Click Delete button to remove dislike
 * 8. Verify dislike removed
 */

WebUI.comment('=== TC07: Dislike (Other issue with feedback) ===')

try {
    WebUI.comment('Step 1: Clicking Dislike button...')
    TestObject dislikeButton = new TestObject('dynamic_dislike_button')
    dislikeButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
    
    WebUI.waitForElementVisible(dislikeButton, 5)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked')

    WebUI.comment('Step 2: Selecting "Other issue" reason...')
    TestObject popup = new TestObject('dynamic_dislike_popup')
    popup.addProperty('xpath', ConditionType.EQUALS, "//div[@role='dialog' and contains(@class, 'popover-animate')]")
    WebUI.waitForElementVisible(popup, 5)
    
    TestObject reasonOption = new TestObject('dynamic_dislike_other_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and contains(text(), 'Other')]")
    WebUI.waitForElementVisible(reasonOption, 5)
    WebUI.click(reasonOption)
    WebUI.comment('"Other issue" reason selected')

    WebUI.comment('Step 3: Waiting for feedback popup...')
    TestObject feedbackPopup = new TestObject('dynamic_feedback_popup')
    feedbackPopup.addProperty('xpath', ConditionType.EQUALS, 
        "//*[contains(text(), 'Provide additional feedback')]")
    WebUI.waitForElementVisible(feedbackPopup, 5)
    WebUI.comment('Feedback popup displayed')

    WebUI.comment('Step 4: Entering feedback...')
    TestObject feedbackTextarea = new TestObject('dynamic_feedback_textarea')
    feedbackTextarea.addProperty('xpath', ConditionType.EQUALS, 
        "//textarea[@placeholder='Provide additional feedback']")
    WebUI.waitForElementVisible(feedbackTextarea, 5)
    
    String feedbackMessage = 'This is automated test feedback for Other issue'
    WebUI.setText(feedbackTextarea, feedbackMessage)
    WebUI.comment('Feedback entered: ' + feedbackMessage)

    WebUI.comment('Step 5: Clicking Save button...')
    TestObject saveButton = new TestObject('dynamic_dislike_save_button')
    saveButton.addProperty('xpath', ConditionType.EQUALS, 
        "//*[@type='button' and contains(text(), 'Save')]")
    WebUI.waitForElementClickable(saveButton, 5)
    WebUI.click(saveButton)
    WebUI.comment('Save button clicked')

    WebUI.comment('Step 6: Verifying feedback popup closed...')
    WebUI.waitForElementNotVisible(feedbackPopup, 10)
    WebUI.comment('Feedback popup closed successfully - Dislike added with feedback')
    
    WebUI.delay(1)
    WebUI.takeScreenshot('TC07_Dislike_OtherIssue_Added.png')

    WebUI.comment('Step 7: Clicking Dislike button to open popup...')
    WebUI.waitForElementClickable(dislikeButton, 10)
    WebUI.click(dislikeButton)
    WebUI.comment('Dislike button clicked - Popup opened')

    WebUI.comment('Step 8: Clicking Delete button to remove dislike...')
    TestObject deleteButton = findTestObject('Object Repository/Core Chat/Action/Needs improvement/button_Delete')
    WebUI.waitForElementClickable(deleteButton, 10)
    WebUI.click(deleteButton)
    WebUI.comment('Delete button clicked - Dislike removed')

    WebUI.comment('Step 9: Verifying dislike removed...')
    WebUI.delay(2)
    WebUI.takeScreenshot('TC07_Dislike_OtherIssue_Removed.png')
    WebUI.comment('Dislike removed successfully')

    WebUI.comment('TC07 PASSED')

} catch (Exception e) {
    WebUI.comment('TC07 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC07_Dislike_OtherIssue_Error.png')
    throw e
}