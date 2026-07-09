import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.SetUp
import com.kms.katalon.core.annotation.SetupTestCase
import com.kms.katalon.core.annotation.TearDown
import com.kms.katalon.core.annotation.TearDownTestCase

/**
 * Some methods below are samples for using SetUp/TearDown in a test suite.
 */

/**
 * Setup test suite environment.
 */
@SetUp(skipped = false) // Please change skipped to be false to activate this method.
def setUp() {
	// Put your code here.
	WebUI.comment('=== SUITE SETUP: Teams Suite ===')
	WebUI.callTestCase(findTestCase('Test Cases/TestCasesForSuite/Setup/Setup_Teams_Suite'), [:], FailureHandling.STOP_ON_FAILURE)
	WebUI.comment('✓ Suite setup completed')
}

/**
 * Clean test suites environment.
 */
@TearDown(skipped = true) // Please change skipped to be false to activate this method.
def tearDown() {
	// Put your code here.
	WebUI.comment('=== SUITE TEARDOWN: Teams Suite ===')
//	WebUI.callTestCase(findTestCase('Test Cases/TestCasesForSuite/Setup/Teardown_CoreChat_Suite'), [:], FailureHandling.OPTIONAL)
	WebUI.comment('✓ Suite teardown completed')
}

/**
 * Run before each test case starts.
 * This will run BEFORE every test case in the suite.
 * We keep the browser open between test cases by doing nothing here.
 */
@SetupTestCase(skipped = false) // Changed to false to activate this method
def setupTestCase() {
	// Put your code here.
	WebUI.comment('=== SETUP TEST CASE: Reusing existing browser ===')
	// Do nothing - keep browser open between test cases
	WebUI.comment('✓ Browser will be reused for this test case')
}

/**
 * Run after each test case ends.
 * This will run AFTER every test case in the suite.
 * We do NOT close the browser here to keep it open for the next test case.
 */
@TearDownTestCase(skipped = true) // Keep as true - do NOT close browser after each test case
def tearDownTestCase() {
	// Put your code here.
	// DO NOT close browser - keep it open for the next test case
	WebUI.comment('=== TEARDOWN TEST CASE: Browser kept open for next test ===')
	// Uncomment the line below if you want to close browser after each test case
	// WebUI.closeBrowser()
}

/**
 * References:
 * Groovy tutorial page: http://docs.groovy-lang.org/next/html/documentation/
 */