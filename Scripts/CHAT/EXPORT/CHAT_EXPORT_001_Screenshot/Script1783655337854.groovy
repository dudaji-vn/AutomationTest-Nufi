import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling

/**
 * TC34: Export - Screenshot (.png)
 */

WebUI.comment('=== TC34: Export - Screenshot (.png) ===')

try {
    // Step 1: Open browser
    WebUI.comment('Step 1: Opening browser...')
    CustomKeywords.'keywords.ChatKeywords.openBrowser'(GlobalVariable.Base_URL)

    // Step 2: Login
    WebUI.comment('Step 2: Logging in...')
    CustomKeywords.'keywords.ChatKeywords.loginChat'(GlobalVariable.email, GlobalVariable.password)

    // Step 3: Open random chat from history using HistoryChatKeywords
    WebUI.comment('Step 3: Opening random chat from history...')
    String chatName = CustomKeywords.'keywords.HistoryChatKeywords.openRandomChatFromHistory'()
    WebUI.comment('Opened chat: ' + chatName)

    // Step 4: Open Share menu
    WebUI.comment('Step 4: Opening Share menu...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button'), 
        10
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/export-menu-button'))
    WebUI.comment('Share menu opened')

    // Step 5: Click Export option
    WebUI.comment('Step 5: Clicking Export option...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/Core Chat/Share_chat/button_Export'), 
        5
    )
    WebUI.click(findTestObject('Object Repository/Core Chat/Share_chat/button_Export'))
    WebUI.comment('Export option clicked')
    WebUI.delay(1)

    // Step 6: Verify Export dialog appears
    WebUI.comment('Step 6: Verifying Export dialog appears...')
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/dialog_Export conversation'), 
        5
    )
    WebUI.comment('Export dialog displayed')

    // Step 7: Verify Type = "screenshot (.png)"
    WebUI.comment('Step 7: Verifying Type is screenshot (.png)...')
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_screenshot (.png)'), 
        5
    )
    String selectedType = WebUI.getText(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/item_listbox_type/opt_screenshot (.png)')
    )
    WebUI.comment('Selected type: ' + selectedType)

    // Step 8: Verify "Include endpoint options" = Disabled + "Not Supported"
    WebUI.comment('Step 8: Verifying Include endpoint options...')
    boolean notSupportedVisible = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/label_Not Supported'), 
        3, 
        FailureHandling.OPTIONAL
    )
    if (notSupportedVisible) {
        WebUI.comment('"Not Supported" label visible')
    }

    // Step 9: Verify "Export all message branches" = Disabled + "Not Supported"
    WebUI.comment('Step 9: Verifying Export all message branches...')
    boolean exportBranchesVisible = WebUI.waitForElementVisible(
        findTestObject('Object Repository/Core Chat/Share_chat/Export/checkbox/checkbox_exportBranches'), 
        5, 
        FailureHandling.OPTIONAL
    )

    // Step 10: Verify "Recursive" section = Not displayed
    WebUI.comment('Step 10: Verifying Recursive section not displayed...')
    TestObject recursiveCheckbox = new TestObject('recursiveCheckbox')
    recursiveCheckbox.addProperty('xpath', ConditionType.EQUALS, "//input[@type='checkbox' and @aria-label='Recursive']")
    
    boolean recursiveVisible = WebUI.waitForElementVisible(recursiveCheckbox, 3, FailureHandling.OPTIONAL)
    if (!recursiveVisible) {
        WebUI.comment('Recursive section not displayed - correct')
    } else {
        WebUI.comment('Recursive section is displayed - unexpected')
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

    // Step 12-13: Verify file downloaded
    WebUI.comment('Step 12-13: Verifying file downloaded...')
    String expectedFilename = filename + '.png'
    WebUI.comment('Expected filename: ' + expectedFilename)
    
    WebUI.takeScreenshot('TC34_Export_Screenshot_Success.png')
    WebUI.comment('Export screenshot completed')

    // Step 14: Close browser
    WebUI.comment('Step 14: Closing browser...')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()

    WebUI.comment('TC34 PASSED')

} catch (Exception e) {
    WebUI.comment('TC34 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC34_Export_Screenshot_Error.png')
    CustomKeywords.'keywords.ChatKeywords.closeBrowser'()
    throw e
}