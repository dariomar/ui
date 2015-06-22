package xxsg.oracle.apps.ont.store.model.modules;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.Locale;

import oracle.adf.share.logging.ADFLogger;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.server.ApplicationModuleImpl;

import oracle.jbo.server.DBTransaction;

import oracle.jbo.server.ViewObjectImpl;

import xxsg.oracle.apps.ont.store.model.modules.common.StoreAM;
import xxsg.oracle.apps.ont.store.model.vo.ResponsibilitiesVOImpl;
import xxsg.oracle.apps.ont.store.model.vo.SessionInfoVOImpl;

// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Jun 04 10:22:35 CEST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class StoreAMImpl extends ApplicationModuleImpl implements StoreAM {
    /**
     * This is the default constructor (do not remove).
     */
    public StoreAMImpl() {
    }
    private ADFLogger logger = ADFLogger.createADFLogger(StoreAMImpl.class);

    public boolean userAuth(String username, String password){
        boolean authRes=false;
        
        logger.warning("AM.userAuth");
        
        String sql = "begin :1 := xxsg_login_pkg.ebs_login(:p_user,:p_pwd); end;";
        DBTransaction t = this.getDBTransaction();
        CallableStatement st = t.createCallableStatement(sql, DBTransaction.DEFAULT);
        try {
            st.registerOutParameter(1, Types.VARCHAR);
            st.setString(2, username);
            st.setString(3, password);
            st.execute();
            String res = st.getString(1);
            logger.warning("result login: "+res);
            if ("Y".equals(res)){
                authRes = true;
            }
            else{
                authRes = false;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            try {st.close();} catch (SQLException e) {}
        }

        return authRes;
    }

    public void setResponsibility(Long userId, BigDecimal respId, BigDecimal applId){
        boolean authRes=false;
        
        logger.warning("AM.userAuth");
        
        String sql =    " begin       fnd_global.apps_initialize (:L_user_id,\n" + 
                        "                                  :L_resp_id,\n" + 
                        "                                  :L_resp_appl_id,\n" + 
                        "                                  security_group_id   => '');\n" + 
                        "      mo_global.init ('ONT'); " +
                        "      end;";
        DBTransaction t = this.getDBTransaction();
        CallableStatement st = t.createCallableStatement(sql, DBTransaction.DEFAULT);
        try {
            st.setInt(1, userId.intValue());
            st.setInt(2, respId.intValue());
            st.setInt(3, applId.intValue());
            st.execute();
            
        } catch (SQLException e) {
            logger.warning("Exception in setResponsibility",e);
            throw new RuntimeException(e);
        }finally{
            try {st.close();} catch (SQLException e) {}
        }

    }
    
    public void setEbsLanguage(String language){
        boolean authRes=false;
        
        logger.warning("AM.userAuth");
        
        String sql = " alter session set nls_language = "+language;
        logger.warning("executing: "+sql);
        DBTransaction t = this.getDBTransaction();
        CallableStatement st = t.createCallableStatement(sql, DBTransaction.DEFAULT);
        try {
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            try {st.close();} catch (SQLException e) {}
        }

    }    

    public long getCurrentUserId(){
        long userId=-1;        
        logger.warning("AM.userAuth");
        
        String sql = "select fnd_global.user_id from dual";
        DBTransaction t = this.getDBTransaction();
        Statement st = t.createStatement(DBTransaction.DEFAULT);
        ResultSet rs=null;
        try {
            rs = st.executeQuery(sql);
            rs.next();
            userId = rs.getLong(1);
            logger.warning("Current login User ID : "+userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            try {st.close();} catch (SQLException e) {}
            try {rs.close();} catch (SQLException e) {}
        }

        return userId;
    }

    public void loginInitialize(){
        this.setDefaultLanguage();
    }

    public void setDefaultLanguage(){
        ViewObjectImpl vo = this.getLanguagesLovVO1();
        
        Key k;
        String[] s = {"I"}; //Default Italiano
        k = new Key(s);
        Row r = vo.getRow(k);
        vo.setCurrentRow(r);
    }

    /**
     * Container's getter for ResponsibilitiesVO1.
     * @return ResponsibilitiesVO1
     */
    public ResponsibilitiesVOImpl getResponsibilitiesVO1() {
        return (ResponsibilitiesVOImpl) findViewObject("ResponsibilitiesVO1");
    }

    /**
     * Container's getter for SessionInfoVO1.
     * @return SessionInfoVO1
     */
    public SessionInfoVOImpl getSessionInfoVO1() {
        return (SessionInfoVOImpl) findViewObject("SessionInfoVO1");
    }

    /**
     * Container's getter for LanguagesLovVO1.
     * @return LanguagesLovVO1
     */
    public ViewObjectImpl getLanguagesLovVO1() {
        return (ViewObjectImpl) findViewObject("LanguagesLovVO1");
    }
}

