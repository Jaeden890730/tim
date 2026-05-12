package com.mxic.oiplus.oimaintain;

public class YieldDefBean {

	//以下 ITEM_* 定義應與 TF_DESCRIPTION.TAG = 40 (CP), 45 (FT) 同步
	private final int ITEM_YIELD = 1;
	private final int ITEM_BIN = 2;
	private final int ITEM_SPECIAL_BIN = 3;
	private final int ITEM_SPECIAL_PATTERN = 4;
	private final int ITEM_PG_YIELD = 5;
	private final int ITEM_DATALOG_BIN = 6;
	private final int ITEM_SMSN_BIN = 16;

	private String sid;

	private String yid;

	private String seq;

	private String facility;

	private String product_code;

	private String brands;

	private String test_mode;

	private String lower_limit;

	private String upper_limit;

	private String flag1;

	private String flag2;

	private String item_type;

	private String item;
	
	private String item_disable;

	private String item_mode2;

	private String item_bins2;

	private String action;
	
	private String dg_action;

	private String actionseq;

	private String change_ipn;
	
	private String change_ipn_last_2;
	
	private String change_ipn_start_10;

	private String route_name;

	private String start_step;

	private String remark;
	
	private String item_seq;
	
	private String version;

	
	// for comparison
	private String type; // remove/insert/old, new

	private int product_code_flag;

	private int test_mode_flag;

	private int brand_flag;

	private int item_flag;

	private int action_flag;

	private int change_ipn_flag;

	private int route_name_flag;

	private int start_step_flag;

	private int remark_flag;
	
	private int item_seq_flag;
	
	private String product_type;
	
	private String acs;
	
	private String by_lot_dg;
	
	private int by_lot_dg_flag;
	
	private String dgrade_special_ipn;
	
	private String dgradeprodcode;
	
	private int dg_action_flag;

	private String groupitems_no;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = String.valueOf(sid);
	}

	public String getYid() {
		return yid;
	}

	public void setYid(int yid) {
		this.yid = String.valueOf(yid);
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = String.valueOf(seq);
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getTest_mode() {
		return test_mode;
	}

	public void setTest_mode(String test_mode) {
		this.test_mode = test_mode;
	}

	public String getBrands() {
		return brands;
	}

	public void setBrands(String brands) {
		this.brands = brands;
	}

	public String getLower_limit() {
		return lower_limit;
	}

	public void setLower_limit(String lower_limit) {
		this.lower_limit = lower_limit;
	}

	public String getUpper_limit() {
		return upper_limit;
	}

	public void setUpper_limit(String upper_limit) {
		this.upper_limit = upper_limit;
	}

	public String getFlag1() {
		return flag1;
	}

	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}

	public String getFlag2() {
		return flag2;
	}

	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}
	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(int item_type) {
		this.item_type = String.valueOf(item_type);
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public String getItem_disable() {
		return item_disable;
	}

	public void setItem_disable(String item_disable) {
		this.item_disable = item_disable;
	}

	public String getItem_mode2() {
		return item_mode2;
	}

	public void setItem_mode2(String item_mode2) {
		this.item_mode2 = item_mode2;
	}

	public String getItem_bins2() {
		return item_bins2;
	}

	public void setItem_bins2(String item_bins2) {
		this.item_bins2 = item_bins2;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getDg_action() {
		return dg_action;
	}

	public void setDg_action(String dg_action) {
		this.dg_action = dg_action;
	}

	public String getActionseq() {
		return actionseq;
	}

	public void setActionseq(String actionseq) {
		this.actionseq = actionseq;
	}

	public String getChange_ipn() {
		return change_ipn;
	}

	public void setChange_ipn(String change_ipn) {
		this.change_ipn = change_ipn;
	}
	
	public String getChange_ipn_last_2() {
		return change_ipn_last_2;
	}

	public void setChange_ipn_last_2(String change_ipn_last_2) {
		this.change_ipn_last_2 = change_ipn_last_2;
	}
	
	public String getChange_ipn_start_10() {
		return change_ipn_start_10;
	}

	public void setChange_ipn_start_10(String change_ipn_start_10) {
		this.change_ipn_start_10 = change_ipn_start_10;
	}

	public String getRoute_name() {
		return route_name;
	}

	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

	public String getStart_step() {
		return start_step;
	}

	public void setStart_step(String start_step) {
		this.start_step = start_step;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWeb_items1() { // for display only
		if (!item_type.equals(String.valueOf(ITEM_SPECIAL_BIN)) && !item_type.equals(String.valueOf(ITEM_SMSN_BIN)))
			return item;

		String result = "";
		if (item_type.equals(String.valueOf(ITEM_SPECIAL_BIN)))
			result = test_mode + "(" + item + ")/";
		if (item_type.equals(String.valueOf(ITEM_SMSN_BIN)))
			result = test_mode + "(" + item + ")-";
		return result;
	}

	public String getWeb_items2() { // for display only
		if (!item_type.equals(String.valueOf(ITEM_SPECIAL_BIN)) && !item_type.equals(String.valueOf(ITEM_SMSN_BIN)))
			return "";

		String result = item_mode2 + "(" + item_bins2 + ")";
		return result;
	}

	public String getFull_items() { // for pdf output / released OI query

		String percent = "%"; 
		String result = getWeb_items1()+getWeb_items2();
		String f1 = (flag1==null?"":(flag1.equals("<=")?"≦":flag1));
		String f2 = (flag2==null?"":(flag2.equals("<=")?"≦":flag2));
		
		// Datalog Bin 之 limit 中即有'單位'資料
		if (this.item.startsWith("Datalog")) percent = ""; 
		if (this.item.startsWith("RBER")) percent = ""; 

		if ((lower_limit != null) && (!lower_limit.equals("")))
			result = lower_limit + percent + " " + f1 + " " + result;
		if ((upper_limit != null) && (!upper_limit.equals("")))
			result = result + " " + f2 + " " + upper_limit + percent;
		return result;
	}

	public String getDisplay_enable() {
		if (action.startsWith("Dgrade")) {
			return "";
		} else {
		    if(product_type != null && !product_type.equals("NVM") && action !=null && acs.indexOf(action) != -1) {
		        return "";    
		    } else {
			return "disabled";
		}
	}
	}

	public int getProduct_code_flag() {
		return product_code_flag;
	}

	public void setProduct_code_flag(int product_code_flag) {
		this.product_code_flag = product_code_flag;
	}

	public String getProduct_code_color() {
		if (product_code_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getTest_mode_flag() {
		return test_mode_flag;
	}

	public void setTest_mode_flag(int test_mode_flag) {
		this.test_mode_flag = test_mode_flag;
	}

	public String getTest_mode_color() {
		if (test_mode_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getBrand_flag() {
		return brand_flag;
	}

	public void setBrand_flag(int brand_flag) {
		this.brand_flag = brand_flag;
	}

	public String getBrand_flag_color() {
		if (brand_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getItem_flag() {
		return item_flag;
	}

	public void setItem_flag(int item_flag) {
		this.item_flag = item_flag;
	}

	public String getItem_flag_color() {
		if (item_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getAction_flag() {
		return action_flag;
	}

	public void setAction_flag(int action_flag) {
		this.action_flag = action_flag;
	}

	public String getAction_flag_color() {
		if (action_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getChange_ipn_flag() {
		return change_ipn_flag;
	}

	public void setChange_ipn_flag(int change_ipn_flag) {
		this.change_ipn_flag = change_ipn_flag;
	}

	public String getChange_ipn_color() {
		if (change_ipn_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getRoute_name_flag() {
		return route_name_flag;
	}

	public void setRoute_name_flag(int route_name_flag) {
		this.route_name_flag = route_name_flag;
	}

	public String getRoute_name_color() {
		if (route_name_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getStart_step_flag() {
		return start_step_flag;
	}

	public void setStart_step_flag(int start_step_flag) {
		this.start_step_flag = start_step_flag;
	}

	public String getStart_step_color() {
		if (start_step_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	public int getRemark_flag() {
		return remark_flag;
	}

	public void setRemark_flag(int remark_flag) {
		this.remark_flag = remark_flag;
	}

	public String getRremark_color() {
		if (remark_flag > 0) {
			return "#FFDDFF";
		} else {
			return "#CCEEFF";
		}
	}

	/**
	 * @return the item_seq
	 */
	public String getItem_seq() {
		return item_seq;
	}

	/**
	 * @param itemSeq the item_seq to set
	 */
	public void setItem_seq(int itemSeq) {
		item_seq =  String.valueOf(itemSeq);
	}

	/**
	 * @return the item_seq_flag
	 */
	public int getItem_seq_flag() {
		return item_seq_flag;
	}

	/**
	 * @param itemSeqFlag the item_seq_flag to set
	 */
	public void setItem_seq_flag(int itemSeqFlag) {
		item_seq_flag = itemSeqFlag;
	}

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getAcs() {
        return acs;
    }

    public void setAcs(String acs) {
        this.acs = acs;
    }

    public String getBy_lot_dg() {
        return by_lot_dg;
    }

    public void setBy_lot_dg(String by_lot_dg) {
        this.by_lot_dg = by_lot_dg;
    }

    public String getDgrade_special_ipn() {
        return dgrade_special_ipn;
    }

    public void setDgrade_special_ipn(String dgrade_special_ipn) {
        this.dgrade_special_ipn = dgrade_special_ipn;
    }

    public int getBy_lot_dg_flag() {
        return by_lot_dg_flag;
    }

    public void setBy_lot_dg_flag(int by_lot_dg_flag) {
        this.by_lot_dg_flag = by_lot_dg_flag;
    }

    public int getDg_action_flag() {
        return dg_action_flag;
    }

    public void setDg_action_flag(int dg_action_flag) {
        this.dg_action_flag = dg_action_flag;
    }
    
    public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDgradeprodcode() {
		return dgradeprodcode;
	}

	public void setDgradeprodcode(String dgradeprodcode) {
		this.dgradeprodcode = dgradeprodcode;
	}

	public String getGroupitems_no() {
		return groupitems_no;
	}

	public void setGroupitems_no(String groupitems_no) {
		this.groupitems_no = groupitems_no;
	}
	
}
