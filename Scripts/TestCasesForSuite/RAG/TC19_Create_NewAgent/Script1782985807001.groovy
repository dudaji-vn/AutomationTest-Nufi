import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC19: Create New Agent
 * Verify creation of new Agent with mandatory fields
 */

WebUI.comment('=== TC19: Create New Agent ===')

try {
    // Step 0: Open Agent Builder
    WebUI.comment('Step 0: Open Agent Builder...')
    TestObject btnAgentBuilder = findTestObject('Object Repository/Core Chat/nav/nav_items/button_Agent Builder')
    WebUI.waitForElementVisible(btnAgentBuilder, 15)
    WebUI.click(btnAgentBuilder)
    WebUI.delay(2)

    // Step 1: Click Create New Agent
    WebUI.comment('Step 1: Click Create New Agent...')
    TestObject btnCreateAgent = new TestObject('btnCreateNewAgent')
    btnCreateAgent.addProperty('xpath', ConditionType.EQUALS, "//button[contains(text(),'Create New Agent') or contains(text(),'New Agent')]")
    WebUI.waitForElementVisible(btnCreateAgent, 15)
    WebUI.click(btnCreateAgent)
    WebUI.delay(3)

    // Step 2: Input Name
    WebUI.comment('Step 2: Input Agent Name...')
    TestObject inputName = findTestObject('Object Repository/Core Chat/nav/RAG/input__name')
    String agentName = "Auto_Test_Agent_" + System.currentTimeMillis()
    WebUI.setText(inputName, agentName)

    // Step 3: Select Category
    WebUI.comment('Step 3: Select Category...')
    TestObject btnCategory = findTestObject('Object Repository/Core Chat/nav/RAG/button_selector_category')
    WebUI.waitForElementVisible(btnCategory, 10)
    WebUI.click(btnCategory)
    WebUI.delay(1.5)

    TestObject optionGeneral = new TestObject('option_General')
    optionGeneral.addProperty('xpath', ConditionType.EQUALS, "//div[@role='option']//span[text()='General']")
    WebUI.waitForElementVisible(optionGeneral, 10)
    WebUI.click(optionGeneral)
    WebUI.comment('Selected category: General')

    // Step 4: Switch to Model tab
    WebUI.comment('Step 4: Switch to Model tab...')
    TestObject btnSelectModelTab = findTestObject('Object Repository/Core Chat/nav/RAG/button_Select a model')
    WebUI.waitForElementVisible(btnSelectModelTab, 10)
    WebUI.click(btnSelectModelTab)
    WebUI.delay(2)

    // Step 5: Select Provider (Nufi)
    WebUI.comment('Step 5: Select Provider (Nufi)...')
    TestObject btnProvider = findTestObject('Object Repository/Core Chat/nav/RAG/button_Select a provider')
    WebUI.waitForElementVisible(btnProvider, 10)
    WebUI.click(btnProvider)
    WebUI.delay(2)

    TestObject nufiOption = new TestObject('provider_Nufi')
    nufiOption.addProperty('xpath', ConditionType.EQUALS, "//div[@role='option']//span[text()='Nufi']")
    WebUI.waitForElementVisible(nufiOption, 10)
    WebUI.click(nufiOption)
    WebUI.comment('Selected provider: Nufi')

    // Step 6: Back to Builder (RẤT QUAN TRỌNG)
    WebUI.comment('Step 6: Back to Builder...')
    TestObject btnBackToBuilder = findTestObject('Object Repository/Core Chat/nav/RAG/button_Back to builder')
    WebUI.waitForElementVisible(btnBackToBuilder, 10)
    WebUI.click(btnBackToBuilder)
    WebUI.delay(2)

    // Step 7: Create Agent
    WebUI.comment('Step 7: Click Create Agent...')
    TestObject btnCreate = findTestObject('Object Repository/Core Chat/nav/RAG/button_Create')
    WebUI.click(btnCreate)
    WebUI.delay(5)

    // Step 8: Verify success
    WebUI.comment('Step 8: Verify Agent created successfully...')
    TestObject uploadBtn = findTestObject('Object Repository/Core Chat/nav/RAG/button_Upload for File Search')
    
    if (WebUI.verifyElementPresent(uploadBtn, 15, FailureHandling.OPTIONAL)) {
        WebUI.comment('SUCCESS: Agent "' + agentName + '" created successfully!')
    } else {
        KeywordUtil.markFailed("FAILED: Upload File section did not appear after creation.")
    }

    WebUI.takeScreenshot('TC19_Create_Agent_Success.png')
    WebUI.comment('=== TC19 Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC19 FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC19_Create_Agent_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}