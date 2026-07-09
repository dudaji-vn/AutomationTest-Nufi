import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys

/**
 * TC17: Parameters - Reset Model Parameters Button
 * Verify Reset Model Parameters button resets all parameters to default values
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get initial default values of parameters
 * 3. Change multiple parameters (Temperature, Top P, Frequency Penalty, Presence Penalty)
 * 4. Click Reset Model Parameters button
 * 5. Verify all parameters reset to default values
 * 6. Final verification
 */

WebUI.comment('=== TC17: Reset Model Parameters Button ===')

try {
    // ============================================================
    // STEP 0: CHECK NAVBAR
    // ============================================================
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
            WebUI.comment('Navbar opened successfully')
        }
    } else {
        WebUI.comment('Navbar is open (aria-hidden="false")')
    }

    // ============================================================
    // STEP 1: VERIFY PARAMETERS TAB
    // ============================================================
    WebUI.comment('Step 1: Checking Parameters tab state...')
    
    TestObject parametersButton = findTestObject('Object Repository/nav/nav_items/button_Parameters')
    WebUI.waitForElementVisible(parametersButton, 10)
    
    String ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
    String isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
    
    WebUI.comment('Parameters button aria-label: ' + ariaLabel)
    WebUI.comment('Parameters button aria-pressed: ' + isPressed)
    
    if (ariaLabel == 'Parameters' && isPressed == 'true') {
        WebUI.comment('Parameters tab is open (correct aria-label and aria-pressed)')
    } else {
        WebUI.comment('Parameters tab not open or wrong label, clicking to open...')
        WebUI.click(parametersButton)
        WebUI.delay(2)
        
        ariaLabel = WebUI.getAttribute(parametersButton, 'aria-label')
        isPressed = WebUI.getAttribute(parametersButton, 'aria-pressed')
        WebUI.comment('After click - Parameters button aria-label: ' + ariaLabel)
        WebUI.comment('After click - Parameters button aria-pressed: ' + isPressed)
        
        if (ariaLabel == 'Parameters' && isPressed == 'true') {
            WebUI.comment('Parameters tab opened successfully')
        }
    }

    // ============================================================
    // STEP 2: GET RESET BUTTON AND PARAMETER ELEMENTS
    // ============================================================
    WebUI.comment('Step 2: Getting Reset Model Parameters button and parameter elements...')
    
    // Reset button
    TestObject resetButton = findTestObject('Object Repository/nav/Parameter/button_Reset Model Parameters')
    WebUI.waitForElementVisible(resetButton, 10)
    WebUI.comment('Reset Model Parameters button found')
    
    // Parameter input elements
    TestObject temperatureInput = findTestObject('Object Repository/nav/Parameter/input_Temperature')
    TestObject topPInput = findTestObject('Object Repository/nav/Parameter/input_Top P')
    TestObject freqPenaltyInput = findTestObject('Object Repository/nav/Parameter/input_Frequency Penalty')
    TestObject presencePenaltyInput = findTestObject('Object Repository/nav/Parameter/input_Presence Penalty')
    
    WebUI.waitForElementVisible(temperatureInput, 10)
    WebUI.waitForElementVisible(topPInput, 10)
    WebUI.waitForElementVisible(freqPenaltyInput, 10)
    WebUI.waitForElementVisible(presencePenaltyInput, 10)
    WebUI.comment('All parameter inputs found')

    // ============================================================
    // STEP 3: GET DEFAULT VALUES
    // ============================================================
    WebUI.comment('Step 3: Getting default values...')
    
    String defaultTemp = WebUI.getAttribute(temperatureInput, 'value')
    String defaultTopP = WebUI.getAttribute(topPInput, 'value')
    String defaultFreq = WebUI.getAttribute(freqPenaltyInput, 'value')
    String defaultPresence = WebUI.getAttribute(presencePenaltyInput, 'value')
    
    WebUI.comment('Default Temperature: ' + defaultTemp)
    WebUI.comment('Default Top P: ' + defaultTopP)
    WebUI.comment('Default Frequency Penalty: ' + defaultFreq)
    WebUI.comment('Default Presence Penalty: ' + defaultPresence)
    WebUI.takeScreenshot('TC17_Reset_Default_Values.png')

    // ============================================================
    // HELPER FUNCTIONS
    // ============================================================
    
    // Helper: Dismiss popup/tooltip
    def dismissPopup = { TestObject element ->
        WebUI.comment("→ Dismissing any popup/tooltip...")
        WebUI.clickOffset(element, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(element, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(element, 0, -100)
        WebUI.delay(0.5)
    }
    
    // Helper: Set parameter value via input field
    def setParameterValue = { TestObject inputElement, String value ->
        // Dismiss popup before clicking
        dismissPopup(inputElement)
        
        WebUI.comment("→ Setting to: " + value)
        WebUI.click(inputElement)
        WebUI.sendKeys(inputElement, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(inputElement, value)
        WebUI.clickOffset(inputElement, 250, 0)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputElement, -50, 50)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputElement, 100, 100)
        WebUI.delay(0.3)
        WebUI.clickOffset(inputElement, 0, -100)
        WebUI.delay(0.5)
        
        String actualValue = WebUI.getAttribute(inputElement, 'value').trim()
        WebUI.comment("→ After blur, value: " + actualValue)
    }

    // Helper: Verify parameter value using numeric comparison (support 0.2 vs 0.20)
    def verifyParameterNumeric = { TestObject inputElement, String expectedStr, String paramName ->
        String actualStr = WebUI.getAttribute(inputElement, 'value').trim()
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal expectedNum = new BigDecimal(expectedStr)
            
            if (actualNum.compareTo(expectedNum) == 0) {
                WebUI.comment("PASSED - ${paramName}: Expected '${expectedStr}' → Actual: '${actualStr}' ✓")
                return true
            } else {
                WebUI.comment("FAILED - ${paramName}: Expected '${expectedStr}' but got '${actualStr}'")
                KeywordUtil.markFailed("FAILED - ${paramName}: Expected '${expectedStr}' but got '${actualStr}'")
                return false
            }
        } catch (Exception e) {
            WebUI.comment("FAILED - ${paramName}: Cannot convert to number. Actual: '${actualStr}'")
            KeywordUtil.markFailed("FAILED - ${paramName}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }

    // ============================================================
    // STEP 4: CHANGE PARAMETERS TO NEW VALUES
    // ============================================================
    WebUI.comment('Step 4: Changing parameters to new values...')
    
    WebUI.comment('--- Changing Temperature to 0.2 ---')
    setParameterValue(temperatureInput, "0.2")
    WebUI.takeScreenshot('TC17_Reset_Temp_Changed.png')
    
    WebUI.comment('--- Changing Top P to 0.3 ---')
    setParameterValue(topPInput, "0.3")
    WebUI.takeScreenshot('TC17_Reset_TopP_Changed.png')
    
    WebUI.comment('--- Changing Frequency Penalty to 1.5 ---')
    setParameterValue(freqPenaltyInput, "1.5")
    WebUI.takeScreenshot('TC17_Reset_Freq_Changed.png')
    
    WebUI.comment('--- Changing Presence Penalty to 1.5 ---')
    setParameterValue(presencePenaltyInput, "1.5")
    WebUI.takeScreenshot('TC17_Reset_Presence_Changed.png')

    // Verify changes applied - using numeric comparison
    WebUI.comment('Verifying changes applied...')
    verifyParameterNumeric(temperatureInput, "0.2", "Temperature after change")
    verifyParameterNumeric(topPInput, "0.3", "Top P after change")
    verifyParameterNumeric(freqPenaltyInput, "1.5", "Frequency Penalty after change")
    verifyParameterNumeric(presencePenaltyInput, "1.5", "Presence Penalty after change")

    // ============================================================
    // STEP 5: CLICK RESET MODEL PARAMETERS BUTTON
    // ============================================================
    WebUI.comment('Step 5: Clicking Reset Model Parameters button...')
    
    WebUI.click(resetButton)
    WebUI.delay(2)
    WebUI.comment('Reset button clicked')
    WebUI.takeScreenshot('TC17_Reset_Button_Clicked.png')

    // ============================================================
    // STEP 6: VERIFY ALL PARAMETERS RESET TO DEFAULT
    // ============================================================
    WebUI.comment('Step 6: Verifying all parameters reset to default...')
    
    // Read values after reset
    String resetTemp = WebUI.getAttribute(temperatureInput, 'value').trim()
    String resetTopP = WebUI.getAttribute(topPInput, 'value').trim()
    String resetFreq = WebUI.getAttribute(freqPenaltyInput, 'value').trim()
    String resetPresence = WebUI.getAttribute(presencePenaltyInput, 'value').trim()
    
    WebUI.comment('Temperature after reset: ' + resetTemp)
    WebUI.comment('Top P after reset: ' + resetTopP)
    WebUI.comment('Frequency Penalty after reset: ' + resetFreq)
    WebUI.comment('Presence Penalty after reset: ' + resetPresence)
    
    // Verify each parameter matches default - using numeric comparison
    boolean allPassed = true
    
    // Helper: Compare reset value with default using numeric comparison
    def verifyResetValue = { String actualStr, String defaultStr, String paramName ->
        try {
            BigDecimal actualNum = new BigDecimal(actualStr)
            BigDecimal defaultNum = new BigDecimal(defaultStr)
            
            if (actualNum.compareTo(defaultNum) == 0) {
                WebUI.comment("PASSED - ${paramName} reset to default: " + actualStr)
                return true
            } else {
                WebUI.comment("FAILED - ${paramName}: Expected default '${defaultStr}' but got '${actualStr}'")
                return false
            }
        } catch (Exception e) {
            WebUI.comment("FAILED - ${paramName}: Cannot convert to number. Actual: '${actualStr}'")
            return false
        }
    }
    
    if (!verifyResetValue(resetTemp, defaultTemp, "Temperature")) allPassed = false
    if (!verifyResetValue(resetTopP, defaultTopP, "Top P")) allPassed = false
    if (!verifyResetValue(resetFreq, defaultFreq, "Frequency Penalty")) allPassed = false
    if (!verifyResetValue(resetPresence, defaultPresence, "Presence Penalty")) allPassed = false

    WebUI.takeScreenshot('TC17_Reset_After_Reset.png')

    // ============================================================
    // STEP 7: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 7: Final verification...')
    
    if (allPassed) {
        WebUI.comment('TC17 PASSED - Reset Model Parameters button works correctly')
    } else {
        WebUI.comment('TC17 FAILED - Reset Model Parameters button did not reset all parameters')
        KeywordUtil.markFailed("FAILED: Reset Model Parameters button did not reset all parameters to default")
    }

    WebUI.takeScreenshot('TC17_Reset_Complete.png')
    WebUI.comment('=== TC17 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC17 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC17_Reset_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}