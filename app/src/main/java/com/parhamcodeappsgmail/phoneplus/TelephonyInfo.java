package com.parhamcodeappsgmail.phoneplus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public final class TelephonyInfo {

	private static TelephonyInfo telephonyInfo;
	private String imeiSIM1;
	private String imeiSIM2;
	private String serialSIM1;
	private String serialSIM2;
	private String opSIM1;
	private String opSIM2;
	private boolean isSIM1Ready;
	private boolean isSIM2Ready;
	private boolean isDual;
	private final String TAG="MainActivity";


	public String getImsiSIM1() {
		return imeiSIM1;
	}

    /*public static void setImsiSIM1(String imeiSIM1) {
        TelephonyInfo.imeiSIM1 = imeiSIM1;
    }*/

	public String getImsiSIM2() {
		return imeiSIM2;
	}

	public String getSerialSIM1() {
		return serialSIM1;
	}
	public String getSerialSIM2() {
		return serialSIM2;
	}

	public String getOpSIM1() {
		return opSIM1;
	}
	public String getOpSIM2() {
		return opSIM2;
	}

    /*public static void setImsiSIM2(String imeiSIM2) {
        TelephonyInfo.imeiSIM2 = imeiSIM2;
    }*/

	public boolean isSIM1Ready() {
		return isSIM1Ready;
	}

    /*public static void setSIM1Ready(boolean isSIM1Ready) {
        TelephonyInfo.isSIM1Ready = isSIM1Ready;
    }*/

	public boolean isSIM2Ready() {
		return isSIM2Ready;
	}

    /*public static void setSIM2Ready(boolean isSIM2Ready) {
        TelephonyInfo.isSIM2Ready = isSIM2Ready;
    }*/

	public boolean isDualSIM() {
		return serialSIM2 != null;
	}

	private TelephonyInfo() {
	}

	public static TelephonyInfo getInstance(Context context) {

		if (telephonyInfo == null) {

			telephonyInfo = new TelephonyInfo();

			TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

			if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
			}


			try {
				telephonyInfo.serialSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
				telephonyInfo.serialSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();

				try {
					telephonyInfo.serialSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
					telephonyInfo.serialSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}

			telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
			telephonyInfo.isSIM2Ready = false;

			try {
				telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
				telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
			} catch (GeminiMethodNotFoundException e) {

				e.printStackTrace();

				try {
					telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
					telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}

			try {
				telephonyInfo.imeiSIM1 = getDeviceserialBySlot(context, "getSimSerialNumberGemini", 0);
				telephonyInfo.imeiSIM2 = getDeviceserialBySlot(context, "getSimSerialNumberGemini", 1);
			} catch (GeminiMethodNotFoundException e) {

				e.printStackTrace();

				try {
					telephonyInfo.imeiSIM1 = getDeviceserialBySlot(context, "getSimSerialNumber", 0);
					telephonyInfo.imeiSIM1 = getDeviceserialBySlot(context, "getSimSerialNumber", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
				}
			}


			try {
				telephonyInfo.opSIM1 = getDeviceOpBySlot(context, "getNetworkOperatorNameGemini", 0);
				telephonyInfo.opSIM2 = getDeviceOpBySlot(context, "getNetworkOperatorNameGemini", 1);
			} catch (GeminiMethodNotFoundException e) {
				e.printStackTrace();
				// Log.i("MainActivity", e+"");

				try {
					telephonyInfo.opSIM1 = getDeviceOpBySlot(context, "getNetworkOperatorName", 0);
					telephonyInfo.opSIM2 = getDeviceOpBySlot(context, "getNetworkOperatorName", 1);
				} catch (GeminiMethodNotFoundException e1) {
					//Call here for next manufacturer's predicted method name if you wish
					e1.printStackTrace();
					// Log.i("MainActivity", e+"");
				}
			}
		}

		return telephonyInfo;
	}

	private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

		String imei = null;

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try{

			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimID.invoke(telephony, obParameter);

			if(ob_phone != null){
				imei = ob_phone.toString();

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Test", "imei read error: "+e+"");
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return imei;
	}

	private static String getDeviceserialBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

		String serial = null;

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try{

			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimSerial = telephonyClass.getMethod(predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimSerial.invoke(telephony, obParameter);

			if(ob_phone != null){
				serial = ob_phone.toString();

			}
		} catch (Exception e) {
			Log.i("MainActivity", e+"");
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return serial;
	}

	private static String getDeviceOpBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

		String op = null;

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try{

			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimOP = telephonyClass.getMethod(predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimOP.invoke(telephony, obParameter);

			if(ob_phone != null){
				op = ob_phone.toString();

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("MainActivity", e+"");
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return op;
	}

	private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

		boolean isReady = false;

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		try{

			Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

			if(ob_phone != null){
				int simState = Integer.parseInt(ob_phone.toString());
				if(simState == TelephonyManager.SIM_STATE_READY){
					isReady = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return isReady;
	}


	private static class GeminiMethodNotFoundException extends Exception {

		private static final long serialVersionUID = -996812356902545308L;

		public GeminiMethodNotFoundException(String info) {
			super(info);
		}
	}
}
