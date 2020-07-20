### Set the default target
.DEFAULT_GOAL := help

${HOME}/truststores/cacerts:
	./scripts/buildtrust

.env:
	echo "Please provide a valid .env file before running tests" && exit 127

checkPreconditions: .env ${HOME}/truststores/cacerts
.PHONY: checkPreconditions

### Use the headless chrome by default
test: testChromeHeadless
.PHONY:	 test

### Test with Chrome in headless versoin
testChromeHeadless: checkPreconditions
	./gradlew chromeHeadlessTest --system-prop "javax.net.ssl.trustStore=${HOME}/truststores/cacerts" --system-prop "javax.net.ssl.trustStorePassword=changeit" --tests "org.ods.e2e.ODSSpec"
.PHONY: testChromeHeadless

### Test with Chrome in desktop version
testChrome: checkPreconditions
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
