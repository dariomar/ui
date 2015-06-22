package xxsg.oracle.apps.ont.store.view.commons;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.ResourceBundle;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.logging.ADFLogger;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.ApplicationModule;

import org.apache.myfaces.trinidad.component.UIXSwitcher;

public class ADFUtils {
    private static ADFLogger adfLogger = ADFLogger.createADFLogger(ADFUtils.class);
            /**
             * When a bounded task flow manages a transaction (marked as requires-transaction,.
             * requires-new-transaction, or requires-existing-transaction), then the
             * task flow must issue any commits or rollbacks that are needed. This is
             * essentially to keep the state of the transaction that the task flow understands
             * in synch with the state of the transaction in the ADFbc layer.
             *
             * Use this method to issue a commit in the middle of a task flow while staying
             * in the task flow.
             */
            public static void saveAndContinue() {
                Map sessionMap =
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                BindingContext context =
                    (BindingContext)sessionMap.get(BindingContext.CONTEXT_ID);
                String currentFrameName = context.getCurrentDataControlFrame();
                DataControlFrame dcFrame =
                    context.findDataControlFrame(currentFrameName);

                dcFrame.commit();
                dcFrame.beginTransaction(null);
            }

            /**
             * Programmatic evaluation of EL.
             *
             * @param el EL to evaluate
             * @return Result of the evaluation
             */
            public static Object evaluateEL(String el) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ELContext elContext = facesContext.getELContext();
                ExpressionFactory expressionFactory =
                    facesContext.getApplication().getExpressionFactory();
                ValueExpression exp =
                    expressionFactory.createValueExpression(elContext, el,
                                                            Object.class);

                return exp.getValue(elContext);
            }

            /**
             * Programmatic invocation of a method that an EL evaluates to.
             * The method must not take any parameters.
             *
             * @param el EL of the method to invoke
             * @return Object that the method returns
             */
            public static Object invokeEL(String el) {
                return invokeEL(el, new Class[0], new Object[0]);
            }

            /**
             * Programmatic invocation of a method that an EL evaluates to.
             *
             * @param el EL of the method to invoke
             * @param paramTypes Array of Class defining the types of the parameters
             * @param params Array of Object defining the values of the parametrs
             * @return Object that the method returns
             */
            public static Object invokeEL(String el, Class[] paramTypes,
                                          Object[] params) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ELContext elContext = facesContext.getELContext();
                ExpressionFactory expressionFactory =
                    facesContext.getApplication().getExpressionFactory();
                MethodExpression exp =
                    expressionFactory.createMethodExpression(elContext, el,
                                                             Object.class, paramTypes);

                return exp.invoke(elContext, params);
            }

            /**
             * Sets a value into an EL object. Provides similar functionality to
             * the <af:setActionListener> tag, except the from is
             * not an EL. You can get similar behavior by using the following...

             * setEL(to, evaluateEL(from))
             *
             * @param el EL object to assign a value
             * @param val Value to assign
             */
            public static void setEL(String el, Object val) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ELContext elContext = facesContext.getELContext();
                ExpressionFactory expressionFactory =
                    facesContext.getApplication().getExpressionFactory();
                ValueExpression exp =
                    expressionFactory.createValueExpression(elContext, el,
                                                            Object.class);

                exp.setValue(elContext, val);
            }
            
            public static BindingContainer getBindings(){
                return BindingContext.getCurrent().getCurrentBindingsEntry();
            }
            
            public  static void resetValueInputItems(AdfFacesContext adfFacesContext, UIComponent component){
              List<UIComponent> items = component.getChildren();
              for ( UIComponent item : items ) {

                resetValueInputItems(adfFacesContext,item);

                if ( item instanceof RichInputText ) {
                  RichInputText input = (RichInputText)item;
                  if ( !input.isDisabled() ) {
                     input.resetValue() ;
                     adfFacesContext.addPartialTarget(input);
                  };
                 } else if ( item instanceof RichInputDate ) {
                  RichInputDate input = (RichInputDate)item;
                  if ( !input.isDisabled() ) {
                     input.resetValue() ;
                     adfFacesContext.addPartialTarget(input);
                  };
                 } 
               }
            }
            
            public static void message(String component, String message){
                FacesContext fctx = FacesContext.getCurrentInstance();
                fctx.addMessage(component,new FacesMessage(FacesMessage.SEVERITY_INFO,message,null));
            }
            
            public static void error(String component, String message){
                FacesContext fctx = FacesContext.getCurrentInstance();
                fctx.addMessage(component,new FacesMessage(FacesMessage.SEVERITY_ERROR,message,null));
            }
            
            public static String getBundleValue(String key) {
                    FacesContext ctx = FacesContext.getCurrentInstance();
                    try {
                        //ResourceBundle resourceBundle = ResourceBundle.getBundle("it.leaseplan.webcenter.srm2.view.ViewControllerBundle");
                        ResourceBundle resourceBundle = BundleFactory.getBundle("it.sopra.corsoadf.view.ViewControllerBundle");
                                                         
                        return resourceBundle.getString(key);
                    } catch (Exception e) {
                        adfLogger.log(adfLogger.ERROR, "getBundelValue error: ",e);
                        return "";
                    }
                }
            

            public static void refresComponentInRoot(String id) {
                UIComponent component = null;
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext != null) {
                    UIComponent root = facesContext.getViewRoot();
                    component = findComponent(root, id);
                }
                AdfFacesContext.getCurrentInstance().addPartialTarget(component);
            }

            public static boolean isTransactionDirty() {
                BindingContext bctx = oracle.adf.controller.binding.BindingUtils.getBindingContext();
                return bctx.findDataControlFrame(bctx.getCurrentDataControlFrame()).isTransactionDirty();
            } 
            
            public static void executeCommit() {
                OperationBinding operationBinding = ADFUtils.getBindings().getOperationBinding("Commit");
                operationBinding.execute();
            }
            
            public static void executeRollback() {
                OperationBinding operationBinding = ADFUtils.getBindings().getOperationBinding("RollBack");
                operationBinding.execute();
            }
            
            public static UIComponent findComponent(UIComponent base, String id) {
                if (id.equals(base.getId()))
                    return base;
         
                UIComponent children = null;
                UIComponent result = null;
                Iterator childrens = base.getFacetsAndChildren();
                while (childrens.hasNext() && (result == null)) {
                    children = (UIComponent)childrens.next();
                    if (id.equals(children.getId())) {
                        result = children;
                        break;
                    }
                    result = findComponent(children, id);
                    if (result != null) {
                        break;
                    }
                }
                return result;
            }
            
            
            
            public static UIComponent findComponentByAttribute(UIComponent base,String attName, String attrVal) {
                    UIComponent children;

                    List<UIComponent> childList = base.getChildren();
                    Iterator<UIComponent> childIter = childList.iterator();
                    while (childIter.hasNext()) {

                        children = childIter.next();
                        if (children instanceof UIXSwitcher) {
                            UIXSwitcher sw = (UIXSwitcher)children;

                            UIComponent facetComponents = sw.getFacet(sw.getFacetName());

                            if (facetComponents instanceof UIXSwitcher) {
                               
                            } else {

                                if (facetComponents instanceof RichSelectBooleanCheckbox) {
                                    if (attrVal.equals(facetComponents.getAttributes().get(attName).toString())){
                                        return facetComponents;
                                    }
                                    

                                } 
                                
                                }

                            }

                        }

                        return null;
                    }
                


            public static void handleNavigation(String outcome) {
                FacesContext context = FacesContext.getCurrentInstance();
                NavigationHandler nh = context.getApplication().getNavigationHandler();
                nh.handleNavigation(context, "", outcome);
            }
            
            public static String counterRowPaginator(String iteratorName){
                
                String counterPage = "";
                
                DCIteratorBinding dciter = (DCIteratorBinding) ADFUtils.getBindings().get(iteratorName);
                if(dciter != null && dciter.getEstimatedRowCount() > 0){
                    
                    int start = dciter.getRangeStart();
                    double rows = dciter.getEstimatedRowCount();
                    long allRows = dciter.getEstimatedRowCount();
                    double rangeSize = dciter.getRangeSize();
                    double numPageNotFormatted = rows / rangeSize;
                    long lnumPages = Math.round(numPageNotFormatted+ 0.5f);
                    long currentPage = Math.round(start / rangeSize) +1;
                    
                    counterPage = "Pagina: "+currentPage+" di "+lnumPages+" Risultati Totali "+allRows;
                }
                
                
                
                
                return counterPage;
            }
            
           
            
            
            

            
            

            /**
             * Restituisce l'ApplicationModule del contesto
             * @param dataControlName Nome del DataControl
             * @return ApplicationModule del contesto
             */
            public static ApplicationModule getApplicationModule(String dataControlName) {
              DCBindingContainer dcBindindContainer = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
              BindingContext bindingContext = dcBindindContainer.getBindingContext();
              DCDataControl dcDataControl = bindingContext.findDataControl(dataControlName);
              ApplicationModule applicationModule = (ApplicationModule) dcDataControl.getDataProvider();
              return applicationModule;
            }
}
