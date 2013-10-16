package com.meritconinc.shiplinx.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.meritconinc.shiplinx.dao.CreditCardTransactionDAO;
import com.meritconinc.shiplinx.model.CCTransaction;
import com.meritconinc.shiplinx.model.CreditCard;
import com.meritconinc.shiplinx.model.MerchantAccount;

public class CreditCardTransactionDAOImpl extends SqlMapClientDaoSupport implements CreditCardTransactionDAO{

	private static final Logger log = LogManager.getLogger(CreditCardTransactionDAOImpl.class);

	public CCTransaction getCCTransaction(Long id){
		return (CCTransaction)getSqlMapClientTemplate().queryForObject("findCCTransactionById", id);
	}
	
	public Long saveCCTransaction(CCTransaction trans){
		try{
			return (Long)getSqlMapClientTemplate().insert("createCCTransaction", trans);
		}catch(Exception e){
			log.debug("-----Exception-----"+e);
			e.printStackTrace();
			return null;
		}
		
	}

	public void updateCCTransaction(CCTransaction trans){
		try{
			getSqlMapClientTemplate().update("updateCCTransaction", trans);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<CCTransaction> searchCCTransactions(CCTransaction ccTran){
		return (List<CCTransaction>)getSqlMapClientTemplate().queryForList("searchCCTrans", ccTran);
	}

	public MerchantAccount getMerchantAccount(long businessId, String currency){
		Map<String, Object> paramObj = new HashMap<String, Object>(2);
		paramObj.put("businessId", businessId);
		paramObj.put("currency", currency);
		
		MerchantAccount merchantAccount =  (MerchantAccount)getSqlMapClientTemplate().queryForObject("getMerchantAccountForBusinessAndCurrency", paramObj);
		return merchantAccount;
	}
	
	public Long addCreditCard(CreditCard creditCard){
		return (Long)getSqlMapClientTemplate().insert("insertCreditCard", creditCard);
	}

	
	public void updateCreditCard(CreditCard creditCard){
		getSqlMapClientTemplate().update("updateCreditCard", creditCard);

	}

}
