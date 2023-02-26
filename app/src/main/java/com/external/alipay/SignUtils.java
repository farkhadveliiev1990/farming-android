package com.external.alipay;

import android.content.Context;

import com.dmy.farming.AppConst;

import org.bee.Utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtils {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM, "BC");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 商户PID
	public static final String PARTNER = "2088521163412676";
	// 商户收款账号
	public static final String SELLER = "2088521163412676";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE =
			"MIICXgIBAAKBgQDB6dAXV5Bk8SEQcSH2zqpZTxv2T91tJ5kEp9qQ2w9HeC1Z8N5FwbLeiHeiBYO5Dd8Uf8Mmk3uCP3Nwhtlvg9l6ecy16TZooBdc7JI07p2KYecf3/FZw0oDlVLfBOMMQ+xFmOzuRAsXNMu4eFZydPG4Y2RacnP/WPeTlvHqeuoC5QIDAQABAoGAUBAsxeZ2jObuQu6jGlc8CIHcRre08eOej0iKurJnvZeGChOkgmK2aqEn2/Kw71Al4j3aImxUW3O9QyG6Vwu2V94x9s6NK6pKfMCFlTO+OrAXwcIOIrRMAEoiA51PbFt0tE54Z1SfT2OP4RVbB7Y/ACDLcS6ZN65QPMYhF/TyIEUCQQDlAzSCdP3IfaM/+qHBmvOSXrF0cNkcqo/7drYM7d4CJ8nQB7upcbyg6sIzZU+Dz9FJbrTVRW03jaNuce8Vte+TAkEA2MO+azFf32xSjSG5Z2OAZR0STUQngc3exZNVR6oo3YoEmvTULOKsyxR9KMaqkceYhZlnfnMOElCtS5KIqeqepwJBAMlJtuaPc9y76Tg7z98+laXqSykgYwRaOjF5FuxoSoE0faK2mVCGopgBn23TNz+Q8tALWTA3VJbGjIZv6Af6ILcCQQDRsyQ+/Sgbs+Z0xEBh5d+rciFngFyb9bbi+rKQvuwDzmbAJcQxnCS/3hVq4i7XEvnZnVuC9/mP/F558sulXeWXAkEA4BdTA4zwOtm5hLvpERYalAmUC8VF91MzzOPYFxU5dPlrSFBEHR6jOh/h5Msq/tatCTgyBIyibBl67HOHdAneQg==";

	final static String NOTIFY_URL = AppConst.SERVER_MONEY + "/MobileAliReturnUrl.jspx";

	public static String getPayInfo(Context context, String order_sn, double price, String desc)
	{
		String orderInfo = getOrderInfo(context, order_sn, price, desc);

		String sign = sign(orderInfo);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		return orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
	}

	/**
	 * desc :  recharge, baoming
	 */
	private static String getOrderInfo(Context context, String order_sn, double price, String desc) {
		String subject = "悟空旅游";

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + order_sn + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + desc + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + Utils.strMoney(price+"") + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + NOTIFY_URL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	private static String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}

//	/**
//	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
//	 *
//	 */
//	private static String getOutTradeNo() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
//	}

}
