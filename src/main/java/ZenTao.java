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




public class ZenTao {
    private  WebDriver driver;
    private LoadExcel excel;
    private  List<ExcelDao> daoList;
    private Config config;
    WebDriver chromeDriver = null;
    public void start() throws InterruptedException, AWTException, IOException {
        String userDir = System.getProperty("user.dir");
        boolean SysOS = !System.getProperty("os.name").contains("Mac");
        System.out.println("目录地址：" + userDir);
        if (SysOS){
            System.setProperty("webdriver.chrome.driver", userDir+"/lib/chromedriver.exe");
        }else {
            System.setProperty("webdriver.chrome.driver", userDir+"/lib/chromedriver");
        }

        ChromeOptions options = new ChromeOptions();
        chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        System.out.println("driver启动成功");
        //登陆
        ZenTao zen = new ZenTao(chromeDriver).login(config.getLoginUrl(),config.getName(),config.getPassword());
        // 进入对应的测试列表
        zen.ceshi(config.getTestUrl(),config.getUrl());
        System.out.println("禅道配置读取成功");
        for (ExcelDao dao:daoList){
            if (dao.getUpload()){
                continue;
            }
            zen.addBug().inputTable(dao);
            String[] imgStr = dao.getBugImgUrl().split(",");
            int count =1;
            for (String str:imgStr){
                String imgUrl = userDir + "/bug/" + str;
                zen.upload_Image(imgUrl,count++);
            }
            zen.submitBug(); //提交bug
            excel.setUpLoad(dao.getId()); //将 bug 上传状态改为True
        }
        //退出
//        close();
    }
    public void close(){
        chromeDriver.close();
    }
    public ZenTao(String path) throws Exception {
        this.excel = new LoadExcel(path);
        this.daoList = excel.readXlSX();
        this.config = excel.readConfig();
    }
    // 测试使用
    public ZenTao(List<ExcelDao> ExcelDaos, Config config){
        this.daoList = ExcelDaos;
        this.config = config;
    }

    public ZenTao(WebDriver driver) {
        this.driver = driver;
    }
    //禅道登录
    public ZenTao login(String url, String name, String password){
        driver.get(url);
        driver.findElement(By.xpath("//*[@id=\"account\"]")).sendKeys(name);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"submit\"]")).submit();
        return this;
    }
    //打开测试页面
    public ZenTao ceshi(String testurl, String url) throws InterruptedException {
        driver.get(testurl);
        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[4]/a")).click();
        driver.get(url);
        Thread.sleep(1000);
        return this;
    }
    public ZenTao addBug(){
        driver.findElement(By.xpath("//*[@id=\"mainMenu\"]/div[3]/a[3]")).click();
        return this;
    }

    /**
     * 输入bug详情信息
     * @param dao
     * @return
     */
    public ZenTao inputTable(ExcelDao dao) throws AWTException, InterruptedException {
        // 指派人
        String name=dao.getDesignee()!=null?dao.getDesignee():"";
        // 影响版本
        String version=dao.getVersion()!=null?dao.getVersion():"";
        // 截止日期
        String date = dao.getDeadline()!=null?dao.getDeadline():"";
        // bug类型
        String bug_Type = dao.getBugType()!=null?dao.getBugType():"";
        // bug 标题
        String title = dao.getBugTitle()!=null?dao.getBugTitle():"";
        // 重现步骤
        String content = dao.getContent()!=null?dao.getContent():"";
        // 严重程度
        String severity = dao.getSeverity()!=null?dao.getSeverity():"";
        // 优先级
        String priority = dao.getPriority()!=null?dao.getPriority():"";
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
        count = 1;
        driver.findElement(By.xpath("//*[@id=\"openedBuild_chosen\"]")).click();
        List<WebElement> elements1 = driver.findElements(By.xpath("//*[@id=\"openedBuild_chosen\"]/div/ul/li"));
        for (WebElement element:elements1){
            System.out.println(element.getText());
            System.out.println(version);
            if (element.getText().equals(version)){
                driver.findElement(By.xpath("//*[@id=\"openedBuild_chosen\"]/div/ul/li["+count+"]")).click();
                break;
            }
            count++;
        }
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
        driver.findElement(By.xpath("//*[@id=\"deadline\"]")).sendKeys(date);
        count = 1;
        List<WebElement> elements3 = driver.findElements(By.xpath("//*[@id=\"type_chosen\"]/div/ul/li"));
        for (WebElement element:elements3){
            if (element.getText().equals(bug_Type)){
                driver.findElement(By.xpath("//*[@id=\"type_chosen\"]/div/ul/li["+count+"]")).click();
                break;
            }
            count++;
        }
        driver.findElement(By.xpath("//*[@id=\"title\"]")).sendKeys(title);
        driver.switchTo().frame(0);
        WebElement text = driver.findElement(By.xpath("/html/body"));
        text.clear();
        text.sendKeys(content);
        driver.switchTo().defaultContent();
        return this;
    }
    public void submitBug() throws InterruptedException {
        driver.findElement(By.id("submit")).click();
        Thread.sleep(3000);
    }
    public ZenTao upload_Image(String imgUrl, int count) throws InterruptedException, AWTException {
//        driver.findElement(By.cssSelector("btn btn-link file-input-btn")).sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//*[@id=\"dataform\"]/table/tbody/tr[10]/td/div/div["+count+"]/div[1]/button")).sendKeys(Keys.ENTER);
        //*[@id="dataform"]/table/tbody/tr[10]/td/div/div[2]/div[1]/button
        StringSelection sel = new StringSelection(imgUrl);

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel,null);

        Robot robot = new Robot();
        Thread.sleep(2000);
        boolean systemOS = !System.getProperty("os.name").contains("Mac");
        System.out.println(systemOS);
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
            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }else{
            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_G);

            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_G);
            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            // ??? CTRL+V
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
        }


        // ?????? Enter
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        //?????????
        Thread.sleep(3000);


        return this;
    }
}
