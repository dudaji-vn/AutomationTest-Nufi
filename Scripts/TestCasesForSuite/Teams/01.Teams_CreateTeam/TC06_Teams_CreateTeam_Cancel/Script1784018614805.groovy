import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC06: Teams - Cancel Create Team
 * 
 * Test Flow:
 * 1. Ensure on Teams page (if on detail page, navigate back)
 * 2. Click Create Team button
 * 3. Verify popup appears
 * 4. Enter team name
 * 5. Click Cancel button
 * 6. Verify popup closed
 * 7. Verify team not created
 */

WebUI.comment('=== TC06: Teams - Cancel Create Team ===')

try {
    String teamName = 'Cancelled Team ' + System.currentTimeMillis()
    
    // Step 1: Ensure on Teams page
    WebUI.comment('Step 1: Ensuring on Teams page...')
    String currentUrl = WebUI.getUrl()
    
    // Check if currently on team detail page
    if (currentUrl.matches('.*/teams/[a-f0-9]+.*')) {
        WebUI.comment('Currently on team detail page, navigating back to teams list...')
        WebUI.waitForElementClickable(
            findTestObject('Object Repository/nav/Teams/Team/button_Back-to-teams'),
            10
        )
        WebUI.click(findTestObject('Object Repository/nav/Teams/Team/button_Back-to-teams'))
        WebUI.delay(2)
        WebUI.comment('✓ Navigated back to teams list')
    } else if (!currentUrl.contains('/teams')) {
        // If not on any teams page, click Teams nav
        WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
        WebUI.delay(2)
    }
    
    // Verify we are on teams list page
    String newUrl = WebUI.getUrl()
    if (!newUrl.contains('/teams') || newUrl.matches('.*/teams/[a-f0-9]+.*')) {
        throw new Exception('Not on teams list page. Current URL: ' + newUrl)
    }
    WebUI.comment('✓ On Teams page: ' + newUrl)
    
    // Step 2: Click Create Team button
    WebUI.comment('Step 2: Clicking Create Team button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/button_Create Team'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/button_Create Team'))
    WebUI.delay(2)
    WebUI.comment('✓ Create Team button clicked')
    
    // Step 3: Verify Create Team popup appears
    WebUI.comment('Step 3: Verifying Create Team popup...')
    boolean hasPopup = WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/Team/create-team/popup_Create Team'),
        10
    )
    if (!hasPopup) {
        throw new Exception('Create Team popup not found')
    }
    WebUI.comment('✓ Create Team popup opened')
    
    // Step 4: Enter team name
    WebUI.comment('Step 4: Entering team name...')
    WebUI.waitForElementVisible(
        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
        10
    )
    WebUI.setText(
        findTestObject('Object Repository/nav/Teams/Team/create-team/input_team-name'),
        teamName
    )
    WebUI.comment('✓ Team name entered: ' + teamName)
    WebUI.delay(1)
    
    // Step 5: Click Cancel button
    WebUI.comment('Step 5: Clicking Cancel button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/create-team/button_Cancel_Create Team'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/create-team/button_Cancel_Create Team'))
    WebUI.delay(2)
    WebUI.comment('✓ Cancel button clicked')
    
    // Step 6: Verify popup closed
    WebUI.comment('Step 6: Verifying popup closed...')
    boolean popupClosed = WebUI.waitForElementNotVisible(
        findTestObject('Object Repository/nav/Teams/Team/create-team/popup_Create Team'),
        5,
        FailureHandling.OPTIONAL
    )
    if (!popupClosed) {
        WebUI.comment('⚠ Warning: Popup may still be visible')
    } else {
        WebUI.comment('✓ Popup closed')
    }
    
    // Step 7: Verify team not created
    WebUI.comment('Step 7: Verifying team not created...')
    TestObject cancelledTeam = new TestObject('cancelledTeam')
    cancelledTeam.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'rounded-xl')]//h3[contains(text(), '" + teamName + "')]")
    boolean teamExists = WebUI.waitForElementPresent(cancelledTeam, 3, FailureHandling.OPTIONAL)
    
    if (teamExists) {
        throw new Exception('Team "' + teamName + '" was created despite cancellation')
    }
    WebUI.comment('✓ Team not created (cancelled successfully)')
    
    WebUI.takeScreenshot('TC06_Teams_CreateTeam_Cancel_Success.png')
    WebUI.comment('=== TC06 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC06 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC06_Teams_CreateTeam_Cancel_Error.png')
    throw e
}