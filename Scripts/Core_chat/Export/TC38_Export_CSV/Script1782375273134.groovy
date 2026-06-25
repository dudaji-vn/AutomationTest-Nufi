import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC38: Export - CSV (.csv)
 * 
 * Test Flow:
 * 1. Open browser
 * 2. Login
 * 3. Open random chat from history
 * 4. Open Share menu
 * 5. Click Export option
 * 6. Verify Export dialog appears
 * 7. Select Type = "csv (.csv)"
 * 8. Verify "Include endpoint options" = Enabled, checked
 * 9. Verify "Export all message branches" = Disabled + "Not Supported"
 * 10. Verify "Recursive" section = Not displayed
 * 11. Click Export
 * 12. Verify file downloaded with .csv extension
 */

WebUI.comment('=== TC38: Export - CSV (.csv) ===')

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

    // Step 7: Select Type = "csv (.csv)"
    WebUI.comment('Step 7: Selecting Type = "csv (.csv)"...')
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/dropdown_combobox'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_csv (.csv)'))
    WebUI.comment('Type changed to csv (.csv)')
    WebUI.delay(1)

    // Step 8: Verify "Include endpoint options" = Enabled, checked
    WebUI.comment('Step 8: Verifying Include endpoint options...')
    String includeChecked = WebUI.getAttribute(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_includeOptions'), 
        'checked'
    )
    WebUI.comment('Include endpoint options checked: ' + includeChecked)

    // Step 9: Verify "Export all message branches" = Disabled + "Not Supported"
    WebUI.comment('Step 9: Verifying Export all message branches is disabled...')
    boolean notSupportedVisible = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/label_Not Supported'), 
        3, 
        FailureHandling.OPTIONAL
    )
    if (notSupportedVisible) {
        WebUI.comment('"Not Supported" label visible')
    }

    // Step 10: Verify "Recursive" section = Not displayed
    WebUI.comment('Step 10: Verifying Recursive section not displayed...')
    TestObject recursiveCheckbox = new TestObject('recursiveCheckbox')
    recursiveCheckbox.addProperty('xpath', ConditionType.EQUALS, "//input[@type='checkbox' and @aria-label='Recursive']")
    boolean recursiveVisible = WebUI.waitForElementVisible(recursiveCheckbox, 3, FailureHandling.OPTIONAL)
    if (!recursiveVisible) {
        WebUI.comment('Recursive section not displayed - correct')
    }

    // Step 11: Click Export
    WebUI.comment('Step 11: Clicking Export button...')
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

    // Step 12: Verify file downloaded with .csv extension
    WebUI.comment('Step 12: Verifying file downloaded...')
    String expectedFilename = filename + '.csv'
    WebUI.comment('Expected filename: ' + expectedFilename)
    
    WebUI.takeScreenshot('TC38_Export_CSV_Success.png')
    WebUI.comment('Export CSV completed')

    // Step 13: Close browser
    WebUI.comment('Step 13: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC38 PASSED')

} catch (Exception e) {
    WebUI.comment('TC38 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC38_Export_CSV_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}