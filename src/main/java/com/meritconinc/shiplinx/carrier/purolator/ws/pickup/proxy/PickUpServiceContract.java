package com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

/**
 * Service Contract Class - PickUpServiceContract
 *
 * This class was generated by Apache CXF 2.5.2
 * 2012-07-13T18:43:59.904-04:00
 * Generated source version: 2.5.2
 * 
 */
@WebService(targetNamespace = "http://purolator.com/pws/service/v1", name = "PickUpServiceContract")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PickUpServiceContract {

    /**
     * GetPickUpHistory
     * @param request GetPickUpHistoryRequest
     * @return GetPickUpHistoryResponse
     */
    @WebResult(name = "GetPickUpHistoryResponse", targetNamespace = "http://purolator.com/pws/datatypes/v1", partName = "GetPickUpHistoryResponse")
    @Action(input = "http://purolator.com/pws/service/v1/GetPickUpHistory", output = "http://purolator.com/pws/service/v1/PickUpServiceContract/GetPickUpHistoryResponse", fault = {@FaultAction(className = PickUpServiceContractGetPickUpHistoryValidationFaultFaultFaultMessage.class, value = "http://purolator.com/pws/service/v1/PickUpServiceContract/GetPickUpHistoryValidationFaultFault")})
    @WebMethod(operationName = "GetPickUpHistory", action = "http://purolator.com/pws/service/v1/GetPickUpHistory")
    public GetPickUpHistoryResponseContainer getPickUpHistory(
        @WebParam(partName = "GetPickUpHistoryRequest", name = "GetPickUpHistoryRequest", targetNamespace = "http://purolator.com/pws/datatypes/v1")
        GetPickUpHistoryRequestContainer getPickUpHistoryRequest
    ) throws PickUpServiceContractGetPickUpHistoryValidationFaultFaultFaultMessage;

    /**
     * ModifyPickUp
     * @param request ModifyPickUpRequest
     * @return ModifyPickUpResponse
     */
    @WebResult(name = "ModifyPickUpResponse", targetNamespace = "http://purolator.com/pws/datatypes/v1", partName = "ModifyPickUpResponse")
    @Action(input = "http://purolator.com/pws/service/v1/ModifyPickUp", output = "http://purolator.com/pws/service/v1/PickUpServiceContract/ModifyPickUpResponse", fault = {@FaultAction(className = PickUpServiceContractModifyPickUpValidationFaultFaultFaultMessage.class, value = "http://purolator.com/pws/service/v1/PickUpServiceContract/ModifyPickUpValidationFaultFault")})
    @WebMethod(operationName = "ModifyPickUp", action = "http://purolator.com/pws/service/v1/ModifyPickUp")
    public ModifyPickUpResponseContainer modifyPickUp(
        @WebParam(partName = "ModifyPickUpRequest", name = "ModifyPickUpRequest", targetNamespace = "http://purolator.com/pws/datatypes/v1")
        ModifyPickUpRequestContainer modifyPickUpRequest
    ) throws PickUpServiceContractModifyPickUpValidationFaultFaultFaultMessage;

    /**
     * VoidPickUp
     * @param request VoidPickUpRequest
     * @return VoidPickUpResponse
     */
    @WebResult(name = "VoidPickUpResponse", targetNamespace = "http://purolator.com/pws/datatypes/v1", partName = "VoidPickUpResponse")
    @Action(input = "http://purolator.com/pws/service/v1/VoidPickUp", output = "http://purolator.com/pws/service/v1/PickUpServiceContract/VoidPickUpResponse", fault = {@FaultAction(className = PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage.class, value = "http://purolator.com/pws/service/v1/PickUpServiceContract/VoidPickUpValidationFaultFault")})
    @WebMethod(operationName = "VoidPickUp", action = "http://purolator.com/pws/service/v1/VoidPickUp")
    public VoidPickUpResponseContainer voidPickUp(
        @WebParam(partName = "VoidPickUpRequest", name = "VoidPickUpRequest", targetNamespace = "http://purolator.com/pws/datatypes/v1")
        VoidPickUpRequestContainer voidPickUpRequest
    ) throws PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage;

    /**
     * ValidatePickUp
     * @param request ValidatePickUpRequest
     * @return ValidatePickUpResponse
     */
    @WebResult(name = "ValidatePickUpResponse", targetNamespace = "http://purolator.com/pws/datatypes/v1", partName = "ValidatePickUpResponse")
    @Action(input = "http://purolator.com/pws/service/v1/ValidatePickUp", output = "http://purolator.com/pws/service/v1/PickUpServiceContract/ValidatePickUpResponse", fault = {@FaultAction(className = PickUpServiceContractValidatePickUpValidationFaultFaultFaultMessage.class, value = "http://purolator.com/pws/service/v1/PickUpServiceContract/ValidatePickUpValidationFaultFault")})
    @WebMethod(operationName = "ValidatePickUp", action = "http://purolator.com/pws/service/v1/ValidatePickUp")
    public ValidatePickUpResponseContainer validatePickUp(
        @WebParam(partName = "ValidatePickUpRequest", name = "ValidatePickUpRequest", targetNamespace = "http://purolator.com/pws/datatypes/v1")
        ValidatePickUpRequestContainer validatePickUpRequest
    ) throws PickUpServiceContractValidatePickUpValidationFaultFaultFaultMessage;

    /**
     * SchedulePickUp
     * @param request SchedulePickUpRequest
     * @return SchedulePickUpResponse
     */
    @WebResult(name = "SchedulePickUpResponse", targetNamespace = "http://purolator.com/pws/datatypes/v1", partName = "SchedulePickUpResponse")
    @Action(input = "http://purolator.com/pws/service/v1/SchedulePickUp", output = "http://purolator.com/pws/service/v1/PickUpServiceContract/SchedulePickUpResponse", fault = {@FaultAction(className = PickUpServiceContractSchedulePickUpValidationFaultFaultFaultMessage.class, value = "http://purolator.com/pws/service/v1/PickUpServiceContract/SchedulePickUpValidationFaultFault")})
    @WebMethod(operationName = "SchedulePickUp", action = "http://purolator.com/pws/service/v1/SchedulePickUp")
    public SchedulePickUpResponseContainer schedulePickUp(
        @WebParam(partName = "SchedulePickUpRequest", name = "SchedulePickUpRequest", targetNamespace = "http://purolator.com/pws/datatypes/v1")
        SchedulePickUpRequestContainer schedulePickUpRequest
    ) throws PickUpServiceContractSchedulePickUpValidationFaultFaultFaultMessage;
}
