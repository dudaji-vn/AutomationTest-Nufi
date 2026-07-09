import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC07b: Teams - Select Existing Team by Name
 * 
 * Test Flow:
 * 1. Check if already on team detail page
 *    - If yes: Verify team name matches target
 *    - If yes: PASS and continue
 *    - If no: Click back button to go to /teams
 * 2. On /teams page: Find team by name
 *    - If found: Click to navigate to detail page
 *    - If not found: FAIL
 * 3. Verify URL is valid team detail page
 * 4. Store team name for later use (TC08)
 */

WebUI.comment('=== TC07b: Teams - Select Existing Team by Name ===')

try {
    // ================================================================
    // CONFIG: Set target team name here
    // ================================================================
    String targetTeamName = 'Team b'  // Change this to your target team name
    WebUI.comment('Target team name: ' + targetTeamName)
    
    // ================================================================
    // STEP 1: Check current URL and state
    // ================================================================
    WebUI.comment('Step 1: Checking current page state...')
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    // Check if we're on a team detail page (/teams/{teamId})
    if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        WebUI.comment('Currently on team detail page. Checking team name...')
        
        // Get team name from detail page header
        TestObject detailTeamName = new TestObject('detailTeamName')
        detailTeamName.addProperty('xpath', ConditionType.EQUALS, 
            "//div[contains(@class, 'flex items-center gap-2')]//h1[contains(@class, 'text-xl')]")
        
        String currentTeamName = WebUI.getText(detailTeamName)
        WebUI.comment('Current team on detail page: ' + currentTeamName)
        
        // Check if current team name matches target
        if (currentTeamName.equals(targetTeamName)) {
            WebUI.comment('✓ Already on correct team detail page: ' + targetTeamName)
            GlobalVariable.selectedTeamName = targetTeamName
            WebUI.comment('✓ Stored team name: ' + targetTeamName)
            
            // Skip to final verification
            WebUI.comment('Skipping to final verification...')
            
            // ================================================================
            // STEP 4: Final verification
            // ================================================================
            WebUI.comment('Step 4: Final verification...')
            currentUrl = WebUI.getUrl()
            if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
                throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
            }
            WebUI.comment('✓ Successfully on team detail page')
            
            // Log selected team information
            WebUI.comment('Selected team information:')
            WebUI.comment('  - Team name: ' + GlobalVariable.selectedTeamName)
            WebUI.comment('  - Team detail URL: ' + currentUrl)
            
            String teamId = currentUrl.replaceAll('.*/teams/([a-f0-9]+).*', '$1')
            WebUI.comment('  - Team ID: ' + teamId)
            
            WebUI.takeScreenshot('TC07b_Teams_SelectTeamByName_Success.png')
            WebUI.comment('=== TC07b PASSED ===')
            return
        } else {
            WebUI.comment('Current team "' + currentTeamName + '" does not match target "' + targetTeamName + '"')
            WebUI.comment('Clicking back button to navigate to /teams...')
            
            // Click back button
            TestObject backButton = new TestObject('backButton')
            backButton.addProperty('xpath', ConditionType.EQUALS, 
                "//button[@aria-label='Teams' and contains(@class, 'inline-flex')]//*[local-name()='svg' and contains(@class, 'lucide-arrow-left')]/ancestor::button")
            
            boolean backButtonFound = WebUI.waitForElementClickable(backButton, 5, FailureHandling.OPTIONAL)
            if (!backButtonFound) {
                TestObject backButtonAlt = new TestObject('backButtonAlt')
                backButtonAlt.addProperty('xpath', ConditionType.EQUALS, 
                    "//button[contains(@class, 'inline-flex')]//*[local-name()='svg' and contains(@class, 'lucide-arrow-left')]/ancestor::button")
                backButtonFound = WebUI.waitForElementClickable(backButtonAlt, 3, FailureHandling.OPTIONAL)
                if (backButtonFound) {
                    WebUI.click(backButtonAlt)
                } else {
                    throw new Exception('Cannot find back button to navigate to /teams')
                }
            } else {
                WebUI.click(backButton)
            }
            
            WebUI.delay(2)
            currentUrl = WebUI.getUrl()
            WebUI.comment('Navigated back. Current URL: ' + currentUrl)
        }
    }
    
    // ================================================================
    // STEP 2: Ensure on /teams page
    // ================================================================
    WebUI.comment('Step 2: Ensuring on Teams page (/teams)...')
    
    // Verify we're on /teams (not detail page)
    if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Still on team detail page. Cannot proceed. URL: ' + currentUrl)
    }
    
    if (!currentUrl.contains('/teams')) {
        WebUI.comment('Not on Teams page. Clicking Teams button on navbar...')
        WebUI.click(findTestObject('Object Repository/Core Chat/nav/nav_items/button_Teams'))
        WebUI.delay(2)
        currentUrl = WebUI.getUrl()
        WebUI.comment('Current URL after navbar click: ' + currentUrl)
    }
    
    // Final verification
    if (!currentUrl.contains('/teams') || currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Failed to navigate to /teams. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ On Teams page: ' + currentUrl)
    
    // ================================================================
    // STEP 3: Find and select team by name
    // ================================================================
    WebUI.comment('Step 3: Finding team by name: ' + targetTeamName)
    
    // Check if team grid exists
    TestObject teamGrid = new TestObject('teamGrid')
    teamGrid.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'grid') and contains(@class, 'grid-cols-1')]")
    boolean hasTeamGrid = WebUI.waitForElementVisible(teamGrid, 5, FailureHandling.OPTIONAL)
    
    if (!hasTeamGrid) {
        throw new Exception('No team grid found. Cannot find team "' + targetTeamName + '"')
    }
    WebUI.comment('✓ Team grid found')
    
    // Find team card by name
    TestObject teamCardByName = new TestObject('teamCardByName')
    teamCardByName.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'rounded-xl')]//h3[contains(text(), '" + targetTeamName + "')]/ancestor::button")
    
    boolean teamFound = WebUI.waitForElementVisible(teamCardByName, 5, FailureHandling.OPTIONAL)
    if (!teamFound) {
        throw new Exception('Team "' + targetTeamName + '" not found in list')
    }
    WebUI.comment('✓ Team "' + targetTeamName + '" found in list')
    
    // Store team name for TC08 BEFORE clicking
    GlobalVariable.selectedTeamName = targetTeamName
    WebUI.comment('✓ Stored team name: ' + targetTeamName)
    
    // Click on the team
    WebUI.click(teamCardByName)
    WebUI.delay(2)
    WebUI.comment('✓ Clicked on team: ' + targetTeamName)
    
    // ================================================================
    // STEP 4: Verify URL is valid team detail page
    // ================================================================
    WebUI.comment('Step 4: Verifying team detail page URL...')
    currentUrl = WebUI.getUrl()
    
    // Check if URL matches pattern /teams/{teamId}
    boolean isValidDetailUrl = currentUrl.matches('.*/teams/[a-f0-9]+.*')
    if (!isValidDetailUrl) {
        throw new Exception('Invalid team detail URL: ' + currentUrl)
    }
    WebUI.comment('✓ Valid team detail URL: ' + currentUrl)
    
    // ================================================================
    // STEP 5: Final verification - ensure we're on a team detail page
    // ================================================================
    WebUI.comment('Step 5: Final verification...')
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Successfully on team detail page')
    
    // ================================================================
    // STEP 6: Log selected team information
    // ================================================================
    WebUI.comment('Step 6: Selected team information:')
    WebUI.comment('  - Team name: ' + GlobalVariable.selectedTeamName)
    WebUI.comment('  - Team detail URL: ' + currentUrl)
    
    // Extract team ID from URL
    String teamId = currentUrl.replaceAll('.*/teams/([a-f0-9]+).*', '$1')
    WebUI.comment('  - Team ID: ' + teamId)
    
    WebUI.takeScreenshot('TC07b_Teams_SelectTeamByName_Success.png')
    WebUI.comment('=== TC07b PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC07b FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC07b_Teams_SelectTeamByName_Error.png')
    throw e
}