
package com.dmy.farming.api;
public class ApiInterface 
{
     // 4. 一般操作
     public static final String UPLOAD_FILE = "/ImageService/taxuploadimg";
     public static final String UPLOAD_CHAT = "/upload/chatupload.jhtml ";

     public static final String QCLOUD_URL = "/mobile/cosv4/getsignv4.jhtml";

     // 1.用户管理
//     public static final String USER_SIGNIN = "/user/signin.jhtml";

     public static final String USER_REQCODE = "/user/reqcode.jhtml";
     public static final String USER_INFO = "/user/info.jhtml";
     public static final String USER_CHECK_3RD = "/user/checkcode3rd.jhtml";
     public static final String USER_SIGNUP_3RD = "/user/signup3rd.jhtml";
     public static final String USER_CHANGE_PAYPWD = "/user/changepaypassword.jhtml";

     public static final String USER_SIGNIN = "/UserService/API_userLogin";
     public static final String USER_SIGNUP = "/UserService/API_userRegister";
     public static final String CHANGE_PWD = "/UserService/API_subNewPassword";
     public static final String USER_LOGOUT = "/UserService/API_userLogout";
     public static final String USER_SIGNIN_3RD = "/UserService/API_threeSidesLogin";
     public static final String SIGNIN = "/UserService/API_signin";
     public static final String CHANGE_PWD2 = "/UserService/API_modifyPWD";
     public static final String USER_UPDATE = "/UserService/modifyUserInfo";
     public static final String USER_VERIFICATIONPHONE = "/UserService/API_verificationphone";
     public static final String USER_BINDPHONE = "/UserService/API_bindingPhone";
     public static final String USER_SERVICEAGREEMENT = "/UserService/API_getServiceAgreement";

     // 7. 首页
     public static final String HOME_ADVERS = "/home/adverlist.jhtml";
     public static final String HOME_RECOMS = "/home/recommendlist.jhtml";
     public static final String HOME_RECOMMENDS = "/home/recommendlists.jhtml";
     public static final String HOME_GROUP_LIST = "/group/lists.jhtml";
     public static final String HOME_ARTICLE = "/home/article.jhtml";
     public static final String HOME_SEARCHGROUP ="/chat/searchqun.jhtml";
     public static final String HOME_SEARCHACTIVITY ="/chat/searchhd.jhtml";
     public static final String HOME_SEARCHROUTE ="/chat/searchroute.jhtml";

     public static final String HOME_SPONSOR = "/DictionaryService/API_getSpon";
    public static final String HOME_SEARCHHISTORY ="/SearchService/API_searchHistory";
    public static final String HOME_HOTSEARCH ="/SearchService/API_hotSearch";
    public static final String HOME_DELETESEARCHHISTORY ="/SearchService/API_deletedSearch";
    public static final String HOME_SEARCH ="/SearchService/API_globalSearch";

    //诊断库
    public static final String DIAGNOSTICLIST = "/ArticleService/API_getDiagonseList";
    public static final String DIAGNOSTICDETAIL = "/ArticleService/API_getDiagonseDetail";

     //农技文章
     public static final String ARTICLE = "/ArticleService/API_getFarmArticleList";
     public static final String ARTICLEDETAIL = "/ArticleService/API_getFarmArticleDetails";
     public static final String ARTICLECOMMENT = "/ArticleService/API_getArticleCommentList";
     public static final String DELETEDCOMMENT = "/ArticleService/API_deletedComment";
     public static final String USERLIKE = "/DictionaryService/API_userLike";
    public static final String  CANCELUSERLIKE = "/DictionaryService/API_canneluserLike";

    //同类文章
    public static final String SIMILARARTICLE = "/ArticleService/API_getFarmSimilararticle";
    //同类视频
    public static final String SIMILARVIDEO = "/ArticleService/API_getFarmSimilarVideo";
    //同类诊断库
    public static final String SIMILARDIAG = "/ArticleService/API_getFarmSimilardiag";


     //农技视频
     public static final String VIDEO = "/ArticleService/API_getFarmVideoList";
     public static final String VIDEODETAIL = "/ArticleService/API_getFarmVideoDetails";

     // 文章
     public static final String ARTICLELIST = "/ArticleService/API_getArticleList";
    public static final String ARTICLEDETAILE = "/ArticleService/API_getArticleDetails";

     //买
     public static final String BUY ="/PriceService/API_getBuyList";
     public static final String BUYDETAIL ="/PriceService/API_getBuyDetails";
     public static final String MYBUY ="/PriceService/API_getMyBuyList";

     //卖
     public static final String SALE ="/SaleService/API_getSaleList";
     public static final String SALEDETAIL="/SaleService/API_getSaleDetails";
     public static final String MYSALE ="/SaleService/API_getMySaleList";
     public static final String MYDELETE ="/PriceService/API_deletedTradeLeads";

     //租
     public static final String RENT ="/RentService/API_getRentList";
     public static final String RENTDETAIL="/RentService/API_getRentDetails";
     public static final String MYRENT ="/RentService/API_getMyRentList";

     //找帮手
     public static final String FINDHELP ="/FindHelperService/API_getFindHelperList";
     public static final String FINDHELPDETAIL="/FindHelperService/API_getFindHelperDetails";

    public static final String FINDMYHELP ="/FindHelperService/API_getMyFindHelperList";

     //价格行情
     public static final String PRICE ="/PriceService/API_getPriceList";
     public static final String PRICEDETAIL="/PriceService/API_publishPriceInfo";
     public static final String PUBLISHPRICE="/PriceService/API_publishPriceInfo";

     //预警消息
     public static final String WARN ="/WarnService/API_getWaringMessageList";
     public static final String WARNDETAIL="/WarnService/API_getWaringDetails";
    public static final String WARNDTEAD="/WarnService/API_getWaring";
    public static final String WARNLIST ="/WarnService/API_getWaringList";

     //发布
     public static final String PUBLISHQUESTION ="/FaqService/API_publishQuestion";
     public static final String MYQUESTIONLIST ="/FaqService/API_getMyQuestionList";
     public static final String PUBLISHSALE ="/SaleService/API_publishSaleInfo";
     public static final String PUBLISHBUY ="/PriceService/API_publishBuyInfo";
     public static final String PUBLISHRENT ="/RentService/API_publishRentInfo";
     public static final String PUBLISHFINDHELPER ="/FindHelperService/API_publishFindHelperInfo";

     //帮助
     public static final String USERHELP ="/UserService/API_userHelpList";
     public static final String USERHELPDETAIL ="/UserService/API_getUserHelpDetails";

    //查询非法字符
    public static final String ILLEGALS ="/DictionaryService/API_getIllegal";

     //收藏
     public static final String COLLECTIONLIST ="/UserService/API_collectionlist";
     public static final String COLLECTION="/UserService/API_collection";
    public static final String CANCELCOLLECTION="/UserService/API_cancelcollection";



     //系统消息
     public static final String NOTICE ="/WarnService/API_getSystemMessageList";
     public static final String NOTICEDETAIL="/WarnService/API_getSystemMessageDetails";
     public static final String getNoticnum="/WarnService/API_getMessageNum";
    public static final String deleteNoice="/WarnService/API_getDeleteMessage";
    public static final String deleteWaring="/WarnService/API_getDeleteWarning";

     //疑问解答
     public static final String QUESTIONLIST ="/FaqService/API_getQuestionList";
     public static final String QUESTIONDETAIL ="/FaqService/API_getQuestionDetails";
     public static final String ADOPTCOMMENT ="/FaqService/API_adoptComment";
     public static final String ANSWER ="/FaqService/API_Answer";
     public static final String ASKANSWER ="/FaqService/API_replayComment";
    public static final String DELETECOMMENT ="/FaqService/API_deleteComment";
    public static final String DELETEQUESTION ="/FaqService/API_deleteFaq";

     //字典
     public static final String CROPTYPE ="/DictionaryService/API_getCropTypeList";
     public static final String QUESTIONTYPE ="/DictionaryService/API_getQuestionTypeList";
     public static final String FOLLOWTYPE ="/DictionaryService/API_getUserAttention";
     public static final String CROPSUB ="/DictionaryService/API_getCropSupList";
     public static final String cropcycle ="/DictionaryService/API_getCycleByCrop";
     public static final String ARTICLELABEL ="/DictionaryService/API_getArticleLabel";
     public static final String GONGQIULABEL ="/DictionaryService/API_getPublishTypeList";
     public static final String SAVEATTENTION ="/DictionaryService/API_saveUserAttention";
     public static final String DELETEATTENTION ="/DictionaryService/API_editUserAttention";
     public static final String ADDCROP ="/DictionaryService/API_addCrop";
     public static final String UNIT ="/DictionaryService/API_getPriceUnit";
     public static final String REPORT ="/DictionaryService/API_userReport";
     public static final String COMMENT ="/DictionaryService/API_evaluearticle";
    public static final String SALELABEL ="/DictionaryService/API_getCropSupList";

     // 活动中心
     public static final String ACTIVITYCENTERLIST ="/ActivityService/API_getActivityList";

    // 专家
    public static final String EXPERT ="/ExpertService/API_getExpertList";
    public static final String EVALUATEEXPERT ="/DictionaryService/API_evaluateExpert";
    public static final String EXPERTDETAIL ="/UserService/API_getExpertInfo";
    public static final String EXPERTSOLVED ="/FaqService/API_getMysolved";

}