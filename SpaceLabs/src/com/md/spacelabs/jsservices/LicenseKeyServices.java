package com.md.spacelabs.jsservices;

import org.mozilla.javascript.ScriptableObject;

import com.md.spacelabs.dex.dexServices.JSServletResponse;

@SuppressWarnings("serial")
public class LicenseKeyServices  extends ScriptableObject {

	public LicenseKeyServices() {
		String[] functionNames = { "checkLicenseKeyFull", "generateLicenseKey", "checkLicenseKeyValidity" };
		defineFunctionProperties(functionNames, LicenseKeyServices.class, ScriptableObject.DONTENUM);
	}

	
	public boolean checkLicenseKeyValidity(String key){
		String[] keyParts = key.split("3333");
		
		if(keyParts.length > 1)
			return false;
		
//		String keyMain = keyParts[0];
//		String keyCheck = keyParts[1];
		
					
		return true;
	}

	
	public boolean checkLicenseKeyFull(String key, String name, String initial){
		String key2 = generateLicenseKey(name, initial);
		if(key.equals(key2))
			return true;
		
		return false;
	}
	
	
	public String generateLicenseKey(String name, String initial) {
        if(name.length() < 4 || initial.length() > 2) return null;
        
        name = name.toUpperCase();
        initial = initial.toUpperCase();
        int val = 0; // 92675666;
        int val2 = 0;
        for (int i = 0; i < name.length(); i++) {
            val += name.charAt(i) * i * 2;
            val += initial.charAt(0) * 234202;
        }
        val2=val;
        int quersumme = 0;
        while (val2 > 0) {
          quersumme += val2%10;
          val2=val2/10;
        }
        int ran = quersumme % 10;

        String key = name + "_" + initial + "_" + ran + (val);
        int chksum = 3333 + key.length();

        for (int i = 1; i < key.length(); i++) {
            chksum += key.charAt(i);
        }
        key = key + "_" + chksum;
        return key;
    }


	@Override
	public String getClassName() {

		return "LicenseKeyServices";
	}
	

}
