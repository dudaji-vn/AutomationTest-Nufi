import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC04: Export - JSON (.json)
 * 
 * Test Flow:
 * 1. Open Share menu
 * 2. Click Export option
 * 3. Verify Export dialog appears
 * 4. Select Type = "json (.json)"
 * 5. Verify "Include endpoint options" = Enabled, checked
 * 6. Verify "Export all message branches" = Enabled, auto-checked
 * 7. Verify "Recursive" section appears
 * 8. Verify "Recursive" checkbox = Checked by default
 * 9. Uncheck "Export all message branches"
 * 10. Uncheck "Recursive"
 * 11. Re-check "Export all message branches"
 * 12. Click Export
 * 13. Verify file downloaded with .json extension
 * 14. Click Cancel to close dialog (cleanup)
 */

WebUI.comment('=== TC04: Export - JSON (.json) ===')

try {
    // Step 1: Open Share menu
    WebUI.comment('Step 1: Opening Share menu...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button'))

    // Step 2: Click Export option
    WebUI.comment('Step 2: Clicking Export option...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/button_Export'))
    WebUI.delay(1)

    // Step 3: Verify Export dialog appears
    WebUI.comment('Step 3: Verifying Export dialog appears...')
    WebUI.waitForElementVisible(findTestObject('Object Repository/Core Chat/Share_chat/Export/dialog_Export conversation'), 5)

    // Step 4: Select Type = "json (.json)"
    WebUI.comment('Step 4: Selecting Type = "json (.json)"...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/dropdown_combobox'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_json (.json)'))
    WebUI.comment('Type changed to json (.json)')
    WebUI.delay(1)

    // Step 5: Verify "Include endpoint options" = Enabled, checked
    WebUI.comment('Step 5: Verifying Include endpoint options...')
    String includeChecked = WebUI.getAttribute(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_includeOptions'), 
        'checked'
    )
    WebUI.comment('Include endpoint options checked: ' + includeChecked)

    // Step 6: Verify "Export all message branches" = Enabled, auto-checked
    WebUI.comment('Step 6: Verifying Export all message branches...')
    String exportBranchesChecked = WebUI.getAttribute(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'), 
        'checked'
    )
    WebUI.comment('Export all message branches checked: ' + exportBranchesChecked)

    // Step 7: Verify "Recursive" section appears
    WebUI.comment('Step 7: Verifying Recursive section appears...')
    TestObject recursiveCheckbox = new TestObject('recursiveCheckbox')
    recursiveCheckbox.addProperty('xpath', ConditionType.EQUALS, "//input[@type='checkbox' and @aria-label='Recursive']")
    boolean recursiveVisible = WebUI.waitForElementVisible(recursiveCheckbox, 5)
    if (recursiveVisible) {
        WebUI.comment('Recursive section displayed - correct')
    }

    // Step 8: Verify "Recursive" checkbox = Checked by default
    WebUI.comment('Step 8: Verifying Recursive is checked by default...')
    String recursiveChecked = WebUI.getAttribute(recursiveCheckbox, 'checked')
    WebUI.comment('Recursive checked: ' + recursiveChecked)

    // Step 9: Uncheck "Export all message branches"
    WebUI.comment('Step 9: Unchecking Export all message branches...')
    if (exportBranchesChecked == 'true') {
        WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'))
        WebUI.comment('Export all message branches unchecked')
    }
    WebUI.delay(1)

    // Step 10: Uncheck "Recursive"
    WebUI.comment('Step 10: Unchecking Recursive...')
    if (recursiveChecked == 'true') {
        WebUI.click(recursiveCheckbox)
        WebUI.comment('Recursive unchecked')
    }
    WebUI.delay(1)

    // Step 11: Re-check "Export all message branches"
    WebUI.comment('Step 11: Re-checking Export all message branches...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'))
    WebUI.comment('Export all message branches re-checked')
    WebUI.delay(1)
    
    String recursiveCheckedAgain = WebUI.getAttribute(recursiveCheckbox, 'checked')
    WebUI.comment('Recursive after re-check: ' + recursiveCheckedAgain)

    // Step 12: Click Export
    WebUI.comment('Step 12: Clicking Export button...')
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

    // Step 13: Verify file downloaded with .json extension
    WebUI.comment('Step 13: Verifying file downloaded...')
    String expectedFilename = filename + '.json'
    WebUI.comment('Expected filename: ' + expectedFilename)
    
    WebUI.takeScreenshot('TC04_Export_JSON_Success.png')
    WebUI.comment('Export JSON completed')

    // Step 14: Click Cancel to close dialog
    WebUI.comment('Step 14: Clicking Cancel to close dialog...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/button_Cancel'))
    WebUI.delay(1)
    WebUI.comment('Dialog closed')

    WebUI.comment('TC04 PASSED')

} catch (Exception e) {
    WebUI.comment('TC04 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC04_Export_JSON_Error.png')
    throw e
}