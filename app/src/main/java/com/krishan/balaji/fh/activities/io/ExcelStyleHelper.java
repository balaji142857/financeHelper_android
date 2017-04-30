package com.krishan.balaji.fh.activities.io;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelStyleHelper {
    CellStyle headingCenterCell, headingLeftCell, headingRightCell;
    CellStyle oddCenterCell, oddLeftCell, oddRightCell;
    CellStyle evenCenterCell, evenLeftCell, evenRightCell;
    static ExcelStyleHelper _this;
    static String emptyString="";
    int columnCount;
    public static ExcelStyleHelper getInstance(Sheet sheet, Class klass){
        _this = new ExcelStyleHelper();
        _this.columnCount=klass.getDeclaredFields().length;
        _this.headingLeftCell = sheet.getRow(0).getCell(0).getCellStyle();
        _this.headingCenterCell = sheet.getRow(0).getCell(1).getCellStyle();
        _this.headingRightCell = sheet.getRow(0).getCell(2).getCellStyle();
        _this.oddLeftCell = sheet.getRow(1).getCell(0).getCellStyle();
        _this.oddCenterCell = sheet.getRow(1).getCell(1).getCellStyle();
        _this.oddRightCell = sheet.getRow(1).getCell(2).getCellStyle();
        _this.evenLeftCell = sheet.getRow(2).getCell(0).getCellStyle();
        _this.evenCenterCell = sheet.getRow(2).getCell(1).getCellStyle();
        _this.evenRightCell = sheet.getRow(2).getCell(2).getCellStyle();
        return _this;
    }

    private ExcelStyleHelper(){}

    public Cell createCell(Sheet sheet,int rowPosition,int columnPosition, Object cellData){
        Cell cell = null;
        Row row;
        if(sheet!=null){
            row = sheet.getRow(rowPosition);
            if(row == null)
                row = sheet.createRow(rowPosition);
            cell = row.getCell(columnPosition);
            if(cell == null)
                cell = row.createCell(columnPosition);
            if(rowPosition == 1){
                if(columnPosition==1)
                    cell.setCellStyle(_this.headingLeftCell);
                else if(columnPosition == _this.columnCount-1)
                    cell.setCellStyle(_this.headingRightCell);
                else cell.setCellStyle(_this.headingCenterCell);
            }else if(rowPosition%2==0){
                if(columnPosition==1)
                    cell.setCellStyle(_this.evenLeftCell);
                else if(columnPosition == _this.columnCount-1)
                    cell.setCellStyle(_this.evenRightCell);
                else cell.setCellStyle(_this.evenCenterCell);
            }else{
                if(columnPosition==1)
                    cell.setCellStyle(_this.oddLeftCell);
                else if(columnPosition == _this.columnCount-1)
                    cell.setCellStyle(_this.oddRightCell);
                else cell.setCellStyle(_this.oddCenterCell);
            }

            if(cellData!=null){
                if(cellData instanceof String){
                    cell.setCellValue(cellData+emptyString);
                }else if(cellData instanceof Double){
                    cell.setCellValue((Double)cellData);
                }
                else if(cellData instanceof Float){
                    cell.setCellValue((Float)cellData);
                }
                else if(cellData instanceof Integer){
                    cell.setCellValue((Integer)cellData);
                }
                else if(cellData instanceof Date){
                    cell.setCellValue((Date)cellData);
                }
                else
                    throw new RuntimeException("Format not accepted, "+cellData.getClass());
            }
        }
        return cell;
    }

    /*public void autoSizeColumns(Sheet sheet) {
        for( int i=1; i<=columnCount;i++)
            sheet.autoSizeColumn(i);
    }*/
}