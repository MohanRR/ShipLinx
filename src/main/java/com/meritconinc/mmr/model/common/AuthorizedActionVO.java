package com.meritconinc.mmr.model.common;

import java.io.Serializable;

/**
 * @author brinzf2
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AuthorizedActionVO implements Serializable {
	private static final long serialVersionUID 		= 9172006;

	private String actionKey=null;
	private String description=null;
	private int menuId;
	private boolean highlight;
	private String parentAction=null;
	private boolean reloadSafe;
	private String namespace=null;
	private String parentNamespace=null;
	
	/**
	 * 
	 */
	public AuthorizedActionVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	public AuthorizedActionVO(String actionKey) {
		super();
		this.actionKey = actionKey;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public String getActionKey() {
		return actionKey;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param string
	 */
	public void setActionKey(String string) {
		actionKey = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(actionKey==null||obj==null) return false;
		return actionKey.equalsIgnoreCase(((AuthorizedActionVO)obj).actionKey);
	}
 
	 /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		// TODO Auto-generated method stub
		return actionKey.hashCode();
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the highlight
	 */
	public boolean isHighlight() {
		return highlight;
	}

	/**
	 * @param highlight the highlight to set
	 */
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	/**
	 * @return the parentAction
	 */
	public String getParentAction() {
		return parentAction;
	}

	/**
	 * @param parentAction the parentAction to set
	 */
	public void setParentAction(String parentAction) {
		this.parentAction = parentAction;
	}

	public boolean isReloadSafe() {
		return reloadSafe;
	}

	public void setReloadSafe(boolean reloadSafe) {
		this.reloadSafe = reloadSafe;
	}

	public String getParentNamespace() {
		return parentNamespace;
	}

	public void setParentNamespace(String parentNamespace) {
		this.parentNamespace = parentNamespace;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}


}