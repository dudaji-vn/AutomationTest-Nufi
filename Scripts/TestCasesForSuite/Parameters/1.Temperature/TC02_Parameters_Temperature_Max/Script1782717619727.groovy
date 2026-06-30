import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC02: Parameters - Temperature Max (2.0)
 * Verify Temperature = 2.0 (Maximum) - Responses should be diverse
 */

WebUI.comment('=== TC02: Parameters - Temperature Max (2.0) ===')

try {
    // ================== CONFIGURATION ==================
    int regenerateCount = 3
    String testMessage = "how are you"
    double expectedMaxTemp = 2.0
    // =============================================

    WebUI.comment("Configuration: Temperature Max (${expectedMaxTemp}) | Regenerate ${regenerateCount} times")

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
        WebUI.comment('✓ Navbar is open (aria-hidden="false")')
    }

    // === VERIFY PARAMETERS TAB ===
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

    // === Set Temperature = 2.0 ===
    WebUI.comment('Step 2: Setting Temperature to 2.0 (Maximum)...')

    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[@id='temperature-dynamic-setting-slider']//span[@role='slider']")

    WebUI.waitForElementVisible(sliderThumb, 10)

    String currentValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Current Temperature before drag: ' + currentValue)

    String widthScript = "return document.evaluate(\"//span[@id='temperature-dynamic-setting-slider']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.comment('Slider width: ' + sliderWidth + 'px')

    WebUI.dragAndDropByOffset(sliderThumb, (int)(sliderWidth * 0.8), 0)
    WebUI.delay(1.5)

    String tempValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    double actualTemp = Double.parseDouble(tempValue)
    WebUI.comment('Temperature after first drag: ' + actualTemp)

    if (actualTemp < 1.8) {
        WebUI.comment('Temperature not close to 2.0, dragging more...')
        WebUI.dragAndDropByOffset(sliderThumb, (int)(sliderWidth * 0.2), 0)
        WebUI.delay(1.5)
        tempValue = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
        actualTemp = Double.parseDouble(tempValue)
        WebUI.comment('Temperature after second drag: ' + actualTemp)
    }

    if (Math.abs(actualTemp - expectedMaxTemp) <= 0.05) {
        WebUI.comment('✅ Temperature successfully set to Maximum (2.0)')
    } else {
        WebUI.comment('⚠ Warning: Temperature is ' + actualTemp + ' instead of 2.0')
    }

    WebUI.takeScreenshot('TC02_Temperature_Set_To_Max.png')

    // === Send message & Regenerate ===
    WebUI.comment('=== Testing with message: ' + testMessage)
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(3)

    // Get last message
    TestObject lastMessage = new TestObject('last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, "(//div[contains(@class,'message-content')])[last()]")
    WebUI.waitForElementVisible(lastMessage, 30)
    WebUI.comment('Message content found')

    // Find regenerate button
    TestObject regenerateButton = new TestObject('regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")

    TestObject thinkingIndicator = new TestObject('thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")

    List<String> responses = new ArrayList<>()

    // Get original response
    String firstResponse = WebUI.getText(lastMessage).trim()
    responses.add(firstResponse)
    WebUI.comment("Original Response: " + firstResponse)
    WebUI.takeScreenshot('TC02_Temp2_Original_Response.png')

    // Regenerate several times
    for (int i = 1; i <= regenerateCount; i++) {
        WebUI.comment("Regenerating... (${i}/${regenerateCount})")
        
        // Wait for regenerate button to become clickable
        try {
            WebUI.waitForElementClickable(regenerateButton, 30)
            WebUI.click(regenerateButton)
            WebUI.comment('Regenerate button clicked')
        } catch (Exception e) {
            WebUI.comment('Failed to click regenerate button: ' + e.getMessage())
            // Fallback: JavaScript click
            WebUI.executeJavaScript(
                "var btn = document.evaluate(\"" + 
                "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']\"" +
                ", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; if(btn) btn.click();", 
                null
            )
            WebUI.comment('Clicked regenerate button via JavaScript')
        }
        
        // Wait for thinking indicator to appear
        try {
            WebUI.waitForElementVisible(thinkingIndicator, 10)
            WebUI.comment('Thinking indicator appeared')
        } catch (Exception e) {
            WebUI.comment('Thinking indicator not visible, continuing...')
        }
        
        // Wait for thinking indicator to disappear
        try {
            WebUI.waitForElementNotVisible(thinkingIndicator, 40)
            WebUI.comment('Thinking indicator disappeared')
        } catch (Exception e) {
            WebUI.comment('Thinking indicator still visible, waiting...')
            WebUI.delay(5)
        }
        
        WebUI.delay(1.5)
        
        // Get new response
        WebUI.waitForElementVisible(lastMessage, 30)
        String resp = WebUI.getText(lastMessage).trim()
        responses.add(resp)
        WebUI.comment("Response ${i+1}: " + resp)
        WebUI.takeScreenshot("TC02_Temp2_Response_${i+1}.png")
    }

    // === VERIFY ===
    WebUI.comment('=== Verifying Responses at Temperature = 2.0 ===')

    Set<String> uniqueResponses = new HashSet<>(responses)
    int uniqueCount = uniqueResponses.size()
    
    WebUI.comment('Total responses: ' + responses.size())
    WebUI.comment('Unique responses: ' + uniqueCount)
    
    // Print details of the responses
    for (int i = 0; i < responses.size(); i++) {
        String resp = responses.get(i)
        String preview = resp.length() > 80 ? resp.substring(0, 80) + '...' : resp
        WebUI.comment("Response ${i+1}: ${preview}")
    }

    if (uniqueCount >= 2) {
        WebUI.comment('✅ PASSED: Temperature = 2.0 produced multiple different responses (Expected behavior)')
        WebUI.comment("Unique responses count: ${uniqueCount}/${responses.size()}")
        WebUI.takeScreenshot('TC02_Temperature_Max_PASSED.png')
    } 
    else {
        WebUI.comment('❌ FAILED: Temperature = 2.0 produced only 1 unique response (Not diverse)')
        WebUI.takeScreenshot('TC02_Temperature_Max_FAILED.png')
        KeywordUtil.markFailed("Temperature = 2.0 responses were not diverse (only 1 unique response)")
    }

    WebUI.comment('=== TC02 Completed - Unique responses: ' + uniqueCount + ' ===')

} catch (Exception e) {
    WebUI.comment('TC02 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC02_Temperature_Max_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}