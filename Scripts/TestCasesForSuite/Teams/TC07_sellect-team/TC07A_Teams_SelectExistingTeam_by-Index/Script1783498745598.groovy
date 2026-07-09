import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC07a: Teams - Select Existing Team by Index
 * 
 * Test Flow:
 * 1. Ensure on Teams page (/teams) - if on detail page, click back button
 * 2. Verify team list exists (if not, create a new team)
 * 3. Select a team by index (first, last, nth position, random)
 * 4. Navigate to team detail page (/teams/{teamId})
 * 5. Verify URL is valid team detail page
 * 6. Store team name and description for later use (TC08)
 */

WebUI.comment('=== TC07a: Teams - Select Existing Team by Index ===')

try {
    // ================================================================
    // STEP 1: Ensure on Teams page (/teams) - NOT detail page
    // ================================================================
    WebUI.comment('Step 1: Ensuring on Teams page (/teams)...')
    String currentUrl = WebUI.getUrl()
    WebUI.comment('Current URL: ' + currentUrl)
    
    // Check if we're on a team detail page (/teams/{teamId})
    if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        WebUI.comment('Currently on team detail page. Clicking back button to navigate to /teams...')
        
        // Click the back button (arrow-left) to go back to Teams list
        TestObject backButton = new TestObject('backButton')
        backButton.addProperty('xpath', ConditionType.EQUALS, 
            "//button[@aria-label='Teams' and contains(@class, 'inline-flex')]//*[local-name()='svg' and contains(@class, 'lucide-arrow-left')]/ancestor::button")
        
        boolean backButtonFound = WebUI.waitForElementClickable(backButton, 5, FailureHandling.OPTIONAL)
        if (!backButtonFound) {
            // Try alternative selector
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
    
    // Verify we're on /teams (not detail page)
    if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Still on team detail page. Cannot proceed. URL: ' + currentUrl)
    }
    
    if (!currentUrl.contains('/teams')) {
        // Try to navigate to Teams via navbar
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
    // STEP 2: Verify team list exists (if not, create a new team)
    // ================================================================
    WebUI.comment('Step 2: Verifying team list exists...')
    TestObject teamGrid = new TestObject('teamGrid')
    teamGrid.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'grid') and contains(@class, 'grid-cols-1')]")
    boolean hasTeamGrid = WebUI.waitForElementVisible(teamGrid, 5, FailureHandling.OPTIONAL)
    
    if (!hasTeamGrid) {
        WebUI.comment('No existing teams found. Creating a new team...')
        String newTeamName = 'Test Team ' + System.currentTimeMillis()
        String newTeamDesc = 'Description for ' + newTeamName
        
        // Click Create Team button
        WebUI.waitForElementClickable(
            findTestObject('Object Repository/Core Chat/nav/Teams/button_Create Team'),
            10
        )
        WebUI.click(findTestObject('Object Repository/Core Chat/nav/Teams/button_Create Team'))
        WebUI.delay(2)
        
        // Verify Create Team popup appears
        boolean hasPopup = WebUI.waitForElementVisible(
            findTestObject('Object Repository/Core Chat/nav/Teams/create-team/popup_Create Team'),
            5
        )
        if (!hasPopup) {
            throw new Exception('Create Team popup not found')
        }
        
        // Enter team name
        WebUI.setText(
            findTestObject('Object Repository/Core Chat/nav/Teams/create-team/input_team-name'),
            newTeamName
        )
        WebUI.delay(1)
        
        // Enter team description
        WebUI.setText(
            findTestObject('Object Repository/Core Chat/nav/Teams/create-team/textarea_team-description'),
            newTeamDesc
        )
        WebUI.delay(1)
        
        // Click Create button
        WebUI.click(findTestObject('Object Repository/Core Chat/nav/Teams/create-team/button_Create_Create Team'))
        WebUI.delay(3)
        
        // Verify toast message
        boolean hasToast = WebUI.waitForElementVisible(
            findTestObject('Object Repository/Toast/Toast_Success'),
            5
        )
        if (!hasToast) {
            WebUI.comment('⚠ Warning: Toast success message not found')
        } else {
            String toastText = WebUI.getText(findTestObject('Object Repository/Toast/Toast_Success'))
            WebUI.comment('Toast message: ' + toastText)
        }
        
        // Store team info
        GlobalVariable.selectedTeamName = newTeamName
        GlobalVariable.selectedTeamDesc = newTeamDesc
        WebUI.comment('✓ Created new team: ' + newTeamName)
        WebUI.comment('✓ Description: ' + newTeamDesc)
        
        // After creating, we should be on team detail page
        currentUrl = WebUI.getUrl()
        WebUI.comment('Current URL after creation: ' + currentUrl)
        
        // Skip to step 5 for verification
        WebUI.comment('Skipping step 3-4 (team selection) as we just created a team...')
        
    } else {
        WebUI.comment('✓ Team grid found')
        
        // ================================================================
        // STEP 3: Select a team by INDEX
        // ================================================================
        WebUI.comment('Step 3: Selecting a team by index...')
        
        // Count total teams
        TestObject teamCard = new TestObject('teamCard')
        teamCard.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@class, 'rounded-xl') and contains(@class, 'border-border-light')]")
        List<TestObject> teamCards = WebUI.findWebElements(teamCard, 5)
        int teamCount = teamCards != null ? teamCards.size() : 0
        
        if (teamCount < 1) {
            throw new Exception('Team grid exists but no team cards found')
        }
        WebUI.comment('Found ' + teamCount + ' team(s)')
        
        // CHOOSE SELECTION STRATEGY:
        // Option 2a: Select FIRST team
        /*
        WebUI.comment('Selecting FIRST team...')
        int selectedIndex = 0
        */
        
        // Option 2b: Select LAST team
        /*
        WebUI.comment('Selecting LAST team...')
        int selectedIndex = teamCount - 1
        */
        
        // Option 2c: Select SPECIFIC position (e.g., 2nd team)
        /*
        WebUI.comment('Selecting team at position 2...')
        int selectedIndex = 1 // 0-based index, so 1 = 2nd team
        if (selectedIndex >= teamCount) {
            throw new Exception('Position ' + (selectedIndex + 1) + ' not available. Only ' + teamCount + ' teams found.')
        }
        */
        
        // Option 2d: Select RANDOM team
        /*
        WebUI.comment('Selecting RANDOM team...')
        import java.util.Random
        int selectedIndex = new Random().nextInt(teamCount)
        */
        
        // ===== DEFAULT: Select FIRST team =====
        WebUI.comment('Selecting FIRST team (default)...')
        int selectedIndex = 0
        int xpathIndex = selectedIndex + 1
        
        // Get team name BEFORE clicking
        String teamNameXPath = "(//button[contains(@class, 'rounded-xl')]//h3[contains(@class, 'text-text-primary')])[" + xpathIndex + "]"
        TestObject teamNameElement = new TestObject('teamNameElement')
        teamNameElement.addProperty('xpath', ConditionType.EQUALS, teamNameXPath)
        String teamName = WebUI.getText(teamNameElement)
        WebUI.comment('Team name to select: ' + teamName)
        
        // Get team description BEFORE clicking
        String teamDesc = ''
        try {
            String descXPath = "(//button[contains(@class, 'rounded-xl')]//p[contains(@class, 'text-text-secondary')])[" + xpathIndex + "]"
            TestObject descElement = new TestObject('descElement')
            descElement.addProperty('xpath', ConditionType.EQUALS, descXPath)
            teamDesc = WebUI.getText(descElement)
            WebUI.comment('Team description: ' + teamDesc)
        } catch (Exception e) {
            teamDesc = '' // No description
            WebUI.comment('Team has no description')
        }
        
        // Store team info for TC08
        GlobalVariable.selectedTeamName = teamName
        GlobalVariable.selectedTeamDesc = teamDesc
        WebUI.comment('✓ Stored team name: ' + teamName)
        WebUI.comment('✓ Stored team description: ' + teamDesc)
        
        // Click on selected team using XPath with index
        String teamCardXPath = "(//button[contains(@class, 'rounded-xl') and contains(@class, 'border-border-light')])[" + xpathIndex + "]"
        TestObject teamCardByIndex = new TestObject('teamCardByIndex')
        teamCardByIndex.addProperty('xpath', ConditionType.EQUALS, teamCardXPath)
        
        WebUI.waitForElementClickable(teamCardByIndex, 10)
        WebUI.click(teamCardByIndex)
        WebUI.delay(2)
        
        WebUI.comment('✓ Clicked on team: ' + teamName)
        
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
    }
    
    // ================================================================
    // STEP 5: Final verification - ensure we're on a team detail page
    // ================================================================
    WebUI.comment('Step 5: Final verification...')
    currentUrl = WebUI.getUrl()
    if (!currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on team detail page. Current URL: ' + currentUrl)
    }
    WebUI.comment('✓ Successfully on team detail page')
    
    // ================================================================
    // STEP 6: Log selected team information
    // ================================================================
    WebUI.comment('Step 6: Selected team information:')
    WebUI.comment('  - Team name: ' + GlobalVariable.selectedTeamName)
    WebUI.comment('  - Team description: ' + GlobalVariable.selectedTeamDesc)
    WebUI.comment('  - Team detail URL: ' + currentUrl)
    
    // Extract team ID from URL
    String teamId = currentUrl.replaceAll('.*/teams/([a-f0-9]+).*', '$1')
    WebUI.comment('  - Team ID: ' + teamId)
    
    WebUI.takeScreenshot('TC07a_Teams_SelectTeamByIndex_Success.png')
    WebUI.comment('=== TC07a PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC07a FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC07a_Teams_SelectTeamByIndex_Error.png')
    throw e
}