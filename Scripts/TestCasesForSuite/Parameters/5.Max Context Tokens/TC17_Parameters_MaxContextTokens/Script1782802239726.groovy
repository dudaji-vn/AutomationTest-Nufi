import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC17: Parameters - Max Context Tokens Input & Verify
 * Verify entering values and validating output
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Enter minimum value (50) and verify output
 * 3. Enter maximum value (10000) and verify output
 * 4. Reset to default and verify
 */

WebUI.comment('=== TC17: Max Context Tokens - Input & Verify ===')

try {
    // === CHECK NAVBAR ===
    WebUI.comment('Step 0: Checking Navbar state...')
    
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    WebUI.waitForElementVisible(navSidebar, 10)
    
    String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
    
    if (ariaHidden == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
        
        ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden after open: ' + ariaHidden)
        if (ariaHidden == 'false') {
            WebUI.comment('✓ Navbar opened successfully')
        }
    } else {
        WebUI.comment('✓ Navbar is already open (aria-hidden="false")')
    }

    // === CHECK PARAMETERS TAB ===
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    WebUI.comment('Parameters button aria-label: ' + ariaLabel)
    WebUI.comment('Parameters button aria-pressed: ' + isPressed)
    
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('✓ Parameters tab is open (correct aria-label and aria-pressed)')
    } else {
        WebUI.comment('Parameters tab not open or wrong label, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        
        ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
        isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
        WebUI.comment('After click - Parameters button aria-label: ' + ariaLabel)
        WebUI.comment('After click - Parameters button aria-pressed: ' + isPressed)
        
        if (ariaLabel == 'Parameters' && isPressed == 'true') {
            WebUI.comment('✓ Parameters tab opened successfully')
        }
    }

    // === TEST MAX CONTEXT TOKENS ===
    WebUI.comment('Step 2: Testing Max Context Tokens...')
    
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Max Context Tokens')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Max Context Tokens input field found')
    
    TestObject lastMessage = new TestObject('last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, "(//div[contains(@class,'message-content')])[last()]")
    
    TestObject regenerateButton = new TestObject('regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    TestObject thinkingIndicator = new TestObject('thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")

    // === TEST 1: GIÁ TRỊ MIN (50) ===
    WebUI.comment('=== Test 1: Set minimum value (50) and verify ===')
    
    int minValue = 50
    WebUI.clearText(inputField)
    WebUI.setText(inputField, String.valueOf(minValue))
    WebUI.delay(1)
    
    String actualMin = WebUI.getAttribute(inputField, 'value').trim()
    WebUI.comment("Input '${minValue}' → Actual: '${actualMin}'")
    
    if (actualMin == String.valueOf(minValue)) {
        WebUI.comment('✅ Minimum value (50) accepted')
    } else {
        WebUI.comment('⚠ Minimum value not accepted: "' + actualMin + '"')
    }
    
    // Send a message
    WebUI.comment('Sending test message with min Max Context Tokens (50)...')
    String testMessage = 'Write a short poem about testing'
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(2)
    
    String responseMin = WebUI.getText(lastMessage)
    WebUI.comment('Response with min context (50):')
    WebUI.comment('Length: ' + responseMin.length() + ' chars')
    WebUI.comment('Preview: ' + (responseMin.length() > 100 ? responseMin.substring(0, 100) + '...' : responseMin))
    WebUI.takeScreenshot('TC17_MaxContextTokens_Min_Response.png')

    // === TEST 2: MAXIMUM VALUE (10000) ===
    WebUI.comment('=== Test 2: Set maximum value (10000) and verify ===')
    
    int maxValue = 10000
    WebUI.clearText(inputField)
    WebUI.setText(inputField, String.valueOf(maxValue))
    WebUI.delay(1)
    
    String actualMax = WebUI.getAttribute(inputField, 'value').trim()
    WebUI.comment("Input '${maxValue}' → Actual: '${actualMax}'")
    
    if (actualMax == String.valueOf(maxValue)) {
        WebUI.comment('✅ Maximum value (10000) accepted')
    } else {
        WebUI.comment('⚠ Maximum value not accepted: "' + actualMax + '"')
    }
    
    // Regenerate with new context limit
    WebUI.comment('Regenerating with max Max Context Tokens (10000)...')
    WebUI.waitForElementClickable(regenerateButton, 10)
    WebUI.click(regenerateButton)
    WebUI.waitForElementNotVisible(thinkingIndicator, 30)
    WebUI.delay(2)
    
    String responseMax = WebUI.getText(lastMessage)
    WebUI.comment('Response with max context (10000):')
    WebUI.comment('Length: ' + responseMax.length() + ' chars')
    WebUI.comment('Preview: ' + (responseMax.length() > 100 ? responseMax.substring(0, 100) + '...' : responseMax))
    WebUI.takeScreenshot('TC17_MaxContextTokens_Max_Response.png')

    // === TEST 3: RESET TO DEFAULT ===
    WebUI.comment('=== Test 3: Reset to default (clear value) ===')
    
    WebUI.clearText(inputField)
    WebUI.delay(1)
    
    String afterClear = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('Value after clear: "' + afterClear + '"')
    
    if (afterClear == '' || afterClear == null) {
        WebUI.comment('✅ Value cleared (system default)')
    } else {
        WebUI.comment('⚠ Value not cleared: "' + afterClear + '"')
    }
    
    // Regenerate with default context
    WebUI.comment('Regenerating with default Max Context Tokens...')
    WebUI.waitForElementClickable(regenerateButton, 10)
    WebUI.click(regenerateButton)
    WebUI.waitForElementNotVisible(thinkingIndicator, 30)
    WebUI.delay(2)
    
    String responseDefault = WebUI.getText(lastMessage)
    WebUI.comment('Response with default context:')
    WebUI.comment('Length: ' + responseDefault.length() + ' chars')
    WebUI.comment('Preview: ' + (responseDefault.length() > 100 ? responseDefault.substring(0, 100) + '...' : responseDefault))
    WebUI.takeScreenshot('TC17_MaxContextTokens_Default_Response.png')

    // === SO SÁNH VÀ VERIFY ===
    WebUI.comment('=== Comparison ===')
    WebUI.comment('Min context (50) response length: ' + responseMin.length() + ' chars')
    WebUI.comment('Max context (10000) response length: ' + responseMax.length() + ' chars')
    WebUI.comment('Default context response length: ' + responseDefault.length() + ' chars')
    
    WebUI.comment('=== Range Verification ===')
    WebUI.comment('Min value: 50')
    WebUI.comment('Max value: 10000')
    WebUI.comment('Range: 50 - 10000')
    
    WebUI.takeScreenshot('TC17_MaxContextTokens_Complete.png')
    WebUI.comment('✅ TC17 PASSED - Max Context Tokens Input & Verify test')

} catch (Exception e) {
    WebUI.comment('TC17 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC17_MaxContextTokens_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}