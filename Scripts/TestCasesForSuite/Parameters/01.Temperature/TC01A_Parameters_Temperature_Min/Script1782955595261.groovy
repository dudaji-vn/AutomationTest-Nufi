import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC01A: Parameters - Temperature Min (0.0) - Deterministic Test
 */

WebUI.comment('=== TC01A: Parameters - Temperature Min (0.0) - Deterministic Test ===')

try {
    // ================== CONFIGURATION ==================
    int regenerateCount = 3
    String testMessage = "how r u"
    // =============================================

    WebUI.comment("Configuration: Regenerate ${regenerateCount} times | Message: '${testMessage}'")

    // === CHECK NAVBAR ===
    WebUI.comment('Step 0: Checking Navbar state...')
    
    TestObject navSidebar = new TestObject('navSidebar')
    navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
    WebUI.waitForElementVisible(navSidebar, 10)
    
    String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
    WebUI.comment('Navbar aria-hidden: ' + ariaHidden)
    
    // If navbar is closed (aria-hidden = "true"), open the sidebar
    if (ariaHidden == 'true') {
        WebUI.comment('Navbar is closed, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
        
        // Verify navbar is open
        ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden after open: ' + ariaHidden)
        if (ariaHidden == 'false') {
            WebUI.comment('✓ Navbar opened successfully')
        } else {
            WebUI.comment('⚠ Navbar is still closed')
        }
    } else {
        WebUI.comment('✓ Navbar is open (aria-hidden="false")')
    }

    // === CHECK PARAMETERS TAB ===
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    // Check correct aria-label="Parameters" and aria-pressed="true"
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    WebUI.comment('Parameters button aria-label: ' + ariaLabel)
    WebUI.comment('Parameters button aria-pressed: ' + isPressed)
    
    // If the Parameters tab is not active, click to open it
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('✓ Parameters tab is open (correct aria-label and aria-pressed)')
    } else {
        WebUI.comment('Parameters tab not open or wrong label, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        
        // Verify again after clicking
        ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
        isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
        WebUI.comment('After click - Parameters button aria-label: ' + ariaLabel)
        WebUI.comment('After click - Parameters button aria-pressed: ' + isPressed)
        
        if (ariaLabel == 'Parameters' && isPressed == 'true') {
            WebUI.comment('✓ Parameters tab opened successfully')
        } else {
            WebUI.comment('⚠ Parameters tab still not opened correctly')
        }
    }

    // === Set Temperature = 0.0 ===
    WebUI.comment('Step 2: Setting Temperature to 0.0...')
    
    TestObject sliderThumb = new TestObject('sliderThumb')
    sliderThumb.addProperty('xpath', ConditionType.EQUALS,
        "//span[@id='temperature-dynamic-setting-slider']//span[@role='slider']")
    
    WebUI.waitForElementVisible(sliderThumb, 10)
    String currentTemp = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Current Temperature: ' + currentTemp)
    
    // Drag slider to 0
    String widthScript = "return document.evaluate(\"//span[@id='temperature-dynamic-setting-slider']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getBoundingClientRect().width;"
    double sliderWidth = (Double) WebUI.executeJavaScript(widthScript, null)
    WebUI.dragAndDropByOffset(sliderThumb, -(int)(sliderWidth * 0.8), 0)
    WebUI.delay(1.5)
    
    String afterDrag = WebUI.getAttribute(sliderThumb, 'aria-valuenow')
    WebUI.comment('Temperature after drag: ' + afterDrag)
    WebUI.takeScreenshot('TC01_Temperature_Set_To_0.png')

    // === Send message & Regenerate ===
    WebUI.comment('=== Testing with message: ' + testMessage)
    CustomKeywords.'keywords.ChatKeywords.sendMessageAndVerifyResponse'(testMessage)
    WebUI.delay(2)

    TestObject lastMessage = new TestObject('last_message')
    lastMessage.addProperty('xpath', ConditionType.EQUALS, "(//div[contains(@class,'message-content')])[last()]")

    TestObject regenerateButton = new TestObject('regenerate_button')
    regenerateButton.addProperty('xpath', ConditionType.EQUALS,
        "(//div[contains(@class,'message-content')])[last()]/ancestor::div[contains(@class,'message')]//button[@title='Regenerate']")

    TestObject thinkingIndicator = new TestObject('thinking_indicator')
    thinkingIndicator.addProperty('xpath', ConditionType.EQUALS, "//span[contains(@class,'result-thinking')]")

    List<String> responses = new ArrayList<>()
    responses.add(WebUI.getText(lastMessage).trim())

    for (int i = 1; i <= regenerateCount; i++) {
        WebUI.click(regenerateButton)
        WebUI.waitForElementVisible(thinkingIndicator, 10)
        WebUI.waitForElementNotVisible(thinkingIndicator, 35)
        WebUI.delay(1.5)

        String resp = WebUI.getText(lastMessage).trim()
        responses.add(resp)
        WebUI.comment("Response ${i+1}: " + resp)
        WebUI.takeScreenshot("TC01_Temp0_Response_${i+1}.png")
    }

    // === VERIFY ===
    WebUI.comment('=== Verifying Deterministic Behavior ===')

    Set<String> uniqueResponses = new HashSet<>(responses)
    int uniqueCount = uniqueResponses.size()

    if (uniqueCount == 1) {
        WebUI.comment('✅ PASSED: All responses are THE SAME')
        WebUI.takeScreenshot('TC01_Temperature_0_Perfect_PASSED.png')
    } 
    else if (uniqueCount == 2) {
        WebUI.comment('⚠ ACCEPTED: There are 2 response variants (common at temp=0)')
        WebUI.takeScreenshot('TC01_Temperature_0_Acceptable.png')
    } 
    else {
        WebUI.comment('❌ FAILED: There are ' + uniqueCount + ' different responses → Temperature=0 is not deterministic')
        WebUI.takeScreenshot('TC01_Temperature_0_FAILED.png')
        KeywordUtil.markFailed("Test FAILED: Temperature = 0.0 but there are ${uniqueCount} different responses")
    }

    WebUI.comment('=== TC01 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Temperature_Min_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}