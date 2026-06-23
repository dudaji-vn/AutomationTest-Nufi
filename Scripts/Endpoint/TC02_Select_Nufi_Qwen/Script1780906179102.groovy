import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Call Setup/Login to prepare authentication
WebUI.callTestCase(findTestCase('Test Cases/Setup/Login'), [:])
WebUI.delay(2)

// Navigate to Base URL - system will auto redirect to /c/new if token exists
WebUI.navigateToUrl(GlobalVariable.Base_URL)
WebUI.delay(3)

// Click on "My Agents" button
WebUI.click(findTestObject('Core Chat/button_My Agents'))
WebUI.delay(2)

// Select Nufi option
WebUI.click(findTestObject('Core Chat/Agent/Sellect_Nufi'))
WebUI.delay(2)

// Select Qwen2.5-0.5B
WebUI.click(findTestObject('Core Chat/Agent/sellect_Qwen2.5-0.5B'))
WebUI.delay(2)

// Verify that span_Agents shows "Qwen2.5-0.5B"
WebUI.waitForElementPresent(
    findTestObject('Core Chat/Agent/span_Agents'), 
    10, FailureHandling.STOP_ON_FAILURE)

String agentName = WebUI.getText(findTestObject('Core Chat/Agent/span_Agents'))
WebUI.comment('Selected Agent: ' + agentName)

if (agentName.contains('Qwen2.5-0.5B')) {
    WebUI.comment('Nufi/Qwen endpoint selection successful')
    WebUI.takeScreenshot('Select_Nufi_Qwen_Success.png')
} else {
    WebUI.comment('ERROR: Expected "Qwen2.5-0.5B" but got: ' + agentName)
    WebUI.takeScreenshot('Select_Nufi_Qwen_Failed.png')
    throw new Exception('Nufi/Qwen endpoint selection failed. Expected "Qwen2.5-0.5B" but got: ' + agentName)
}

WebUI.delay(2)
WebUI.closeBrowser()
