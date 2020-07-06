import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class loadExcel {
    List<excelDao> excelDaoList = null;
    InputStream is = null;
    Workbook excel = null;
    OutputStream out = null;
    String path;
    public loadExcel(String path) throws IOException, InvalidFormatException {
        this.path = path;
        this.excelDaoList =  new ArrayList<excelDao>();;
        this.is = new FileInputStream(new File(path));;
        this.excel = WorkbookFactory.create(is);
        is.close();
    }

    public  config readConfig() throws IOException, InvalidFormatException {
        config config = new config();
        // ???????????
        Sheet sheet = excel.getSheetAt(0);
        Row row = sheet.getRow(1);
        config.setUrl(row.getCell(1).getStringCellValue());
        row = sheet.getRow(2);
        config.setName(row.getCell(1).getStringCellValue());
        row = sheet.getRow(3);
        config.setPassword(row.getCell(1).getStringCellValue());
        sheet = excel.getSheetAt(1);
        row = sheet.getRow(1);
        config.setLoginUrl(row.getCell(1).getStringCellValue());
        row = sheet.getRow(2);
        config.setMyUrl(row.getCell(1).getStringCellValue());
        row = sheet.getRow(3);
        config.setProductUrl(row.getCell(1).getStringCellValue());
        row = sheet.getRow(4);
        config.setProjectUrl(row.getCell(1).getStringCellValue());
        row = sheet.getRow(5);
        config.setTestUrl(row.getCell(1).getStringCellValue());
        return config;
    };

    public  int getRowMax(Sheet sheet){
        int count = 5;
        Row row  = null;
        for (int rowNum =5;rowNum<sheet.getLastRowNum();rowNum++){
            row = sheet.getRow(rowNum);
            if(row.getCell(0)!=null){
                try {
                    count = 5+Integer.parseInt(row.getCell(0).getStringCellValue());
                }catch (Exception e){
                    break;
                }
            }
        }
        return count;
    }

    public  List<excelDao> readXlSX() throws Exception {
        // ???????????
            Sheet sheet = excel.getSheetAt(0);
            //??????????
            for (int rowNum = 5; rowNum < getRowMax(sheet); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                excelDao stu = new excelDao();
                Cell id = row.getCell(0);
                stu.setId(id.getStringCellValue());
                Cell exampleId = row.getCell(1);
                stu.setExampleId(exampleId.getStringCellValue());
                Cell bugTitle = row.getCell(2);
                stu.setBugTitle(bugTitle.getStringCellValue());
                Cell content = row.getCell(3);
                stu.setContent(content.getStringCellValue());
                Cell bugImgUrl = row.getCell(4);
                stu.setBugImgUrl(bugImgUrl.getStringCellValue());
                Cell version = row.getCell(5);
                stu.setVersion(version.getStringCellValue());
                Cell designee = row.getCell(6);
                stu.setDesignee(designee.getStringCellValue());
                Cell bugType = row.getCell(7);
                stu.setBugType(bugType.getStringCellValue());
                Cell deadline = row.getCell(8);
                stu.setDeadline(deadline.getStringCellValue());
                Cell severity = row.getCell(9);
                stu.setSeverity(severity.getStringCellValue());
                Cell priority = row.getCell(10);
                stu.setPriority(priority.getStringCellValue());
                Cell upload = row.getCell(11);
                stu.setUpload(upload.getBooleanCellValue());
                excelDaoList.add(stu);
            }
        return excelDaoList;
    }
    public  void setUpLoad(String id) throws IOException {
        // ???????????
        Sheet sheet = excel.getSheetAt(0);
        //??????????
        for (int rowNum = 5; rowNum < getRowMax(sheet); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            if (id.equals(row.getCell(0).getStringCellValue())){
                row.getCell(11).setCellValue(true);
                break;
            }
        }
        FileOutputStream excelFileOutPutStream = new FileOutputStream(path);//????????????????
        excel.write(excelFileOutPutStream);
        excelFileOutPutStream.flush();
    }
}
