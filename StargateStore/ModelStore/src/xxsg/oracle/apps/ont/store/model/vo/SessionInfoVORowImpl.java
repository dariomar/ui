package xxsg.oracle.apps.ont.store.model.vo;

import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Jun 04 11:23:13 CEST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class SessionInfoVORowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        UserId,
        UserName,
        RespId,
        ApplId,
        CurrentLanguage;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public int index() {
            return SessionInfoVORowImpl.AttributesEnum.firstIndex() + ordinal();
        }

        public static final int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return SessionInfoVORowImpl.AttributesEnum.firstIndex() +
                   SessionInfoVORowImpl.AttributesEnum.staticValues().length;
        }

        public static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = SessionInfoVORowImpl.AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int USERID = AttributesEnum.UserId.index();
    public static final int USERNAME = AttributesEnum.UserName.index();
    public static final int RESPID = AttributesEnum.RespId.index();
    public static final int APPLID = AttributesEnum.ApplId.index();
    public static final int CURRENTLANGUAGE = AttributesEnum.CurrentLanguage.index();

    /**
     * This is the default constructor (do not remove).
     */
    public SessionInfoVORowImpl() {
    }
}
