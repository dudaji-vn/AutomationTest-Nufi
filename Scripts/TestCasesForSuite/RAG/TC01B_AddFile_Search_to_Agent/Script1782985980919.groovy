import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC01B: Add File Search to Existing Agent
 * Verify adding File Search capability to an existing Agent
 */

WebUI.comment('=== TC01B: Add File Search to Agent ===')

try {
    // ============================================================
    // Step 0: Check Agent Builder and Navbar state
    // ============================================================
    WebUI.comment('Step 0: Checking Agent Builder and Navbar state...')

    // Step 0.1: Check if Agent Builder is open
    WebUI.comment('Step 0.1: Checking if Agent Builder is open...')

    TestObject agentConfigForm = new TestObject('agentConfigForm')
    agentConfigForm.addProperty('xpath', ConditionType.EQUALS,
        "//form[@aria-label='Agent configuration form']")

    boolean isAgentBuilderOpen = WebUI.waitForElementVisible(agentConfigForm, 5, FailureHandling.OPTIONAL)

    if (!isAgentBuilderOpen) {
        WebUI.comment('Agent Builder is not open, clicking Agent Builder button...')

        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        WebUI.waitForElementVisible(navSidebar, 10)

        String ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
        WebUI.comment('Navbar aria-hidden: ' + ariaHidden)

        if (ariaHidden == 'true') {
            WebUI.comment('Navbar is closed, opening sidebar...')
            TestObject openSidebarButton = new TestObject('openSidebarButton')
            openSidebarButton.addProperty('xpath', ConditionType.EQUALS, "//button[@id='open-sidebar-button']")
            WebUI.waitForElementClickable(openSidebarButton, 5)
            WebUI.click(openSidebarButton)
            WebUI.delay(1)
            WebUI.comment('✓ Sidebar opened')
        } else {
            WebUI.comment('✓ Navbar is already open (aria-hidden="false")')
        }

        TestObject btnAgentBuilder = findTestObject('Object Repository/nav/nav_items/button_Agent Builder')
        WebUI.waitForElementVisible(btnAgentBuilder, 15)
        WebUI.click(btnAgentBuilder)
        WebUI.delay(3)

        WebUI.waitForElementVisible(agentConfigForm, 10)
        WebUI.comment('✓ Agent Builder is open')
    } else {
        WebUI.comment('✓ Agent Builder is already open')
    }

    // ============================================================
    // Step 1: Open existing Agent from dropdown
    // ============================================================
    WebUI.comment('Step 1: Opening existing Agent from dropdown...')

    TestObject btnDropdownAgent = findTestObject('Object Repository/nav/RAG/button_Dropdown_Agent')
    WebUI.waitForElementVisible(btnDropdownAgent, 15)
    WebUI.click(btnDropdownAgent)
    WebUI.delay(2)
    WebUI.comment('✓ Agent dropdown opened')

    // ============================================================
    // OPTION 1: Select first item in list (DEFAULT)
    // ============================================================
    WebUI.comment('Selecting first agent in list (default)...')
    TestObject firstAgent = new TestObject('firstAgent')
    firstAgent.addProperty('xpath', ConditionType.EQUALS,
        "(//div[@role='option'])[1]")
    WebUI.waitForElementVisible(firstAgent, 10)
    WebUI.click(firstAgent)
    WebUI.delay(2)
    WebUI.comment('✓ First agent selected')

    // ============================================================
    // OPTION 2: Select random item in list
    // ============================================================
    /*
    WebUI.comment('Selecting random agent in list...')
    import java.util.Random
    List<TestObject> options = WebUI.findWebElements(
        new TestObject('options').addProperty('xpath', ConditionType.EQUALS, "//div[@role='option']"),
        5
    )
    int totalOptions = options.size()
    WebUI.comment('Total agents found: ' + totalOptions)

    if (totalOptions > 0) {
        Random rand = new Random()
        int randomIndex = rand.nextInt(totalOptions) + 1
        WebUI.comment('Random index selected: ' + randomIndex)

        TestObject randomAgent = new TestObject('randomAgent')
        randomAgent.addProperty('xpath', ConditionType.EQUALS,
            "(//div[@role='option'])[" + randomIndex + "]")
        WebUI.waitForElementVisible(randomAgent, 10)
        WebUI.click(randomAgent)
        WebUI.comment('✓ Random agent selected')
    } else {
        throw new Exception('No agents found in dropdown')
    }
    */

    // ============================================================
    // OPTION 3: Search by name then select
    // ============================================================
    /*
    WebUI.comment('Searching for agent by name...')
    String searchAgentName = "Auto_Test_Agent"

    TestObject searchInput = new TestObject('searchInput')
    searchInput.addProperty('xpath', ConditionType.EQUALS,
        "//input[@placeholder='Search agents by name']")
    WebUI.waitForElementVisible(searchInput, 10)
    WebUI.setText(searchInput, searchAgentName)
    WebUI.delay(1.5)
    WebUI.comment('✓ Searched for: ' + searchAgentName)

    TestObject matchedAgent = new TestObject('matchedAgent')
    matchedAgent.addProperty('xpath', ConditionType.EQUALS,
        "//div[@role='option']//span[contains(text(), '" + searchAgentName + "')]/ancestor::div[@role='option']")
    WebUI.waitForElementVisible(matchedAgent, 10)
    WebUI.click(matchedAgent)
    WebUI.comment('✓ Agent found and selected: ' + searchAgentName)
    */

    // ============================================================
    // Step 2: Enable File Search
    // ============================================================
    WebUI.comment('Step 2: Enabling File Search...')

    TestObject fileSearchCheckbox = findTestObject('Object Repository/nav/RAG/button_File Search_file-search-checkbox')
    WebUI.waitForElementVisible(fileSearchCheckbox, 10)
    WebUI.click(fileSearchCheckbox)
    WebUI.delay(1)
    WebUI.comment('✓ File Search enabled')

    // ============================================================
    // Step 2.5: Click Save
    // ============================================================
    WebUI.comment('Step 2.5: Clicking Save after enabling File Search...')

    TestObject saveButton = new TestObject('saveButton')
    saveButton.addProperty('xpath', ConditionType.EQUALS, "//button[contains(text(),'Save')]")
    
    WebUI.waitForElementVisible(saveButton, 10)
    WebUI.click(saveButton)
    WebUI.delay(3)
    WebUI.comment('✓ Save button clicked')

    WebUI.takeScreenshot('TC01B_Stopped_at_Save.png')
    WebUI.comment('=== TC01B Stopped at Save Button ===')

} catch (Exception e) {
    WebUI.comment('TC01B FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01B_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}