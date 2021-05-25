package com.wushi.scheduler.api.enums;

import com.wushi.scheduler.api.interfaces.BaseErrorCode;

/**
 * 错误码枚举类
 *
 * @author lhx
 * @date 2020/1/7 8:54
 */
public enum ErrorCode implements BaseErrorCode {

    /**
     * 未知的错误
     */
    UnknownError("未知的错误", 99999),

    // Env------------------------------------------------

    Env_NotSupportOS("不支持此操作系统", 10001),
    Env_DotNetCoreSdkNotInstall("未安装jdk", 10002),
    Env_DotNetCoreSdkVersionIncorrect("jdk版本不正确", 10003),
    Env_DotNetFrameworkVersionIncorrect("jdk版本不正确", 10004),

    // Config------------------------------------------------

    Config_InvalidConfiguration("无效的配置", 13001),
    Config_InvalidConfigCenterEndpoint("无效的配置中心端点", 13002),
    Config_InvalidAppConfigDirectory("无效的应用程序配置目录", 13003),
    Config_InvalidAppScriptDirectory("无效的查询脚本目录", 13004),

    // Db------------------------------------------------

    Db_ConnectionConfigError("数据连接配置错误", 14000),
    Db_ConnectionStringNotFound("未找到指定的数据库连接串", 14001),
    Db_NotSupoortOperation("不支持的操作", 14002),
    Db_ConnectFailure("数据库连接失败", 14003),
    Db_ConnectTimeout("数据库连接超时", 14004),
    Db_InvalidDataProvider("无效的数据提供器", 14005),
    Db_InvalidParameter("无效的参数", 14006),
    Db_SqlSyntaxError("Sql语法错误", 14007),
    Db_SqlExecuteError("Sql语句执行错误", 14008),
    Db_SqlParseError("Sql语句解析错误", 14009),
    Db_DataProviderNotFound("数据提供器未找到，请确认是否已安装", 14015),
    Db_BatcherProviderNotFound("批量插入提供器未找到，请确认是已注册到DI容器", 14016),
    Db_ReturningNotFound("Returning处理器未找到，请确认是已注册到DI容器", 14017),
    Db_ConnectionIsNull("数据库连接对象为空", 14020),
    // Mapping------------------------------------------------

    Mapping_InvalidAttribute("无效的mapping配置属性", 15001),
    Mapping_IdAttributeNotFound("mapping配置缺少id属性", 15002),
    Mapping_TitleAttributeNotFound("mapping配置缺少title属性", 15003),
    Mapping_AuthorAttributeNotFound("mapping配置缺少author属性", 15004),
    Mapping_ConnNameAttributeNotFound("mapping节点下sql的配置缺少connName属性", 15005),
    Mapping_TypeValueInvalid("mapping节点下parameters配置中，item的类型（type）属性值后缺失或者不在预期范围", 15006),
    Mapping_NotFound("未找到指定的的mapping配置", 15007),
    Mapping_NotFilesFound("未找到任何的mapping配置文件", 15008),
    Mapping_DuplicateID("重复的mapping（id）配置", 15009),
    Mapping_MissingNode("缺失的节点", 15010),
    Mapping_MissingAttribute("缺失的属性", 15011),
    Mapping_NameAttributeNotFound("缺少name属性", 15012),

    // Http------------------------------------------------

    Http_NotAllowedParameter("不合法的参数", 16001),
    Http_RequiredParameter("必要的参数", 16002),
    Http_NotSupportFileType("不支持的的文件类型", 16003),
    Http_RemoteHostCannotConnect("无法连接到远程服务器", 16004),
    Http_NotAllowedIP("不允许的IP地址", 16005),
    Http_BadRequest("请求无效", 16400),
    Http_NotFound("请求的资源未找到", 16404),
    Http_MethodNotAllowed("无效的Http请求方法", 16405),
    Http_Unauthorized("未经授权的", 16401),
    Http_Forbidden("请求被禁止", 16403),
    Http_RequestEntityTooLarge("请求的实体过大", 16413),
    Http_RequestUriTooLong("请求的uri地址太长", 16414),
    Http_UnsupportedMediaType("不支持的媒体类型", 16415),
    Http_TooManyRequests("请求过多", 16429),
    Http_InternalServerError("内部服务器错误", 16500),
    Http_NotImplemented("未实现", 16501),
    Http_BadGateway("网关错误", 16502),
    Http_ServiceUnavailable("服务不可用", 16503),
    Http_GatewayTimeout("网关超时", 16504),

    // File------------------------------------------------

    File_FileOrDirectoryNotFound("文件或目录未找到", 17001),
    File_UploadFailure("文件上传失败", 17002),
    File_UnsupportFileFormat("不支持的文件格式", 17003),
    File_FileOrDirectoryAlreadyExists("同名的文件或目录已存在", 17004),
    File_FormatConvertError("文件格式转换错误", 17005),
    File_SizeTooLarge("文件过大", 17006),


    // 序列化------------------------------------------------

    Json_SerializationFailure("json序列化失败", 18000),
    Json_DeserializationFailure("json反序列化失败", 18001),
    Xml_SerializationFailure("xml序列化失败", 18002),
    Xml_DeserializationFailure("xml反序列化失败", 18003),
    Binary_SerializationFailure("二进制序列化失败", 18004),
    Binary_DeserializationFailure("二进制反序列化失败", 18005),


    // 用户------------------------------------------------

    User_InvalidUserName("无效的用户名", 21000),
    User_InvalidPassword("无效的密码", 21001),
    User_Locked("用户帐户被锁定", 21002),
    User_Expired("用户帐户已过期", 21003),
    User_LoginInElsewhere("用户帐户在另一处登陆", 21004),
    User_InvalidClientId("无效的ClientID", 21005),

    // Token相关------------------------------------------------

    Token_InvalidSecretKey("无效的密钥", 22000),
    Token_Invalid("无效的Token", 22001),
    Token_Expired("过期的Token", 22002),
    Token_InvalidGrantScope("无效的Grant Scope", 22003),
    Token_InvalidGrantType("无效的Grant Type", 22004),
    Token_Required("缺少Token", 22011),

    // ApiKey相关------------------------------------------------

    ApiKey_Invalid("无效的ApiKey", 22050),
    ApiKey_Expired("已过期的ApiKey", 22051),

    // 验证码、短信相关------------------------------------------------
    SMS_InvalidVerifyCode("无效(错误)的验证码", 56001),
    SMS_ExpiredVerifyCode("验证码已过期", 56002),
    SMS_VerifyCodeUnsent("验证码未发送", 56003),

    // API签名认证------------------------------------------------
    Sign_RequiredHeaderParameter("缺少必要的Header参数", 23001),
    Sign_NotAllowedParameter("非法的签名参数", 23002),
    Sign_UnsupportedMediaType("不支持的媒体类型", 23415),
    Sign_BodyDigestError("Body摘要不一致", 23004),
    Sign_SignatureError("签名不一致", 23010),
    Sign_SignatureTimestampExpired("签名时间戳已过期", 23011),
    Sign_NonceDuplicated("签名请求的随机数重复", 23012),
    Service_NonRegister("服务未注册", 24001);


    /**
     * 构造方法
     */
    ErrorCode(String errorText, int errorCode) {
        this.errorText = errorText;
        this.errorCode = errorCode;
    }

    /**
     * 获取描述
     */
    public static String getText(int index) {
        for (ErrorCode c : ErrorCode.values()) {
            if (c.getErrorCode() == index) {
                return c.errorText;
            }
        }
        return null;
    }

    /**
     * 错误编码描述信息
     */
    private String errorText;
    /**
     * 错误编码
     */
    private int errorCode;

    /**
     * 获取错误编码描述信息
     *
     * @return {String}
     */
    @Override
    public String getErrorText() {
        return this.errorText;
    }

    /**
     * 设置错误编码描述信息
     *
     * @param errorCodeDescription 错误编码信息
     */
    public void setErrorText(String errorCodeDescription) {
        this.errorText = errorCodeDescription;
    }

    /**
     * 获取错误编码
     *
     * @return {int}
     */
    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * 设置错误编码
     *
     * @param errorCode 错误编码
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
