package com.sctjsj.gasdrawer.entity.javaBean;

/**
 * Created by liuha on 2017/5/22.
 */

/***
 *
 */
public  class NetAddress {

    public static int USERTYPE=0;

    public  static  final String zbb="192.168.1.73:8080/wp   118.123.22.190:8003/erp_gas  120.76.225.224:8001/erp_gas(打包用)"  ;

    public static final String xs="120.76.225.224:8001/erp_gas";

    //待施工数据
    public static final String BACKLOG_CON="http://"+xs+"/user/pageSearch$ajax.htm";
    //待上传数据
    public static final String BACKLOG_SC="http://"+xs+"/user/pageSearch$ajax.htm";
    //登陆接口
    public static final String GAS_LOGIN="http://"+xs+"/admin/login.htm";
    //首页公告通知的接口
    public static final String GAS_HOME_NOTICE="http://"+xs+"/user/pageSearch$ajax.htm";
    //消息fragment的数据接口
    public static final String GAS_MESSAGE="http://"+xs+"/user/pageSearch$ajax.htm";
    //分类
    public static final String CLASS_IFICATION ="http://"+xs+"/pageSearch$ajax.htm";
    //材料使用清单
    public static final String USE_LIST="http://"+xs+"/pageSearch$ajax.htm";

    public static final String SERVICE_ITEMS="http://"+xs+"/pageSearch$ajax.htm";
    //请求客户信息
    public static final String GAS_CUS="http://"+xs+"/user/singleSearch$ajax.htm";
    //请求个人信息
    public static final String GAS_USERMSG="http://"+xs+"/user/singleSearch$ajax.htm";
    //修改用户信息
    public static final String GAS_CHANGEPAS="http://"+xs+"/admin/user/update_login_user$ajax.htm";
    //上传图片
    public static final String GAS_UPIMG="http://"+xs+"/upload/uploadImgByAccessory.htm?type=client";
    //历史纪录
    public static final String HISTORICAL="http://"+xs+"/user/pageSearch$ajax.htm";
    //退出登陆
    public static final String GAS_LOGOUT="http://"+xs+"/tuser/logout$ajax.htm";
    //获取首页的banner图
    public static final String GAS_BANNER="http://"+xs+"/pageSearch$ajax.htm";
    //上传安装作业单
    public static final String GAS_INSTAL="http://"+xs+"/user/order_upload$ajax.htm";
    //上传安装作业确认单
    public static final String GAS_ERE="http://"+xs+"/user/addInstallOrder$ajax.htm";
    //通过审核
    public static final String GAS_AUDIT="http://"+xs+"/user/auditorder$ajax.htm";

    //上传维修作业单
    public static final String REPAIR="http://"+xs+"/user/addRepairOrder$ajax.htm";


}
