import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC07: Dislike - Other issue with feedback
 */

WebUI.comment('=== TC07: Dislike (Other issue with feedback) ===')

try {
    WebUI.comment('Step 1: Clicking Dislike button...')
    TestObject dislikeButton = new TestObject('dynamic_dislike_button')
    dislikeButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Needs improvement']")
   
    WebUI.waitForElementVisible(dislikeButton, 8)
    WebUI.click(dislikeButton)

    // Step 2: Select "Other issue"
    WebUI.comment('Step 2: Selecting "Other issue" reason...')
    TestObject reasonOption = new TestObject('dynamic_dislike_other_reason')
    reasonOption.addProperty('xpath', ConditionType.EQUALS,
        "//*[@type='button' and contains(text(), 'Other')] | //button[contains(., 'Other')]")
    
    WebUI.waitForElementVisible(reasonOption, 10)
    WebUI.click(reasonOption)

    // Step 3: Enter feedback
    WebUI.comment('Step 3: Entering feedback...')
    TestObject feedbackTextarea = new TestObject('dynamic_feedback_textarea')
    feedbackTextarea.addProperty('xpath', ConditionType.EQUALS,
        "//textarea[@placeholder='Provide additional feedback' or contains(@placeholder, 'feedback')]")
    
    WebUI.waitForElementVisible(feedbackTextarea, 10)
    
    String feedbackMessage = 'This is automated test feedback for Other issue'
    WebUI.setText(feedbackTextarea, feedbackMessage)
    WebUI.comment('Feedback entered successfully')

    // Step 4: Click Save
    WebUI.comment('Step 4: Clicking Save button...')
    TestObject saveButton = new TestObject('dynamic_dislike_save_button')
    saveButton.addProperty('xpath', ConditionType.EQUALS,
        "//button[contains(text(), 'Save') or contains(text(), 'Submit')]")
    
    WebUI.waitForElementClickable(saveButton, 8)
    WebUI.click(saveButton)

    WebUI.comment('Step 5: Verifying feedback submitted...')
    WebUI.waitForElementNotVisible(feedbackTextarea, 10)  // Hoặc chờ popup đóng

    WebUI.delay(2)
    WebUI.takeScreenshot('TC07_Dislike_OtherIssue_Success.png')

    // Phần xóa dislike (nếu cần)
    WebUI.comment('Step 6: Removing dislike...')
    WebUI.click(dislikeButton)
    TestObject deleteButton = findTestObject('Object Repository/Core Chat/Action/Needs improvement/button_Delete')
    WebUI.waitForElementClickable(deleteButton, 8)
    WebUI.click(deleteButton)

    WebUI.comment('✅ TC07 PASSED')
    
} catch (Exception e) {
    WebUI.comment('❌ TC07 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC07_Dislike_OtherIssue_Error.png')
    throw e
}