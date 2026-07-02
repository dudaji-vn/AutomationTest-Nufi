import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC06: Parameters - Max Output Tokens Input & Verify
 * Verify entering values and validating output
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Enter minimum value (50) and verify output
 * 3. Enter maximum value (500) and verify output
 * 4. Enter non-numeric value and verify NaN
 * 5. Reset to default and verify
 */

WebUI.comment('=== TC06: Max Output Tokens - Input & Verify ===')

try {
    // === CHECK NAVBAR ===
    WebUI.comment('Step 0: Checking Navbar state...')
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    WebUI.waitForElementVisible(navSidebar, 10)

    if (WebUI.getAttribute(navSidebar, 'aria-hidden') == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openBtn = new TestObject('openSidebarButton')
        openBtn.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.click(openBtn)
        WebUI.delay(1)
    }

    // === OPEN PARAMETERS TAB ===
    WebUI.comment('Step 1: Opening Parameters tab...')
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)

    if (WebUI.getAttribute(parametersButton, 'aria-pressed') != 'true') {
        WebUI.click(parametersButton)
        WebUI.delay(2)
    }

    // === MAX OUTPUT TOKENS INPUT ===
    WebUI.comment('Step 2: Testing Max Output Tokens input field...')
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Max Output Tokens')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Max Output Tokens input field found')

    // === MESSAGE ELEMENTS ===
    TestObject lastMessage = new TestObject('last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, "(//div[contains(@class,'message-content')])[last()]")
    
    TestObject regenerateButton = new TestObject('regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    TestObject thinkingIndicator = new TestObject('thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")

    // === CHAT INPUT ELEMENT ===
    TestObject chatInput = findTestObject('Object Repository/Core Chat/chat_input')

    // ================== HELPER FUNCTIONS ==================
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, value)
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(1.2)
    }

    def verifyValue = { String expected, String testStep ->
        String actual = WebUI.getAttribute(inputField, 'value').trim()
        if (actual == expected) {
            WebUI.comment("PASSED - ${testStep}: Expected '${expected}' → Actual: '${actual}'")
            return true
        } else {
            KeywordUtil.markFailed("FAILED - ${testStep}: Expected ${expected} but got ${actual}")
            return false
        }
    }

    def dismissModalAndRegenerate = {
        WebUI.comment("Clicking chat input to dismiss any modal/overlay...")
        WebUI.click(chatInput)
        WebUI.delay(1)
        
        WebUI.comment("Clicking regenerate button...")
        WebUI.waitForElementClickable(regenerateButton, 10)
        WebUI.click(regenerateButton)
        WebUI.waitForElementNotVisible(thinkingIndicator, 30)
        WebUI.delay(2)
    }

    // === TEST 1: MIN VALUE (50) ===
    WebUI.comment('=== Test 1: Set minimum value (50) and verify ===')
    setValueAndBlur("50")
    verifyValue("50", "Min value test")
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Min_Input.png')

    WebUI.comment('Sending test message with min Max Output Tokens (50)...')
    String testMessage = 'Write a detailed essay about artificial intelligence, at least 500 words'
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(2)
    
    String responseMin = WebUI.getText(lastMessage)
    WebUI.comment('Response with min output limit (50): Length: ' + responseMin.length() + ' chars')
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Min_Response.png')

    // === TEST 2: MAX VALUE (500) ===
    WebUI.comment('=== Test 2: Set maximum value (500) and verify ===')
    setValueAndBlur("500")
    verifyValue("500", "Max value test")
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Max_Input.png')

    WebUI.comment('Regenerating with max Max Output Tokens (500)...')
    dismissModalAndRegenerate()
    
    String responseMax = WebUI.getText(lastMessage)
    WebUI.comment('Response with max output limit (500): Length: ' + responseMax.length() + ' chars')
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Max_Response.png')

    // === TEST 3: INVALID INPUT (NON-NUMERIC) -> SHOULD SHOW NaN ===
    WebUI.comment('=== Test 3: Invalid input (non-numeric) -> should show NaN ===')

    String[] invalidInputs = ["abc", "xyz123", "hello!"]

    for (String invalid : invalidInputs) {
        WebUI.comment("--- Testing invalid input: " + invalid + " ---")

        // Clear and enter invalid text directly
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, invalid)

        // Trigger onblur
        WebUI.clickOffset(inputField, 250, 0)
        WebUI.delay(1.5)

        String actualAfterInvalid = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment("Input '" + invalid + "' -> Actual: '" + actualAfterInvalid + "'")

        if (actualAfterInvalid == "NaN" || actualAfterInvalid == "nan" || actualAfterInvalid == "") {
            WebUI.comment("PASSED: Invalid input '" + invalid + "' correctly handled as NaN")
        } else {
            KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' not handled correctly. Actual: " + actualAfterInvalid)
        }

        WebUI.takeScreenshot("TC18_MaxOutputTokens_Invalid_" + invalid + ".png")
    }

    // === TEST 4: RESET TO DEFAULT ===
    WebUI.comment('=== Test 4: Reset to default (clear value) ===')
    
    WebUI.click(inputField)
    WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
    WebUI.sendKeys(inputField, "\b")
    WebUI.clickOffset(inputField, 250, 0)
    WebUI.delay(1)
    
    String afterClear = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('Value after clear: "' + afterClear + '"')
    
    if (afterClear == '' || afterClear == null) {
        WebUI.comment('Value cleared (system default)')
    } else {
        WebUI.comment('Value not cleared: "' + afterClear + '"')
        KeywordUtil.markFailed("FAILED: Value not cleared. Actual: '" + afterClear + "'")
    }
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Reset.png')

    WebUI.comment('Regenerating with default Max Output Tokens...')
    dismissModalAndRegenerate()
    
    String responseDefault = WebUI.getText(lastMessage)
    WebUI.comment('Response with default output limit: Length: ' + responseDefault.length() + ' chars')
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Default_Response.png')

    // === COMPARISON ===
    WebUI.comment('=== Comparison ===')
    WebUI.comment('Min output limit (50) response length: ' + responseMin.length() + ' chars')
    WebUI.comment('Max output limit (500) response length: ' + responseMax.length() + ' chars')
    WebUI.comment('Default output limit response length: ' + responseDefault.length() + ' chars')
    
    if (responseMin.length() < responseMax.length()) {
        WebUI.comment('Min output limit (50) produced shorter response (expected)')
        WebUI.comment('   Difference: ' + (responseMax.length() - responseMin.length()) + ' chars')
    } else {
        WebUI.comment('Min output limit response length (' + responseMin.length() + 
                      ') not shorter than max (' + responseMax.length() + ')')
    }

    WebUI.takeScreenshot('TC18_MaxOutputTokens_Complete.png')
    WebUI.comment('TC18 Completed - Max Output Tokens Input & Verify test')
    WebUI.comment('TC18 PASSED')

} catch (Exception e) {
    WebUI.comment('TC18 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}