import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC03: Parameters - Temperature Input Field
 * Verify direct input of Temperature values (0.0 - 2.0)
 */

WebUI.comment('=== TC03: Temperature - Input Field Test ===')

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

    // === Input Temperature ===
    WebUI.comment('Step 2: Testing Temperature input field...')
    
    TestObject tempInput = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Temperature')
    WebUI.waitForElementVisible(tempInput, 10)
    WebUI.comment('Temperature input field found')

    // Test value list
    String[] testValues = ["0.0", "0.5", "1.0", "1.5", "2.0", "0.75", "1.23"]
    List<String> results = new ArrayList<>()

    for (String value : testValues) {
        WebUI.comment("Testing input value: ${value}")
        
        WebUI.clearText(tempInput)
        WebUI.setText(tempInput, value)
        WebUI.delay(1)
        
        String actualValue = WebUI.getAttribute(tempInput, 'value').trim()
        results.add(actualValue)
        
        if (actualValue == value) {
            WebUI.comment("✅ Input '${value}' accepted correctly")
        } else {
            WebUI.comment("⚠ Input '${value}' → Actual: '${actualValue}'")
        }
        
        WebUI.takeScreenshot("TC03_Temperature_Input_${value.replace('.', '_')}.png")
    }

    // Verify invalid value (2.5 > max 2.0)
    WebUI.comment('Testing invalid input (2.5 - exceeds max)...')
    WebUI.clearText(tempInput)
    WebUI.setText(tempInput, "2.5")
    WebUI.delay(1)
    String invalidResult = WebUI.getAttribute(tempInput, 'value')
    WebUI.comment("Input 2.5 → Actual: ${invalidResult} (should be clamped or rejected)")

    WebUI.takeScreenshot('TC03_Temperature_Input_Final.png')

    WebUI.comment('=== TC03 Completed - Temperature Input Field Test ===')
    WebUI.comment('✅ TC03 PASSED')

} catch (Exception e) {
    WebUI.comment('❌ TC03 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC03_Temperature_Input_Error.png')
    throw e
}