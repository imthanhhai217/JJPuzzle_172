package com.supho.utils;

import android.content.Context;
import android.util.Base64;

import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.Utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
	public static final String TAG = EncryptionUtils.class.getSimpleName();
	private static final String K1 = "aG4U5ZLJAR7IkCOdrku2UlfvLRVRyl07juehXuOK0XNax9YnVvI=";
	private static final String P2 = "DAF6kfGhYHCv9Vf1xzjBPSWLXnE+pClY5obPOYb+uRopsLlVMoE=";
	
	private static String k() {
		byte[] x0 = Base64.decode(P2, 0);
		byte[] x1 = Base64.decode(K1, 0);

		byte[] x = new byte[x0.length];
		for (int i = 0; i < x1.length; i++) {
			x[i] = (byte) (x0[i] ^ x1[i]);
		}
		return new String(x);
	}

	public static String getSignedString(Context context) {
		try {
			String base = 
					GameConfigs.getInstance().getAppKey()
							+ Utils.getGameVersion(context)
							+ DeviceUtils.getAdvertisingID(context)
							+ DeviceUtils.getOSInfo()
							+ DeviceUtils.getDevice()
							+ k();
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
	}
	public static String getSignedString(Context context , String accountId) {
		try {
			String base = 
					GameConfigs.getInstance().getAppKey()
					+ Utils.getGameVersion(context) 
					+ accountId
					+ k();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
	}
	
	public static String decryptionDataBlowfish(byte[] encrypted) {
		try {
			byte[] key = k().getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(key, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] decrypted = cipher.doFinal(encrypted);
			String decrytionToString = new String(decrypted, "UTF-8");
			return decrytionToString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
