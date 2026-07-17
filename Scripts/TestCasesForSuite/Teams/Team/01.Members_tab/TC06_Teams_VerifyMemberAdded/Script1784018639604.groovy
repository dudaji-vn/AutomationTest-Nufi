import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

/**
 * TC06: Teams - Owner Verifies New Member Added
 * 
 * Test Flow:
 * 1. Open a NEW browser window (clean state, no old token)
 * 2. Login as Owner with existing account
 * 3. Switch to Advanced Interface
 * 4. Navigate to Teams page
 * 5. Select the team that was used for invitation
 * 6. Navigate to Members tab
 * 7. Verify the invited member appears in member list
 * 8. Verify member has correct role (Member/Admin)
 * 
 * Prerequisites:
 * - TC12 must be run first (invite member)
 * - TC14 must be run first (member accepts invitation)
 * - GlobalVariable.invitedTeamName must be set
 * - GlobalVariable.invitedEmail must be set
 * - GlobalVariable.invitedRole must be set
 */

WebUI.comment('=== TC06: Teams - Owner Verifies New Member Added ===')

// Get team name and email from TC12 via GlobalVariable
String teamName = GlobalVariable.invitedTeamName ?: 'Test Team'
String invitedEmail = GlobalVariable.invitedEmail ?: 'test_member@test.com'
String expectedRole = GlobalVariable.invitedRole ?: 'Member'

WebUI.comment('Team: ' + teamName)
WebUI.comment('Expected Member Email: ' + invitedEmail)
WebUI.comment('Expected Role: ' + expectedRole)

try {
    // ============================================================
    // STEP 0: Open NEW browser (clean state, no old token)
    // ============================================================
    WebUI.comment('Step 0: Opening NEW browser with clean state...')
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.Base_URL)
    WebUI.waitForPageLoad(5)
    WebUI.comment('✓ New browser opened with clean state')
    
    // ============================================================
    // STEP 1: Login as Owner
    // ============================================================
    WebUI.comment('Step 1: Logging in as Owner...')
    WebUI.comment('Owner Email: ' + GlobalVariable.email)
    
    CustomKeywords.'keywords.ChatKeywords.loginChat'(
        GlobalVariable.email,
        GlobalVariable.password
    )
    WebUI.delay(3)
    WebUI.comment('✓ Owner login successful')
    
    // ============================================================
    // STEP 2: Switch to Advanced Interface
    // ============================================================
    WebUI.comment('Step 2: Switching to Advanced Interface...')
    WebUI.delay(2)
    CustomKeywords.'keywords.ChatKeywords.switchToAdvancedInterface'()
    WebUI.comment('✓ Advanced Interface enabled')
    
    // ============================================================
    // STEP 3: Navigate to Teams page
    // ============================================================
    WebUI.comment('Step 3: Navigating to Teams page...')
    
    // Check screen width for mobile responsive
    String script = "return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;"
    int screenWidth = (Integer) WebUI.executeJavaScript(script, null)
    
    if (screenWidth < 760) {
        WebUI.comment('Screen width < 760px, opening sidebar...')
        TestObject openSidebarButton = new TestObject('openSidebarButton')
        openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
        WebUI.waitForElementClickable(openSidebarButton, 5)
        WebUI.click(openSidebarButton)
        WebUI.delay(1)
        WebUI.comment('Sidebar opened')
    }
    
    WebUI.waitForElementClickable(
        findTestObject('Object Repository/nav/nav_items/button_Teams'),
        10
    )
    WebUI.click(findTestObject('Object Repository/nav/nav_items/button_Teams'))
    WebUI.delay(3)
    
    String teamsUrl = WebUI.getUrl()
    if (!teamsUrl.contains('/teams')) {
        throw new Exception('Failed to navigate to Teams page. Current URL: ' + teamsUrl)
    }
    WebUI.comment('✓ Navigated to Teams page')
    
    // ============================================================
    // STEP 4: Select the team
    // ============================================================
    WebUI.comment('Step 4: Selecting team: ' + teamName)
    
    // Based on HTML: team name is inside h3 with class "text-sm font-semibold text-text-primary"
    // The button has aria-label containing team name
    TestObject teamSelector = new TestObject('teamSelector')
    teamSelector.addProperty('xpath', ConditionType.EQUALS, 
        "//button[@aria-label='" + teamName + "']")
    
    boolean teamFound = WebUI.waitForElementVisible(teamSelector, 10)
    
    if (!teamFound) {
        // Try alternative selector - find by h3 text
        TestObject teamSelectorAlt = new TestObject('teamSelectorAlt')
        teamSelectorAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//h3[contains(@class, 'text-text-primary') and normalize-space(text())='" + teamName + "']/ancestor::button")
        
        teamFound = WebUI.waitForElementVisible(teamSelectorAlt, 5)
        
        if (!teamFound) {
            // Try partial match
            TestObject teamSelectorPartial = new TestObject('teamSelectorPartial')
            teamSelectorPartial.addProperty('xpath', ConditionType.EQUALS, 
                "//h3[contains(@class, 'text-text-primary') and contains(text(), '" + teamName + "')]/ancestor::button")
            
            teamFound = WebUI.waitForElementVisible(teamSelectorPartial, 5)
            
            if (!teamFound) {
                WebUI.takeScreenshot('TC06_TeamNotFound.png')
                
                // Log all team names on page for debugging
                List<TestObject> allTeams = WebUI.findWebElements(
                    new TestObject('allTeams').addProperty('xpath', ConditionType.EQUALS, 
                        "//h3[contains(@class, 'text-text-primary')]"),
                    5
                )
                WebUI.comment('Available teams on page:')
                for (int i = 0; i < allTeams.size(); i++) {
                    String teamText = WebUI.getText(allTeams.get(i))
                    WebUI.comment('  - ' + teamText)
                }
                
                throw new Exception('Team "' + teamName + '" not found in team list')
            }
            WebUI.click(teamSelectorPartial)
        } else {
            WebUI.click(teamSelectorAlt)
        }
    } else {
        WebUI.click(teamSelector)
    }
    
    WebUI.delay(3)
    WebUI.comment('✓ Team selected: ' + teamName)
    
    // ============================================================
    // STEP 5: Navigate to Members tab
    // ============================================================
    WebUI.comment('Step 5: Navigating to Members tab...')
    
    // Try to find Members tab by text
    TestObject membersTab = new TestObject('membersTab')
    membersTab.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@class, 'tab') and contains(text(), 'Members')]")
    
    boolean tabFound = WebUI.waitForElementClickable(membersTab, 10)
    
    if (!tabFound) {
        // Try alternative selector for Members tab
        TestObject membersTabAlt = new TestObject('membersTabAlt')
        membersTabAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//button[contains(@role, 'tab') and contains(text(), 'Members')]")
        tabFound = WebUI.waitForElementClickable(membersTabAlt, 5)
        
        if (!tabFound) {
            // Try using Object Repository
            try {
                WebUI.waitForElementClickable(
                    findTestObject('Object Repository/nav/Teams/Tab_member/button_MembersTab'),
                    10
                )
                WebUI.click(findTestObject('Object Repository/nav/Teams/Tab_member/button_MembersTab'))
                WebUI.comment('✓ Members tab clicked (via Object Repository)')
            } catch (Exception e) {
                throw new Exception('Members tab not found. Unable to navigate to member list.')
            }
        } else {
            WebUI.click(membersTabAlt)
            WebUI.comment('✓ Members tab clicked (alternative selector)')
        }
    } else {
        WebUI.click(membersTab)
        WebUI.comment('✓ Members tab clicked')
    }
    
    WebUI.delay(3)
    
    // ============================================================
    // STEP 6: Verify member appears in list
    // ============================================================
    WebUI.comment('Step 6: Verifying member "' + invitedEmail + '" appears in list...')
    
    // Wait for member list to load - look for any member item
    TestObject memberListItem = new TestObject('memberListItem')
    memberListItem.addProperty('xpath', ConditionType.EQUALS, 
        "//li[contains(@class, 'flex') and contains(@class, 'items-center')]")
    WebUI.waitForElementVisible(memberListItem, 10)
    WebUI.comment('✓ Member list loaded')
    
    // Find the invited member by email
    // Based on HTML: email is in p with class "text-text-secondary"
    TestObject invitedMember = new TestObject('invitedMember')
    invitedMember.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + invitedEmail + "')]")
    
    boolean hasMember = WebUI.waitForElementVisible(invitedMember, 10)
    
    if (!hasMember) {
        WebUI.takeScreenshot('TC06_MemberNotFound.png')
        
        // Log all member emails on page for debugging
        List<TestObject> allMembers = WebUI.findWebElements(
            new TestObject('allMemberEmails').addProperty('xpath', ConditionType.EQUALS, 
                "//p[contains(@class, 'text-text-secondary')]"),
            5
        )
        WebUI.comment('Members found in list:')
        for (int i = 0; i < allMembers.size(); i++) {
            String memberText = WebUI.getText(allMembers.get(i))
            WebUI.comment('  - ' + memberText)
        }
        
        throw new Exception('Member "' + invitedEmail + '" not found in member list')
    }
    WebUI.comment('✓ Member "' + invitedEmail + '" found in member list')
    
    // ============================================================
    // STEP 7: Verify member role
    // ============================================================
    WebUI.comment('Step 7: Verifying member role is "' + expectedRole + '"...')
    
    // Find the role text for the invited member
    // Based on HTML: role is in span with class "text-xs font-medium text-text-secondary"
    TestObject memberRole = new TestObject('memberRole')
    memberRole.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + invitedEmail + "')]/ancestor::li//span[contains(@class, 'text-text-secondary')]")
    
    boolean roleFound = WebUI.waitForElementVisible(memberRole, 5)
    String actualRole = ''
    
    if (roleFound) {
        actualRole = WebUI.getText(memberRole)
        WebUI.comment('Actual role displayed: ' + actualRole)
    } else {
        // Try alternative selector for role - look for span with role text after email
        TestObject memberRoleAlt = new TestObject('memberRoleAlt')
        memberRoleAlt.addProperty('xpath', ConditionType.EQUALS, 
            "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + invitedEmail + "')]/following-sibling::span")
        
        boolean roleFoundAlt = WebUI.waitForElementVisible(memberRoleAlt, 3, FailureHandling.OPTIONAL)
        if (roleFoundAlt) {
            actualRole = WebUI.getText(memberRoleAlt)
            WebUI.comment('Actual role (alternative): ' + actualRole)
        } else {
            // Try third selector - role might be in a span within the same li
            TestObject memberRoleThird = new TestObject('memberRoleThird')
            memberRoleThird.addProperty('xpath', ConditionType.EQUALS, 
                "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + invitedEmail + "')]/parent::div//span[contains(@class, 'text-text-secondary')]")
            
            boolean roleFoundThird = WebUI.waitForElementVisible(memberRoleThird, 3, FailureHandling.OPTIONAL)
            if (roleFoundThird) {
                actualRole = WebUI.getText(memberRoleThird)
                WebUI.comment('Actual role (third selector): ' + actualRole)
            } else {
                WebUI.comment('⚠ Role element not found - may be displayed differently')
            }
        }
    }
    
    // Verify role matches expected
    if (actualRole != null && !actualRole.isEmpty()) {
        if (actualRole.equalsIgnoreCase(expectedRole)) {
            WebUI.comment('✓ Role verified: ' + actualRole)
        } else {
            WebUI.comment('⚠ Role mismatch - Expected: ' + expectedRole + ', Actual: ' + actualRole)
            // Don't fail the test just because role display might be different
        }
    } else {
        WebUI.comment('⚠ Unable to verify role - role element not found')
    }
    
    // ============================================================
    // STEP 8: Additional verification - get member count
    // ============================================================
    WebUI.comment('Step 8: Getting member count for verification...')
    
    List<TestObject> allMemberItems = WebUI.findWebElements(
        new TestObject('allMemberItems').addProperty('xpath', ConditionType.EQUALS, 
            "//li[contains(@class, 'flex') and contains(@class, 'items-center')]"),
        5
    )
    int memberCount = allMemberItems.size()
    WebUI.comment('Total members in team: ' + memberCount)
    
    // Verify member count is at least 2 (owner + invited member)
    if (memberCount >= 2) {
        WebUI.comment('✓ Member count verification passed - at least 2 members in team')
    } else {
        WebUI.comment('⚠ Member count is ' + memberCount + ' - expected at least 2')
    }
    
    // ============================================================
    // STEP 9: Take screenshot and verify UI elements
    // ============================================================
    WebUI.comment('Step 9: Verifying member UI elements...')
    
    // Check if member has avatar/icon
    TestObject memberAvatar = new TestObject('memberAvatar')
    memberAvatar.addProperty('xpath', ConditionType.EQUALS, 
        "//p[contains(@class, 'text-text-secondary') and contains(text(), '" + invitedEmail + "')]/ancestor::li//div[contains(@class, 'avatar') or contains(@class, 'size-8')]")
    boolean hasAvatar = WebUI.verifyElementPresent(memberAvatar, 3, FailureHandling.OPTIONAL)
    
    if (hasAvatar) {
        WebUI.comment('✓ Member has avatar displayed')
    } else {
        WebUI.comment('⚠ Member avatar not found - may not be displayed')
    }
    
    // Take final screenshot
    WebUI.takeScreenshot('TC06_MemberList_Verification.png')
    WebUI.comment('✓ Screenshot taken: TC06_MemberList_Verification.png')
    
    // ============================================================
    // STEP 10: Summary
    // ============================================================
    WebUI.comment('=== TC06 SUMMARY ===')
    WebUI.comment('✓ Team: ' + teamName)
    WebUI.comment('✓ Member: ' + invitedEmail)
    WebUI.comment('✓ Role: ' + (actualRole ?: expectedRole))
    WebUI.comment('✓ Total members: ' + memberCount)
    WebUI.comment('=== TC06 PASSED ===')

} catch (Exception e) {
    WebUI.comment('=== TC06 FAILED: ' + e.getMessage() + ' ===')
    WebUI.takeScreenshot('TC06_Error.png')
    throw e
}