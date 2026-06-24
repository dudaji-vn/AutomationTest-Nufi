import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC03: Edit Response Test
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Gemini endpoint + model
 * 5. Send a message and get response
 * 6. Click Edit button of target message using index variable
 * 7. Edit the response text
 * 8. Save & Submit
 * 9. Verify edited text appears in target message
 */

def TARGET_INDEX = 1

WebUI.comment('=== TC03: Edit Response Test (Message index: ' + TARGET_INDEX + ') ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(
        GlobalVariable.Base_URL
    )

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )

    // Step 3: Open new conversation
    WebUI.comment('Step 3: Opening new conversation...')
    CustomKeywords.'keywords.ChatKeywords.openNewConversation'(
        GlobalVariable.Base_URL
    )

    // Step 4: Select endpoint + model
    WebUI.comment('Step 4: Selecting Gemini endpoint and model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'(
        'Nufi',
        'Qwen2.5-0.5B'
    )

    // Step 5: Send message and get response
    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Test message for edit response'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    // Step 6: Click Edit button of target message
    WebUI.comment('Step 6: Clicking Edit button of message index: ' + TARGET_INDEX)
    
    String editButtonXpath = "(//div[contains(@class,'message-content')])[" + TARGET_INDEX + "]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Edit') or contains(@title, 'Edit')]"
    
    TestObject editButton = new TestObject('dynamic_edit_button')
    editButton.addProperty('xpath', ConditionType.EQUALS, editButtonXpath)
    
    WebUI.waitForElementVisible(editButton, 30)
    WebUI.click(editButton)
    WebUI.comment('Edit button of message ' + TARGET_INDEX + ' clicked')
    WebUI.delay(2)

    // Step 7: Edit response text using data-testid
    WebUI.comment('Step 7: Editing response text...')
    TestObject textareaResponse = new TestObject('dynamic_edit_textarea')
    textareaResponse.addProperty('xpath', ConditionType.EQUALS, 
        "//textarea[@data-testid='message-text-editor']")
    
    WebUI.waitForElementVisible(textareaResponse, 10)
    String originalText = WebUI.getAttribute(textareaResponse, 'value')
    WebUI.comment('Original text length: ' + (originalText == null ? 0 : originalText.length()))
    
    String editedText = (originalText != null && originalText.trim().length() > 0) 
        ? originalText + ' [edited by automated test]' 
        : '[edited by automated test]'
    
    WebUI.clearText(textareaResponse)
    WebUI.setText(textareaResponse, editedText)
    WebUI.comment('Text edited successfully')
    WebUI.delay(1)

    // Step 8: Click Save & Submit button
    WebUI.comment('Step 8: Saving edited response...')
    TestObject saveButton = new TestObject('dynamic_save_button')
    saveButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(),'Save & Submit')]")
    
    WebUI.waitForElementClickable(saveButton, 10)
    WebUI.click(saveButton)
    WebUI.comment('Save & Submit button clicked')
    WebUI.delay(3)

    // Step 9: Verify edited text appears in target message
    WebUI.comment('Step 9: Verifying edited text in message ' + TARGET_INDEX + '...')
    
    String targetMessageXpath = "(//div[contains(@class,'message-content')])[" + TARGET_INDEX + "]"
    TestObject targetMessage = new TestObject('dynamic_target_message')
    targetMessage.addProperty('xpath', ConditionType.EQUALS, targetMessageXpath)
    
    WebUI.waitForElementVisible(targetMessage, 10)
    String updatedResponse = WebUI.getText(targetMessage)
    WebUI.comment('Updated message: ' + (updatedResponse.length() > 100 ? updatedResponse.substring(0, 100) + '...' : updatedResponse))
    
    if (updatedResponse.contains('edited by automated test')) {
        WebUI.comment('Edit saved successfully in message ' + TARGET_INDEX)
        WebUI.takeScreenshot('TC03_EditMessage_Success.png')
    } else {
        WebUI.comment('Edited text not found in message ' + TARGET_INDEX)
        WebUI.takeScreenshot('TC03_EditMessage_Failure.png')
        throw new Exception('Edited text not visible in message ' + TARGET_INDEX)
    }

    // Step 10: Close browser
    WebUI.comment('Step 10: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC03 PASSED')

} catch (Exception e) {
    WebUI.comment('TC03 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC03_EditMessage_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}