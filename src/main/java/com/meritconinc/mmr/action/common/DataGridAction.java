package com.meritconinc.mmr.action.common;

import java.util.List;

import com.meritconinc.mmr.action.BaseAction;
import com.meritconinc.mmr.model.datagrid.DataGrid;
import com.meritconinc.mmr.model.datagrid.DataGridNavigation;

public abstract class DataGridAction extends BaseAction 
	implements DataGridNavigation  {

    private DataGrid dataGrid;

    public DataGridAction() {
    	setDataGridPages();
    }
    
    // overridate in subclass to set page parameters
    public abstract void setDataGridPages();
    
    // PAGE NAVIGATION: START
    public String goToPage(int pageNumber) throws Exception{
    	dataGrid.setCurPageNum(pageNumber);
    	// refresh data rows
		dataGrid.setDataRows(refreshDataRows());
    	
    	return SUCCESS;
    }

    public String goToPage() throws Exception{  
    	// refresh data rows
		dataGrid.setDataRows(refreshDataRows());
    	
    	return SUCCESS;
    }
    
	// overridate in subclass to refresh data rows
    public abstract List refreshDataRows() throws Exception; 
    // PAGE NAVIGATION: END
    
	public DataGrid getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(DataGrid dataGrid) {
		this.dataGrid = dataGrid;
	}

}