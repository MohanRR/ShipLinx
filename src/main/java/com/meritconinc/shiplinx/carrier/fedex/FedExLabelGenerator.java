package com.meritconinc.shiplinx.carrier.fedex;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BarcodePDF417;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.meritconinc.shiplinx.carrier.utils.GenericLabelGenerator;
import com.meritconinc.shiplinx.dao.ShippingDAO;
import com.meritconinc.shiplinx.model.ShippingLabel;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.utils.PDFRenderer;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class FedExLabelGenerator extends GenericLabelGenerator {
	private static Logger logger = Logger.getLogger(FedExLabelGenerator.class);
	
	private ShippingDAO shippingDAO;
	
	public void setShippingDAO(ShippingDAO dao) {
		this.shippingDAO = dao;
	}
	
	
	
	public FedExLabelGenerator(){
		setUp();
	}
	
	public void setUp() {
		this.labelHeader = "PACKAGE LABEL : FEDEX";
	}
	
	public void generateShippingLabel(ShippingOrder order, OutputStream outputStream) {
		
//		this.generateShippingLabelPNG(order, outputStream);
		this.generateShippingLabel(order, outputStream, null, null);
		
	}	

	
	private void append(Chunk c, String s){
		if(s!=null && s.length()>0)
			c.append(s.toUpperCase() + "\n");
	}
	
	private String formatTrackNum(ShippingOrder order, String trackingNum){
		
		StringBuffer stb = new StringBuffer();
		if(order.getServiceId()==3){//ground
			stb.append("(" + trackingNum.substring(0,7) + ")\t" + trackingNum.substring(7,14) + " " + trackingNum.substring(14,trackingNum.length()));
			return stb.toString();
		}
		
		if(trackingNum==null || trackingNum.length()==0)
			return "";
		
		stb.append(trackingNum.substring(0,4));
		stb.append(" ");
		stb.append(trackingNum.substring(4,8));
		stb.append(" ");
		stb.append(trackingNum.substring(8,12));	
		
		return stb.toString();
	}

	
	public void generateShippingLabelPDF(ShippingOrder order, OutputStream outputStream){
		try {
			List<ShippingLabel> shippingLabels = shippingDAO.getLabelsByOrderId(order.getId().longValue());
			
      
			int page = 1;
			PDFRenderer pdfRenderer = new PDFRenderer();
			java.util.ArrayList srcList = new ArrayList();
			for(ShippingLabel label:shippingLabels) {
				
				String pdfFileName = pdfRenderer.getUniqueTempPDFFileName("label" + order.getOrderNum() + page);
				File f = new File(pdfFileName);
				BufferedOutputStream sbos = new BufferedOutputStream(new FileOutputStream(f));
				sbos.write(label.getLabel());
				sbos.close();
				srcList.add(pdfFileName);		
				page++;
			}
			// delete temp files
			pdfRenderer.concatPDF(srcList, outputStream);				
			pdfRenderer.deleteFiles(srcList);

		}catch(Exception e) {
			logger.error("Fedex label error", e);
//			e.printStackTrace();
			logger.error(e);			
		}
	}

	public void generateShippingLabelPNG2PDF(ShippingOrder order, OutputStream outputStream, Document document, PdfWriter writer){
		Rectangle pdfRect = new Rectangle(4*72,6*72);
	    boolean isDocNull = false;
	    document=null;
	    writer=null;
	    if(document==null){
	    	isDocNull = true;
	    	document = new Document(pdfRect, 0, 0, 0, 0);
	    }
	    
	    
		try {
	        // step 2: creation of the writer
	        if(writer==null){
	        	writer = PdfWriter.getInstance(document, outputStream);
	            // step 3: we open the document
	            document.open();
	        }
			List<ShippingLabel> shippingLabels = shippingDAO.getLabelsByOrderId(order.getId().longValue());
			
			int page = 1;
			for(ShippingLabel label:shippingLabels) {
				if(page > 1) {
					document.newPage();
				}	
				
				Image png = Image.getInstance(label.getLabel());
				logger.debug("HEIGHT, WIDTH " + png.getHeight() + " " + png.getWidth());
				//png.scaleAbsolute(4*72, 6*72);
				png.scaleToFit(4*72,6*72);
				document.add(png);		
				page++;
				
				if(label.getCodLabel()!=null){
					png = Image.getInstance(label.getCodLabel());
					logger.debug("HEIGHT, WIDTH " + png.getHeight() + " " + png.getWidth());
					//png.scaleAbsolute(4*72, 6*72);
					png.scaleToFit(4*72,6*72);
					document.add(png);			
					page++;

				}
				
			}
	        if(isDocNull){
	        	document.close();
	        }			
		}catch(Exception e) {
			logger.error("Fedex label error", e);
//			e.printStackTrace();
			logger.error(e);
		}
	}


	public void generateShippingLabel(ShippingOrder order, OutputStream outputStream, Document document, PdfWriter writer) {
		
		//for reverse compatibility, check if label was created using a thermal response.
		List<ShippingLabel> shippingLabels = shippingDAO.getLabelsByOrderId(order.getId().longValue());
		ShippingLabel label = shippingLabels.get(0);		
		if(label.getLabel()!=null){
			if(label.getLabelType()==ShiplinxConstants.LABEL_TYPE_PNG_STRING){
				logger.info("Label type is PNG, converting to PDF");
				generateShippingLabelPNG2PDF(order, outputStream, document, writer);
				return;
			}
			else {
				logger.info("Label type is PDF");
				generateShippingLabelPDF(order, outputStream);
				return;
			}
			
			
		}
		
		Rectangle pdfRect = new Rectangle(4*72,6*72);
        boolean isDocNull = false;
        if(document==null){
        	isDocNull = true;
        	document = new Document(pdfRect, 10, 5, 10, 5);
        }
        try {            
            // step 2: creation of the writer
            if(writer==null){
            	writer = PdfWriter.getInstance(document, outputStream);
                // step 3: we open the document
                document.open();
            }

        	if(isDocNull){
        		document.close();
        	}   	
        }
        catch (Exception de) {
			logger.error("Fedex label error",de);
//			de.printStackTrace();
        }     
    
	}
	
	
	
	
}
