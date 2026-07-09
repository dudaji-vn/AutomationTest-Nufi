import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

/**
 * TC19A: Create New Agent with Avatar
 */

WebUI.comment('=== TC19A: Create New Agent with Avatar ===')

try {
    // Step 0: Open Agent Builder
    WebUI.comment('Step 0: Open Agent Builder...')
    TestObject btnAgentBuilder = findTestObject('Object Repository/nav/nav_items/button_Agent Builder')
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

    // Step 2: Add Avatar (Sử dụng XPath tạm thời)
    WebUI.comment('Step 2: Add Avatar...')
    TestObject btnAddAvatar = new TestObject('btnAddAvatar')
    btnAddAvatar.addProperty('xpath', ConditionType.EQUALS, 
        "//button[contains(@id,'add-img') or contains(text(),'Add image') or contains(@class,'avatar')]")
    
    WebUI.waitForElementVisible(btnAddAvatar, 10)
    WebUI.click(btnAddAvatar)
    WebUI.delay(1.5)

    // Upload image (nếu có file)
    TestObject uploadImg = findTestObject('Object Repository/nav/RAG/Upload an image')
    WebUI.click(uploadImg)
    WebUI.delay(2)
    WebUI.comment('Avatar upload section opened')

    // Step 3: Input Name
    WebUI.comment('Step 3: Input Agent Name...')
    TestObject inputName = findTestObject('Object Repository/nav/RAG/input__name')
    String agentName = "Agent_With_Avatar_" + System.currentTimeMillis()
    WebUI.setText(inputName, agentName)

    // Step 4: Select Category
    WebUI.comment('Step 4: Select Category...')
    TestObject btnCategory = findTestObject('Object Repository/nav/RAG/button_selector_category')
    WebUI.click(btnCategory)
    WebUI.delay(1.5)
    TestObject optionGeneral = new TestObject('option_General')
    optionGeneral.addProperty('xpath', ConditionType.EQUALS, "//div[@role='option']//span[text()='General']")
    WebUI.click(optionGeneral)

    // Step 5: Switch to Model tab & Select Model
    WebUI.comment('Step 5: Switch to Model tab...')
    WebUI.click(findTestObject('Object Repository/nav/RAG/button_Select a model'))
    WebUI.delay(1.5)

    WebUI.click(findTestObject('Object Repository/nav/RAG/button_Select a provider'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/nav/RAG/button_Select a model'))
    WebUI.delay(1.5)

    // Step 6: Create
    WebUI.comment('Step 6: Create Agent...')
    WebUI.click(findTestObject('Object Repository/nav/RAG/button_Create'))
    WebUI.delay(5)

    WebUI.verifyElementPresent(findTestObject('Object Repository/nav/RAG/button_Upload for File Search'), 10)
    WebUI.takeScreenshot('TC19A_Create_With_Avatar_Success.png')
    WebUI.comment('=== TC19A Completed Successfully ===')

} catch (Exception e) {
    WebUI.comment('TC19A FAILED: ' + e.getMessage())
    WebUI.takeScreenshot('TC19A_Create_Agent_Error.png')
    KeywordUtil.markFailedAndStop("Exception occurred: " + e.getMessage())
}