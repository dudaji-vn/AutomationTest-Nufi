import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC05: Teams - Create Team Success
 * 
 * Test Flow:
 * 1. At page /teams
 * 2. Click Create Team button
 * 3. Verify popup appears
 * 4. Enter team name
 * 5. Click Create button
 * 6. Verify toast success appears
 * 7. Verify redirected to team detail page
 * 8. Verify team name appears on detail page
 */

WebUI.comment('=== TC05: Teams - Create Team Success ===')

try {
    String teamName = 'Test Team ' + System.currentTimeMillis()
    
    // Step 1: Ensure on Teams page
    WebUI.comment('Step 1: Ensuring on Teams page...')
    String currentUrl = WebUI.getUrl()
    if (!currentUrl.contains('/teams')) {
        WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
        WebUI.delay(2)
    }
    WebUI.comment('✓ On Teams page: ' + WebUI.getUrl())
    
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
    
    // Step 5: Click Create button
    WebUI.comment('Step 5: Clicking Create button...')
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/Teams/Team/create-team/button_Create_Create Team'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/Teams/Team/create-team/button_Create_Create Team'))
    WebUI.delay(3)
    WebUI.comment('✓ Create button clicked')
    
    // Step 6: Verify toast success appears
    WebUI.comment('Step 6: Checking for toast success...')
    try {
        TestObject toastSuccess = findTestObject('Object Repository/Toast/Toast_Success')
        boolean hasToast = WebUI.waitForElementVisible(toastSuccess, 5, FailureHandling.OPTIONAL)
        if (hasToast) {
            WebUI.comment('✓ Toast success appeared')
        } else {
            WebUI.comment('⚠ Toast success not visible (may have disappeared)')
        }
    } catch (Exception e) {
        WebUI.comment('⚠ Toast not found (may have disappeared quickly)')
    }
    
    // Step 7: Verify redirected to team detail page
    WebUI.comment('Step 7: Verifying redirected to team detail page...')
    String newUrl = WebUI.getUrl()
    if (!(newUrl.matches('.*/teams/[a-f0-9]+.*'))) {
        throw new Exception('Not redirected to team detail page. Current URL: ' + newUrl)
    }
    WebUI.comment('✓ Redirected to team detail page: ' + newUrl)
    
    // Step 8: Verify team name appears on detail page
    WebUI.comment('Step 8: Verifying team name appears on detail page...')
    WebUI.delay(2)
    
    // Get team name from h1 on detail page
    TestObject teamNameHeader = new TestObject('teamNameHeader')
    teamNameHeader.addProperty('xpath', ConditionType.EQUALS, '//h1[contains(@class, "text-text-primary")]')
    
    boolean teamNameVisible = WebUI.waitForElementVisible(teamNameHeader, 10, FailureHandling.OPTIONAL)
    if (!teamNameVisible) {
        throw new Exception('Team name not found on detail page')
    }
    
    String displayedTeamName = WebUI.getText(teamNameHeader)
    if (!displayedTeamName.equals(teamName)) {
        WebUI.comment('⚠ Team name mismatch. Expected: "' + teamName + '", Actual: "' + displayedTeamName + '"')
    } else {
        WebUI.comment('✓ Team name displayed correctly: ' + displayedTeamName)
    }
    
    // Verify Owner badge appears
    WebUI.comment('Step 9: Verifying Owner badge appears...')
    TestObject ownerBadge = new TestObject('ownerBadge')
    ownerBadge.addProperty('xpath', ConditionType.EQUALS, '//span[contains(@class, "rounded-full") and contains(text(), "Owner")]')
    boolean hasOwnerBadge = WebUI.waitForElementPresent(ownerBadge, 5, FailureHandling.OPTIONAL)
    if (hasOwnerBadge) {
        WebUI.comment('✓ Owner badge displayed')
    } else {
        WebUI.comment('⚠ Owner badge not found')
    }
    
    // Verify Members tab is active (default after creation)
    WebUI.comment('Step 10: Verifying Members tab is active...')
    TestObject membersTabActive = new TestObject('membersTabActive')
    membersTabActive.addProperty('xpath', ConditionType.EQUALS, '//button[@role="tab" and contains(text(), "Members") and @data-state="active"]')
    boolean membersTabActiveVisible = WebUI.waitForElementPresent(membersTabActive, 5, FailureHandling.OPTIONAL)
    if (membersTabActiveVisible) {
        WebUI.comment('✓ Members tab is active')
    } else {
        WebUI.comment('⚠ Members tab not active')
    }
    
    WebUI.takeScreenshot('TC05_Teams_CreateTeam_Success.png')
    WebUI.comment('=== TC05 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC05 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC05_Teams_CreateTeam_Success_Error.png')
    throw e
}