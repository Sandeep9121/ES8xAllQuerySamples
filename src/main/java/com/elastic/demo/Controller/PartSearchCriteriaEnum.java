package com.elastic.demo.Controller;

public enum PartSearchCriteriaEnum {

	YEAR("year"),
	MAKE("make"),
	MODEL("model"),
	ENGINE("engine"),
	APPLICATION_SEARCH_KEY("applicationSearchKey"),
	APPLICATION_SEARCH_KEY_PREFIX("applicationSearchKey.applicationSearchKey_edgengram"),
	APPLICATION_SEARCH_KEY_WILDCARD("applicationSearchKey.applicationSearchKey_ngram"),
	APPLICATION_SEARCH_KEY_COMPLETE("applicationSearchKey.applicationSearchKey"),
	APPLICATION_SEARCH_KEY_TRIMMED("applicationSearchKey.applicationSearchKey_trimmed"),
	APPLICATION_SEARCH_KEY_EXACT("applicationSearchKey.applicationSearchKey_exact"),
	APPLICATION_SEARCH_KEY_RAW("applicationSearchKey.applicationSearchKey_raw"),
	//-----------------
	ORIGIONAL_PART_NUMBER("origional_part_number"),
	ORIGIONAL_PART_NUMBER_EXACT("origional_part_number.origional_part_number_exact"),
	CREATED_BY("created_by"),
	UPDATED_BY("updated_by"),
	CREATED_ON("created_on"),
	UPDATED_ON("updated_on"),
	ID("_id"),
	
	/** The part number. */
	PART_NUMBER("part_number"),
	PART_NUMBER_RAW("part_number.part_number_raw"),
	
	SALEABLE("saleable"),
	IS_SALEABLE("is_saleable"),
	STATUS("status"),
	PART_TYPE("part_type"),
	PART_PARENT("parent"),
	PART_PARENT_ID("parent.id"),
	PART_PARENT_NAME("parent.name"),
	PART_PARENT_NO("parent.no"),
	PART_PARENT_TYPE("parent.type"),
	PART_PARENT_SUB_PARENT_NAME("parent.sub_parent_name"),
	PART_PARENT_SUB_PARENT_NO("parent.sub_parent_no"),
	PART_PARENT_SUB_PARENT_CATALOG("parent.sub_parent_catalog"),
	PART_PARENT_SUB_PARENT_TYPE("parent.sub_parent_type"),
	PART_PARENT_QTY("parent.qty"),
	PART_PARENT_CATALOG("parent.catalog"),
	
	/** The part number complete. */
	PART_NUMBER_COMPLETE("part_number.part_number"),
	
	/** The part number exact match only case insensitive match*/
	PART_NUMBER_EXACT("part_number.part_number_exact"),
	
	PART_NUMBER_BRAND("part_number_brand"),

	PART_NUMBER_CATALOG("part_number_catalog"),
	
	ORIGIONAL_PART_NUMBER_BRAND("origional_part_number_brand"),

	ORIGIONAL_PART_NUMBER_CATALOG("origional_part_number_catalog"),
	
	ORIGIONAL_PART_NUMBER_PREFIX("origional_part_number.origional_part_number_edgengram"),
	
	ORIGIONAL_PART_NUMBER_COMPLETE("origional_part_number.origional_part_number"),

	/** The part number match by removing special characters and spaces*/
	PART_NUMBER_TRIMMED("part_number.part_number_trimmed"),
	
	/** The part number wildcard. */
	PART_NUMBER_WILDCARD("part_number.part_number_ngram"),
	
	/** The part number prefix. */
	PART_NUMBER_PREFIX("part_number.part_number_edgengram"),
	
	/** The interchg part number. */
	INTERCHG_PART_NUMBER("interchg_part_number.part_number"),
	
	/** The interchg part number wildcard. */
	INTERCHG_PART_NUMBER_WILDCARD("interchg_part_number.part_number_ngram"),
	
	/** The interchg part number prefix. */
	INTERCHG_PART_NUMBER_PREFIX("interchg_part_number.part_number_edgengram"),
	
	/** The competitor part number. */
	COMPETITOR_PART_NUMBER("competitor_part_number"),
	
	/** The part desc. */
	PART_DESC("part_desc"),

	ASSETS("assets"),

	ASSETS_FILE("assets.file"),

	ASSETS_MEDIA_TYPE("assets.media_type"),

	ASSETS_TYPE("assets.asset_type"),

	BRAND("brand"),
	
	BRAND_CODE("brand_code"),
	
	/** The part desc complete. */
	PART_DESC_COMPLETE("part_desc.part_desc"),
	
	/** The part desc prefix. */
	PART_DESC_PREFIX("part_desc.part_desc_edgengram"),
	
	/** The long desc. */
	LONG_DESC("long_desc"),
	
	/** The long desc complete. */
	LONG_DESC_COMPLETE("long_desc.long_desc"),
	
	/** The long desc prefix. */
	LONG_DESC_PREFIX("long_desc.long_desc_edgengram"),
	
	/** The marketing desc. */
	MARKETING_DESC("sales_desc"),
	
	RTB_CODE("rtb_code"),
	
	TAX_CODE("tax_code"),
	
	PACKAGE_TYPE("package_type"),
	
	PACKAGE_QTY("package_qty"),
	
	MIN_QTY("min_qty"),
	
	CORE_PART_NUM("core_part_number"),
	
	CORE_PARTS("core_parts"),
	
	CORE_PART_REQ("core_part_req"),
	
	WT("wt"),
	
	WT_UOM("wt_uom"),
	
	/** The app summary desc. */
	APP_SUMMARY_DESC("app_summary_desc"),
	
	CATEGORY_HIERARCHY("cat_hierarchy"),
	CATEGORY_GROUP("cat_group"),
	
	/** The part category name. */
	PART_CATEGORY_NAME("cat_name.name"),
	
	/** The part category name prefix. */
	PART_CATEGORY_NAME_PREFIX("cat_name.name_edgengram"),
		
	/** The part category path. */
	PART_CATEGORY_PATH("cat_path"),
	
	/** The part category Name. */
	CATEGORY_NAME("cat_name"),
	
	/** The part category id. */
	PART_CATEGORY_ID("cat_id"),
		
	/** The attribute values. */
	ATTRIBUTE_VALUES("attr_values"),
	
	/** The attribute name values. */
	ATTRIBUTE_NAME_VALUES("attr_name_values"),
	
	/** The attribute string. */
	ATTRIBUTE_STRING("string_attr"),
	
	/** The attribute string name. */
	ATTRIBUTE_STRING_NAME("string_attr.name"),
	
	ATTTRIBUTE_STRING_NONSEARCHABLE("non_searchable_attr"),
	
	/** The attribute string value. */
	ATTRIBUTE_STRING_VALUE("string_attr.value"),
	
	/** The attribute string searchable. */
	ATTRIBUTE_STRING_SEARCHABLE("string_attr.searchable"),
	
	/** The attribute string disporder. */
	ATTRIBUTE_STRING_DISPORDER("string_attr.dispOrder"),
	
	/** The attribute numeric. */
	ATTRIBUTE_NUMERIC("numeric_attr"),
	
	/** The attribute numeric name. */
	ATTRIBUTE_NUMERIC_NAME("numeric_attr.name"),
	
	/** The attribute numeric value. */
	ATTRIBUTE_NUMERIC_VALUE("numeric_attr.value"),
	
	/** The attribute numeric searchable. */
	ATTRIBUTE_NUMERIC_SEARCHABLE("numeric_attr.searchable"),
	
	/** The attribute numeric disporder. */
	ATTRIBUTE_NUMERIC_DISPORDER("numeric_attr.dispOrder"),
	
	/** The bom. */
	BOM("bom"),
	
	/** The BOM part number. */
	BOM_PART_NUMBER("bom.part_number.part_number"),
	
    BOM_PART_NUMBER_RAW("bom.part_number.part_number_raw"),
	
	/** The BOM part number. */
	BOM_PART_NUMBER_TRIMMED("bom.part_number.part_number_trimmed"),
	
	/** The BOM part number. */
	BOM_PART_NUMBER_NGRAM("bom.part_number.part_number_ngram"),
	
	/** The BOM part number. */
	BOM_PART_NUMBER_EDGENGRAM("bom.part_number.part_number_edgengram"),
	
	CATALOG("catalog"),
	
	DISTRIBUTOR_CODES("distributor_codes"),

	/** The intrechange. */
	INTRECHANGE("interchanges"),
	
	/** The interchange part number. */
	INTERCHANGE_PART_NUMBER("interchange.part_number.part_number"),
	
	INTERCHANGE_NAME("interchange.name"),
	
	INTERCHANGE_FOOT_NOTES("interchange.foot_notes"),
	
	INTERCHANGE_TYPE("interchangetype"),
	
	INTERCHANGE_CMTS("interchange.comments"),
	
	INTERCHANGE_PART_NUMBER_RAW("interchange.part_number.part_number_raw"),
	
	/** The interchange part number. */
	INTERCHANGE_PART_NUMBER_TRIMMED("interchange.part_number.part_number_trimmed"),
	
	/** The interchange part number. */
	INTERCHANGE_PART_NUMBER_NGRAM("interchange.part_number.part_number_ngram"),
	
	/** The interchange part number. */
	INTERCHANGE_PART_NUMBER_EDGENGRAM("interchange.part_number.part_number_edgengram"),
	
	/** The interchanges. */
	INTERCHANGES("interchange"),
	
	INTERCHANGETYPE("interchange.type"),
	
	/** The application suggest. */
	//APPLICATION_SUGGEST("app_suggest"),
	
	/** The application. */
	APPLICATION("app"),
	
	/** The application year. */
	APPLICATION_YEAR("app.year.year"),
	
	/** The application make. */
	APPLICATION_MAKE("app.make.make"),
	
	/** The application model. */
	APPLICATION_MODEL("app.model.model"),
	
	YOKE_FLANGE_APP("app.flange_yoke_app.flange_yoke_app"),
	
	YOKE_FLANGE_MAKE("app.flange_yoke_make.flange_yoke_make"),
	
	YOKE_FLANGE_MODEL("app.flange_yoke_model.flange_yoke_model"),
	
	YOKE_FLANGE_POSITION("app.flange_yoke_position"),
	
	KING_PIN_MAKE("app.king_pin_make.king_pin_make"),
	
	KING_PIN_MODEL("app.king_pin_model.king_pin_model"),

	APPLICATION_FITMENT_NOTES("app.fitment_notes"),
	
	APPLICATION_POSITION("app.position"),

	APPLICATION_DRIVETYPE("app.drive_type"),
	
	APPLICATION_PER_CAR_QTY("app.percar_qty"),
	
	APPLICATION_ENGINE("app.engine"),
	
	APPLICATION_IMGS("app.imgs"),
	
	APPLICATION_IMGS_FILE("app.imgs.file"),
	
	APPLICATION_IMGS_DESC("app.imgs.desc"),
	
	APPLICATION_IMGS_LOCNO("app.imgs.loc_no"),
	
	APPLICATION_ENGINE_DESC("app.engine_desc.engine_desc"),
	
	APPLICATION_CATEGORY("app.category.category"),
	
	APPLICATION_SUBCATEGORY("app.sub_category.sub_category"),
	
	APPLICATION_SUBPARENT("app.sub_parent"),
	
	APPLICATION_TYPE_INDICATION_VIN("app.type_indication_vin.type_indication_vin"),

	APPLICATION_FILTER_YEAR("app.year.year_edgengram"),
	
	APPLICATION_FILTER_MAKE("app.make.make_edgengram"),
	
	APPLICATION_FILTER_MODEL("app.model.model_edgengram"),
	
	APPLICATION_FILTER_ENGINE_DESC("app.engine_desc.engine_desc_edgengram"),

	APPLICATION_FILTER_CATEGORY("app.category.category_edgengram"),

	APPLICATION_FILTER_SUBCATEGORY("app.sub_category.sub_category_edgengram"),

	APPLICATION_FILTER_TYPEVIN("app.type_indication_vin.type_indication_vin_edgengram"),

	MAKE_YEAR_SUMMARY("make_yr_smry"),

	/** The application detail. */
	APPLICATION_DETAIL("app_dtl"),
	
	/** The popularity code. */
	POPULARITY_CODE("pop_code"),
	
	/** The popularity code group. */
	POPULARITY_CODE_GROUP("pop_code_group"),
	
	/** The popularity code group. */
	CORE_DEPOSIT("core_deposit"),
	
	CUSTOMER_INTERCHANGE_NUMBER("customer_interchange"),
	
	CUSTOMER_INTERCHANGE("customer_interchange"),
	
	/** The CUSTOMER part number */
	CUSTOMER_INTERCHANGE_PART_NUMBER("customer_interchange.part_number.part_number"),
	
	CUSTOMER_INTERCHANGE_PART_NUMBER_RAW("customer_interchange.part_number.part_number_raw"),
	
	/** The CUSTOMER part number exact */
	CUSTOMER_INTERCHANGE_PART_NUMBER_TRIMMED("customer_interchange.part_number.part_number_trimmed"),
	
	CUSTOMER_INTERCHANGE_CUASTOMER_NUMBER("customer_interchange.customer_number"),
	
	CAT_MENU("cat_menu"),
	
	CAT_MENU_LVL1("cat_menu.lvl1"),
	
	CAT_MENU_LVL2("cat_menu.lvl2"),
	
	CAT_MENU_LVL3("cat_menu.lvl3"),
	
	RELATED_PARTS("related_parts"),
	
	ML_RELATED_PARTS("ml_related_parts"),
	
	SERIAL_NUMBERS("serial_numbers"),
	
	SERIAL_NUMBERS_SNO("serial_numbers.sno"),
	
	SERIAL_NUMBERS_SNO_RAW("serial_numbers.sno.sno_raw"),
	
	SERIAL_NUMBERS_SNO_PART("sno"),
	
	PRODUCT_CODE("product_code"),
	
	RECOMMENDED_PARTS("recomnd_parts"),
	
	ORIGIN_COUNTRY("country_of_origin"),
	
	PART_CATALOG("catalog"),
	
	FOOT_NOTE("foot_note"),
	
	COMPARABLE("comparable"),
	
	STATUS_CODE("statusCode"),
	
	PART_NUMBER_THREE_PREFIX("part_number_three_prefix"),
	
	PART_NUMBER_FOUR_PREFIX("part_number_four_prefix"),
	
	PART_NUMBER_TWO_PREFIX("part_number_two_prefix"),
	
	URL_SLUG("url_slug");

	/** The name. */
	private String name;

    /**
     * Instantiates a new dana part search criteria enum.
     *
     * @param stringVal the string val
     */
    private PartSearchCriteriaEnum(String stringVal) {
        name=stringVal;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Gets the value.
     *
     * @param code the code
     * @return the value
     */
    public static String getValue(String code){
        for(PartSearchCriteriaEnum e : PartSearchCriteriaEnum.values()){
            if(code == e.name) 
            	return e.name();
        }
        return null;
    }
    
    /**
     * Gets the by value.
     *
     * @param code the code
     * @return the by value
     */
    public static PartSearchCriteriaEnum getByValue(String code){
        for(PartSearchCriteriaEnum e : PartSearchCriteriaEnum.values()){
            if(code.equals(e.name)) 
            	return e;
        }
        return null;
    }
}
