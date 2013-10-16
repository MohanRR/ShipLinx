package com.meritconinc.shiplinx.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.meritconinc.mmr.utilities.MessageUtil;
import com.meritconinc.mmr.utilities.security.UserUtil;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.PackageTypes;
import com.meritconinc.shiplinx.model.ProductLine;
import com.meritconinc.shiplinx.model.Products;
import com.meritconinc.shiplinx.model.WarehouseLocation;
import com.meritconinc.shiplinx.service.CustomerManager;
import com.meritconinc.shiplinx.service.ProductManager;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;

public class ProductManagerAction extends BaseAction implements Preparable,ServletRequestAware
{
	private static final long serialVersionUID	= 2105201224001L;
	private static final Logger log = LogManager.getLogger(CustomerManagerAction.class);
	private ProductManager productManagerService;
	private CustomerManager customerManagerService;
	public HttpServletRequest request;
	private List<Products> productList;
	
	private List<ProductLine> productLineList;
	
	private List<PackageTypes> packageTypesList;
	private Map<String, Long> productSearchResult = new HashMap<String, Long>();
	
	private Map<String, Long> productLineSearchResult = new HashMap<String, Long>();
	
	private Map<String, Long> productByProductLineSearchResult = new HashMap<String, Long>();
	
	//private Map<String, String> productPopulateResult = new HashMap<String, String>();
	
	public Map session = (Map) ActionContext.getContext().get("session");
	
	
	public String listProducts()
	{
		
		log.debug("Inside listProducts() method of ProductManagerAction");
		String strmethod = request.getParameter("method");
		try {
			if(strmethod!=null)
			{
				getSession().remove("products");
				return SUCCESS;
			}
			String strSrch = request.getParameter("searchString");
			String customerId = String.valueOf(UserUtil.getMmrUser().getCustomerId());
			log.debug("Search string is : " + strSrch);
			if(customerId==null) //don't return any addresses, this should not happen
				return SUCCESS;
					
			Products p = new Products();
			p.setProductDescription(strSrch);
			p.setCustomerId(Long.valueOf(customerId));
			
			List<Products> productsList = productManagerService.searchProducts(p,false);
					
			for(Products prods: productsList)
			{
					productSearchResult.put(prods.getProductDescription()+" - "+prods.getProductHarmonizedCode(),prods.getProductId());
			}
			
		} catch (Exception e) {
			log.debug("---------------Exception in ListProducts()..."+e);
		}
		return SUCCESS;
		
	}
	
	public String populateProductsList()
	{
		log.debug("Inside populateProductsList() method of ProductManagerAction");
		String strProductId = request.getParameter("productId");
		String strProductDesc = request.getParameter("product_desc");
		String strProductHCode = request.getParameter("product_hcode");
		String CID = String.valueOf(UserUtil.getMmrUser().getCustomerId());
		
		try {
			if(strProductId!=null && strProductId.trim().length()>0)
			{
				Products products = new Products();
				if(CID!=null)
					products.setCustomerId(Long.valueOf(CID));
				if(strProductDesc!=null)
					products.setProductDescription(strProductDesc);
				if(strProductHCode!=null)
					products.setProductHarmonizedCode(strProductHCode);
				if(strProductId!=null)
					products.setProductId(Long.valueOf(strProductId));
				
				List<Products> productList = productManagerService.searchProducts(products,true);
			
			
				for(Products prods: productList)
				{
					products.setProductHarmonizedCode(prods.getProductHarmonizedCode());
					products.setProductDescription(prods.getProductDescription());
					products.setUnitPrice(prods.getUnitPrice());
					products.setOriginCountry(prods.getOriginCountry());
				}
				this.setProducts(products);
			}
		} catch (Exception e) {
			log.debug("---------------Exception in populateProductsList()..."+e);
		}
		return SUCCESS;
	}
	
	public String newProduct()
	{
		log.debug("Inside newProduct() method of ProductManagerAction");
		try {
			getSession().remove("edit");
			getSession().remove("products");
			Products products = new Products();
			String CustomerId = String.valueOf(UserUtil.getMmrUser().getCustomerId());
			products.setCustomerId(Long.valueOf(CustomerId));
			
		} catch (Exception e) {
			log.debug("---------------Exception in newProduct()..."+e);
		}
		return SUCCESS;
	}
	
	public String deleteProduct()
	{
		log.debug("Inside deleteProduct() method of ProductManagerAction");	
				
		String strPid = request.getParameter("productId");
		try {
			Products products = getProducts();
			products.setProductId(Long.valueOf(strPid));
			
			productManagerService.deleteProducts(products);
			
			addActionMessage(getText("product.delete.successfully"));
			
		} catch (Exception e) {
			log.debug("---------------Exception in deleteProduct()..."+e);
			addActionError(getText("product.delete.failed"));
		}
		
		return getProductsList();
	}
	
	public String getProductsList()
	{
		log.debug("Inside getProductsList() method of ProductManagerAction");
		String strmethod = request.getParameter("method");	
		Products products = getProducts();
		ProductLine productLine = getProductLine();
		Customer customer= new Customer();
		try {
			
			if(request.getParameter("cid")!=null && !request.getParameter("cid").toString().equals("null"))
			{
				long lCustomerId = Long.valueOf(request.getParameter("cid").toString());
				if(lCustomerId!=0)
				{
					products.setCustomerId(lCustomerId);
					customer=customerManagerService.getCustomerInfoByCustomerId(lCustomerId);
				}
				else
				{
					products.setCustomerId(UserUtil.getMmrUser().getCustomerId());
					customer=customerManagerService.getCustomerInfoByCustomerId(UserUtil.getMmrUser().getCustomerId());
				}
				getSession().put("customerName",customer.getName());
				request.setAttribute("cust_name", customer.getName());
			}
			productLine.setProductLineId(products.getProductLineId());
			productLineList = productManagerService.getProductLineList(productLine);
			for(ProductLine pl: productLineList)
			{
				productLine.setLineName(pl.getLineName());
			}
			this.setProducts(products);
			if(strmethod==null)		
				productList = productManagerService.getProductsList(products);
			else
				getSession().remove("products");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String editProduct()
	{
		log.debug("Inside editProduct() method of ProductManagerAction");	
		getSession().put("edit", "true");
		
		String strPid = request.getParameter("productId");
		
		try 
		{
			Products products = getProducts();
			products.setProductId(Long.valueOf(strPid));
			List<Products> productList = productManagerService.searchProducts(products,true);
			WarehouseLocation warehouseLocation = new WarehouseLocation();
			
			ProductLine productLine = getProductLine();
			Customer customer = new Customer();
			
			for(Products p: productList)
			{
				products.setProductName(p.getProductName());
				products.setProductDescription(p.getProductDescription());
				products.setProductHarmonizedCode(p.getProductHarmonizedCode());
				products.setProductCode(p.getProductCode());
				products.setUnitPrice(p.getUnitPrice());
				products.setOriginCountry(p.getOriginCountry());
				products.setPrimaryLocationId(p.getPrimaryLocationId());
				productLine.setProductLineId(p.getProductLineId());
				customer = customerManagerService.getCustomerInfoByCustomerId(p.getCustomerId());
			}
			
			/*List<ProductLine> productLineList = productManagerService.getProductLineListById(productLine);
			warehouseLocation.setLocationId(products.getPrimaryLocationId());
			List<WarehouseLocation> whloclist = warehouseManagerService.getWarehouseLocationInfoByLocationId(warehouseLocation);
			if(productLineList.size()>0)
			{
				request.setAttribute("plname", productLineList.get(0).getLineName());
				request.setAttribute("plnameid", productLineList.get(0).getProductLineId());
			}
			request.setAttribute("cname", customer.getName());
			request.setAttribute("cnameid", customer.getId());
			if(whloclist.size()>0)
			{
				request.setAttribute("primaryloc", whloclist.get(0).getLocationName()+" : "+whloclist.get(0).getWarehouse().getWarehouseName());
				request.setAttribute("primarylocId", whloclist.get(0).getLocationId());
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return SUCCESS;
	}
	
	public String addOrUpdateProducts()
	{
		log.debug("Inside addOrUpdateProducts() method of ProductManagerAction");
		long lcustomerId=0;
		long lProductLineId=0;
		long lPrimaryLocId=0;
		try {
			
				if(request.getParameter("cid")!=null && !request.getParameter("cid").toString().equals("null"))
					lcustomerId = Long.valueOf(request.getParameter("cid"));
				if(request.getParameter("plid")!=null && !request.getParameter("plid").toString().equals("null"))
					lProductLineId = Long.valueOf(request.getParameter("plid"));
				if(request.getParameter("primaryloc")!=null && !request.getParameter("primaryloc").toString().equals("null"))
					lPrimaryLocId = Long.valueOf(request.getParameter("primaryloc"));
				
				
				//Edit functionality: Editing an existing product
				if(getSession().get("edit")!=null)
				{
					Products products = getProducts();
					if(lcustomerId!=0)
						products.setCustomerId(lcustomerId);
					else
						products.setCustomerId(UserUtil.getMmrUser().getCustomerId());
					//ProductLine productLine = getProductLine();
					products.setProductLineId(lProductLineId);
					products.setPrimaryLocationId(lPrimaryLocId);
					
					productManagerService.addOrUpdate(products,false); //only edit, set to false if we are not adding
					
					addActionMessage(getText("product.save.successfully"));
				}
				//Add Functionality: Adding a new product
				else
				{
					Products products = getProducts();
					if(lcustomerId!=0)
						products.setCustomerId(lcustomerId);
					else
						products.setCustomerId(UserUtil.getMmrUser().getCustomerId());
					//ProductLine productLine = getProductLine();
					products.setProductLineId(lProductLineId);
					products.setPrimaryLocationId(lPrimaryLocId);
					
					productManagerService.addOrUpdate(products,true); // set to true to add a new product
					
					addActionMessage(getText("product.create.successfully"));
					
				}
				getSession().remove("products");
				getSession().remove("edit");
			
		} catch (Exception e) {
			log.debug("---------------Exception in addOrUpdateProducts()..."+e);
			addActionError(getText("product.save.failed"));
		}
		return getProductsList();
	}
	
	public String listProductLines()
	{
		log.debug("Inside listProductLines() method of ProductManagerAction");
		try {
			String strSearch = request.getParameter("searchString");
			ProductLine productLine = getProductLine();
			productLine.setLineName(strSearch);
			
			productLineList = productManagerService.getProductLineList(productLine);
			
			for(ProductLine pl: productLineList)
			{
				productLineSearchResult.put(pl.getLineName(), pl.getProductLineId());
			}
			
		} catch (Exception e) {
			log.debug("---------------Exception in listProductLines()..."+e);
		}
		return SUCCESS;
	}
	
	public String listProductsByProductLine()
	{
		log.debug("Inside listProductsByProductLine() method of ProductManagerAction");
		long lprodlinekey=0;
		long lCustomerId = 0;
		try 
		{
			String strSearch = request.getParameter("searchString");
			lCustomerId = UserUtil.getMmrUser().getCustomerId();
			Products products = getProducts();
			products.setProductName(strSearch);
			products.setCustomerId(lCustomerId);
			
			if(request.getParameter("productLineId")!=null)
				lprodlinekey = Long.valueOf(request.getParameter("productLineId")+"");
			products.setProductLineId(lprodlinekey);
			
			productList = productManagerService.getProductsByProductLineId(products);
			for(Products p: productList)
			{
				productByProductLineSearchResult.put(p.getProductName(), p.getProductId());
			}
		} 
		catch (Exception e) 
		{
			log.debug("---------------Exception in listProductsByProductLine()..."+e);
		}
		return SUCCESS;
		
	}
	
	public String showProductLine()
	{
		log.debug("Inside showProductLine() method of ProductManagerAction");
		getSession().remove("edit");
		getSession().remove("productline");
		String strRole = UserUtil.getMmrUser().getUserRole();
		ProductLine productLine = getProductLine();
		Customer customer = new Customer();
		try {
			
			if(strRole.equals("busadmin"))
			{
				if(request.getParameter("cid")!=null)
				{
					long lCustomerId = Long.valueOf(request.getParameter("cid").toString());
					productLine.setCustomerId(lCustomerId);
					customer = customerManagerService.getCustomerInfoByCustomerId(lCustomerId);
					getSession().put("customerName",customer.getName());
				}
			}
			else
			{
				long lCustomerId = UserUtil.getMmrUser().getCustomerId();
				productLine.setCustomerId(lCustomerId);
				customer = customerManagerService.getCustomerInfoByCustomerId(lCustomerId);
				getSession().put("customerName",customer.getName());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}				
		
		productLineList = productManagerService.getProductLineList(productLine);
		
		return SUCCESS;
	}
	
	public String showEditProductLines()
	{
		log.debug("Inside showEditProductLines() method of ProductManagerAction");
		try {
			getSession().put("edit", "true");
			
			long lProductLineId = Long.valueOf(request.getParameter("productLineId"));
			ProductLine productLine = getProductLine();
			productLine.setProductLineId(lProductLineId);
			
			List<ProductLine> plist = productManagerService.getProductLineListById(productLine);
			
			for(ProductLine pl: plist)
			{
				productLine.setLineCode(pl.getLineCode());
				productLine.setLineName(pl.getLineName());
				productLine.setLineDescription(pl.getLineDescription());
			}
			
		} catch (Exception e) {
			log.debug("---------------Exception in showEditProductLines()..."+e);
		}
		return SUCCESS;
	}
	
	public String editProductLines()
	{
		log.debug("Inside editProductLines() method of ProductManagerAction");
		try {
			boolean boolEdit = Boolean.valueOf(request.getParameter("edit"));
			String strRole = UserUtil.getMmrUser().getUserRole();
			ProductLine productLine = getProductLine();
			//reset
			//resetProductLine(productLine);
			if(boolEdit) // Edit existing productLine
			{
				long lProductLineId = Long.valueOf(request.getParameter("productLineId"));
				
				productLine.setProductLineId(lProductLineId);
			}
			
			if(strRole.equals("busadmin"))
			{
				if(request.getParameter("cid")!=null)
				{
					long lCustomerId = Long.valueOf(request.getParameter("cid").toString());
					productLine.setCustomerId(lCustomerId);
				}
			}
			else
			{
				long lCustomerId = UserUtil.getMmrUser().getCustomerId();
				productLine.setCustomerId(lCustomerId);
			}
			
			productManagerService.addOrUpdateProductLine(productLine, boolEdit);
			
			getSession().remove("edit");
			getSession().remove("productline");
			if(boolEdit)
				addActionMessage(getText("productLine.save.successfully"));
			else
				addActionMessage(getText("productLine.create.successfully"));
			
		} catch (Exception e) {
			log.debug("---------------Exception in editProductLines()..."+e);
			addActionError(getText("productLine.save.failed"));
		}
		return getProductLinesList();
	}
	
	public String deleteProductLines()
	{
		log.debug("Inside deleteProductLines() method of ProductManagerAction");
		try {
			String strRole = UserUtil.getMmrUser().getUserRole();
			ProductLine productLine = getProductLine();
			
			long lProductLineId = Long.valueOf(request.getParameter("productLineId"));
			productLine.setProductLineId(lProductLineId);
			
			if(strRole.equals("busadmin"))
			{
				if(request.getParameter("cid")!=null)
				{
					long lCustomerId = Long.valueOf(request.getParameter("cid").toString());
					productLine.setCustomerId(lCustomerId);
				}
			}
			else
			{
				long lCustomerId = UserUtil.getMmrUser().getCustomerId();
				productLine.setCustomerId(lCustomerId);			
			}		
			
			productManagerService.deleteProductLine(productLine);
			
			addActionMessage(getText("productLine.deleted.successfully"));
		} catch (Exception e) {
			log.debug("---------------Exception in deleteProductLines()..."+e);
			addActionError(getText("productLine.deleted.failed"));
		}
		 
		return getProductLinesList();
	}
	
	public String getProductLinesList()
	{
		log.debug("Inside getProductsList() method of ProductManagerAction");
		String strmethod = request.getParameter("method");	
		ProductLine productLine = getProductLine();
		Customer customer= new Customer();
		try {
			
			if(request.getParameter("cid")!=null)
			{
				long lCustomerId = Long.valueOf(request.getParameter("cid").toString());
				productLine.setCustomerId(lCustomerId);
				customer=customerManagerService.getCustomerInfoByCustomerId(lCustomerId);
				getSession().put("customerName",customer.getName());
			}
			this.setProductLine(productLine);
			if(strmethod==null)		
				productLineList = productManagerService.getProductLineList(productLine);
			else
				getSession().remove("productline");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String listPackages()
	{
		
		log.debug("Inside listPackages() method of ProductManagerAction");
		String strmethod = request.getParameter("method");
		try {
			
			String customerId = String.valueOf(UserUtil.getMmrUser().getCustomerId());
			
			if(customerId==null) //don't return any addresses, this should not happen
				return SUCCESS;
					
			PackageTypes pt = new PackageTypes();
			pt.setCustomerId(Long.valueOf(customerId));
			
			packageTypesList = productManagerService.searchPackageTypes(pt);
					
			
		} catch (Exception e) {
			log.debug("---------------Exception in listPackages()..."+e);
		}
		return SUCCESS;
		
	}
	
	public String populatePackageTypes()
	{
		log.debug("Inside populatePackageTypes() method of ProductManagerAction");
		
		String strIndex = request.getParameter("index");
		String customerId = String.valueOf(UserUtil.getMmrUser().getCustomerId());
		
		PackageTypes pt = new PackageTypes();
		pt.setCustomerId(Long.valueOf(customerId));
		
		packageTypesList = productManagerService.searchPackageTypes(pt);
		PackageTypes packageTypes = getPackageTypes();
		for(PackageTypes ptypes : packageTypesList)
		{
			if(ptypes.getPackageTypeId()==Long.valueOf(strIndex))
			{
				packageTypes.setPackageDesc(ptypes.getPackageDesc());
				packageTypes.setPackageLength(ptypes.getPackageLength());
				packageTypes.setPackageWidth(ptypes.getPackageWidth());
				packageTypes.setPackageHeight(ptypes.getPackageHeight());
				packageTypes.setPackageWeight(ptypes.getPackageWeight());
			}
		}
		return SUCCESS;
	}
	
	public String goToAddNewPackage()
	{
		log.debug("Inside goToAddNewPackage() method of ProductManagerAction");
		getSession().remove("packageTypes");
		getSession().remove("edit");
		return SUCCESS;
	}
	
	public String editPackageType()
	{
		log.debug("Inside editPackageType() method of ProductManagerAction");
		getSession().put("edit", true);
		
		String packageTypeId = request.getParameter("pid");
		PackageTypes packageTypes = getPackageTypes();
		packageTypesList = productManagerService.fetchAPackageById(Long.valueOf(packageTypeId));
		for(PackageTypes pt : packageTypesList)
		{
			packageTypes.setPackageTypeId(pt.getPackageTypeId());
			packageTypes.setPackageName(pt.getPackageName());
			packageTypes.setPackageDesc(pt.getPackageDesc());
			packageTypes.setPackageLength(pt.getPackageLength());
			packageTypes.setPackageWidth(pt.getPackageWidth());
			packageTypes.setPackageHeight(pt.getPackageHeight());
			packageTypes.setPackageWeight(pt.getPackageWeight());
		}
		setPackageTypes(packageTypes);
		return SUCCESS;
	}
	
	public String deletePackageType()
	{
		log.debug("Inside deletePackageType() method of ProductManagerAction");
		boolean deleted = false;
		String packageTypeId = request.getParameter("pid");
		deleted = productManagerService.deletePackageType(Long.valueOf(packageTypeId));
		if(deleted)
			addActionMessage(MessageUtil.getMessage("packagetype.delete.successfully"));
		
		return listPackages();
	}
	
	public String addOrUpdatePackageTypes()
	{
		log.debug("Inside addOrUpdatePackageTypes() method of ProductManagerAction");
		try {
				PackageTypes packageTypes = getPackageTypes();
				packageTypes.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			
				//Edit functionality: Editing an existing pacakge type
				if(request.getParameter("edit")!=null)
				{
					productManagerService.addOrUpdatePackageTypes(packageTypes,false); //only edit, set to false if we are not adding
					
					addActionMessage(MessageUtil.getMessage("packagetype.save.successfully"));
				}
				//Add Functionality: Adding a new package type
				else
				{
					productManagerService.addOrUpdatePackageTypes(packageTypes,true); // set to true to add a new product
					
					addActionMessage(MessageUtil.getMessage("packagetype.create.successfully"));
					getSession().remove("packageTypes");
					getSession().remove("edit");
				}
			
		} catch (Exception e) {
			log.debug("---------------Exception in addOrUpdateProducts()..."+e);
			addActionError(getText("product.save.failed"));
		}
		return listPackages();
	}
		
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ProductManager getProductManagerService() {
		return productManagerService;
	}

	public void setProductManagerService(ProductManager productManagerService) {
		this.productManagerService = productManagerService;
	}

	public Map<String, Long> getProductSearchResult() {
		return productSearchResult;
	}

	public void setProductSearchResult(Map<String, Long> productSearchResult) {
		this.productSearchResult = productSearchResult;
	}
	
	public Products getProducts() {
		Products product = (Products)getSession().get("products");
		if (product == null) {
			product = new Products();
			product.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			setProducts(product);
		}
		return product;
	}
	
	public ProductLine getProductLine()
	{
		ProductLine productLine = (ProductLine) getSession().get("productline");
		if(productLine == null)
		{
			productLine = new ProductLine();
			productLine.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			setProductLine(productLine);
		}
		return productLine;
	}
	
	public void setProducts(Products products) 
	{
		getSession().put("products", products);
	}
	
	public void setProductLine(ProductLine productLine) 
	{
		getSession().put("productline", productLine);
	}
	
	
	public PackageTypes getPackageTypes()
	{
		PackageTypes packageTypes = (PackageTypes) getSession().get("packageTypes");
		if(packageTypes == null)
		{
			packageTypes = new PackageTypes();
			packageTypes.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			setPackageTypes(packageTypes);
		}
		return packageTypes;
	}
	
	public void setPackageTypes(PackageTypes packageTypes) 
	{
		getSession().put("packageTypes", packageTypes);
	}
			
	
	public List<Products> getProductList()
	{
		return productList;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public List<ProductLine> getProductLineList() {
		return productLineList;
	}

	public void setProductLineList(List<ProductLine> productLineList) {
		this.productLineList = productLineList;
	}

	public CustomerManager getCustomerManagerService() {
		return customerManagerService;
	}

	public void setCustomerManagerService(CustomerManager customerManagerService) {
		this.customerManagerService = customerManagerService;
	}

	public Map<String, Long> getProductLineSearchResult() {
		return productLineSearchResult;
	}

	public void setProductLineSearchResult(Map<String, Long> productLineSearchResult) {
		this.productLineSearchResult = productLineSearchResult;
	}

	public Map<String, Long> getProductByProductLineSearchResult() {
		return productByProductLineSearchResult;
	}

	public void setProductByProductLineSearchResult(
			Map<String, Long> productByProductLineSearchResult) {
		this.productByProductLineSearchResult = productByProductLineSearchResult;
	}

	public List<PackageTypes> getPackageTypesList() {
		return packageTypesList;
	}

	public void setPackageTypesList(List<PackageTypes> packageTypesList) {
		this.packageTypesList = packageTypesList;
	}

}