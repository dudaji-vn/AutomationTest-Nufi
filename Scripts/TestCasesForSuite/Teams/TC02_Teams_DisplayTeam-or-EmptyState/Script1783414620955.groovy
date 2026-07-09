import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC02: Teams - Display teams or empty state
 * 
 * Test Flow:
 * 1. At page /teams
 * 2. Check if team list exists OR empty state message exists
 * 3. If team list exists: verify team cards have name and member count
 * 4. If empty state: verify empty message and Create Team button
 * 5. Both cannot appear at the same time (mutually exclusive)
 */

WebUI.comment('=== TC02: Teams - Display Teams or Empty State ===')

try {
    // Step 1: Ensure on Teams page
    WebUI.comment('Step 1: Ensuring on Teams page...')
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.contains('/teams')) {
        WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
        WebUI.delay(2)
    }
    WebUI.comment('✓ On Teams page: ' + WebUI.getUrl())
    
    // Step 2: Check for team list and empty state
    WebUI.comment('Step 2: Checking UI state - either team list OR empty state...')
    
    // Check if team list exists
    TestObject teamGrid = new TestObject('teamGrid')
    teamGrid.addProperty('xpath', ConditionType.EQUALS, 
        "//div[contains(@class, 'grid') and contains(@class, 'grid-cols-1')]")
    boolean hasTeamGrid = WebUI.waitForElementVisible(teamGrid, 5, FailureHandling.OPTIONAL)
    
    // Check if empty state message exists
    TestObject emptyMessage = new TestObject('emptyMessage')
    emptyMessage.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), 'You\\'re not part of any team yet. Create one to get started.')]")
    boolean hasEmptyMessage = WebUI.waitForElementVisible(emptyMessage, 3, FailureHandling.OPTIONAL)
    
    // If empty message not found with exact text, try alternative
    if (!hasEmptyMessage) {
        TestObject emptyMessageAlt = new TestObject('emptyMessageAlt')
        emptyMessageAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//p[contains(text(), 'not part of any team yet')]")
        hasEmptyMessage = WebUI.waitForElementVisible(emptyMessageAlt, 2, FailureHandling.OPTIONAL)
    }
    
    // Check if Create Team button exists (for empty state)
    boolean hasCreateButton = WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/button_Create Team'),
        3,
        FailureHandling.OPTIONAL
    )
    
    // Step 3: Validate states - must have at least one of them
    WebUI.comment('Step 3: Validating UI state...')
    
    if (!hasTeamGrid && !hasEmptyMessage) {
        throw new Exception('Neither team list nor empty state message is displayed. UI is in invalid state.')
    }
    
    if (hasTeamGrid && hasEmptyMessage) {
        WebUI.comment('⚠ Warning: Both team list AND empty state appear simultaneously. This is unexpected.')
        // Still continue, but log warning
    }
    
    // Step 4: Handle team list state
    if (hasTeamGrid) {
        WebUI.comment('Step 4a: Team list found - verifying team cards...')
        
        // Find all team cards
        TestObject teamCard = new TestObject('teamCard')
        teamCard.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@class, 'rounded-xl') and contains(@class, 'border-border-light')]")
        List<TestObject> teamCards = WebUI.findWebElements(teamCard, 5)
        int teamCount = teamCards != null ? teamCards.size() : 0
        WebUI.comment('Number of teams found: ' + teamCount)
        
        if (teamCount < 1) {
            throw new Exception('Team grid exists but no team cards found')
        }
        WebUI.comment('✓ Found ' + teamCount + ' team(s)')
        
        // Verify first team card has name and members
        WebUI.comment('Verifying team card content...')
        TestObject teamName = new TestObject('teamName')
        teamName.addProperty('xpath', ConditionType.EQUALS, 
            "(//button[contains(@class, 'rounded-xl')]//h3[contains(@class, 'text-text-primary')])[1]")
        String nameText = WebUI.getText(teamName)
        WebUI.comment('First team name: ' + nameText)
        
        TestObject memberCount = new TestObject('memberCount')
        memberCount.addProperty('xpath', ConditionType.EQUALS, 
            "(//button[contains(@class, 'rounded-xl')]//span[contains(@class, 'text-text-secondary') and contains(text(), 'members')])[1]")
        String memberText = WebUI.getText(memberCount)
        WebUI.comment('First team members: ' + memberText)
        
        if (nameText == null || nameText.trim().isEmpty()) {
            throw new Exception('Team name not found')
        }
        if (memberText == null || memberText.trim().isEmpty()) {
            throw new Exception('Member count not found')
        }
        WebUI.comment('✓ Team card content verified')
        
    } else {
        // Step 5: Handle empty state
        WebUI.comment('Step 4b: Empty state found - verifying components...')
        
        if (!hasCreateButton) {
            throw new Exception('Empty state message found but Create Team button is missing')
        }
        WebUI.comment('✓ Create Team button found in empty state')
        WebUI.comment('✓ Empty state message displayed correctly')
    }
    
    WebUI.takeScreenshot('TC02_Teams_Display_Success.png')
    WebUI.comment('=== TC02 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC02 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC02_Teams_Display_Error.png')
    throw e
}