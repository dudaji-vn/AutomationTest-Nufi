import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC07c: Teams - Select Existing Team by Role (First Occurrence)
 * 
 * Test Flow:
 * 1. Ensure on Teams page (/teams) - if on detail page, click back button
 * 2. Verify team list exists (if not, create a new team)
 * 3. Find the first team card that contains a specific role (e.g., "Owner" or "Admin")
 * 4. Click on the team card to navigate to its detail page
 * 5. Verify URL is a valid team detail page
 * 6. Store team name for later use (TC08)
 */

WebUI.comment('=== TC07c: Teams - Select Existing Team by Role (First Occurrence) ===')

try {
    // ================================================================
    // CONFIG: Define the target role to search for
    // ================================================================
    String targetRole = 'Owner'  // Change this to 'Admin' or other role as needed
    WebUI.comment('Target role: ' + targetRole)

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
        WebUI.comment('No existing teams found. Creating a new team with default role...')
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
        WebUI.comment('✓ Created new team: ' + newTeamName)

        // After creating, we should be on team detail page
        currentUrl = WebUI.getUrl()
        WebUI.comment('Current URL after creation: ' + currentUrl)

        // Skip to step 5 for verification
        WebUI.comment('Skipping step 3-4 (team selection by role) as we just created a team...')

    } else {
        WebUI.comment('✓ Team grid found')

        // ================================================================
        // STEP 3: Find and select team by role (first occurrence)
        // ================================================================
        WebUI.comment('Step 3: Finding the first team with role: ' + targetRole)

        // Build XPath to find the first team card containing the target role
        // The role is displayed in a span with class 'rounded-full bg-surface-tertiary'
        String teamCardByRoleXPath = "(//button[contains(@class, 'rounded-xl')]//span[contains(@class, 'rounded-full') and contains(text(), '" + targetRole + "')]/ancestor::button)[1]"

        TestObject teamCardByRole = new TestObject('teamCardByRole')
        teamCardByRole.addProperty('xpath', ConditionType.EQUALS, teamCardByRoleXPath)

        // Wait for the team card with the target role to be visible
        boolean teamFound = WebUI.waitForElementVisible(teamCardByRole, 10, FailureHandling.OPTIONAL)
        if (!teamFound) {
            throw new Exception('No team found with role: ' + targetRole)
        }
        WebUI.comment('✓ Found a team with role: ' + targetRole)

        // Extract team name using full XPath from the found team card
        // Instead of using setParent(), build the complete XPath to get the team name
        String teamNameXPath = "(//button[contains(@class, 'rounded-xl')]//span[contains(@class, 'rounded-full') and contains(text(), '" + targetRole + "')]/ancestor::button[1]//h3[contains(@class, 'text-text-primary')])"
        
        TestObject teamNameElement = new TestObject('teamNameElement')
        teamNameElement.addProperty('xpath', ConditionType.EQUALS, teamNameXPath)

        String teamName = WebUI.getText(teamNameElement)
        WebUI.comment('Team name to select: ' + teamName)

        // Store team name for TC08
        GlobalVariable.selectedTeamName = teamName
        WebUI.comment('✓ Stored team name: ' + teamName)

        // Click on the team card
        WebUI.click(teamCardByRole)
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
    WebUI.comment('  - Team detail URL: ' + currentUrl)

    // Extract team ID from URL
    String teamId = currentUrl.replaceAll('.*/teams/([a-f0-9]+).*', '$1')
    WebUI.comment('  - Team ID: ' + teamId)

    WebUI.takeScreenshot('TC07c_Teams_SelectTeamByRole_Success.png')
    WebUI.comment('=== TC07c PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC07c FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC07c_Teams_SelectTeamByRole_Error.png')
    throw e
}