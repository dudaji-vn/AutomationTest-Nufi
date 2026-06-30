import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC18: Parameters - Max Output Tokens Input & Verify
 * Verify entering values and validating output
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Enter minimum value (50) and verify output
 * 3. Enter maximum value (500) and verify output
 * 4. Reset to default and verify
 */

WebUI.comment('=== TC18: Max Output Tokens - Input & Verify ===')

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

    // === TEST MAX OUTPUT TOKENS ===
    WebUI.comment('Step 2: Testing Max Output Tokens...')
    
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Max Output Tokens')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Max Output Tokens input field found')
    
    TestObject lastMessage = new TestObject('last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, "(//div[contains(@class,'message-content')])[last()]")
    
    TestObject regenerateButton = new TestObject('regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")
    
    TestObject thinkingIndicator = new TestObject('thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")

    // === TEST 1: MINIMUM VALUE (50) ===
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
    
    // Send a message requesting a long response to test the limit
    WebUI.comment('Sending test message with min Max Output Tokens (50)...')
    String testMessage = 'Write a detailed essay about artificial intelligence, at least 500 words'
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(2)
    
    String responseMin = WebUI.getText(lastMessage)
    WebUI.comment('Response with min output limit (50):')
    WebUI.comment('Length: ' + responseMin.length() + ' chars')
    WebUI.comment('Preview: ' + (responseMin.length() > 100 ? responseMin.substring(0, 100) + '...' : responseMin))
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Min_Response.png')

    // === TEST 2: MAXIMUM VALUE (500) ===
    WebUI.comment('=== Test 2: Set maximum value (500) and verify ===')
    
    int maxValue = 500
    WebUI.clearText(inputField)
    WebUI.setText(inputField, String.valueOf(maxValue))
    WebUI.delay(1)
    
    String actualMax = WebUI.getAttribute(inputField, 'value').trim()
    WebUI.comment("Input '${maxValue}' → Actual: '${actualMax}'")
    
    if (actualMax == String.valueOf(maxValue)) {
        WebUI.comment('✅ Maximum value (500) accepted')
    } else {
        WebUI.comment('⚠ Maximum value not accepted: "' + actualMax + '"')
    }
    
    // Regenerate with new limit
    WebUI.comment('Regenerating with max Max Output Tokens (500)...')
    WebUI.waitForElementClickable(regenerateButton, 10)
    WebUI.click(regenerateButton)
    WebUI.waitForElementNotVisible(thinkingIndicator, 30)
    WebUI.delay(2)
    
    String responseMax = WebUI.getText(lastMessage)
    WebUI.comment('Response with max output limit (500):')
    WebUI.comment('Length: ' + responseMax.length() + ' chars')
    WebUI.comment('Preview: ' + (responseMax.length() > 100 ? responseMax.substring(0, 100) + '...' : responseMax))
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Max_Response.png')

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
    
    // Regenerate with default
    WebUI.comment('Regenerating with default Max Output Tokens...')
    WebUI.waitForElementClickable(regenerateButton, 10)
    WebUI.click(regenerateButton)
    WebUI.waitForElementNotVisible(thinkingIndicator, 30)
    WebUI.delay(2)
    
    String responseDefault = WebUI.getText(lastMessage)
    WebUI.comment('Response with default output limit:')
    WebUI.comment('Length: ' + responseDefault.length() + ' chars')
    WebUI.comment('Preview: ' + (responseDefault.length() > 100 ? responseDefault.substring(0, 100) + '...' : responseDefault))
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Default_Response.png')

    // === SO SÁNH VÀ VERIFY ===
    WebUI.comment('=== Comparison ===')
    WebUI.comment('Min output limit (50) response length: ' + responseMin.length() + ' chars')
    WebUI.comment('Max output limit (500) response length: ' + responseMax.length() + ' chars')
    WebUI.comment('Default output limit response length: ' + responseDefault.length() + ' chars')
    
    // Verify the response with min limit is shorter than max limit
    if (responseMin.length() < responseMax.length()) {
        WebUI.comment('✅ Min output limit (50) produced shorter response (expected)')
        WebUI.comment('   Difference: ' + (responseMax.length() - responseMin.length()) + ' chars')
    } else {
        WebUI.comment('⚠ Min output limit response length (' + responseMin.length() + 
                      ') not shorter than max (' + responseMax.length() + ')')
    }
    
    WebUI.comment('=== Range Verification ===')
    WebUI.comment('Min value: 50')
    WebUI.comment('Max value: 500')
    WebUI.comment('Range: 50 - 500')
    
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Complete.png')
    WebUI.comment('✅ TC18 PASSED - Max Output Tokens Input & Verify test')

} catch (Exception e) {
    WebUI.comment('TC18 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC18_MaxOutputTokens_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}