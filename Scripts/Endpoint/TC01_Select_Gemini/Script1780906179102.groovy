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

// Select Gemini option
WebUI.click(findTestObject('Core Chat/Sellect_Gemini'))
WebUI.delay(2)

// Select Gemini (1)
WebUI.click(findTestObject('Core Chat/sellect_gemini (1)'))
WebUI.delay(2)

// Verify that span_Agents shows "gemini"
WebUI.waitForElementPresent(
    findTestObject('Core Chat/span_Agents'), 
    10, FailureHandling.STOP_ON_FAILURE)

String agentName = WebUI.getText(findTestObject('Core Chat/span_Agents')).toLowerCase()
WebUI.comment('Selected Agent: ' + agentName)

if (agentName.contains('gemini')) {
    WebUI.comment('Gemini endpoint selection successful')
    WebUI.takeScreenshot('Select_Gemini_Success.png')
} else {
    WebUI.comment('ERROR: Expected "gemini" but got: ' + agentName)
    WebUI.takeScreenshot('Select_Gemini_Failed.png')
    throw new Exception('Gemini endpoint selection failed. Expected "gemini" but got: ' + agentName)
}

WebUI.delay(2)
WebUI.closeBrowser()
