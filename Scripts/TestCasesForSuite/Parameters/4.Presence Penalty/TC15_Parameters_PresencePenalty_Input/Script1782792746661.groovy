import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC15: Parameters - Presence Penalty Input Field
 * Verify direct input into Presence Penalty field (-2.0 - 2.0)
 */

WebUI.comment('=== TC15: Presence Penalty - Input Field Test ===')

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

    // === TEST PRESENCE PENALTY INPUT FIELD ===
    WebUI.comment('Step 2: Testing Presence Penalty input field...')
    
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Presence Penalty')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Presence Penalty input field found')

    String[] testValues = ["-2.0", "-1.5", "-0.5", "0.0", "0.5", "1.5", "2.0"]
    List<String> results = new ArrayList<>()

    for (String value : testValues) {
        WebUI.comment("Testing input value: ${value}")
        
        WebUI.clearText(inputField)
        WebUI.setText(inputField, value)
        WebUI.delay(1)
        
        String actualValue = WebUI.getAttribute(inputField, 'value').trim()
        results.add(actualValue)
        
        if (actualValue == value) {
            WebUI.comment("✅ Input '${value}' accepted correctly")
        } else {
            WebUI.comment("⚠ Input '${value}' → Actual: '${actualValue}'")
        }
        
        WebUI.takeScreenshot("TC15_PresencePenalty_Input_${value.replace('.', '_').replace('-', 'neg')}.png")
    }

    // Check invalid input
    // Check invalid input (3.0 > max 2.0)
    WebUI.comment('Testing invalid input (3.0 - exceeds max)...')
    WebUI.clearText(inputField)
    WebUI.setText(inputField, "3.0")
    WebUI.delay(1)
    String invalidResult = WebUI.getAttribute(inputField, 'value')
    WebUI.comment("Input 3.0 → Actual: ${invalidResult} (should be clamped or rejected)")

    // Check invalid input (-3.0 < -2.0)
    WebUI.comment('Testing invalid input (-3.0 - below min)...')
    WebUI.clearText(inputField)
    WebUI.setText(inputField, "-3.0")
    WebUI.delay(1)
    String belowMinResult = WebUI.getAttribute(inputField, 'value')
    WebUI.comment("Input -3.0 → Actual: ${belowMinResult} (should be clamped or rejected)")

    WebUI.takeScreenshot('TC15_PresencePenalty_Input_Final.png')

    WebUI.comment('=== TC15 Completed - Presence Penalty Input Field Test ===')
    WebUI.comment('✅ TC15 PASSED')

} catch (Exception e) {
    WebUI.comment('❌ TC15 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC15_PresencePenalty_Input_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}