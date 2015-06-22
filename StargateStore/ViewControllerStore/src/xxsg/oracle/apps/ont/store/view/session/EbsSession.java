package xxsg.oracle.apps.ont.store.view.session;

import java.math.BigDecimal;

import java.util.Locale;

import javax.faces.context.FacesContext;

import oracle.adf.model.binding.DCBindingContainer;

import xxsg.oracle.apps.ont.store.view.commons.ADFUtils;

public class EbsSession {
    public EbsSession() {
        super();
    }
    
    private BigDecimal userId;
    private BigDecimal responsibilityId;
    private BigDecimal applicationId;
    private String     languageCode;

    public static EbsSession getEbsSessionInstance(){
        EbsSession sessionObj=null;
        sessionObj = (EbsSession) ADFUtils.evaluateEL("#{sessionScope.CurrentEbsSession}");
        if (sessionObj == null){
            sessionObj = new EbsSession();
        }
        
        return sessionObj;
    }

    public static void changeLocale(String language){  
        System.out.println("changeLocale: "+language);  
        Locale newLocale = new Locale(language);  
        FacesContext context = FacesContext.getCurrentInstance();  
        context.getViewRoot().setLocale(newLocale);  
    } 
    
    public static void setEbsSessionInstance(EbsSession s){
        ADFUtils.setEL("#{sessionScope.CurrentEbsSession}", s);
    }

    //Getters and setters
    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public void setResponsibilityId(BigDecimal responsibilityId) {
        this.responsibilityId = responsibilityId;
    }

    public BigDecimal getResponsibilityId() {
        return responsibilityId;
    }

    public void setApplicationId(BigDecimal applicationId) {
        this.applicationId = applicationId;
    }

    public BigDecimal getApplicationId() {
        return applicationId;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
