import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;




public class zentao {
    private  WebDriver driver;
    private  loadExcel excel;
    private  List<excelDao> daoList;
    private  config config;
    WebDriver chromeDriver = null;
    public void start() throws InterruptedException, AWTException, IOException {
        String userDir = System.getProperty("user.dir");
        boolean SysOS = System.getProperty("os.name").toString().indexOf("Mac")==-1 ? true:false;
//        System.out.println(userDir);
        if (SysOS){
            System.setProperty("webdriver.chrome.driver", userDir+"/lib/chromedriver.exe");
        }else {
            System.setProperty("webdriver.chrome.driver", userDir+"/lib/chromedriver");
        }

//        System.setProperty("webdriver.chrome.driver", "/Users/apple/Documents/xxxx/zentao/target/jfx/app/lib/chromedriver");
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
////        options.addArguments("--headless")USER; // only if you are ACTUALLY running headless
//        options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
//        options.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
//        options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
//        options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
//        options.addArguments("--disable-gpu");
        System.out.println("成功");
        chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        zentao zen = new zentao(chromeDriver).login(config.getLoginUrl(),config.getName(),config.getPassword()).ceshi(config.getTestUrl(),config.getUrl());
        for (excelDao dao:daoList){
            if (dao.getUpload()){
                continue;
            }
            zen.sumbitBug().inputTable(dao);
            String[] imgStr = dao.getBugImgUrl().split(",");
            int count =1;
//            userDir = userDir.substring(1,userDir.length());
//            System.out.println(userDir);
            for (String str:imgStr){
                zen.upload_Image(userDir+"/bug/"+str,count++);
            }
            zen.submit();

            excel.setUpLoad(dao.getId());
        }
        //退出
//        chromeDriver.close();
    }
    public void close(){
        chromeDriver.close();
    }
    public  zentao(String path) throws Exception {
        System.out.println("test");
        this.excel = new loadExcel(path);
        this.daoList = excel.readXlSX();
        this.config = excel.readConfig();
    }


    public zentao(WebDriver driver) {
        this.driver = driver;
    }

    public zentao login(String url,String name,String password){
        driver.get(url);
        driver.findElement(By.xpath("//*[@id=\"account\"]")).sendKeys(name);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"submit\"]")).submit();
        return this;
    }
    public zentao ceshi(String testurl,String url) throws InterruptedException {
        driver.get(testurl);
        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[4]/a")).click();
        driver.get(url);
        Thread.sleep(1000);
        return this;
    }
    public zentao sumbitBug(){
        driver.findElement(By.xpath("//*[@id=\"mainMenu\"]/div[3]/a[3]")).click();
        return this;
    }

    /**
     * ????
     * @param dao
     * @return
     */
    public zentao inputTable(excelDao dao) throws AWTException, InterruptedException {
        String name=dao.getDesignee();
        String version=dao.getVersion();
        String date = dao.getDeadline();
        String bug_Type = dao.getBugType();
        String title = dao.getBugTitle();
        String content = dao.getContent();
        String severity = dao.getSeverity();
        String priority = dao.getPriority();
        int count =1 ;
        driver.findElement(By.xpath("//*[@id=\"assignedTo_chosen\"]/a")).click();
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"assignedTo_chosen\"]/div/ul/li"));
        for (WebElement element:elements){
           if (element.getText().equals(name)){
               driver.findElement(By.xpath("//*[@id=\"assignedTo_chosen\"]/div/ul/li["+count+"]")).click();
               break;
           }
           count++;
        }
        count =1;
        driver.findElement(By.xpath("//*[@id=\"openedBuild_chosen\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"openedBuild_chosen\"]/div/ul/li")).click();
//        List<WebElement> elements1 = driver.findElements(By.xpath("//*[@id=\"openedBuild_chosen\"]/div/ul/li"));
//        for (WebElement element:elements1){
//            System.out.println(element.getText());
//            System.out.println(version);
//            if (element.getText().equals(version)){
//                driver.findElement(By.xpath("//*[@id=\"openedBuild_chosen\"]/div/ul/li["+count+"]")).click();
//                break;
//            }
//            count++;
//        }
        count = 1;
        driver.findElement(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[5]/td/div/div[2]/button")).click();
        List<WebElement> elements2 = driver.findElements(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[5]/td/div/div[2]/div/div/span"));
        for (WebElement element:elements2){
            if (element.getText().equals(severity)){
                driver.findElement(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[5]/td/div/div[2]/div/div/span["+count+"]")).click();
                break;
            }
            count++;
        }
        count = 1;
        driver.findElement(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[5]/td/div/div[3]/button")).click();
        List<WebElement> elements4 = driver.findElements(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[5]/td/div/div[3]/div/div/span"));
        for (WebElement element:elements4){
            if (element.getText().equals(priority)){
                driver.findElement(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[5]/td/div/div[3]/div/div/span["+count+"]")).click();
                break;
            }
            count++;
        }
//        driver.findElement(By.xpath("//*[@id=\"deadline\"]")).sendKeys(date);
        count = 1;
        List<WebElement> elements3 = driver.findElements(By.xpath("//*[@id=\"type_chosen\"]/div/ul/li"));
        for (WebElement element:elements3){
            if (element.getText().equals(bug_Type)){
                driver.findElement(By.xpath("//*[@id=\"type_chosen\"]/div/ul/li["+count+"]")).click();
                break;
            }
            count++;
        }
        count = 0;
        driver.findElement(By.xpath("//*[@id=\"title\"]")).sendKeys(title);
        driver.switchTo().frame(0);
        WebElement text = driver.findElement(By.xpath("/html/body"));
        text.clear();
        text.sendKeys(content);
        driver.switchTo().defaultContent();
        return this;
    }
    public void submit() throws InterruptedException {
        driver.findElement(By.id("submit")).click();
        Thread.sleep(5000);
    }
    public zentao upload_Image(String imgUrl,int count) throws InterruptedException, AWTException {
//        driver.findElement(By.cssSelector("btn btn-link file-input-btn")).sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[10]/td/div/div["+count+"]/div[1]/button")).sendKeys(Keys.ENTER);
        //*[@id="dataform"]/table/tbody/tr[10]/td/div/div[2]/div[1]/button
        StringSelection sel = new StringSelection(imgUrl);

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel,null);

        Robot robot = new Robot();
        Thread.sleep(1000);
        boolean systemOS = System.getProperty("os.name").toString().indexOf("Mac")==-1 ? true:false;
        if(systemOS){
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }else {

            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_G);

            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_G);
        }

        Thread.sleep(1000);

        // ???? CTRL+V
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        // ??? CTRL+V
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        Thread.sleep(1000);
        if(!systemOS){
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(2000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }


        // ?????? Enter
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        //?????????
        Thread.sleep(3000);


        return this;
    }
}
