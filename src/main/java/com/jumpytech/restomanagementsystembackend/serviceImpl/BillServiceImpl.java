package com.jumpytech.restomanagementsystembackend.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jumpytech.restomanagementsystembackend.JWT.JwtFilter;
import com.jumpytech.restomanagementsystembackend.POJO.Bill;
import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.dao.BillDao;
import com.jumpytech.restomanagementsystembackend.service.BillService;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class BillServiceImpl implements BillService {
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    BillDao billDao;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        try{
            String fileName;
            if(validateRequesMap(requestMap)){
                if(requestMap.containsKey("isGenerate")&&!(Boolean)requestMap.get("isGenerate")){
                    fileName=(String) requestMap.get("uuid");
                }else {
                    fileName=RestoUtilis.getUUID();
                    requestMap.put("uuid",fileName);
                    insertBill(requestMap);
                }
                String data="Name: "+requestMap.get("name")+"\n" +"Contact Number: "+requestMap.get("contactNumber")+
                        "\n"+"Email :"+requestMap.get("email")+"\n"+"Payment Method :"+requestMap.get("paymentMethod");
                Document document=new Document();
                PdfWriter.getInstance(document,new FileOutputStream(RestoConstants.STORE_LOCATION+"\\"+fileName+".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph chunk= new Paragraph("Resto Management System",getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph=new Paragraph(data+"\n \n",getFont( "Data"));
                document.add(paragraph);

                PdfPTable table=new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);


                JSONArray jsonArray=RestoUtilis.getJsonArrayFromSting((String) requestMap.get("productDetails"));
                for(int i=0;i<jsonArray.length();i++){
                    addRows(table,RestoUtilis.getMapFromJson(jsonArray.getString(i)));

                }
                document.add(table);
                Paragraph footer =new Paragraph("Total :"+requestMap.get("totalAmount")+"\n"
                +"Thank you for Visiting. Please visit again",getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\""+fileName+ "\"}",HttpStatus.OK);
            }
            return RestoUtilis.getResponseEnity("Required data not Found",HttpStatus.BAD_REQUEST);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
            List<Bill> list=new ArrayList<>();
            if(jwtFilter.isAdmin()){
                list=billDao.getBills();
            }else{
                list=billDao.getBillByUserName(jwtFilter.getCurrentUser());
            }
            return new ResponseEntity<>(list,HttpStatus.OK);

    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Inside getPdf : requestMap {}",requestMap);
        try {
            byte[] byteArray=new byte[0];
            if(!requestMap.containsKey("uuid")&&validateRequesMap(requestMap))
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            String filePath=RestoConstants.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
            if(RestoUtilis.isFileExist(filePath)){
                byteArray=getByteArray(filePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }else{
                requestMap.put("isGenerate",false);
                generateReport(requestMap);
                byteArray=getByteArray(filePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try{
            Optional optional=billDao.findById(id);
            if(!optional.isEmpty()){
                billDao.deleteById(id);
                return RestoUtilis.getResponseEnity("Bill deleted Successfully!",HttpStatus.OK);
            }
            return RestoUtilis.getResponseEnity("Bill id does not Exist",HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws IOException {
        File initialFile=new File(filePath);
        InputStream targetStream=new FileInputStream(initialFile);
        byte[] byteArray= IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }


    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String)data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell((Double.toString((Double) data.get("totalAmount"))) );
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTable Header ");
        Stream.of("Name","Category","Quantity","Price","Sub Total")
                .forEach(columnTitle->{
                    PdfPCell header=new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.ORANGE);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont=FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();

        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside The SetRectangleInPdf");
        Rectangle rectangle=new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try{
            Bill bill=new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String)requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod( (String) requestMap.get("paymentMethod"));
            bill.setTotal( Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateRequesMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name")&&
                requestMap.containsKey("contactNumber")&&
                requestMap.containsKey("email")&&
                requestMap.containsKey("paymentMethod")&&
                requestMap.containsKey("productDetails")&&
                requestMap.containsKey("totalAmount");
    }
}
