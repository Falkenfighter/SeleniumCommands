package org.qa.selenium;

import com.google.common.base.Function;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.qa.selenium.internal.ByCSS;
import org.qa.selenium.internal.ByID;
import org.qa.selenium.internal.ByWebElement;
import org.qa.selenium.internal.ByXPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created By: Justin Graham
 * Date: 1/29/13
 */
public class Commands implements SeleniumCommands, ByXPath, ByCSS, ByID, ByWebElement
{
	/** Instance of WebDriver to use when calling commands */
	private final WebDriver driver;

	private Logger logger = LogManager.getLogger(getClass().getSimpleName());

	/** Initial values for FluentWait's timeout and polling in Seconds */
	private long waitForElement = 15;
	private long pollingForElement = 1;

	/** Holds the toString of the last command we called allowing us to throw which command we hit an error on */
	private String lastCommand;

	/** Hold the url of the page we last called a command on */
	private String currentUrl;

	public Commands(WebDriver driver)
	{
		this.driver = driver;
	}

	/*
	 * Global Overrides
	 */
	@Override
	public SeleniumCommands Click(Using locator)
	{
		setCurrentUrl();
		setLastCommand("Click Using " + locator);
		logger.info("Click Using " + locator);
		locator.Click(this);
		return this;
	}

	@Override
	public SeleniumCommands Click(Using locator, String linkName)
	{
		setCurrentUrl();
		setLastCommand("Click '" + linkName + "' Using " + locator);
		logger.info("Click '" + linkName + "' Using " + locator);
		locator.Click(this);
		return this;
	}

	@Override
	public SeleniumCommands ClickRandom(Using locator, String linkName)
	{
		setCurrentUrl();
		setLastCommand("Click Random '" + linkName + "' Using " + locator);
		logger.info("Click Random '" + linkName + "' Using " + locator);
		locator.ClickRandom(this);
		return this;
	}

	@Override
	public SeleniumCommands ClickRandom(Using locator)
	{
		setCurrentUrl();
		setLastCommand("Click Random Using " + locator);
		logger.info("Click Random Using " + locator);
		locator.ClickRandom(this);
		return this;
	}

	@Override
	public WebElement GetElement(Using locator)
	{
		setCurrentUrl();
		setLastCommand("GetElement Using " + locator);
		return locator.GetElement(this);
	}

	@Override
	public int GetElementCount(Using locator)
	{
		setCurrentUrl();
		setLastCommand("GetElementCount Using " + locator);
		return locator.GetElementCount(this);
	}

	@Override
	public SeleniumCommands Type(String input, Using locator)
	{
		setCurrentUrl();
		setLastCommand("Type '" + input + "' Using " + locator);
		logger.info("Type '" + input + "' Using " + locator);
		locator.Type(input, this);
		return this;
	}

	@Override
	public SeleniumCommands Type(String input, Using locator, String inputName)
	{
		setCurrentUrl();
		setLastCommand("Type '" + input + "' into " + inputName + " Using " + locator);
		logger.info("Type '" + input + "' into " + inputName + " Using " + locator);
		locator.Type(input, this);
		return this;
	}

	@Override
	public SeleniumCommands ComboBoxByText(String visibleText, Using locator)
	{
		setCurrentUrl();
		setLastCommand("ComboBoxByText '" + visibleText + "' Using " + locator);
		logger.info("Select '" + visibleText + "' Using " + locator);
		locator.ComboBoxText(visibleText, this);
		return this;
	}

	@Override
	public SeleniumCommands ComboBoxByText(
			String visibleText, Using locator, String comboBoxName
	)
	{
		setCurrentUrl();
		setLastCommand("ComboBoxByText '" + visibleText + "' from '" + comboBoxName + "' Using " + locator);
		logger.info("Select '" + visibleText + "' from '" + comboBoxName + "' Using " + locator);
		locator.ComboBoxText(visibleText, this);
		return this;
	}

	@Override
	public SeleniumCommands ComboBoxRandom(Using locator)
	{
		setCurrentUrl();
		setLastCommand("ComboBoxRandom Using " + locator);
		logger.info("ComboBoxRandom Using " + locator);
		locator.ComboBoxRandom(this);
		return this;
	}

	@Override
	public SeleniumCommands ComboBoxRandom(Using locator, String comboBoxName)
	{
		setCurrentUrl();
		setLastCommand("ComboBoxRandom from '" + comboBoxName + "' Using " + locator);
		logger.info("ComboBoxRandom from '" + comboBoxName + "' Using " + locator);
		locator.ComboBoxRandom(this);
		return this;
	}

	@Override
	public SeleniumCommands WaitForElement(Using locator)
	{
		setCurrentUrl();
		setLastCommand("WaitForElement Using " + locator);
		logger.info("Wait for " + locator);
		locator.WaitForElement(this);
		return this;
	}

	@Override
	public SeleniumCommands WaitForElement(Using locator, String elementName)
	{
		setCurrentUrl();
		setLastCommand("WaitForElement '" + elementName + "' Using " + locator);
		logger.info("Wait for '" + elementName + "' Using " + locator);
		locator.WaitForElement(this);
		return this;
	}

	@Override
	public List<WebElement> GetElements(Using locator)
	{
		setCurrentUrl();
		setLastCommand("GetElements Using " + locator);
		return locator.GetElements(this);
	}

	@Override
	public String GetElementAttribute(String attribute, Using locator)
	{
		setCurrentUrl();
		setLastCommand("GetElementAttribute '" + attribute + "' Using " + locator);
		return locator.GetElementAttribute(attribute, this);
	}

	/*
	 * CSS Functions
	 */
	@Override
	public SeleniumCommands clickElementByCSS(String css)
	{
		WebElement element = fluentWaitForElementCss(css);
		fluentWaitForClickableCss(css);
		new Actions(driver).moveToElement(element).perform();
		element.click();
		return this;
	}

	@Override
	public SeleniumCommands clickRandomElementByCSS(String css)
	{
		waitForCSS(css);
		List<WebElement> elements = getAllVisibleElements(fluentWaitForElementsCss(css));
		return clickRandom(elements);
	}

	@Override
	public WebElement getElementByCSS(String css)
	{
		return fluentWaitForElementCss(css);
	}

	@Override
	public int getElementCountByCSS(String css)
	{
		List<WebElement> element = fluentWaitForElementsCss(css);
		return (element == null) ? 0 : element.size();
	}

	@Override
	public List<WebElement> getElementsByCSS(String css)
	{
		return fluentWaitForElementsCss(css);
	}

	@Override
	public SeleniumCommands waitForCSS(String css)
	{
		fluentWaitForElementCss(css);
		return this;
	}

	@Override
	public SeleniumCommands comboBoxVisibleTextByCSS(String visibleText, String css)
	{
		WebElement element = getFirstVisibleElement(fluentWaitForElementsCss(css));
		Select comboBox = new Select(element);
		comboBox.selectByVisibleText(visibleText);
		return this;
	}

	@Override
	public SeleniumCommands comboBoxRandomByCSS(String css)
	{
		WebElement element = fluentWaitForElementCss(css);
		return comboBoxSelectRandom(new Select(element));
	}

	@Override
	public SeleniumCommands typeByCSS(String input, String css)
	{
		WebElement element;
		if (fluentWaitForElementsCss(css).size() > 1)
			element = getFirstVisibleElement(fluentWaitForElementsCss(css));
		else
			element = fluentWaitForElementCss(css);
		fluentWaitForVisibilityOfElement(element);
		element.clear();
		element.sendKeys(input + "\t");
		return this;
	}

	@Override
	public String getElementAttributeByCSS(String attribute, String css)
	{
		WebElement element = fluentWaitForElementCss(css);
		return element.getAttribute(attribute);
	}

	/*
	 * ID Functions
	 */
	@Override
	public SeleniumCommands clickElementByID(String id)
	{
		WebElement element = fluentWaitForElementId(id);
		fluentWaitForClickableId(id);
		new Actions(driver).moveToElement(element).perform();
		element.click();
		return this;
	}

	@Override
	public SeleniumCommands clickRandomElementByID(String id)
	{
		waitForID(id);
		List<WebElement> elements = getAllVisibleElements(fluentWaitForElementsId(id));
		return clickRandom(elements);
	}

	@Override
	public WebElement getElementByID(String id)
	{
		return fluentWaitForElementId(id);
	}

	@Override
	public int getElementCountByID(String id)
	{
		List<WebElement> element = fluentWaitForElementsId(id);
		return (element == null) ? 0 : element.size();
	}

	@Override
	public SeleniumCommands typeByID(String input, final String id)
	{
		WebElement element;
		if (fluentWaitForElementsId(id).size() > 1)
			element = getFirstVisibleElement(fluentWaitForElementsId(id));
		else
			element = fluentWaitForElementId(id);
		fluentWaitForVisibilityOfElement(element);
		element.clear();
		element.sendKeys(input + "\t");
		return this;
	}

	@Override
	public SeleniumCommands comboBoxVisibleTextByID(String visibleText, String id)
	{
		WebElement element = getFirstVisibleElement(fluentWaitForElementsId(id));
		Select comboBox = new Select(element);
		comboBox.selectByVisibleText(visibleText);
		return this;
	}

	@Override
	public SeleniumCommands comboBoxRandomByID(String id)
	{
		WebElement element = fluentWaitForElementId(id);
		return comboBoxSelectRandom(new Select(element));
	}

	@Override
	public SeleniumCommands waitForID(String id)
	{
		fluentWaitForElementId(id);
		return this;
	}

	@Override
	public List<WebElement> getElementsByID(String id)
	{
		return fluentWaitForElementsId(id);
	}

	@Override
	public String getElementAttributeByID(String attribute, String id)
	{
		WebElement element = fluentWaitForElementId(id);
		return element.getAttribute(attribute);
	}

	/*
	 * XPath Functions
	 */
	@Override
	public SeleniumCommands clickElementByXPath(String xpath)
	{
		WebElement element = fluentWaitForElementXPath(xpath);
		fluentWaitForClickableXPath(xpath);
		new Actions(driver).moveToElement(element).perform();
		element.click();
		return this;
	}

	@Override
	public SeleniumCommands clickRandomElementByXPath(String xpath)
	{
		waitForXPath(xpath);
		List<WebElement> elements = getAllVisibleElements(fluentWaitForElementsXPath(xpath));
		return clickRandom(elements);
	}

	@Override
	public WebElement getElementByXPath(String xpath)
	{
		return fluentWaitForElementXPath(xpath);
	}

	@Override
	public int getElementCountByXPath(String xpath)
	{
		List<WebElement> element = fluentWaitForElementsXPath(xpath);
		return (element == null) ? 0 : element.size();
	}

	@Override
	public SeleniumCommands typeByXPath(String input, String xpath)
	{
		WebElement element;
		if (fluentWaitForElementsXPath(xpath).size() > 1)
			element = getFirstVisibleElement(fluentWaitForElementsXPath(xpath));
		else
			element = fluentWaitForElementXPath(xpath);
		fluentWaitForVisibilityOfElement(element);
		element.clear();
		element.sendKeys(input + "\t");
		return this;
	}

	@Override
	public SeleniumCommands comboBoxVisibleTextByXPath(String visibleText, String xpath)
	{
		WebElement element = getFirstVisibleElement(fluentWaitForElementsXPath(xpath));
		Select comboBox = new Select(element);
		comboBox.selectByVisibleText(visibleText);
		return this;
	}

	@Override
	public SeleniumCommands comboBoxRandomByXPath(String xpath)
	{
		WebElement element = fluentWaitForElementXPath(xpath);
		return comboBoxSelectRandom(new Select(element));
	}

	@Override
	public SeleniumCommands waitForXPath(String xpath)
	{
		fluentWaitForElementXPath(xpath);
		return this;
	}

	@Override
	public List<WebElement> getElementsByXPath(String xpath)
	{
		return fluentWaitForElementsXPath(xpath);
	}

	@Override
	public String getElementAttributeByXPath(String attribute, String xpath)
	{
		WebElement element = fluentWaitForElementXPath(xpath);
		return element.getAttribute(attribute);
	}

	/*
	 * WebElement Functions
	 */
	@Override
	public SeleniumCommands clickElementByWebElement(WebElement element)
	{
		fluentWaitForVisibilityOfElement(element);
		new Actions(driver).moveToElement(element).perform();
		element.click();
		return this;
	}

	@Override
	public SeleniumCommands clickRandomElementByWebElement(List<WebElement> elements)
	{
		return clickRandom(elements);
	}

	@Override
	public SeleniumCommands typeByWebElement(String input, WebElement element)
	{
		fluentWaitForVisibilityOfElement(element);
		element.clear();
		element.sendKeys(input + "\t");
		return this;
	}

	@Override
	public SeleniumCommands comboBoxVisibleTextByWebElement(String visibleText, WebElement element)
	{
		Select comboBox = new Select(element);
		comboBox.selectByVisibleText(visibleText);
		return this;
	}

	@Override
	public SeleniumCommands comboBoxRandomByWebElement(WebElement element)
	{
		return comboBoxSelectRandom(new Select(element));
	}

	@Override
	public SeleniumCommands waitForWebElement(WebElement element)
	{
		fluentWaitForElementXPath(GetElementXPath(element));
		return this;
	}

	@Override
	public List<WebElement> getElementsByWebElement(WebElement element)
	{
		ArrayList<WebElement> elementList = new ArrayList<WebElement>();
		elementList.add(element);
		return elementList;
	}

	@Override
	public WebElement getElementByWebElement(WebElement element)
	{
		return element;
	}

	@Override
	public int getElementCountByWebElement(WebElement element)
	{
		return 1;
	}

	@Override
	public String getElementAttributeByWebElement(String attribute, WebElement element)
	{
		return element.getAttribute(attribute);
	}

	/*
	 * SeleniumCommand Functions
	 */
	@Override
	public SeleniumCommands SetFluentWaitTime(
			Integer waitTime, TimeUnit waitUnit, Integer pollingTime, TimeUnit pollingUnit
	)
	{
		waitTime = (waitTime == null || waitTime < 0) ? 0 : waitTime;
		pollingTime = (pollingTime == null || pollingTime < 0) ? 0 : pollingTime;
		waitUnit = (waitUnit == null) ? TimeUnit.SECONDS : waitUnit;
		pollingUnit = (pollingUnit == null) ? TimeUnit.SECONDS : pollingUnit;

		waitForElement = waitUnit.toSeconds(waitTime);
		pollingForElement = pollingUnit.toSeconds(pollingTime);

		if (waitForElement < pollingForElement)
			throw new IllegalStateException("Wait time must be greater than or equal to polling time");

		return this;
	}

	@Override
	public SeleniumCommands WaitForTime(long time, TimeUnit unit)
	{
		if (time <= 0) throw new IllegalArgumentException("Wait time must be positive and greater than 0");
		long waitUntil = System.currentTimeMillis() + unit.toMillis(time);
		while (System.currentTimeMillis() < waitUntil)
		{/*Sleep*/}
		return this;
	}

	@Override
	public SeleniumCommands Open(String url)
	{
		if (url == null) throw new NullPointerException("URL must not be Null");
		driver.get(url);
		return this;
	}

	@Override
	public SeleniumCommands Close()
	{
		try
		{
			driver.quit();
		} catch (Exception e)
		{/*Ignore if driver is already closed*/}
		return this;
	}

	@Override
	public SeleniumCommands PopWebFrame()
	{
		driver.switchTo().defaultContent();
		return this;
	}

	@Override
	public String GetElementXPath(WebElement element)
	{
		return (String) ((JavascriptExecutor) driver).executeScript(
			"getXPath=function(node)" +
			"{" +
				"if (node.id !== '')" +
				"{" +
					"return '//' + node.tagName.toLowerCase() + '[@id=\"' + node.id + '\"]'" +
				"}" +

				"if (node === document.body)" +
				"{" +
					"return node.tagName.toLowerCase()" +
				"}" +

				"var nodeCount = 0;" +
				"var childNodes = node.parentNode.childNodes;" +

				"for (var i=0; i<childNodes.length; i++)" +
				"{" +
					"var currentNode = childNodes[i];" +

					"if (currentNode === node)" +
					"{" +
						"return getXPath(node.parentNode) + '/' + node.tagName.toLowerCase() + '[' + (nodeCount+1) + ']'" +
					"}" +

					"if (currentNode.nodeType === 1 && " +
						"currentNode.tagName.toLowerCase() === node.tagName.toLowerCase())" +
					"{" +
						"nodeCount++" +
					"}" +
				"}" +
			"};" +

			"return getXPath(arguments[0]);", element);
	}

	/*
	 * Class Functions
	 */
	private Wait<WebDriver> Wait()
	{
		return new FluentWait<WebDriver>(driver)
				.withMessage(lastCommand + " on " + currentUrl)
				.withTimeout(waitForElement, TimeUnit.SECONDS)
				.pollingEvery(pollingForElement, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
	}

	/*private Wait<WebDriver> debugWait()
	{
		return new FluentWait<WebDriver>(driver)
				.withTimeout(2, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);
	}*/

	private WebElement fluentWaitForElementXPath(final String xpath)
	{
		return Wait().until(new Function<WebDriver, WebElement>()
		{
			@Override
			public WebElement apply(WebDriver d)
			{
				return d.findElement(By.xpath(xpath));
			}
		});
	}

	private void fluentWaitForClickableXPath(final String xpath)
	{
		Wait().until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

	private List<WebElement> fluentWaitForElementsXPath(final String xpath)
	{
		return Wait().until(new Function<WebDriver, List<WebElement>>()
		{
			@Override
			public List<WebElement> apply(WebDriver d)
			{
				return d.findElements(By.xpath(xpath));
			}
		});
	}

	private WebElement fluentWaitForElementCss(final String css)
	{
		return Wait().until(new Function<WebDriver, WebElement>()
		{
			@Override
			public WebElement apply(WebDriver d)
			{
				return d.findElement(By.cssSelector(css));
			}
		});
	}

	private List<WebElement> fluentWaitForElementsCss(final String css)
	{
		return Wait().until(new Function<WebDriver, List<WebElement>>()
		{
			@Override
			public List<WebElement> apply(WebDriver d)
			{
				return d.findElements(By.cssSelector(css));
			}
		});
	}

	private void fluentWaitForClickableCss(final String css)
	{
		Wait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(css)));
	}

	private WebElement fluentWaitForElementId(final String id)
	{
		return Wait().until(new Function<WebDriver, WebElement>()
		{
			@Override
			public WebElement apply(WebDriver d)
			{
				return d.findElement(By.id(id));
			}
		});
	}

	private List<WebElement> fluentWaitForElementsId(final String id)
	{
		return Wait().until(new Function<WebDriver, List<WebElement>>()
		{
			@Override
			public List<WebElement> apply(WebDriver d)
			{
				return d.findElements(By.id(id));
			}
		});
	}

	private void fluentWaitForClickableId(final String id)
	{
		Wait().until(ExpectedConditions.elementToBeClickable(By.id(id)));
	}

	private WebElement getFirstVisibleElement(List<WebElement> elements)
	{
		for (WebElement element : elements)
		{
			if (element.isDisplayed())
			{
				return element;
			}
		}

		throw new ElementNotVisibleException("Element is not currently visible and so may not be interacted with");
	}

	private List<WebElement> getAllVisibleElements(List<WebElement> elements)
	{
		List<WebElement> visibleElements = new ArrayList<WebElement>();

		for (WebElement element : elements)
		{
			if (element.isDisplayed())
			{
				visibleElements.add(element);
			}
		}

		if (visibleElements.isEmpty())
		{
			throw new ElementNotVisibleException("Element is not currently visible and so may not be interacted with");
		}

		return visibleElements;
	}

	private WebElement fluentWaitForVisibilityOfElement(WebElement element)
	{
		return Wait().until(ExpectedConditions.visibilityOf(element));
	}

	private synchronized void setLastCommand(String lastCommand)
	{
		this.lastCommand = lastCommand;
	}

	private synchronized void setCurrentUrl()
	{
		currentUrl = driver.getCurrentUrl();
	}

	private SeleniumCommands clickRandom(List<WebElement> elements)
	{
		WebElement clickElement;
		if (elements.size() == 1)
		{
			clickElement = elements.get(0);
		}
		else
		{
			int size = elements.size();
			Random rand = new Random();
			int selection = rand.nextInt(size);
			clickElement = elements.get(selection);
		}
		fluentWaitForVisibilityOfElement(clickElement);
		new Actions(driver).moveToElement(clickElement).perform();
		clickElement.click();
		return this;
	}

	private SeleniumCommands comboBoxSelectRandom(Select comboBox)
	{
		int size = comboBox.getOptions().size();
		Random rand = new Random();
		comboBox.selectByIndex(rand.nextInt(size));
		return this;
	}
}