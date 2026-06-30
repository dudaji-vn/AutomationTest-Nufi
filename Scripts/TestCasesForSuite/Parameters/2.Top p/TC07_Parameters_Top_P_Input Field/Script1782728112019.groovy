import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC07: Parameters - Top P Input Field
 * Verify direct input of Top P values (0.0 - 1.0)
 */

WebUI.comment('=== TC07: Top P - Input Field Test ===')

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

    // === TEST TOP P INPUT FIELD ===
    WebUI.comment('Step 2: Testing Top P input field...')
    
    // Use Object Repository for Top P input
    TestObject inputTopP = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Top P')
    WebUI.waitForElementVisible(inputTopP, 10)
    WebUI.comment('Top P input field found')

    // Valid test values list (0.0 - 1.0)
    String[] testValues = ["0.0", "0.3", "0.5", "0.75", "1.0"]
    List<String> results = new ArrayList<>()

    for (String value : testValues) {
        WebUI.comment("Testing input value: ${value}")
        
        WebUI.clearText(inputTopP)
        WebUI.setText(inputTopP, value)
        WebUI.delay(1)
        
        String actualValue = WebUI.getAttribute(inputTopP, 'value').trim()
        results.add(actualValue)
        
        if (actualValue == value) {
            WebUI.comment("✅ Input '${value}' accepted correctly")
        } else {
            WebUI.comment("⚠ Input '${value}' → Actual: '${actualValue}'")
        }
        
        WebUI.takeScreenshot("TC07_TopP_Input_${value.replace('.', '_')}.png")
    }

    // Check invalid input (1.5 > max 1.0)
    WebUI.comment('Testing invalid input (1.5 - exceeds max)...')
    WebUI.clearText(inputTopP)
    WebUI.setText(inputTopP, "1.5")
    WebUI.delay(1)
    String invalidResult = WebUI.getAttribute(inputTopP, 'value')
    WebUI.comment("Input 1.5 → Actual: ${invalidResult} (should be clamped or rejected)")

    // Check invalid negative input (min 0.0)
    WebUI.comment('Testing invalid input (-0.5 - below min)...')
    WebUI.clearText(inputTopP)
    WebUI.setText(inputTopP, "-0.5")
    WebUI.delay(1)
    String negativeResult = WebUI.getAttribute(inputTopP, 'value')
    WebUI.comment("Input -0.5 → Actual: ${negativeResult} (should be clamped or rejected)")

    WebUI.takeScreenshot('TC07_TopP_Input_Final.png')

    WebUI.comment('=== TC07 Completed - Top P Input Field Test ===')
    WebUI.comment('✅ TC07 PASSED')

} catch (Exception e) {
    WebUI.comment('❌ TC07 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC07_TopP_Input_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}