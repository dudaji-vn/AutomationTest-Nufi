import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC07B: Parameters - Stop Sequences Chat Behavior
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Set stop sequence = "Python"
 * 3. Send message: "programming languages"
 * 4. Verify AI response stops at "Python" (no text after Python)
 */

WebUI.comment('=== TC07B: Stop Sequences - Chat Behavior ===')

try {
    // Step 1: Open Parameters panel
    WebUI.comment('Step 1: Opening Parameters panel...')
    
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    
    if (screenWidth <= 768) {
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
    }
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Parameters'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Parameters'))
    WebUI.delay(2)

    // Step 2: Set stop sequence = "Python"
    WebUI.comment('Step 2: Setting stop sequence to "Python"...')
    
    TestObject inputField = findTestObject('Object Repository/nav/Parameter/input_Stop Sequence')
    WebUI.waitForElementVisible(inputField, 10)
    
    WebUI.click(inputField)
    WebUI.clearText(inputField)
    WebUI.setText(inputField, "Python" + "\n")
    WebUI.delay(1)
    WebUI.comment('Stop sequence "Python" added')
    WebUI.takeScreenshot('TC20_StopSequences_Set.png')

    // Step 3: Send message
    WebUI.comment('Step 3: Sending test message...')
    String testMessage = 'programming languages'
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(2)

    // Step 4: Get AI response
    WebUI.comment('Step 4: Getting AI response...')
    TestObject lastMessage = new TestObject('last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, "(//div[contains(@class,'message-content')])[last()]")
    WebUI.waitForElementVisible(lastMessage, 30)
    
    String response = WebUI.getText(lastMessage)
    WebUI.comment('=== AI Response ===')
    WebUI.comment(response)
    WebUI.comment('=== End Response ===')
    WebUI.takeScreenshot('TC20_StopSequences_Response.png')

    // Step 5: Verify stop sequence
    WebUI.comment('Step 5: Verifying stop sequence...')
    
    String responseLower = response.toLowerCase()
    int pythonIndex = responseLower.indexOf("python")
    
    if (pythonIndex == -1) {
        // Case 1: No "python" in response → PASS
        WebUI.comment('✅ PASSED: Response does not contain "Python"')
        WebUI.comment('   The AI did not mention Python in its response')
        WebUI.takeScreenshot('TC20_StopSequences_Pass_NoPython.png')
    } else {
        // Case 2: Has "python" → check if anything after it
        WebUI.comment('ℹ️ Response contains "Python" at position: ' + pythonIndex)
        
        String afterPython = response.substring(pythonIndex + 6).trim()
        String afterClean = afterPython.replaceAll("^[.,!?;:\\s]*", "")
        
        WebUI.comment('Text after "Python": "' + afterPython + '"')
        WebUI.comment('After cleaning: "' + afterClean + '"')
        
        if (afterClean.isEmpty()) {
            // Has "Python" and nothing after → PASS
            WebUI.comment('✅ PASSED: Response stopped at "Python"')
            WebUI.comment('   No text found after "Python" - stop sequence worked correctly')
            WebUI.takeScreenshot('TC20_StopSequences_Pass_StopAtPython.png')
        } else {
            // Has text after "Python" → FAIL
            String foundText = afterClean.substring(0, Math.min(50, afterClean.length()))
            WebUI.comment('❌ FAILED: Response continued after "Python"')
            WebUI.comment('   Found text after "Python": "' + foundText + '"')
            WebUI.takeScreenshot('TC20_StopSequences_Fail.png')
            KeywordUtil.markFailedAndStop(
                "FAILED: Response did not stop at 'Python'. Found: '" + foundText + "'"
            )
        }
    }

    WebUI.comment('TC20 PASSED')

} catch (Exception e) {
    WebUI.comment('TC20 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC20_StopSequences_Error.png')
    throw e
}