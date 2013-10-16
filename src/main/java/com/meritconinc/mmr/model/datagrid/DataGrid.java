package com.meritconinc.mmr.model.datagrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DataGrid implements Serializable {
	
	private static final Logger log = Logger.getLogger(DataGrid.class);	
	// page navigation params
	public static int FIRST_PAGE_NUM = 1;
	private int curPageNum;			// current page number
	private int totPageNum;			// total number of pages (depends on rowsPerPage)
	private int totRowCount;		// total number of records - NOT the same as dataRows.size()!!!
	// data row params
	private int rowsPerPage = 2;		// number of rows per page: OVERRIDE IN SUBCLASS OF DataGridAction
	private int curPageRowStartIndex;	// start row index on current page
	private int curPageRowEndIndex;		// end row index on current page

	private List dataRows;	// data row
	

	// Empty constructor - Should NEVER be called (see the other two below) !!!
	public DataGrid() {
		setInitInfo(0, rowsPerPage);
	}

	// This constructor is called every time the Action is instantiated
	public DataGrid(int rowsPerPage) {
		setCurPageNum(FIRST_PAGE_NUM);
		setRowsPerPage(rowsPerPage);

		dataRows = new ArrayList();		
	}

	// This constructor is called first time the DataGrid is used in the Action
	public DataGrid(int dataRowsCount, int rowsPerPage) {
		setInitInfo(dataRowsCount, rowsPerPage);
	}

	public void setInitInfo(int dataRowsCount, int rowsPerPage) {
		setTotRowCount(dataRowsCount);
		setCurPageNum(FIRST_PAGE_NUM);
		setRowsPerPage(rowsPerPage);
		log.debug("Total Page Number: " + getTotPageNum());
		
		double dataRowsCountDbl = dataRowsCount;
		double dataRowsPerPageDbl = getRowsPerPage();
		double totalPageNumDbl = dataRowsCountDbl / dataRowsPerPageDbl;
		
		dataRows = new ArrayList();		
	}
	
	public int getCurPageRowEndIndex() {
		curPageRowEndIndex = Math.min(curPageNum * rowsPerPage, getTotRowCount());
		log.debug("Total Row Count: " + curPageNum * rowsPerPage);
		log.debug("Current Page: " + curPageNum);
		log.debug("Rows Per Page: " + rowsPerPage);
		log.debug("Current Page End Index: " + curPageRowEndIndex);
		
		return curPageRowEndIndex;
	}
	public void setCurPageRowEndIndex(int curPageRowEndIndex) {
		this.curPageRowEndIndex = curPageRowEndIndex;
	}
	public int getCurPageNum() {
		return curPageNum;
	}
	public void setCurPageNum(int curPageNum) {
		this.curPageNum = curPageNum;
	}
	public int getCurPageRowStartIndex() {
		curPageRowStartIndex = ((curPageNum - 1)) * rowsPerPage + 1;
		log.debug("Curr Page: " + curPageNum);
		log.debug("Rows Per Page: " + rowsPerPage);
		log.debug("Current Page Start Index: " + curPageRowStartIndex);
		return curPageRowStartIndex;
	}
	public void setCurPageRowStartIndex(int curPageRowStartIndex) {
		this.curPageRowStartIndex = curPageRowStartIndex;
	}
	public List getDataRows() {
		return dataRows;
	}
	public void setDataRows(List dataRows) {
		this.dataRows = dataRows;
	}
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	public int getTotPageNum() {
		return totPageNum;
	}

	public int getTotRowCount() {
		return totRowCount;
	}

	public void setTotRowCount(int totRowCount) {
		this.totRowCount = totRowCount;
	}
	
	public void resetCurrentPage() {
		setCurPageNum(1);
		FIRST_PAGE_NUM = 1;
	}

}
