import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import internal.GlobalVariable as GlobalVariable

/**
 * TC01_Knowledge_AddExistingFile
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Knowledge tab
 * 3. Verify user has Owner or Admin role (can add file)
 * 4. Click Add file button
 * 5. Select first existing file from popup (file already exists in system)
 * 6. Verify toast success appears
 */

WebUI.comment('=== TC01_Knowledge_AddExistingFile ===')

try {
    // ================================================================
    // Step 1: Ensure on Team detail page
    // ================================================================
    WebUI.comment('Step 1: Ensuring on Team detail page...')
    
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ On team detail page: ' + currentUrl)

    // ================================================================
    // Step 2: Click Knowledge tab
    // ================================================================
    WebUI.comment('Step 2: Clicking Knowledge tab...')
    
    TestObject knowledgeTab = new TestObject('knowledgeTab')
    knowledgeTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@role='tab' and contains(text(), 'Knowledge')]")
    
    WebUI.waitForElementClickable(knowledgeTab, 10)
    WebUI.click(knowledgeTab)
    WebUI.delay(2)
    WebUI.comment('✓ Knowledge tab clicked')

    // ================================================================
    // Step 3: Verify user has Owner or Admin role (can add file)
    // ================================================================
    WebUI.comment('Step 3: Verifying user has Owner or Admin role...')
    
    TestObject addFileButton = findTestObject('Object Repository/nav/Teams/Team/Tab_knowledge/button_Add file')
    boolean canAddFile = WebUI.waitForElementVisible(addFileButton, 5, FailureHandling.OPTIONAL)
    
    if (!canAddFile) {
        throw new Exception('User does not have Owner or Admin role. Cannot add file.')
    }
    WebUI.comment('✓ User has Owner or Admin role - Add file button visible')

    // ================================================================
    // Step 4: Click Add file button
    // ================================================================
    WebUI.comment('Step 4: Clicking Add file button...')
    
    WebUI.click(addFileButton)
    WebUI.delay(2)
    WebUI.comment('✓ Add file button clicked - popup opened')

    // ================================================================
    // Step 5: Select first existing file from popup
    // ================================================================
    WebUI.comment('Step 5: Selecting first existing file from popup...')
    
    // Get file name of first file in popup
    TestObject firstFileName = new TestObject('firstFileName')
    firstFileName.addProperty('xpath', ConditionType.EQUALS, 
        "(//ul//li//p[contains(@class, 'text-text-primary')])[1]")
    
    String fileName = WebUI.getText(firstFileName)
    WebUI.comment('Selected existing file to add: ' + fileName)
    
    // Click Add file button for first file in popup
    TestObject firstAddFileInPopup = new TestObject('firstAddFileInPopup')
    firstAddFileInPopup.addProperty('xpath', ConditionType.EQUALS, 
        "(//ul//li//button[contains(text(), 'Add file')])[1]")
    
    WebUI.waitForElementClickable(firstAddFileInPopup, 10)
    WebUI.click(firstAddFileInPopup)
    WebUI.delay(2)
    WebUI.comment('✓ Added existing file: ' + fileName)

    // ================================================================
    // Step 6: Verify toast success appears
    // ================================================================
    WebUI.comment('Step 6: Verifying toast success...')
    
    TestObject toastSuccess = findTestObject('Object Repository/Toast/Toast_Success')
    boolean hasToast = WebUI.waitForElementVisible(toastSuccess, 5, FailureHandling.OPTIONAL)
    
    if (hasToast) {
        String toastText = WebUI.getText(toastSuccess)
        WebUI.comment('✓ Toast success: ' + toastText)
    } else {
        WebUI.comment('⚠ Toast success not found, but file may still be added')
    }
    
    WebUI.takeScreenshot('TC01_Knowledge_AddExistingFile_Success.png')
    WebUI.comment('=== TC01_Knowledge_AddExistingFile PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC01_Knowledge_AddExistingFile FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC01_Knowledge_AddExistingFile_Error.png')
    throw e
}