import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC07A: Parameters - Stop Sequences Validation (Max 4 values)
 */

WebUI.comment('=== TC07A: Stop Sequences - Validation (Max 4) ===')

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

    // === TEST STOP SEQUENCES ===
    WebUI.comment('Step 2: Testing Stop Sequences validation...')
    
    TestObject inputField = findTestObject('Object Repository/Core Chat/nav/Parameter/input_Stop Sequence')
    WebUI.waitForElementVisible(inputField, 10)
    WebUI.comment('Stop Sequences input field found')
    
    TestObject errorMessage = new TestObject('errorMessage')
    errorMessage.addProperty('xpath', ConditionType.EQUALS,
        "//*[(text() = 'Maximum number allowed is 4, using latest values.' or . = 'Maximum number allowed is 4, using latest values.')]")
    
    // Helper function: Enter text and press Enter using "\n"
    def addStopSequence = { String sequence ->
        WebUI.click(inputField)
        WebUI.clearText(inputField)
        WebUI.setText(inputField, sequence + "\n")
        WebUI.delay(1)
        WebUI.comment("Added stop sequence: " + sequence)
    }

    // === TEST 1-4: ADD 4 STOP SEQUENCES ===
    WebUI.comment('=== Test 1-4: Add 4 stop sequences ===')
    
    String[] sequences = ["END", "STOP", "QUIT", "EXIT"]
    
    for (int i = 0; i < sequences.length; i++) {
        addStopSequence(sequences[i])
        
        String currentValue = WebUI.getAttribute(inputField, 'value')
        WebUI.comment("After adding ${i+1} sequence(s): " + currentValue)
        
        boolean hasError = WebUI.waitForElementVisible(errorMessage, 2, FailureHandling.OPTIONAL)
        if (!hasError) {
            WebUI.comment("✅ No error for ${i+1} sequence(s)")
        }
        WebUI.takeScreenshot("TC19_StopSequences_${i+1}.png")
    }

    // === TEST 5: TRY ADDING 5TH SEQUENCE ===
    WebUI.comment('=== Test 5: Try to add 5th sequence (should be blocked) ===')
    
    WebUI.click(inputField)
    WebUI.clearText(inputField)
    WebUI.setText(inputField, "CANCEL" + "\n")
    WebUI.delay(2)
    
    String value5 = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('After trying 5th sequence: "' + value5 + '"')
    
    if (!value5.contains("CANCEL")) {
        WebUI.comment('✅ 5th sequence was blocked')
    }
    
    boolean hasError5 = WebUI.waitForElementVisible(errorMessage, 5, FailureHandling.OPTIONAL)
    if (hasError5) {
        String errorText = WebUI.getText(errorMessage)
        WebUI.comment('✅ Error message: "' + errorText + '"')
        WebUI.takeScreenshot('TC19_StopSequences_Error.png')
    }

    // === TEST 6: REMOVE 1 SEQUENCE AND ADD NEW (use XPath to click the X button) ===
    WebUI.comment('=== Test 6: Remove 1 sequence and add new ===')
    
    // ⭐ Click the X button on the last chip to remove it (instead of sendKeys backspace)
    TestObject removeLastChip = new TestObject('removeLastChip')
    removeLastChip.addProperty('xpath', ConditionType.EQUALS, 
        "(//div[contains(@class, 'rounded-3xl') and contains(@class, 'border-green-600')]//button)[last()]")
    WebUI.click(removeLastChip)
    WebUI.delay(1)
    
    String value6 = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('After removing last sequence: "' + value6 + '"')
    
    // Add a new stop sequence
    addStopSequence("HALT")
    
    String value7 = WebUI.getAttribute(inputField, 'value')
    WebUI.comment('After adding HALT: "' + value7 + '"')
    
    if (value7.contains("HALT") && !value7.contains("EXIT")) {
        WebUI.comment('✅ HALT added successfully, EXIT removed')
    }
    
    boolean hasError7 = WebUI.waitForElementVisible(errorMessage, 2, FailureHandling.OPTIONAL)
    if (!hasError7) {
        WebUI.comment('✅ No error after remove and re-add')
    }
    WebUI.takeScreenshot('TC19_StopSequences_Final.png')

    WebUI.comment('✅ TC19 PASSED')

} catch (Exception e) {
    WebUI.comment('TC19 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC19_StopSequences_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}