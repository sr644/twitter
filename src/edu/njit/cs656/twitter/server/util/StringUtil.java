package edu.njit.cs656.twitter.server.util;

public class StringUtil {
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
    
   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */

    
   /*
    * ====================================================================
    * Constructors
    * ====================================================================
    */

    
   /*
    * ====================================================================
    * Public Methods
    * ====================================================================
    */  
    public static String upperTrimString(String value){
    	return trimString(value).toUpperCase();
    }

    public static String trimString(String value){
        return value != null ? value.trim() : "";
    }

    public static boolean isEmptyString(String val){
        return trimString(val).equals("");
    }

    public static boolean isNotEmptyString(String val){
        return !isEmptyString(val);
    }
    
    public static boolean isLengthGreaterThan(String val, int length){
    	if(isNotEmptyString(val)) {
    		if(val.length() > length) {
    			return true;
    		} else {
    			return false;
    		}
    	} else {
    		return false;
    	}
    }
    
    public static boolean areEqualIgnoreNull(String first, String second){  
    	return trimString(first).equals(trimString(second));
    }

    public static boolean areNotEqualIgnoreNull(String first, String second){  
    	return !areEqualIgnoreNull(first, second);
    }
    
    public static boolean areEqual(String first, String second){  
    	if(first == second){ // covers both being null
    		return true;
    	}
    	if(first == null || second == null){
    		return false;
    	}   
    	return first.equals(second);
    }
    
    public static boolean areNotEqual(String first, String second){    	
    	return !areEqual(first, second);
    }

    /*
    * ====================================================================
    * Private Methods
    * ====================================================================
    */


}
