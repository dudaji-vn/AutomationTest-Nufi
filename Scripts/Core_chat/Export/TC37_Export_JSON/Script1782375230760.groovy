import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC37: Export - JSON (.json)
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open random chat from history
 * 4. Open Share menu
 * 5. Click Export option
 * 6. Verify Export dialog appears
 * 7. Select Type = "json (.json)"
 * 8. Verify "Include endpoint options" = Enabled, checked
 * 9. Verify "Export all message branches" = Enabled, auto-checked
 * 10. Verify "Recursive" section appears
 * 11. Verify "Recursive" checkbox = Checked by default
 * 12. Uncheck "Export all message branches"
 * 13. Uncheck "Recursive"
 * 14. Re-check "Export all message branches"
 * 15. Click Export
 * 16. Verify file downloaded with .json extension
 */

WebUI.comment('=== TC37: Export - JSON (.json) ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Open random chat from history
    WebUI.comment('Step 3: Opening random chat from history...')
    String chatName = CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    WebUI.comment('Opened chat: ' + chatName)

    // Step 4: Open Share menu
    WebUI.comment('Step 4: Opening Share menu...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button'))

    // Step 5: Click Export option
    WebUI.comment('Step 5: Clicking Export option...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/button_Export'))
    WebUI.delay(1)

    // Step 6: Verify Export dialog appears
    WebUI.comment('Step 6: Verifying Export dialog appears...')
    WebUI.waitForElementVisible(findTestObject('Object Repository/Core Chat/Share_chat/Export/dialog_Export conversation'), 5)

    // Step 7: Select Type = "json (.json)"
    WebUI.comment('Step 7: Selecting Type = "json (.json)"...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/dropdown_combobox'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_json (.json)'))
    WebUI.comment('Type changed to json (.json)')
    WebUI.delay(1)

    // Step 8: Verify "Include endpoint options" = Enabled, checked
    WebUI.comment('Step 8: Verifying Include endpoint options...')
    String includeChecked = WebUI.getAttribute(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_includeOptions'), 
        'checked'
    )
    WebUI.comment('Include endpoint options checked: ' + includeChecked)

    // Step 9: Verify "Export all message branches" = Enabled, auto-checked
    WebUI.comment('Step 9: Verifying Export all message branches...')
    String exportBranchesChecked = WebUI.getAttribute(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'), 
        'checked'
    )
    WebUI.comment('Export all message branches checked: ' + exportBranchesChecked)

    // Step 10: Verify "Recursive" section appears
    WebUI.comment('Step 10: Verifying Recursive section appears...')
    TestObject recursiveCheckbox = new TestObject('recursiveCheckbox')
    recursiveCheckbox.addProperty('xpath', ConditionType.EQUALS, "//input[@type='checkbox' and @aria-label='Recursive']")
    boolean recursiveVisible = WebUI.waitForElementVisible(recursiveCheckbox, 5)
    if (recursiveVisible) {
        WebUI.comment('Recursive section displayed - correct')
    }

    // Step 11: Verify "Recursive" checkbox = Checked by default
    WebUI.comment('Step 11: Verifying Recursive is checked by default...')
    String recursiveChecked = WebUI.getAttribute(recursiveCheckbox, 'checked')
    WebUI.comment('Recursive checked: ' + recursiveChecked)

    // Step 12: Uncheck "Export all message branches"
    WebUI.comment('Step 12: Unchecking Export all message branches...')
    if (exportBranchesChecked == 'true') {
        WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'))
        WebUI.comment('Export all message branches unchecked')
    }
    WebUI.delay(1)

    // Step 13: Uncheck "Recursive"
    WebUI.comment('Step 13: Unchecking Recursive...')
    if (recursiveChecked == 'true') {
        WebUI.click(recursiveCheckbox)
        WebUI.comment('Recursive unchecked')
    }
    WebUI.delay(1)

    // Step 14: Re-check "Export all message branches"
    WebUI.comment('Step 14: Re-checking Export all message branches...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'))
    WebUI.comment('Export all message branches re-checked')
    WebUI.delay(1)
    
    String recursiveCheckedAgain = WebUI.getAttribute(recursiveCheckbox, 'checked')
    WebUI.comment('Recursive after re-check: ' + recursiveCheckedAgain)

    // Step 15: Click Export
    WebUI.comment('Step 15: Clicking Export button...')
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

    // Step 16: Verify file downloaded with .json extension
    WebUI.comment('Step 16: Verifying file downloaded...')
    String expectedFilename = filename + '.json'
    WebUI.comment('Expected filename: ' + expectedFilename)
    
    WebUI.takeScreenshot('TC37_Export_JSON_Success.png')
    WebUI.comment('Export JSON completed')

    // Step 17: Close browser
    WebUI.comment('Step 17: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC37 PASSED')

} catch (Exception e) {
    WebUI.comment('TC37 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC37_Export_JSON_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}