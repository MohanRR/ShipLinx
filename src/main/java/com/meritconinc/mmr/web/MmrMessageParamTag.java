package com.meritconinc.mmr.web;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyTag;



// this is inner tag for MmrMessageTag so it 
// should be processed appropriately
public class MmrMessageParamTag extends BodyTagSupport {
	private static final long serialVersionUID	= 10092012;
	public MmrMessageParamTag() {}
	
	public int doStartTag() throws JspException {

		return BodyTag.EVAL_BODY_BUFFERED;
	}
	
	public int doAfterBody() throws JspException {
		MmrMessageTag messageTag =  
			(MmrMessageTag)findAncestorWithClass(this, MmrMessageTag.class);
		if (messageTag != null){
			messageTag.addParam(getBodyContent().getString());
			try{getBodyContent().clear();}catch(Exception _){System.out.println("IO Exception");}
		}
		return BodyTag.SKIP_BODY;
	}
}
