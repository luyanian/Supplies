package com.maverick.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 
 * SharedPreference 存储的工具类 
 *
 */

public class Cfg {
	
	private static SharedPreferences mSharedPreferences;
	
	public static void init(Context context){
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	} 
	
	public static void clearAllData(Context context){
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		mSharedPreferences.edit().clear().commit();
	}
	
	/**
	 * 存储String类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStr(Context context,String key,String value){
		
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		mSharedPreferences.edit().putString(key, value).apply();
		
	}
	/**
	 * 获取String类型的数据
	 * @param context
	 * @param key
	 * @return
	 */
	public static String loadStr(Context context,String key){
		
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		String value = mSharedPreferences.getString(key, "defaults");
		
		return value;
		
	}
	
	
	/**
	 * 存储int类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context,String key,int value){
		
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		mSharedPreferences.edit().putInt(key, value).commit();
		
	}
	
	/**
	 * 获取Int类型的数据
	 * @param context
	 * @param key
	 * @return
	 */
	public static int loadInt(Context context,String key,int defvalues){
		
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		int value = mSharedPreferences.getInt(key, defvalues);
		
		return value;
		
	}
	
	
	/**
	 * 存储int类型的数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context,String key,boolean value){
		
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		mSharedPreferences.edit().putBoolean(key, value).commit();
		
	}
	
	
	/**
	 * 获取Boolean类型的数据
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean loadBoolean(Context context,String key,boolean defvalues){
		
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		boolean value = mSharedPreferences.getBoolean(key, defvalues);
		
		return value;
	}
	
	/**
	 * 删除缓存里的字符串
	 */
	public static void deleteData(Context context,String key){
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		mSharedPreferences.edit().remove(key).commit();
	}
	
	

}
