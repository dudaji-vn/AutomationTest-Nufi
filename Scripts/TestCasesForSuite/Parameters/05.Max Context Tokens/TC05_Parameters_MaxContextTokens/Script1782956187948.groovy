import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC05: Parameters - Max Context Tokens Input & Verify
 * Verify entering values and validating output
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Enter minimum value (50) and verify output
 * 3. Enter maximum value (1000) and verify output
 * 4. Enter non-numeric value and verify NaN
 * 5. Reset to default and verify
 */

WebUI.comment('=== TC05: Max Context Tokens - Input & Verify ===')

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
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)

    if (WebUI.getAttribute(parametersButton, 'aria-pressed') != 'true') {
        WebUI.click(parametersButton)
        WebUI.delay(2)
    }

    // === MAX CONTEXT TOKENS INPUT ===
    WebUI.comment('Step 2: Testing Max Context Tokens input field...')
    TestObject inputField = findTestObject('Object Repository/nav/Parameter/input_Max Context Tokens')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Max Context Tokens input field found')

    // === MESSAGE ELEMENTS ===
    // Lấy message AI cuối cùng (không phải message user)
    TestObject lastAIMessage = new TestObject('lastAIMessage')
    lastAIMessage.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content') and not(contains(@class,'user-message'))])[last()]")
    
    // Container của message AI cuối cùng
    TestObject lastAIContainer = new TestObject('lastAIContainer')
    lastAIContainer.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class,'message-content') and not(contains(@class,'user-message'))])[last()]/ancestor::div[contains(@class,'message')]")
    
    // === REGENERATE BUTTON - CÁCH 1: Tìm theo title trong container action buttons ===
    TestObject regenerateButton = new TestObject('regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content') and not(contains(@class,'user-message'))])[last()]/following::div[contains(@class,'mt-1')]//button[@title='Regenerate']")
    
    // === REGENERATE BUTTON - CÁCH 2: Tìm trực tiếp (dự phòng) ===
    TestObject regenerateButtonSimple = new TestObject('regenerate_button_simple')
    regenerateButtonSimple.addProperty('xpath', ConditionType.EQUALS,
        "//button[@title='Regenerate']")
    
    TestObject thinkingIndicator = new TestObject('thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")
    
    // === CLICK OUTSIDE ELEMENT (to trigger onblur and close popups) ===
    TestObject clickOutside = new TestObject('clickOutside')
    clickOutside.addProperty('xpath', ConditionType.EQUALS, "//body")

    // ================== HELPER FUNCTIONS ==================
    def setValueAndBlur = { String value ->
        WebUI.comment("→ Entering: " + value)
        WebUI.click(inputField)
        // Select all text (Ctrl+A)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        // Type new value
        WebUI.sendKeys(inputField, value)
        // Trigger onblur by clicking outside
        WebUI.click(clickOutside)
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
    
    def hoverOnLastAIMessage = {
        WebUI.comment('Hovering on last AI message to show action buttons...')
        WebUI.scrollToElement(lastAIContainer, 1)
        WebUI.mouseOver(lastAIContainer)
        WebUI.delay(1.5)  // Tăng thời gian để button hiển thị
        WebUI.comment('✓ Hover completed')
    }
    
    def waitForRegenerateButtonAndClick = {
        WebUI.comment('Waiting for Regenerate button to be clickable...')
        
        // Thử XPath chính xác trước
        boolean buttonFound = WebUI.waitForElementClickable(regenerateButton, 5, FailureHandling.OPTIONAL)
        
        // Nếu không tìm thấy, thử XPath đơn giản hơn
        if (!buttonFound) {
            WebUI.comment('Primary XPath not found, trying simple XPath...')
            buttonFound = WebUI.waitForElementClickable(regenerateButtonSimple, 5, FailureHandling.OPTIONAL)
            if (buttonFound) {
                WebUI.comment('✓ Found regenerate button with simple XPath')
                WebUI.click(regenerateButtonSimple)
            } else {
                KeywordUtil.markFailed('Regenerate button not found with either XPath')
                return
            }
        } else {
            WebUI.comment('✓ Found regenerate button with primary XPath')
            WebUI.click(regenerateButton)
        }
        
        WebUI.comment('✓ Regenerate button clicked')
    }
    
    def waitForResponseComplete = {
        WebUI.comment('Waiting for response to complete...')
        WebUI.waitForElementNotVisible(thinkingIndicator, 30)
        WebUI.delay(2)
        WebUI.comment('✓ Response completed')
    }

    // === TEST 1: MIN VALUE (50) ===
    WebUI.comment('=== Test 1: Set minimum value (50) and verify ===')
    setValueAndBlur("50")
    verifyValue("50", "Min value test")
    WebUI.takeScreenshot('TC05_MaxContextTokens_Min_Input.png')

    // Send message with min value
    WebUI.comment('Sending test message with min Max Context Tokens (50)...')
    String testMessage = 'Write a short poem about testing'
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(2)
    
    String responseMin = WebUI.getText(lastAIMessage)
    WebUI.comment('Response with min context (50): Length: ' + responseMin.length() + ' chars')
    WebUI.takeScreenshot('TC05_MaxContextTokens_Min_Response.png')

    // === TEST 2: MAX VALUE (1000) ===
    WebUI.comment('=== Test 2: Set maximum value (1000) and verify ===')
    setValueAndBlur("1000")
    verifyValue("1000", "Max value test")
    WebUI.takeScreenshot('TC05_MaxContextTokens_Max_Input.png')

    // Hover to show regenerate button
    hoverOnLastAIMessage()
    
    // Regenerate with max value
    WebUI.comment('Regenerating with max Max Context Tokens (1000)...')
    waitForRegenerateButtonAndClick()
    waitForResponseComplete()
    
    String responseMax = WebUI.getText(lastAIMessage)
    WebUI.comment('Response with max context (1000): Length: ' + responseMax.length() + ' chars')
    WebUI.takeScreenshot('TC05_MaxContextTokens_Max_Response.png')

    // === TEST 3: INVALID INPUT (NON-NUMERIC) -> SHOULD SHOW NaN ===
    WebUI.comment('=== Test 3: Invalid input (non-numeric) -> should show NaN ===')
    
    String[] invalidInputs = ["abc", "xyz123", "hello!"]
    
    for (String invalid : invalidInputs) {
        WebUI.comment("--- Testing invalid input: " + invalid + " ---")

        // Enter invalid text
        WebUI.click(inputField)
        WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputField, invalid)

        // Trigger onblur by clicking outside
        WebUI.click(clickOutside)
        WebUI.delay(1.5)

        // Check value after blur - should be NaN
        String actualAfterInvalid = WebUI.getAttribute(inputField, 'value').trim()
        WebUI.comment("Input '" + invalid + "' -> Actual: '" + actualAfterInvalid + "'")

        if (actualAfterInvalid == "NaN" || actualAfterInvalid == "nan" || actualAfterInvalid == "") {
            WebUI.comment("PASSED: Invalid input '" + invalid + "' correctly handled as NaN")
        } else {
            KeywordUtil.markFailed("FAILED: Invalid input '" + invalid + "' not handled correctly. Actual: " + actualAfterInvalid)
        }

        WebUI.takeScreenshot("TC05_MaxContextTokens_Invalid_" + invalid + ".png")
    }

    // === TEST 4: RESET TO DEFAULT ===
    WebUI.comment('=== Test 4: Reset to default (clear value) ===')
    
    WebUI.click(inputField)
    WebUI.sendKeys(inputField, Keys.chord(Keys.CONTROL, "a"))
    // Use "\b" (backspace) instead of Keys.BACK_SPACE to avoid Katalon compatibility issue
    WebUI.sendKeys(inputField, "\b")
    WebUI.click(clickOutside)
    WebUI.delay(1)
    
    String afterClear = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('Value after clear: "' + afterClear + '"')
    
    if (afterClear == '' || afterClear == null) {
        WebUI.comment('Value cleared (system default)')
    } else {
        WebUI.comment('Value not cleared: "' + afterClear + '"')
        KeywordUtil.markFailed("FAILED: Value not cleared. Actual: '" + afterClear + "'")
    }
    WebUI.takeScreenshot('TC05_MaxContextTokens_Reset.png')

    // Hover to show regenerate button
    hoverOnLastAIMessage()
    
    // Regenerate with default context
    WebUI.comment('Regenerating with default Max Context Tokens...')
    waitForRegenerateButtonAndClick()
    waitForResponseComplete()
    
    String responseDefault = WebUI.getText(lastAIMessage)
    WebUI.comment('Response with default context: Length: ' + responseDefault.length() + ' chars')
    WebUI.takeScreenshot('TC05_MaxContextTokens_Default_Response.png')

    // === COMPARISON ===
    WebUI.comment('=== Comparison ===')
    WebUI.comment('Min context (50) response length: ' + responseMin.length() + ' chars')
    WebUI.comment('Max context (1000) response length: ' + responseMax.length() + ' chars')
    WebUI.comment('Default context response length: ' + responseDefault.length() + ' chars')

    WebUI.takeScreenshot('TC05_MaxContextTokens_Complete.png')
    WebUI.comment('TC05 Completed - Max Context Tokens Input & Verify test')
    WebUI.comment('TC05 PASSED')

} catch (Exception e) {
    WebUI.comment('TC05 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC05_MaxContextTokens_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}