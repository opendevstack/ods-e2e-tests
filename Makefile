### Set the default target
.DEFAULT_GOAL := help

### Include the environment information
include .env

### Use the headless chrome by default
test: testChromeHeadless
.PHONY:	 test

### Test with Chrome in headless versoin
testChromeHeadless: 
	./gradlew chromeHeadlessTest --tests "org.ods.e2e.ODSSpec"
.PHONY: testChromeHeadless

### Test with Chrome in desktop version
testChrome:
	./gradlew chromeTest --tests "org.ods.e2e.ODSSpec"
.PHONY: testChrome

### Default goal, information
help:
	@ echo 'Usage'
	@ echo '  make <target>'
	@ echo ''
	@ echo 'Targets:'
	@ echo '  make test               : Using the Chrome Headless by default' 
	@ echo '  make testChrome         : Chrome displaying the browser '
	@ echo '  make testChromeHeadless : Chrome without displaying the browser'
.PHONY: help
