package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;


import javax.print.PrintService;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;

public class PrintUtils {
    /**
     * 实现PDF文件打印的工具类
     * @param printName
     * @param file
     */
    public void printPDF(String printName,File file) throws IOException, PrinterException {
        //读取pdf文件
        PDDocument document=PDDocument.load(file);
        //创建打印任务
        PrinterJob job=PrinterJob.getPrinterJob();
        //遍历所有的打印机的名称
        for (PrintService ps:PrinterJob.lookupPrintServices()){
            String psName=ps.getName();
            //选用指定打印机
            if (psName.equals(printName)){
                job.setPrintService(ps);
                break;
            }
        }
        job.setPageable(new PDFPageable(document));
        //设置纸张
        Paper paper=new Paper();
        //设置纸张大小
        paper.setSize(598,842);
        //设置打印位置，坐标
        paper.setImageableArea(0,0,paper.getWidth(),paper.getHeight());
        PageFormat pageFormat=new PageFormat();
        pageFormat.setPaper(paper);
        Book book=new Book();
        book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE),pageFormat,1);
        job.setPageable(book);
        job.print();
    }

}
