package com.elastic.demo.Controller;

public enum YMMType {
	

	AUTO(3),
	MARINE(2),
	OH_MODEL(3),
	END_YOKE(3),
	COMMERCIAL(3),
	OFFHIGHWAY(3),
	KING_PIN(2),
	MAKE_MODEL_YEAR(1);
	
	private int minCount;
	
	YMMType(int minCount) {
		this.minCount = minCount;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	
	public static int getMinAmongTypes() {
		int mc =  AUTO.minCount;
		for (YMMType type : YMMType.values()) {
			if (type.getMinCount() < mc) {
				mc = type.getMinCount();
			}
		}
		return mc;
	}
	
	public static YMMType getTypeByName(String name) {
		for (YMMType type : YMMType.values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		return null;
	}

}
