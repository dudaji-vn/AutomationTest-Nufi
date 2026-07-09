import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC09: Teams - Cancel Delete Team
 * 
 * Pre-condition: 
 * - TC07a or TC07b must be executed first to select a team
 * - Team name is stored in GlobalVariable.selectedTeamName
 * - Must be on team detail page (/teams/{teamId})
 * 
 * Test Flow:
 * 1. Verify current URL is valid team detail page
 * 2. If not on detail page, navigate using stored team name
 * 3. Click Delete Team button
 * 4. Verify Delete Team popup appears
 * 5. Click Cancel button (NOT Delete)
 * 6. Verify popup closes
 * 7. Verify still on team detail page (team NOT deleted)
 * 8. Keep team name for future use
 */

WebUI.comment('=== TC09: Teams - Cancel Delete Team ===')

try {
    // ================================================================
    // STEP 1: Verify current URL is valid team detail page
    // ================================================================
    WebUI.comment('Step 1: Verifying on team detail page...')
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    // If not on detail page, try to navigate using stored team name
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        WebUI.comment('Not on team detail page. Attempting to navigate...')
        
        String teamName = GlobalVariable.selectedTeamName
        WebUI.comment('Team name from GlobalVariable: ' + teamName)
        
        if (teamName != null && !teamName.isEmpty()) {
            WebUI.comment('Using team from previous test: ' + teamName)
            
            if (!currentUrl.contains('/teams')) {
                WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Teams'))
                WebUI.delay(2)
                currentUrl = WebUI.getUrl()
            }
            
            if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
                TestObject backButton = new TestObject('backButton')
                backButton.addProperty('xpath', ConditionType.EQUALS, 
                    "//button[@aria-label='Teams' and contains(@class, 'inline-flex')]//*[local-name()='svg' and contains(@class, 'lucide-arrow-left')]/ancestor::button")
                
                boolean backButtonFound = WebUI.waitForElementClickable(backButton, 5, FailureHandling.OPTIONAL)
                if (backButtonFound) {
                    WebUI.click(backButton)
                    WebUI.delay(2)
                    currentUrl = WebUI.getUrl()
                }
            }
            
            TestObject teamCard = new TestObject('teamCard')
            teamCard.addProperty('xpath', ConditionType.EQUALS, 
                "//button[contains(@class, 'rounded-xl')]//h3[contains(text(), '" + teamName + "')]/ancestor::button")
            
            boolean teamFound = WebUI.waitForElementVisible(teamCard, 5, FailureHandling.OPTIONAL)
            if (!teamFound) {
                throw new Exception('Team "' + teamName + '" not found in list.')
            }
            
            WebUI.click(teamCard)
            WebUI.delay(2)
            currentUrl = WebUI.getUrl()
            WebUI.comment('Navigated to team detail: ' + currentUrl)
            
        } else {
            throw new Exception('No team selected. Please run TC07a or TC07b first.')
        }
    }
    
    // Verify URL is valid team detail
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Invalid team detail URL: ' + currentUrl)
    }
    WebUI.comment('✓ On team detail page: ' + currentUrl)
    
    String teamId = currentUrl.replaceAll('.*/teams/([a-f0-9]+).*', '$1')
    WebUI.comment('Team ID: ' + teamId)
    
    // ================================================================
    // STEP 2: Click Delete Team button
    // ================================================================
    WebUI.comment('Step 2: Clicking Delete Team button...')
    
    TestObject deleteButton = new TestObject('deleteButton')
    deleteButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Delete team']")
    
    boolean deleteButtonFound = WebUI.waitForElementClickable(deleteButton, 5, FailureHandling.OPTIONAL)
    if (!deleteButtonFound) {
        TestObject deleteButtonAlt = new TestObject('deleteButtonAlt')
        deleteButtonAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//button[.//*[local-name()='svg' and contains(@class, 'lucide-trash2')]]")
        deleteButtonFound = WebUI.waitForElementClickable(deleteButtonAlt, 5, FailureHandling.OPTIONAL)
        if (deleteButtonFound) {
            WebUI.click(deleteButtonAlt)
        } else {
            throw new Exception('Delete Team button not found')
        }
    } else {
        WebUI.click(deleteButton)
    }
    
    WebUI.delay(1)
    WebUI.comment('✓ Delete Team button clicked')
    
    // ================================================================
    // STEP 3: Verify Delete Team popup appears
    // ================================================================
    WebUI.comment('Step 3: Verifying Delete Team popup...')
    
    TestObject deletePopup = new TestObject('deletePopup')
    deletePopup.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@role, 'dialog') and contains(., 'Delete')]")
    
    boolean hasPopup = WebUI.waitForElementVisible(deletePopup, 5, FailureHandling.OPTIONAL)
    if (!hasPopup) {
        throw new Exception('Delete Team popup not found')
    }
    WebUI.comment('✓ Delete Team popup appeared')
    
    // ================================================================
    // STEP 4: Click Cancel button (NOT Delete)
    // ================================================================
    WebUI.comment('Step 4: Clicking Cancel button...')
    
    TestObject cancelButton = new TestObject('cancelButton')
    cancelButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Cancel')]")
    
    boolean cancelFound = WebUI.waitForElementClickable(cancelButton, 5, FailureHandling.OPTIONAL)
    if (!cancelFound) {
        TestObject cancelButtonAlt = new TestObject('cancelButtonAlt')
        cancelButtonAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@class, 'outline') and contains(text(), 'Cancel')]")
        cancelFound = WebUI.waitForElementClickable(cancelButtonAlt, 3, FailureHandling.OPTIONAL)
        if (cancelFound) {
            WebUI.click(cancelButtonAlt)
        } else {
            throw new Exception('Cancel button not found in popup')
        }
    } else {
        WebUI.click(cancelButton)
    }
    
    WebUI.delay(2)
    WebUI.comment('✓ Cancel button clicked')
    
    // ================================================================
    // STEP 5: Verify popup closes
    // ================================================================
    WebUI.comment('Step 5: Verifying popup closed...')
    
    boolean popupClosed = WebUI.waitForElementNotPresent(deletePopup, 5)
    if (!popupClosed) {
        throw new Exception('Popup did not close after clicking Cancel')
    }
    WebUI.comment('✓ Popup closed')
    
    // ================================================================
    // STEP 6: Verify still on team detail page (team NOT deleted)
    // ================================================================
    WebUI.comment('Step 6: Verifying still on team detail page...')
    currentUrl = WebUI.getUrl()
    
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on team detail page. Team may have been deleted. URL: ' + currentUrl)
    }
    WebUI.comment('✓ Still on team detail page: ' + currentUrl)
    
    // Verify team still exists by checking team name on detail page
    TestObject detailTeamName = new TestObject('detailTeamName')
    detailTeamName.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'flex items-center gap-2')]//h1[contains(@class, 'text-xl')]")
    
    String currentTeamName = WebUI.getText(detailTeamName)
    WebUI.comment('Team name still on detail page: ' + currentTeamName)
    
    String expectedTeamName = GlobalVariable.selectedTeamName
    if (expectedTeamName != null && !expectedTeamName.isEmpty()) {
        if (!currentTeamName.equals(expectedTeamName)) {
            WebUI.comment('⚠ Warning: Team name mismatch. Expected: ' + expectedTeamName + ', Actual: ' + currentTeamName)
        } else {
            WebUI.comment('✓ Team "' + currentTeamName + '" still exists (not deleted)')
        }
    }
    
    // ================================================================
    // STEP 7: Keep team name for future use
    // ================================================================
    WebUI.comment('Step 7: Team NOT deleted - keeping for future use')
    WebUI.comment('  - Team name: ' + GlobalVariable.selectedTeamName)
    WebUI.comment('  - Team detail URL: ' + currentUrl)
    
    WebUI.takeScreenshot('TC09_Teams_CancelDelete_Success.png')
    WebUI.comment('=== TC09 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC09 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC09_Teams_CancelDelete_Error.png')
    throw e
}