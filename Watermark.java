package com.itez.ncl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Watermark {

	/**
	 * 程式名稱：PDF加註浮水印文字
	 * 作者：Leo Liao / ITEZ Co. Ltd.
	 * 建立日期：2022-06-01
	 * 授權條款：AGPL v3.0
	 * 本程式使用 iTextPDF 開源函式庫產出PDF檔案。
	 * 啟動時需有浮水印文字的參數
	 */
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("請輸入浮水印字串為啟動參數。");
			return;
		}
		
		try {
			File dirCurr = new File("."); 
			Watermark wm = new Watermark();
			wm.start(dirCurr, args[0]);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void start(File dirNow, String strWater) throws Exception{
		List<File> listPdf = new ArrayList<>();
		for(File f: dirNow.listFiles()) {
			if(!f.isFile()) continue;
			if(!f.getName().toLowerCase().endsWith("pdf")) continue;
			
			listPdf.add(f);
		}
		if(listPdf.isEmpty()) {
			System.out.println(dirNow.getCanonicalPath() + "內查無任何Pdf檔案。");
			return;
		}
		
		com.itextpdf.text.pdf.BaseFont baseFont = com.itextpdf.text.pdf.BaseFont.createFont("c:/windows/fonts/kaiu.ttf",
				com.itextpdf.text.pdf.BaseFont.IDENTITY_H,
				com.itextpdf.text.pdf.BaseFont.EMBEDDED);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 48);
		com.itextpdf.text.BaseColor baseColor = com.itextpdf.text.BaseColor.GRAY;
		font.setColor(baseColor);
		
		for(File filePdf: listPdf) {
			//壓上浮水印後的檔案
			File fileDest = new File(filePdf.getCanonicalPath().replace(".pdf", "-w.pdf"));
			com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(filePdf.getCanonicalPath());
			com.itextpdf.text.pdf.PdfStamper stamper = new com.itextpdf.text.pdf.PdfStamper(reader, new FileOutputStream(fileDest.getCanonicalPath()));
			
			//浮水印所在頁
			com.itextpdf.text.Rectangle pageSize = reader.getPageSize(2);
			com.itextpdf.text.pdf.PdfContentByte over = stamper.getOverContent(2);
		    
			com.itextpdf.text.pdf.PdfGState gs1 = new com.itextpdf.text.pdf.PdfGState();
		    gs1.setFillOpacity(0.5f);
		    over.setGState(gs1);
		   
		    float x = pageSize.getWidth() / 2f;
		    float y = pageSize.getBottom(com.itextpdf.text.Utilities.millimetersToPoints(40)); 
		    
		    com.itextpdf.text.Phrase phrase = new com.itextpdf.text.Phrase(strWater, font);
		    com.itextpdf.text.pdf.ColumnText.showTextAligned(over,
		    		com.itextpdf.text.Element.ALIGN_CENTER,
		    		phrase,
		    		x,
		    		y,
		    		0);
		    over.saveState();
		    //
		    stamper.close();
		    reader.close();
		}
	}

}
