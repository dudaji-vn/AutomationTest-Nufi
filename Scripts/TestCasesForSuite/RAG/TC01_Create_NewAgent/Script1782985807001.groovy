import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC01: Create New Agent
 * Verify creation of new Agent with mandatory fields
 */

WebUI.comment('=== TC01: Create New Agent ===')

try {
    // ============================================================
    // Step 0: Check Agent Builder and Navbar state
    // ============================================================
    WebUI.comment('Step 0: Checking Agent Builder and Navbar state...')
    
    // Step 0.1: Check if Agent Builder is open
    WebUI.comment('Step 0.1: Checking if Agent Builder is open...')
    
    // Check if Agent configuration form is visible (aria-label="Agent configuration form")
    TestObject agentConfigForm = new TestObject('agentConfigForm')
    agentConfigForm.addProperty('xpath', ConditionType.EQUALS, 
        "//form[@aria-label='Agent configuration form']")
    
    boolean isAgentBuilderOpen = WebUI.waitForElementVisible(agentConfigForm, 5, FailureHandling.OPTIONAL)
    
    if (!isAgentBuilderOpen) {
        WebUI.comment('Agent Builder is not open, clicking Agent Builder button...')
        
        // Step 0.2: Check Navbar is open before clicking
        WebUI.comment('Step 0.2: Checking Navbar state...')
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
            
            // Verify navbar is open
            ariaHidden = WebUI.getAttribute(navSidebar, 'aria-hidden')
            WebUI.comment('Navbar aria-hidden after open: ' + ariaHidden)
            if (ariaHidden == 'false') {
                WebUI.comment('✓ Navbar opened successfully (aria-hidden="false")')
            }
        } else {
            WebUI.comment('✓ Navbar is already open (aria-hidden="false")')
        }
        
        // Click Agent Builder button
        TestObject btnAgentBuilder = findTestObject('Object Repository/nav/nav_items/button_Agent Builder')
        WebUI.waitForElementVisible(btnAgentBuilder, 15)
        WebUI.click(btnAgentBuilder)
        WebUI.delay(3)
        
        // Verify Agent Builder is open
        WebUI.waitForElementVisible(agentConfigForm, 10)
        WebUI.comment('✓ Agent Builder is open')
    } else {
        WebUI.comment('✓ Agent Builder is already open')
        
        // Still need to ensure navbar is open for other operations
        WebUI.comment('Checking Navbar state...')
        TestObject navSidebar = new TestObject('navSidebar')
        navSidebar.addProperty('xpath', ConditionType.EQUALS, "//nav")
        WebUI.waitForElementVisible(navSidebar, 5)
        
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
    }

    // ============================================================
    // Step 1: Click Create New Agent
    // ============================================================
    WebUI.comment('Step 1: Click Create New Agent...')
    TestObject btnCreateAgent = new TestObject('btnCreateNewAgent')
    btnCreateAgent.addProperty('xpath', ConditionType.EQUALS, 
        "//form[@aria-label='Agent configuration form']//button[@aria-label='Create New Agent']")
    WebUI.waitForElementVisible(btnCreateAgent, 15)
    WebUI.click(btnCreateAgent)
    WebUI.delay(3)
    WebUI.comment('✓ Create New Agent clicked')

    // ============================================================
    // Step 2: Input Name
    // ============================================================
    WebUI.comment('Step 2: Input Agent Name...')
    TestObject inputName = findTestObject('Object Repository/nav/RAG/input__name')
    String agentName = "Auto_Test_Agent_" + System.currentTimeMillis()
    WebUI.setText(inputName, agentName)
    WebUI.comment('Agent Name: ' + agentName)

    // ============================================================
    // Step 3: Select Category
    // ============================================================
    WebUI.comment('Step 3: Select Category...')
    TestObject btnCategory = findTestObject('Object Repository/nav/RAG/button_selector_category')
    WebUI.waitForElementVisible(btnCategory, 10)
    WebUI.click(btnCategory)
    WebUI.delay(1.5)

    TestObject optionGeneral = new TestObject('option_General')
    optionGeneral.addProperty('xpath', ConditionType.EQUALS, "//div[@role='option']//span[text()='General']")
    WebUI.waitForElementVisible(optionGeneral, 10)
    WebUI.click(optionGeneral)
    WebUI.comment('✓ Selected category: General')

    // ============================================================
    // Step 4: Switch to Model tab
    // ============================================================
    WebUI.comment('Step 4: Switch to Model tab...')
    TestObject btnSelectModelTab = findTestObject('Object Repository/nav/RAG/button_Select a model')
    WebUI.waitForElementVisible(btnSelectModelTab, 10)
    WebUI.click(btnSelectModelTab)
    WebUI.delay(2)
    WebUI.comment('✓ Model tab opened')

    // ============================================================
    // Step 5: Select Provider
    // ============================================================
    WebUI.comment('Step 5: Select Provider...')
    
    String targetProvider = "Gemini"  // Hoặc "Nufi", "nufi-lab"
    
    TestObject btnProvider = findTestObject('Object Repository/nav/RAG/button_Select a provider')
    WebUI.waitForElementVisible(btnProvider, 10)
    WebUI.click(btnProvider)
    WebUI.delay(2)

    TestObject providerOption = new TestObject('providerOption')
    providerOption.addProperty('xpath', ConditionType.EQUALS, 
        "//div[@role='option']//span[contains(text(), '" + targetProvider + "')]")
    
    WebUI.waitForElementVisible(providerOption, 10)
    WebUI.click(providerOption)
    WebUI.comment('✓ Selected provider: ' + targetProvider)
    // ============================================================
    // Step 6: Back to Builder
    // ============================================================
    WebUI.comment('Step 6: Back to Builder...')
    TestObject btnBackToBuilder = findTestObject('Object Repository/nav/RAG/button_Back to builder')
    WebUI.waitForElementVisible(btnBackToBuilder, 10)
    WebUI.click(btnBackToBuilder)
    WebUI.delay(2)
    WebUI.comment('✓ Back to Builder clicked')

    // ============================================================
    // Step 7: Create Agent
    // ============================================================
    WebUI.comment('Step 7: Click Create Agent...')
    TestObject btnCreate = findTestObject('Object Repository/nav/RAG/button_Create')
    WebUI.click(btnCreate)
    WebUI.delay(5)
    WebUI.comment('✓ Create button clicked')

    // ============================================================
    // Step 8: Verify success
    // ============================================================
    WebUI.comment('Step 8: Verify Agent created successfully...')
    TestObject uploadBtn = findTestObject('Object Repository/nav/RAG/button_Upload for File Search')
    
    if (WebUI.verifyElementPresent(uploadBtn, 15, FailureHandling.OPTIONAL)) {
        WebUI.comment('✓ SUCCESS: Agent "' + agentName + '" created successfully!')
    } else {
        KeywordUtil.markFailed("FAILED: Upload File section did not appear after creation.")
    }

    WebUI.takeScreenshot('TC01_Create_Agent_Success.png')
    WebUI.comment('=== TC01 Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC01 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC01_Create_Agent_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}