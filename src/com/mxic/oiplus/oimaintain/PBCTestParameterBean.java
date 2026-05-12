package com.mxic.oiplus.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class PBCTestParameterBean extends ActionForm {
	private int sid;
	private String tag;
	private int pgm_id;
	private String product_body;
	private String brand;
	private String version;
	private String test_type;
	private String backend_option;
	private int pin_count;
	private String package_type;
	private String tester;
	private String site;
	private String program_name;
	private String i_grade;
	private String c_grade;
    private String w_grade;
    private String y_grade;
    private String j_grade;
    private String k_grade;
    private String l_grade;
    private String n_grade;
    private String b_grade;
    private String e_grade;
	private String s_grade;
	private String tf_comment;
	private String body_size;
	private String actual_file;
	private String hw_configure;

	public ActionErrors validate(ActionMapping actionMapping,
			HttpServletRequest httpServletRequest) {
		/** @todo: finish this method, this is just the skeleton.*/
		return null;
	}

	public void reset(ActionMapping actionMapping,
			HttpServletRequest servletRequest) {
		try {
			//轉成中文big5編碼
			servletRequest.setCharacterEncoding("big5");
		} catch (UnsupportedEncodingException ex) {
		}
	}

	public String getActual_file() {
		return actual_file;
	}

	public void setActual_file(String actual_file) {
		this.actual_file = actual_file;
	}

	public String getBackend_option() {
		return backend_option;
	}

	public void setBackend_option(String backend_option) {
		this.backend_option = backend_option;
	}

	public String getBody_size() {
		return body_size;
	}

	public void setBody_size(String body_size) {
		this.body_size = body_size;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getC_grade() {
		return c_grade;
	}

	public String getCGrade() {
		return c_grade;
	}

	public void setC_grade(String c_grade) {
		this.c_grade = c_grade;
	}
	
	public String getS_grade() {
		return s_grade;
	}
	
	public String getSGrade() {
		return s_grade;
	}
	
	public void setS_grade(String s_grade) {
		this.s_grade = s_grade;
	}

	public String getI_grade() {
		return i_grade;
	}

	public String getIGrade() {
		return i_grade;
	}

	public void setI_grade(String i_grade) {
		this.i_grade = i_grade;
	}

	public String getPackage_type() {
		return package_type;
	}

	public void setPackage_type(String package_type) {
		this.package_type = package_type;
	}

	public int getPgm_id() {
		return pgm_id;
	}

	public void setPgm_id(int pgm_id) {
		this.pgm_id = pgm_id;
	}

	public int getPin_count() {
		return pin_count;
	}

	public void setPin_count(int pin_count) {
		this.pin_count = pin_count;
	}

	public String getProduct_body() {
		return product_body;
	}

	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}

	public String getProgram_name() {
		return program_name;
	}

	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTest_type() {
		return test_type;
	}

	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}

	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getTf_comment() {
		return tf_comment;
	}

	public void setTf_comment(String tf_comment) {
		this.tf_comment = tf_comment;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getChangecolor(){

		if (tag.equals("1")){
			return "#FFDDFF";
		}else{
			return "#CCEEFF";
		}
	}

	public String getHw_configure() {
		return hw_configure;
	}

	public void setHw_configure(String hw_configure) {
		this.hw_configure = hw_configure;
	}

    public String getW_grade() {
        return w_grade;
    }

    public void setW_grade(String w_grade) {
        this.w_grade = w_grade;
    }

    public String getY_grade() {
        return y_grade;
    }

    public void setY_grade(String y_grade) {
        this.y_grade = y_grade;
    }

    public String getJ_grade() {
        return j_grade;
    }

    public void setJ_grade(String j_grade) {
        this.j_grade = j_grade;
    }

    public String getK_grade() {
        return k_grade;
    }

    public void setK_grade(String k_grade) {
        this.k_grade = k_grade;
    }

    public String getL_grade() {
        return l_grade;
    }

    public void setL_grade(String l_grade) {
        this.l_grade = l_grade;
    }

    public String getN_grade() {
        return n_grade;
    }

    public void setN_grade(String n_grade) {
        this.n_grade = n_grade;
    }

    public String getB_grade() {
        return b_grade;
    }

    public void setB_grade(String b_grade) {
        this.b_grade = b_grade;
    }

    public String getE_grade() {
        return e_grade;
    }

    public void setE_grade(String e_grade) {
        this.e_grade = e_grade;
    }
}
