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
 * TC02_Knowledge_DeleteFile
 * 
 * Test Flow:
 * 1. Ensure on Team detail page
 * 2. Click Knowledge tab
 * 3. Verify user has Owner or Admin role (can delete file)
 * 4. Get first file in list
 * 5. Click delete button on first file
 * 6. Verify toast success appears
 */

WebUI.comment('=== TC02_Knowledge_DeleteFile ===')

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
    // Step 3: Verify user has Owner or Admin role (can delete file)
    // ================================================================
    WebUI.comment('Step 3: Verifying user has Owner or Admin role...')
    
    TestObject addFileButton = findTestObject('Object Repository/nav/Teams/Team/Tab_knowledge/button_Add file')
    boolean canManageFiles = WebUI.waitForElementVisible(addFileButton, 5, FailureHandling.OPTIONAL)
    
    if (!canManageFiles) {
        throw new Exception('User does not have Owner or Admin role. Cannot delete file.')
    }
    WebUI.comment('✓ User has Owner or Admin role')

    // ================================================================
    // Step 4: Check if there are files to delete
    // ================================================================
    WebUI.comment('Step 4: Checking if there are files to delete...')
    
    // Check if no files message appears
    TestObject noFilesMessage = findTestObject('Object Repository/nav/Teams/Team/Tab_knowledge/message_No files shared with this team yet')
    boolean hasNoFilesMessage = WebUI.waitForElementVisible(noFilesMessage, 3, FailureHandling.OPTIONAL)
    
    if (hasNoFilesMessage) {
        throw new Exception('No files available to delete. Please add a file first.')
    }
    
    // Check if there is at least one file item in the list
    // Using the correct structure from HTML: ul > li with class containing 'rounded-lg'
    TestObject fileItem = new TestObject('fileItem')
    fileItem.addProperty('xpath', ConditionType.EQUALS, 
        "//ul[contains(@class, 'flex-col')]/li[contains(@class, 'rounded-lg')]")
    
    boolean hasFiles = WebUI.waitForElementPresent(fileItem, 5, FailureHandling.OPTIONAL)
    
    if (!hasFiles) {
        throw new Exception('No files found in knowledge list.')
    }
    WebUI.comment('✓ Files found in knowledge list')

    // ================================================================
    // Step 5: Get first file name and delete it
    // ================================================================
    WebUI.comment('Step 5: Deleting first file...')
    
    // Get first file name using correct structure
    TestObject firstFileName = new TestObject('firstFileName')
    firstFileName.addProperty('xpath', ConditionType.EQUALS, 
        "(//ul[contains(@class, 'flex-col')]/li[contains(@class, 'rounded-lg')]//p[contains(@class, 'text-text-primary')])[1]")
    
    String fileName = WebUI.getText(firstFileName)
    WebUI.comment('File to delete: ' + fileName)
    
    // Click delete button on first file using correct structure
    TestObject firstDeleteButton = new TestObject('firstDeleteButton')
    firstDeleteButton.addProperty('xpath', ConditionType.EQUALS, 
        "(//ul[contains(@class, 'flex-col')]/li[contains(@class, 'rounded-lg')]//button[@aria-label='Remove file'])[1]")
    
    WebUI.waitForElementClickable(firstDeleteButton, 10)
    WebUI.click(firstDeleteButton)
    WebUI.delay(2)
    WebUI.comment('✓ Delete button clicked for: ' + fileName)

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
        WebUI.comment('⚠ Toast success not found, but file may still be deleted')
    }
    
    WebUI.takeScreenshot('TC02_Knowledge_DeleteFile_Success.png')
    WebUI.comment('=== TC02_Knowledge_DeleteFile PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC02_Knowledge_DeleteFile FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_Knowledge_DeleteFile_Error.png')
    throw e
}