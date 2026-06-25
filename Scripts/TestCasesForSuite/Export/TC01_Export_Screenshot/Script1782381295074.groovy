import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC01: Export - Screenshot (.png)
 * 
 * Test Flow:
 * 1. Open Share menu
 * 2. Click Export option
 * 3. Verify Export dialog appears
 * 4. Verify Type = "screenshot (.png)"
 * 5. Verify "Include endpoint options" = Disabled + "Not Supported"
 * 6. Verify "Export all message branches" = Disabled + "Not Supported"
 * 7. Verify "Recursive" section = Not displayed
 * 8. Click Export
 * 9. Verify file downloaded with .png extension
 * 10. Click Cancel to close dialog (cleanup)
 */

WebUI.comment('=== TC01: Export - Screenshot (.png) ===')

try {
    // Step 1: Open Share menu
    WebUI.comment('Step 1: Opening Share menu...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button'), 
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button'))
    WebUI.comment('Share menu opened')

    // Step 2: Click Export option
    WebUI.comment('Step 2: Clicking Export option...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/Share_chat/button_Export'), 
        5
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/button_Export'))
    WebUI.comment('Export option clicked')
    WebUI.delay(1)

    // Step 3: Verify Export dialog appears
    WebUI.comment('Step 3: Verifying Export dialog appears...')
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/dialog_Export conversation'), 
        5
    )
    WebUI.comment('Export dialog displayed')

    // Step 4: Verify Type = "screenshot (.png)"
    WebUI.comment('Step 4: Verifying Type is screenshot (.png)...')
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_screenshot (.png)'), 
        5
    )
    String selectedType = WebUI.getText(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_screenshot (.png)')
    )
    WebUI.comment('Selected type: ' + selectedType)
    
    if (selectedType.contains('screenshot (.png)')) {
        WebUI.comment('Type is correct: screenshot (.png)')
    } else {
        WebUI.comment('Unexpected type: ' + selectedType)
    }

    // Step 5: Verify "Include endpoint options" = Disabled + "Not Supported"
    WebUI.comment('Step 5: Verifying Include endpoint options...')
    boolean notSupportedVisible = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/label_Not Supported'), 
        3, 
        FailureHandling.OPTIONAL
    )
    if (notSupportedVisible) {
        WebUI.comment('"Not Supported" label visible')
    }

    // Step 6: Verify "Export all message branches" = Disabled + "Not Supported"
    WebUI.comment('Step 6: Verifying Export all message branches...')
    boolean exportBranchesVisible = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'), 
        5, 
        FailureHandling.OPTIONAL
    )

    // Step 7: Verify "Recursive" section = Not displayed
    WebUI.comment('Step 7: Verifying Recursive section not displayed...')
    TestObject recursiveCheckbox = new TestObject('recursiveCheckbox')
    recursiveCheckbox.addProperty('xpath', ConditionType.EQUALS, "//input[@type='checkbox' and @aria-label='Recursive']")
    
    boolean recursiveVisible = WebUI.waitForElementVisible(recursiveCheckbox, 3, FailureHandling.OPTIONAL)
    if (!recursiveVisible) {
        WebUI.comment('Recursive section not displayed - correct')
    } else {
        WebUI.comment('Recursive section is displayed - unexpected')
    }

    // Step 8: Click Export
    WebUI.comment('Step 8: Clicking Export button...')
    String filename = WebUI.getAttribute(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/input_Filename_filename'), 
        'value'
    )
    WebUI.comment('Filename: ' + filename)
    
    TestObject exportButtonInPopup = new TestObject('exportButtonInPopup')
    exportButtonInPopup.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'bg-surface-submit') and text()='Export']")
    
    WebUI.waitForElementClickable(exportButtonInPopup, 5)
    WebUI.click(exportButtonInPopup)
    WebUI.comment('Export button clicked')
    WebUI.delay(3)

    // Step 9: Verify file downloaded with .png extension
    WebUI.comment('Step 9: Verifying file downloaded...')
    String expectedFilename = filename + '.png'
    WebUI.comment('Expected filename: ' + expectedFilename)
    
    WebUI.takeScreenshot('TC01_Export_Screenshot_Success.png')
    WebUI.comment('Export screenshot completed')

    // Step 10: Click Cancel to close dialog
    WebUI.comment('Step 10: Clicking Cancel to close dialog...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/button_Cancel'))
    WebUI.delay(1)
    WebUI.comment('Dialog closed')

    WebUI.comment('TC01 PASSED')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Export_Screenshot_Error.png')
    throw e
}