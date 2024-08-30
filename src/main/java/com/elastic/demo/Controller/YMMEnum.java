package com.elastic.demo.Controller;

public enum YMMEnum {
	
	YEAR, MAKE, MODEL, ENGINE, CATEGORY, SUBCATEGORY, TYPEVIN, DRIVE_TYPE, LOCATOR_IMAGE, LOCATOR_LOC_NO, YOKE_FLANGE_APP, YOKE_FLANGE_MAKE, YOKE_FLANGE_MODEL, KING_PIN_MAKE, KING_PIN_MODEL;

	/**
	 * Find bykey.
	 *
	 * @param key the key
	 * @return the YMM enum
	 */
	public static YMMEnum findBykey(String key) {
		
		for (YMMEnum ymm : YMMEnum.values()) {
			if (ymm.name().equalsIgnoreCase(key)) {
				return ymm;
			}
		}
		return null;
	}

}
