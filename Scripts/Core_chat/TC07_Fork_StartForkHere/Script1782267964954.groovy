import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC07: Fork Test - Start From Here with All Options
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open new conversation
 * 4. Select Nufi endpoint + Qwen model
 * 5. Send a message and get response
 * 6. Click Fork menu button of the last message
 * 7. Check "Start fork here" checkbox (uncheck Remember if checked)
 * 8. Test with "Visible messages only" option
 * 9. Test with "Include related branches" option
 * 10. Test with "Include all to/from here" option
 */

WebUI.comment('=== TC07: Fork Test (Start From Here with All Options) ===')

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
    WebUI.comment('Step 4: Selecting Nufi endpoint and Qwen model...')
    CustomKeywords.'keywords.ChatKeywords.selectEndpointAndModel'(
        'Nufi',
        'Qwen2.5-0.5B'
    )

    // Step 5: Send message and get response
    WebUI.comment('Step 5: Sending test message...')
    String testMessage = 'Fork test message - start from here'
    String response = CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.comment('Response received: ' + (response.length() > 100 ? response.substring(0, 100) + '...' : response))

    // Step 6: Click Fork menu button of the last message
    WebUI.comment('Step 6: Clicking Fork menu button of the last message...')
    
    TestObject forkMenuButton = new TestObject('dynamic_fork_menu_button')
    forkMenuButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[contains(@aria-label, 'Fork') or contains(@title, 'Fork')]")
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked')

    // Step 7: Check "Start fork here" checkbox and uncheck "Remember"
    WebUI.comment('Step 7: Checking "Start fork here" checkbox...')
    
    TestObject startForkCheckbox = new TestObject('dynamic_start_fork_checkbox')
    startForkCheckbox.addProperty('xpath', ConditionType.EQUALS, 
        "//label[contains(text(),'Start fork here')]//input[@type='checkbox']")
    
    WebUI.waitForElementVisible(startForkCheckbox, 5)
    WebUI.click(startForkCheckbox)
    WebUI.comment('"Start fork here" checkbox checked')
    
    TestObject rememberCheckbox = new TestObject('dynamic_remember_checkbox')
    rememberCheckbox.addProperty('xpath', ConditionType.EQUALS, 
        "//label[contains(text(),'Remember')]//input[@type='checkbox']")
    
    WebUI.waitForElementVisible(rememberCheckbox, 5)
    boolean isChecked = WebUI.verifyElementChecked(rememberCheckbox, 2, FailureHandling.OPTIONAL)
    if (isChecked) {
        WebUI.click(rememberCheckbox)
        WebUI.comment('"Remember" checkbox unchecked')
    } else {
        WebUI.comment('"Remember" checkbox already unchecked')
    }

    // Step 8: Test with "Visible messages only" option
    WebUI.comment('Step 8: Testing with "Visible messages only" option...')
    
    TestObject visibleMessagesOption = new TestObject('dynamic_visible_messages_option')
    visibleMessagesOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Visible messages only']")
    
    WebUI.waitForElementVisible(visibleMessagesOption, 5)
    WebUI.click(visibleMessagesOption)
    WebUI.comment('"Visible messages only" option clicked')
    
    TestObject forkSuccessMessage = findTestObject('Object Repository/Core Chat/Page_Fork Test Message/div_Successfully forked conversation')
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed for Visible messages only')
    WebUI.takeScreenshot('TC07_Fork_VisibleMessagesOnly_StartFromHere.png')
    WebUI.waitForElementNotVisible(forkSuccessMessage, 5)

    // Step 9: Test with "Include related branches" option
    WebUI.comment('Step 9: Testing with "Include related branches" option...')
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked again')
    
    WebUI.waitForElementVisible(startForkCheckbox, 5)
    WebUI.click(startForkCheckbox)
    WebUI.comment('"Start fork here" checkbox checked')
    
    WebUI.waitForElementVisible(rememberCheckbox, 5)
    isChecked = WebUI.verifyElementChecked(rememberCheckbox, 2, FailureHandling.OPTIONAL)
    if (isChecked) {
        WebUI.click(rememberCheckbox)
        WebUI.comment('"Remember" checkbox unchecked')
    }
    
    TestObject includeRelatedOption = new TestObject('dynamic_include_related_option')
    includeRelatedOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Include related branches']")
    
    WebUI.waitForElementVisible(includeRelatedOption, 5)
    WebUI.click(includeRelatedOption)
    WebUI.comment('"Include related branches" option clicked')
    
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed for Include related branches')
    WebUI.takeScreenshot('TC07_Fork_IncludeRelated_StartFromHere.png')
    WebUI.waitForElementNotVisible(forkSuccessMessage, 5)

    // Step 10: Test with "Include all to/from here" option
    WebUI.comment('Step 10: Testing with "Include all to/from here" option...')
    
    WebUI.waitForElementVisible(forkMenuButton, 5)
    WebUI.click(forkMenuButton)
    WebUI.comment('Fork menu button clicked again')
    
    WebUI.waitForElementVisible(startForkCheckbox, 5)
    WebUI.click(startForkCheckbox)
    WebUI.comment('"Start fork here" checkbox checked')
    
    WebUI.waitForElementVisible(rememberCheckbox, 5)
    isChecked = WebUI.verifyElementChecked(rememberCheckbox, 2, FailureHandling.OPTIONAL)
    if (isChecked) {
        WebUI.click(rememberCheckbox)
        WebUI.comment('"Remember" checkbox unchecked')
    }
    
    TestObject includeFromHereOption = new TestObject('dynamic_include_from_here_option')
    includeFromHereOption.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Include all to/from here']")
    
    WebUI.waitForElementVisible(includeFromHereOption, 5)
    WebUI.click(includeFromHereOption)
    WebUI.comment('"Include all to/from here" option clicked')
    
    WebUI.waitForElementVisible(forkSuccessMessage, 5)
    WebUI.comment('Fork success message displayed for Include all to/from here')
    WebUI.takeScreenshot('TC07_Fork_IncludeFromHere_StartFromHere.png')

    // Step 11: Close browser
    WebUI.comment('Step 11: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC07 PASSED')

} catch (Exception e) {
    WebUI.comment('TC07 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC07_Fork_StartFromHere_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}