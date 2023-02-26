package com.dmy.farming;
import android.content.Context;


public class FarmingManager {

	private static RegisterApp registerApp;

	private static Context mContext;

	// 获取友盟key
	public static String getUmengKey(Context context)
    {
        return "xxx";
	}
	
	// 获取快递key
	public static String getKuaidiKey(Context context) {
        return "xxx";
	}
	
	// 获取科大讯飞key
	public static String getXunFeiCode(Context context)
    {
        return "xxx";
	}
	
	// 获取百度push的key
	public static String getPushKey(Context context)
    {
        return "xxx";
	}
	
	// 获取百度push的seckey
	public static String getPushSecKey(Context context)
    {
        return "xxx";
	}
	
	// 获取支付宝parterID(合作者身份)
	public static String getAlipayParterId(Context context)
    {
        return "2088521163412676";
	}
	
	// 获取支付宝sellerID(收款账户)
	public static String getAlipaySellerId(Context context)
    {
        return "2088521163412676";
	}
	
	// 获取支付宝key
	public static String getAlipayKey(Context context)
    {
        return "xxx";
	}
	
	// 获取支付宝rsa_alipay_public(公钥)
	public static String getRsaAlipayPublic(Context context)
    {
        return "xxx";
	}
	
	// 获取支付宝rsa_private(私钥)
	public static String getRsaPrivate(Context context)
    {
        return "MIICXgIBAAKBgQDB6dAXV5Bk8SEQcSH2zqpZTxv2T91tJ5kEp9qQ2w9HeC1Z8N5FwbLeiHeiBYO5Dd8Uf8Mmk3uCP3Nwhtlvg9l6ecy16TZooBdc7JI07p2KYecf3/FZw0oDlVLfBOMMQ+xFmOzuRAsXNMu4eFZydPG4Y2RacnP/WPeTlvHqeuoC5QIDAQABAoGAUBAsxeZ2jObuQu6jGlc8CIHcRre08eOej0iKurJnvZeGChOkgmK2aqEn2/Kw71Al4j3aImxUW3O9QyG6Vwu2V94x9s6NK6pKfMCFlTO+OrAXwcIOIrRMAEoiA51PbFt0tE54Z1SfT2OP4RVbB7Y/ACDLcS6ZN65QPMYhF/TyIEUCQQDlAzSCdP3IfaM/+qHBmvOSXrF0cNkcqo/7drYM7d4CJ8nQB7upcbyg6sIzZU+Dz9FJbrTVRW03jaNuce8Vte+TAkEA2MO+azFf32xSjSG5Z2OAZR0STUQngc3exZNVR6oo3YoEmvTULOKsyxR9KMaqkceYhZlnfnMOElCtS5KIqeqepwJBAMlJtuaPc9y76Tg7z98+laXqSykgYwRaOjF5FuxoSoE0faK2mVCGopgBn23TNz+Q8tALWTA3VJbGjIZv6Af6ILcCQQDRsyQ+/Sgbs+Z0xEBh5d+rciFngFyb9bbi+rKQvuwDzmbAJcQxnCS/3hVq4i7XEvnZnVuC9/mP/F558sulXeWXAkEA4BdTA4zwOtm5hLvpERYalAmUC8VF91MzzOPYFxU5dPlrSFBEHR6jOh/h5Msq/tatCTgyBIyibBl67HOHdAneQg==";
	}
	
	// 获取支付宝回调地址
	public static String getAlipayCallback(Context context)
    {
        return "xxx";
	}
	
	// 获取新浪key
	public static String getSinaKey(Context context)
    {
        return "xxx";
	}
	
	// 获取新浪secret
	public static String getSinaSecret(Context context)
    {
        return "xxx";
	}
	
	// 获取新浪的回调地址
    public static String getSinaCallback(Context context)
    {
        return "xxx";
    }
	
	// 获取微信id
	public static String getWeixinAppId(Context context)
    {
        return "wxa03e036c1cdd7ca6";
	}
	
	// 获取微信key
	public static String getWeixinAppKey(Context context)
    {
        return "xxx";
	}
	public static String getWeixinAppPartnerId(Context context)
	{
		return "xxx";
	}
	
	// 获取腾讯key
	public static String getTencentKey(Context context)
    {
        return "xxx";
	}

	
	// 获取腾讯secret
	public static String getTencentSecret(Context context)
    {
        return "xxx";
	}
	
	// 获取腾讯callback
	public static String getTencentCallback(Context context)
    {
        return "xxx";
	}
	
	public static void registerApp(RegisterApp register) {
		registerApp = register;
	}
	
	public static interface RegisterApp {
		public void onRegisterResponse(boolean success);
	}
}
