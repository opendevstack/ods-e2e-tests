import geb.report.ScreenshotReporter
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import util.SpecHelper

def properties = new SpecHelper().getApplicationProperties()

// Define timing to get information from a page
waiting {
	timeout = 25
	retryInterval = 0.5
	includeCauseInMessage = true
	presets {
		slow {
			timeout = 50
			retryInterval = 1
		}
		quick {
			timeout = 10
		}
	}
}

environments {
	
	// run via “./gradlew chromeTest”
	// See: http://code.google.com/p/selenium/wiki/ChromeDriver
	htmlUnit {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME, true)

		def env = System.getenv()
		if (env.HTTP_PROXY) {
			Proxy proxy = new Proxy();
			URL url = new URL(env.HTTP_PROXY);
			proxy.setHttpProxy("${url.getHost()}:${url.getPort()}");
			proxy.setNoProxy(env.NO_PROXY)
			driver.setProxySettings(proxy);
		}

		return driver
	}
	chrome {
		driver = { new ChromeDriver() }
	}

	// run via “./gradlew chromeHeadlessTest”
	// See: http://code.google.com/p/selenium/wiki/ChromeDriver
	chromeHeadless {
		driver = {
			ChromeOptions o = new ChromeOptions()
			o.addArguments('headless')
			new ChromeDriver(o)
		}
	}
	
	// run via “./gradlew firefoxTest”
	// See: http://code.google.com/p/selenium/wiki/FirefoxDriver
	firefox {
		atCheckWaiting = 1

		driver = { new FirefoxDriver() }
	}

}
// Report configuration
reporter =  new ScreenshotReporter()
reportsDir = new File(properties."config.reports.dir")
