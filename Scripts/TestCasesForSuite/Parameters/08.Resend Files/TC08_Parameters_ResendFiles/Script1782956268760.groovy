import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC08: Parameters - Resend Files Toggle
 * 
 * Test Flow:
 * 1. Open Parameters panel
 * 2. Get Resend Files toggle and verify state
 * 3. Toggle to opposite state and verify
 * 4. Toggle back to original state and verify
 * 5. Final verification
 */

WebUI.comment('=== TC08: Resend Files - Toggle Test ===')

try {
    // Step 1: Open Parameters panel
    WebUI.comment('Step 1: Opening Parameters panel...')
    
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    
    if (screenWidth <= 768) {
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
    }
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Parameters'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Parameters'))
    WebUI.delay(2)

    // Step 2: Get Resend Files toggle
    WebUI.comment('Step 2: Getting Resend Files toggle...')
    
    TestObject resendFilesToggle = findTestObject('Object Repository/nav/Parameter/button_Resend Files')
    WebUI.waitForElementVisible(resendFilesToggle, 10)
    WebUI.comment('Resend Files toggle found')

    // Step 3: Get initial state
    WebUI.comment('Step 3: Checking initial state...')
    String initialAriaChecked = WebUI.getAttribute(resendFilesToggle, 'aria-checked')
    WebUI.comment('Initial aria-checked: ' + initialAriaChecked)
    
    boolean isInitiallyChecked = (initialAriaChecked == 'true')
    
    if (isInitiallyChecked) {
        WebUI.comment('Resend Files is ON by default')
    } else {
        WebUI.comment('Resend Files is OFF by default')
    }
    WebUI.takeScreenshot('TC21_ResendFiles_Initial.png')

    // Step 4: Toggle to opposite state
    WebUI.comment('Step 4: Toggling Resend Files...')
    WebUI.click(resendFilesToggle)
    WebUI.delay(1)
    
    String afterFirstToggle = WebUI.getAttribute(resendFilesToggle, 'aria-checked')
    WebUI.comment('After first toggle - aria-checked: ' + afterFirstToggle)
    
    boolean isCheckedAfterFirst = (afterFirstToggle == 'true')
    
    if (isCheckedAfterFirst != isInitiallyChecked) {
        WebUI.comment('Toggle changed state successfully')
    } else {
        WebUI.comment('Toggle did not change state')
        KeywordUtil.markFailed("FAILED: Toggle did not change state")
    }
    WebUI.takeScreenshot('TC21_ResendFiles_AfterFirstToggle.png')

    // Step 5: Toggle back to original state
    WebUI.comment('Step 5: Toggling back to original state...')
    WebUI.click(resendFilesToggle)
    WebUI.delay(1)
    
    String afterSecondToggle = WebUI.getAttribute(resendFilesToggle, 'aria-checked')
    WebUI.comment('After second toggle - aria-checked: ' + afterSecondToggle)
    
    boolean isCheckedAfterSecond = (afterSecondToggle == 'true')
    
    if (isCheckedAfterSecond == isInitiallyChecked) {
        WebUI.comment('Toggle returned to original state successfully')
    } else {
        WebUI.comment('Toggle did not return to original state')
        KeywordUtil.markFailed("FAILED: Toggle did not return to original state")
    }
    WebUI.takeScreenshot('TC21_ResendFiles_AfterSecondToggle.png')

    // Step 6: Final verification
    WebUI.comment('Step 6: Final verification...')
    String finalAriaChecked = WebUI.getAttribute(resendFilesToggle, 'aria-checked')
    WebUI.comment('Final aria-checked: ' + finalAriaChecked)
    
    if (finalAriaChecked == initialAriaChecked) {
        WebUI.comment('Resend Files toggle test passed')
        WebUI.takeScreenshot('TC21_ResendFiles_Success.png')
    } else {
        WebUI.comment('Resend Files toggle final state does not match initial')
        WebUI.takeScreenshot('TC21_ResendFiles_FinalMismatch.png')
        KeywordUtil.markFailed("FAILED: Final state does not match initial state")
    }

    WebUI.comment('TC21 PASSED')

} catch (Exception e) {
    WebUI.comment('TC21 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC21_ResendFiles_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}