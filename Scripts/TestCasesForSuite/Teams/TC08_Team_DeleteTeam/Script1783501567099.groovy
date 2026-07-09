import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC08: Teams - Delete Team
 * 
 * Pre-condition: 
 * - TC07a must be executed first to select a team
 * - Team name and description are stored in GlobalVariable
 * - Must be on team detail page (/teams/{teamId})
 * 
 * Test Flow:
 * 1. Verify current URL is valid team detail page
 * 2. Count teams BEFORE deletion
 * 3. Click Delete Team button
 * 4. Verify Delete Team popup appears
 * 5. Click Delete button to confirm
 * 6. Verify URL returns to /teams
 * 7. Count teams AFTER deletion and compare
 * 8. Verify team is no longer in the list (by name + description)
 * 9. Clear stored team name
 */

WebUI.comment('=== TC08: Teams - Delete Team ===')

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
        
        // Check if we have a team name from previous test (TC07a)
        String teamName = GlobalVariable.selectedTeamName
        WebUI.comment('Team name from GlobalVariable: ' + teamName)
        
        if (teamName != null && !teamName.isEmpty()) {
            WebUI.comment('Using team from previous test: ' + teamName)
            
            // Navigate to Teams page if not already there
            if (!currentUrl.contains('/teams')) {
                WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Teams'))
                WebUI.delay(2)
                currentUrl = WebUI.getUrl()
            }
            
            // If on detail page but different team, go back to /teams first
            if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
                // Click back button
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
            
            // Click on the team by name
            TestObject teamCard = new TestObject('teamCard')
            teamCard.addProperty('xpath', ConditionType.EQUALS, 
                "//button[contains(@class, 'rounded-xl')]//h3[text()='" + teamName + "']/ancestor::button")
            
            boolean teamFound = WebUI.waitForElementVisible(teamCard, 5, FailureHandling.OPTIONAL)
            if (!teamFound) {
                throw new Exception('Team "' + teamName + '" not found in list. It may have been deleted already.')
            }
            
            WebUI.click(teamCard)
            WebUI.delay(2)
            currentUrl = WebUI.getUrl()
            WebUI.comment('Navigated to team detail: ' + currentUrl)
            
        } else {
            throw new Exception('No team selected. Please run TC07a first to select a team.')
        }
    }
    
    // Verify URL is valid team detail
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Invalid team detail URL: ' + currentUrl)
    }
    WebUI.comment('✓ On team detail page: ' + currentUrl)
    
    // Extract team ID from URL
    String teamId = currentUrl.replaceAll('.*/teams/([a-f0-9]+).*', '$1')
    WebUI.comment('Team ID to delete: ' + teamId)
    
    // Get team info from GlobalVariable
    String deletedTeamName = GlobalVariable.selectedTeamName
    String deletedTeamDesc = GlobalVariable.selectedTeamDesc
    WebUI.comment('Team to delete: "' + deletedTeamName + '" - "' + deletedTeamDesc + '"')
    
    // ================================================================
    // STEP 2: Count teams BEFORE deletion
    // ================================================================
    WebUI.comment('Step 2: Counting teams before deletion...')
    
    // Navigate to /teams to count (if not already there)
    if (!currentUrl.contains('/teams') || currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        // Click back button to go to /teams
        TestObject backButton = new TestObject('backButton')
        backButton.addProperty('xpath', ConditionType.EQUALS, 
            "//button[@aria-label='Teams' and contains(@class, 'inline-flex')]//*[local-name()='svg' and contains(@class, 'lucide-arrow-left')]/ancestor::button")
        WebUI.click(backButton)
        WebUI.delay(2)
        currentUrl = WebUI.getUrl()
    }
    
    // Count teams
    TestObject teamCard = new TestObject('teamCard')
    teamCard.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'rounded-xl') and contains(@class, 'border-border-light')]")
    List<TestObject> teamCardsBefore = WebUI.findWebElements(teamCard, 5)
    int teamCountBefore = teamCardsBefore != null ? teamCardsBefore.size() : 0
    WebUI.comment('Teams before deletion: ' + teamCountBefore)
    
    // Go back to detail page
    WebUI.comment('Navigating back to team detail page...')
    TestObject teamCardByName = new TestObject('teamCardByName')
    teamCardByName.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'rounded-xl')]//h3[text()='" + deletedTeamName + "']/ancestor::button")
    WebUI.click(teamCardByName)
    WebUI.delay(2)
    currentUrl = WebUI.getUrl()
    WebUI.comment('Back on detail page: ' + currentUrl)
    
    // ================================================================
    // STEP 3: Click Delete Team button
    // ================================================================
    WebUI.comment('Step 3: Clicking Delete Team button...')
    
    // Try to find Delete button with aria-label
    TestObject deleteButton = new TestObject('deleteButton')
    deleteButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='Delete team']")
    
    boolean deleteButtonFound = WebUI.waitForElementClickable(deleteButton, 5, FailureHandling.OPTIONAL)
    if (!deleteButtonFound) {
        // Try alternative: button with trash icon
        TestObject deleteButtonAlt = new TestObject('deleteButtonAlt')
        deleteButtonAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//button[.//*[local-name()='svg' and contains(@class, 'lucide-trash2')]]")
        deleteButtonFound = WebUI.waitForElementClickable(deleteButtonAlt, 5, FailureHandling.OPTIONAL)
        if (deleteButtonFound) {
            WebUI.click(deleteButtonAlt)
        } else {
            throw new Exception('Delete Team button not found on detail page')
        }
    } else {
        WebUI.click(deleteButton)
    }
    
    WebUI.delay(1)
    WebUI.comment('✓ Delete Team button clicked')
    
    // ================================================================
    // STEP 4: Verify Delete Team popup appears
    // ================================================================
    WebUI.comment('Step 4: Verifying Delete Team popup...')
    
    TestObject deletePopup = new TestObject('deletePopup')
    deletePopup.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@role, 'dialog') and contains(., 'Delete')]")
    
    boolean hasPopup = WebUI.waitForElementVisible(deletePopup, 5, FailureHandling.OPTIONAL)
    if (!hasPopup) {
        throw new Exception('Delete Team popup not found')
    }
    WebUI.comment('✓ Delete Team popup appeared')
    
    // ================================================================
    // STEP 5: Click Delete button to confirm
    // ================================================================
    WebUI.comment('Step 5: Confirming deletion...')
    
    TestObject confirmDeleteButton = new TestObject('confirmDeleteButton')
    confirmDeleteButton.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(text(), 'Delete') and contains(@class, 'destructive')]")
    
    boolean confirmButtonFound = WebUI.waitForElementClickable(confirmDeleteButton, 5, FailureHandling.OPTIONAL)
    if (!confirmButtonFound) {
        TestObject confirmDeleteAlt = new TestObject('confirmDeleteAlt')
        confirmDeleteAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@class, 'bg-destructive') and contains(text(), 'Delete')]")
        confirmButtonFound = WebUI.waitForElementClickable(confirmDeleteAlt, 3, FailureHandling.OPTIONAL)
        if (confirmButtonFound) {
            WebUI.click(confirmDeleteAlt)
        } else {
            throw new Exception('Confirm Delete button not found in popup')
        }
    } else {
        WebUI.click(confirmDeleteButton)
    }
    
    WebUI.delay(3)
    WebUI.comment('✓ Deletion confirmed')
    
    // ================================================================
    // STEP 6: Verify URL returns to /teams
    // ================================================================
    WebUI.comment('Step 6: Verifying return to Teams page...')
    currentUrl = WebUI.getUrl()
    WebUI.comment('URL after deletion: ' + currentUrl)
    
    if (!currentUrl.contains('/teams')) {
        throw new Exception('Not redirected to /teams after deletion. Current URL: ' + currentUrl)
    }
    
    // Make sure we're not still on a detail page
    if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Still on team detail page. Deletion may have failed. URL: ' + currentUrl)
    }
    WebUI.comment('✓ Redirected to Teams page: ' + currentUrl)
    
    // ================================================================
    // STEP 7: Count teams AFTER deletion and compare
    // ================================================================
    WebUI.comment('Step 7: Verifying team was removed...')
    
    // Wait for list to refresh
    WebUI.delay(2)
    
    // Get current list of teams after deletion
    TestObject teamCardAfter = new TestObject('teamCardAfter')
    teamCardAfter.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'rounded-xl') and contains(@class, 'border-border-light')]")
    List<TestObject> teamCardsAfter = WebUI.findWebElements(teamCardAfter, 5)
    int teamCountAfter = teamCardsAfter != null ? teamCardsAfter.size() : 0
    WebUI.comment('Teams after deletion: ' + teamCountAfter)
    
    // Check if count decreased by 1
    if (teamCountAfter != teamCountBefore - 1) {
        throw new Exception('Team count did not decrease by 1. Before: ' + teamCountBefore + ', After: ' + teamCountAfter)
    }
    WebUI.comment('✓ Team count decreased from ' + teamCountBefore + ' to ' + teamCountAfter)
    
    // ================================================================
    // STEP 8: Verify the deleted team is NOT in the new list
    // ================================================================
    WebUI.comment('Step 8: Verifying deleted team not in list...')
    
    boolean teamStillExists = false
    if (teamCardsAfter != null && teamCardsAfter.size() > 0) {
        for (int i = 0; i < teamCardsAfter.size(); i++) {
            // Get team name
            String xpathName = "(//button[contains(@class, 'rounded-xl')]//h3[contains(@class, 'text-text-primary')])[" + (i + 1) + "]"
            TestObject nameObj = new TestObject('nameObj')
            nameObj.addProperty('xpath', ConditionType.EQUALS, xpathName)
            String currentName = WebUI.getText(nameObj)
            
            // Get team description (if exists)
            String currentDesc = ''
            try {
                String xpathDesc = "(//button[contains(@class, 'rounded-xl')]//p[contains(@class, 'text-text-secondary')])[" + (i + 1) + "]"
                TestObject descObj = new TestObject('descObj')
                descObj.addProperty('xpath', ConditionType.EQUALS, xpathDesc)
                currentDesc = WebUI.getText(descObj)
            } catch (Exception e) {
                currentDesc = '' // No description
            }
            
            // Check if this is the deleted team (match both name AND description)
            boolean nameMatches = currentName.equals(deletedTeamName)
            boolean descMatches = (deletedTeamDesc == null || deletedTeamDesc.isEmpty()) ? 
                currentDesc.isEmpty() : currentDesc.equals(deletedTeamDesc)
            
            if (nameMatches && descMatches) {
                teamStillExists = true
                WebUI.comment('⚠ Found deleted team: "' + currentName + '" - "' + currentDesc + '"')
                break
            }
        }
    }
    
    if (teamStillExists) {
        throw new Exception('Team "' + deletedTeamName + '" with description "' + deletedTeamDesc + '" still appears in the list')
    }
    WebUI.comment('✓ Team "' + deletedTeamName + '" successfully removed from list')
    
    // Check for empty state
    TestObject emptyMessage = new TestObject('emptyMessage')
    emptyMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), 'You\\'re not part of any team yet. Create one to get started.')]")
    boolean hasEmptyMessage = WebUI.waitForElementVisible(emptyMessage, 3, FailureHandling.OPTIONAL)
    if (hasEmptyMessage) {
        WebUI.comment('✓ Empty state displayed - no teams left')
    }
    
    // ================================================================
    // STEP 9: Clear stored team info since it's deleted
    // ================================================================
    GlobalVariable.selectedTeamName = null
    GlobalVariable.selectedTeamDesc = null
    WebUI.comment('✓ Cleared stored team info')
    
    WebUI.takeScreenshot('TC08_Teams_DeleteTeam_Success.png')
    WebUI.comment('=== TC08 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC08 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC08_Teams_DeleteTeam_Error.png')
    
    // Try to cancel if popup is open
    try {
        TestObject deletePopup = new TestObject('deletePopup')
        deletePopup.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@role, 'dialog') and contains(., 'Delete')]")
        if (WebUI.waitForElementVisible(deletePopup, 2, FailureHandling.OPTIONAL)) {
            TestObject cancelButton = new TestObject('cancelButton')
            cancelButton.addProperty('xpath', ConditionType.EQUALS, 
                "//button[contains(text(), 'Cancel')]")
            if (WebUI.waitForElementClickable(cancelButton, 2, FailureHandling.OPTIONAL)) {
                WebUI.click(cancelButton)
                WebUI.comment('Popup cancelled due to error')
            }
        }
    } catch (Exception ignore) {
        WebUI.comment('Could not cancel popup')
    }
    
    throw e
}