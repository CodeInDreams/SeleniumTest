import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.csvreader.CsvReader;

public class SeleniumTest {
	private WebDriver driver;
	private String baseUrl;
	
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://www.ncfxy.com";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	@Test
	public void testWeb() throws Exception {
		ArrayList<String[]> csvList = new ArrayList<String[]>();
		String csvFilePath = "/home/bigbigwolf/文档/软件测试/info.csv";
		CsvReader reader = new CsvReader(csvFilePath,',',Charset.forName("SJIS"));
		while(reader.readRecord()){
			csvList.add(reader.getValues());
		}
		reader.close();
		
		for(int row=0;row<csvList.size();row++){
			String  sid = csvList.get(row)[0];
			String  email=csvList.get(row)[1];
			String pwd="";
			pwd=sid.substring(4);
			driver.get(baseUrl + "/");
			driver.findElement(By.id("name")).clear();
			driver.findElement(By.id("name")).sendKeys(sid);
			driver.findElement(By.id("pwd")).clear();
			driver.findElement(By.id("pwd")).sendKeys(pwd);
			driver.findElement(By.id("submit")).click();
			String string=driver.getPageSource();
			string=string.substring(string.indexOf("邮箱"),string.indexOf("学号"));
			Pattern p=Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(string);
			string=m.replaceAll("");
			string = string.substring(string.indexOf("<td>")+4,string.indexOf("</tr>")-5);
			assertEquals(email,string);
		}
	}
}
