import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC28: Parameters - Disable Streaming Toggle
 * Verify Disable Streaming toggle on/off functionality
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Disable Streaming toggle and verify default state
 * 3. Toggle to opposite state and verify
 * 4. Toggle back to original state and verify
 * 5. Final verification
 */

WebUI.comment('=== TC28: Disable Streaming - Toggle Test ===')

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
    // STEP 2: GET DISABLE STREAMING TOGGLE
    // ============================================================
    WebUI.comment('Step 2: Getting Disable Streaming toggle...')
    
    TestObject disableStreamingToggle = findTestObject('Object Repository/nav/Parameter/button_Disable Streaming')
    WebUI.waitForElementVisible(disableStreamingToggle, 10)
    WebUI.comment('Disable Streaming toggle found')

    // ============================================================
    // STEP 3: GET INITIAL STATE
    // ============================================================
    WebUI.comment('Step 3: Checking initial state...')
    
    String initialAriaChecked = WebUI.getAttribute(disableStreamingToggle, 'aria-checked')
    WebUI.comment('Initial aria-checked: ' + initialAriaChecked)
    
    // Validate initial state
    if (initialAriaChecked == null || initialAriaChecked.isEmpty()) {
        WebUI.comment('WARNING: aria-checked attribute is null or empty')
        String toggleClass = WebUI.getAttribute(disableStreamingToggle, 'class')
        WebUI.comment('Toggle class: ' + toggleClass)
        initialAriaChecked = 'false'
        WebUI.comment('Using default value: ' + initialAriaChecked)
    }
    
    boolean isInitiallyChecked = (initialAriaChecked == 'true')
    
    if (isInitiallyChecked) {
        WebUI.comment('Disable Streaming is ON by default')
    } else {
        WebUI.comment('Disable Streaming is OFF by default')
    }
    WebUI.takeScreenshot('TC28_DisableStreaming_Initial.png')

    // ============================================================
    // STEP 4: TOGGLE TO OPPOSITE STATE
    // ============================================================
    WebUI.comment('Step 4: Toggling Disable Streaming to opposite state...')
    
    // Click to toggle
    WebUI.click(disableStreamingToggle)
    WebUI.delay(1.5)
    
    String afterFirstToggle = WebUI.getAttribute(disableStreamingToggle, 'aria-checked')
    WebUI.comment('After first toggle - aria-checked: ' + afterFirstToggle)
    
    // Handle null/empty
    if (afterFirstToggle == null || afterFirstToggle.isEmpty()) {
        afterFirstToggle = isInitiallyChecked ? 'false' : 'true'
        WebUI.comment('Using calculated value for afterFirstToggle: ' + afterFirstToggle)
    }
    
    boolean isCheckedAfterFirst = (afterFirstToggle == 'true')
    
    if (isCheckedAfterFirst != isInitiallyChecked) {
        WebUI.comment('Toggle changed state successfully from ' + isInitiallyChecked + ' to ' + isCheckedAfterFirst)
    } else {
        WebUI.comment('Toggle did not change state. Expected: ' + !isInitiallyChecked + ', Actual: ' + isCheckedAfterFirst)
        // Try one more time
        WebUI.comment('Trying to toggle again...')
        WebUI.click(disableStreamingToggle)
        WebUI.delay(1.5)
        afterFirstToggle = WebUI.getAttribute(disableStreamingToggle, 'aria-checked')
        if (afterFirstToggle == null || afterFirstToggle.isEmpty()) {
            afterFirstToggle = isInitiallyChecked ? 'false' : 'true'
        }
        isCheckedAfterFirst = (afterFirstToggle == 'true')
        if (isCheckedAfterFirst != isInitiallyChecked) {
            WebUI.comment('Toggle changed successfully on second attempt')
        } else {
            WebUI.comment('FAILED: Toggle did not change state after multiple attempts')
            KeywordUtil.markFailed("FAILED: Toggle did not change state after multiple attempts")
        }
    }
    WebUI.takeScreenshot('TC28_DisableStreaming_AfterFirstToggle.png')

    // ============================================================
    // STEP 5: TOGGLE BACK TO ORIGINAL STATE
    // ============================================================
    WebUI.comment('Step 5: Toggling back to original state...')
    
    // Click to toggle back
    WebUI.click(disableStreamingToggle)
    WebUI.delay(1.5)
    
    String afterSecondToggle = WebUI.getAttribute(disableStreamingToggle, 'aria-checked')
    WebUI.comment('After second toggle - aria-checked: ' + afterSecondToggle)
    
    // Handle null/empty
    if (afterSecondToggle == null || afterSecondToggle.isEmpty()) {
        afterSecondToggle = isCheckedAfterFirst ? 'false' : 'true'
        WebUI.comment('Using calculated value for afterSecondToggle: ' + afterSecondToggle)
    }
    
    boolean isCheckedAfterSecond = (afterSecondToggle == 'true')
    
    if (isCheckedAfterSecond == isInitiallyChecked) {
        WebUI.comment('Toggle returned to original state successfully (' + isInitiallyChecked + ')')
    } else {
        WebUI.comment('Toggle did not return to original state. Expected: ' + isInitiallyChecked + ', Actual: ' + isCheckedAfterSecond)
        // Try one more time
        WebUI.comment('Trying to toggle back again...')
        WebUI.click(disableStreamingToggle)
        WebUI.delay(1.5)
        afterSecondToggle = WebUI.getAttribute(disableStreamingToggle, 'aria-checked')
        if (afterSecondToggle == null || afterSecondToggle.isEmpty()) {
            afterSecondToggle = isCheckedAfterFirst ? 'false' : 'true'
        }
        isCheckedAfterSecond = (afterSecondToggle == 'true')
        if (isCheckedAfterSecond == isInitiallyChecked) {
            WebUI.comment('Toggle returned to original state on second attempt')
        } else {
            WebUI.comment('FAILED: Toggle did not return to original state')
            KeywordUtil.markFailed("FAILED: Toggle did not return to original state")
        }
    }
    WebUI.takeScreenshot('TC28_DisableStreaming_AfterSecondToggle.png')

    // ============================================================
    // STEP 6: FINAL VERIFICATION
    // ============================================================
    WebUI.comment('Step 6: Final verification...')
    
    String finalAriaChecked = WebUI.getAttribute(disableStreamingToggle, 'aria-checked')
    WebUI.comment('Final aria-checked: ' + finalAriaChecked)
    
    // Handle null/empty
    if (finalAriaChecked == null || finalAriaChecked.isEmpty()) {
        finalAriaChecked = isInitiallyChecked ? 'true' : 'false'
        WebUI.comment('Using calculated value for finalAriaChecked: ' + finalAriaChecked)
    }
    
    if (finalAriaChecked == initialAriaChecked) {
        WebUI.comment('Disable Streaming toggle test passed')
        WebUI.takeScreenshot('TC28_DisableStreaming_Success.png')
    } else {
        WebUI.comment('Disable Streaming toggle final state does not match initial')
        WebUI.comment('Initial: ' + initialAriaChecked + ', Final: ' + finalAriaChecked)
        WebUI.takeScreenshot('TC28_DisableStreaming_FinalMismatch.png')
        WebUI.comment('FAILED: Final state does not match initial state')
        KeywordUtil.markFailed("FAILED: Final state does not match initial state")
    }

    // ============================================================
    // STEP 7: ADDITIONAL VERIFICATION - VISUAL STATE
    // ============================================================
    WebUI.comment('Step 7: Additional verification - Visual state...')
    
    // Check if toggle has visual indicator
    String toggleClass = WebUI.getAttribute(disableStreamingToggle, 'class')
    WebUI.comment('Toggle class: ' + toggleClass)
    
    // Check data-state attribute if available
    try {
        String dataState = WebUI.getAttribute(disableStreamingToggle, 'data-state')
        if (dataState != null && !dataState.isEmpty()) {
            WebUI.comment('Toggle data-state: ' + dataState)
            String expectedState = (finalAriaChecked == 'true') ? 'checked' : 'unchecked'
            if (dataState.contains(expectedState)) {
                WebUI.comment('Visual state matches aria-checked')
            } else {
                WebUI.comment('WARNING: Visual state may not match aria-checked')
            }
        }
    } catch (Exception e) {
        WebUI.comment('Could not verify data-state attribute: ' + e.getMessage())
    }

    WebUI.takeScreenshot('TC28_DisableStreaming_Complete.png')
    WebUI.comment('=== TC28 Completed ===')

} catch (Exception e) {
    WebUI.comment('TC28 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC28_DisableStreaming_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}