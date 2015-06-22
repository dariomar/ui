package xxsg.oracle.apps.ont.store.view.beans;

import java.math.BigDecimal;
import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.server.ViewObjectImpl;

import xxsg.oracle.apps.ont.store.model.lov.LanguagesLovVORowImpl;
import xxsg.oracle.apps.ont.store.model.modules.StoreAMImpl;
import xxsg.oracle.apps.ont.store.view.commons.ADFUtils;
import xxsg.oracle.apps.ont.store.view.session.EbsSession;

public class LoginMB {
    public LoginMB() {
        super();
        this.setCurrentLanguage("Italiano");
        EbsSession.changeLocale("it");
        
    }
    private ADFLogger logger = ADFLogger.createADFLogger(LoginMB.class);
    private boolean authResult;
    private String userName;
    private String password;
    private boolean ricordami;
    private String currentLanguage; //inizializzazione

    public void setRicordami(boolean ricordami) {
        this.ricordami = ricordami;
    }

    public boolean isRicordami() {
        return ricordami;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
        logger.warning("currentLanguage: "+currentLanguage);
        
        
        StoreAMImpl am = (StoreAMImpl) ADFUtils.getApplicationModule("StoreAMDataControl");
        ViewObjectImpl vo = am.getLanguagesLovVO1();
        Row[] rows = vo.getFilteredRows("Description", currentLanguage);

        if (rows != null){        
            LanguagesLovVORowImpl row = (LanguagesLovVORowImpl) rows[0];
            vo.setCurrentRow(row); //setting current row
            String isoLanguage = row.getIsoLanguage().toLowerCase();
            logger.warning("setCurrentLanguage()-> language selected: "+isoLanguage);
            EbsSession.changeLocale(isoLanguage);
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAuthResult(boolean authResult) {
        this.authResult = authResult;
    }

    public boolean isAuthResult() {
        return authResult;
    }

    public String doLogin(){

        try {
            StoreAMImpl am = (StoreAMImpl) ADFUtils.getApplicationModule("StoreAMDataControl");
            String userName = this.getUserName();
            String password = this.getPassword();
            logger.warning("username: "+userName);
            logger.warning("password: "+password);

    /*
            //PRIMO MODO
            OperationBinding userAuth = ADFUtils.getBindings().getOperationBinding("userAuth");
            userAuth.execute();
            Boolean res2 = (Boolean) ADFUtils.evaluateEL("#{bindings.userAuth_return}");
            this.setAuthResult(res2);
    */
    /*        
            //SECONDO MODO
            Boolean res = (Boolean) ADFUtils.invokeEL("#{bindings.userAuth.execute}"); //accedo al Bindign della pagina (PageDef)
            this.setAuthResult(res);
    */        
    
            //TERZO MODO
            //DeliveryAMImpl am = (DeliveryAMImpl) ADFUtils.getApplicationModule("DeliveryAMDataControl");
            Boolean res3 = am.userAuth(userName, password);
            this.setAuthResult(res3);
    
            
            if (!isAuthResult()){
                ADFUtils.error(null, "Errore login"); //popup di errore
            }
            
            // recupero userId
            BigDecimal currentUserId = new BigDecimal(am.getCurrentUserId());
            
            EbsSession ebs = EbsSession.getEbsSessionInstance();
            ebs.setUserId(currentUserId);
            EbsSession.setEbsSessionInstance(ebs);
            logger.warning("settato userId: "+currentUserId);
            
            //Reading language
            LanguagesLovVORowImpl row = (LanguagesLovVORowImpl) am.getLanguagesLovVO1().getCurrentRow();
            String langCode = row.getLanguageCode();
            String nlsLanguage = row.getNlsLanguage();
            String language = row.getDescription();
            String isoLanguage = row.getIsoLanguage().toLowerCase();
            logger.warning("language selected: "+langCode);
            
            // Impostare la lingua
            am.setEbsLanguage(nlsLanguage);
            ebs.setLanguageCode(langCode);
            EbsSession.changeLocale(isoLanguage);
            this.setCurrentLanguage(language);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "check"; //nome della freccia nel task-flow
    }    
    
}
