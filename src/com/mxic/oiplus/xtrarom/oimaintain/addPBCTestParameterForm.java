package com.mxic.oiplus.xtrarom.oimaintain;

import org.apache.struts.action.ActionForm;


public class addPBCTestParameterForm extends ActionForm {

	private int sid;
	private String tag;
	private String product_body;
	private String brand;
	private String version;
	private String[] pgm_id;
	private String[] test_type;
	private String[] backend_option;
	private int[] pin_count;
	private String[] package_type;
	private String[] tester;
	private String[] site;
	private String[] program_name;
	private String[] i_grade;
	private String[] c_grade;
	private String[] tf_comment;
	private String[] body_size;
	private String[] actual_file;
	private String listControl;
	private String record_id;
	private String site_str;
	private String[] hw_configure;

	public String[] getHw_configure() {
		return hw_configure;
	}
	public void setHw_configure(String[] hw_configure) {
		this.hw_configure = hw_configure;
	}
	/**
	 * @return Returns the actual_file.
	 */
	public String[] getActual_file() {
		return actual_file;
	}
	/**
	 * @param actual_file The actual_file to set.
	 */
	public void setActual_file(String[] actual_file) {
		this.actual_file = actual_file;
	}
	/**
	 * @return Returns the backend_option.
	 */
	public String[] getBackend_option() {
		return backend_option;
	}
	/**
	 * @param backend_option The backend_option to set.
	 */
	public void setBackend_option(String[] backend_option) {
		this.backend_option = backend_option;
	}
	/**
	 * @return Returns the body_size.
	 */
	public String[] getBody_size() {
		return body_size;
	}
	/**
	 * @param body_size The body_size to set.
	 */
	public void setBody_size(String[] body_size) {
		this.body_size = body_size;
	}
	/**
	 * @return Returns the brand.
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand The brand to set.
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return Returns the c_grade.
	 */
	public String[] getC_grade() {
		return c_grade;
	}
	/**
	 * @param c_grade The c_grade to set.
	 */
	public void setC_grade(String[] c_grade) {
		this.c_grade = c_grade;
	}
	/**
	 * @return Returns the i_grade.
	 */
	public String[] getI_grade() {
		return i_grade;
	}
	/**
	 * @param i_grade The i_grade to set.
	 */
	public void setI_grade(String[] i_grade) {
		this.i_grade = i_grade;
	}
	/**
	 * @return Returns the package_type.
	 */
	public String[] getPackage_type() {
		return package_type;
	}
	/**
	 * @param package_type The package_type to set.
	 */
	public void setPackage_type(String[] package_type) {
		this.package_type = package_type;
	}
	/**
	 * @return Returns the pgm_id.
	 */
	public String[] getPgm_id() {
		return pgm_id;
	}
	/**
	 * @param pgm_id The pgm_id to set.
	 */
	public void setPgm_id(String[] pgm_id) {
		this.pgm_id = pgm_id;
	}
	/**
	 * @return Returns the pin_count.
	 */
	public int[] getPin_count() {
		return pin_count;
	}
	/**
	 * @param pin_count The pin_count to set.
	 */
	public void setPin_count(int[] pin_count) {
		this.pin_count = pin_count;
	}
	/**
	 * @return Returns the product_body.
	 */
	public String getProduct_body() {
		return product_body;
	}
	/**
	 * @param product_body The product_body to set.
	 */
	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}
	/**
	 * @return Returns the program_name.
	 */
	public String[] getProgram_name() {
		return program_name;
	}
	/**
	 * @param program_name The program_name to set.
	 */
	public void setProgram_name(String[] program_name) {
		this.program_name = program_name;
	}
	/**
	 * @return Returns the sid.
	 */
	public int getSid() {
		return sid;
	}
	/**
	 * @param sid The sid to set.
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}
	/**
	 * @return Returns the site.
	 */
	public String[] getSite() {
		return site;
	}
	/**
	 * @param site The site to set.
	 */
	public void setSite(String[] site) {
		this.site = site;
	}
	/**
	 * @return Returns the tag.
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag The tag to set.
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return Returns the test_type.
	 */
	public String[] getTest_type() {
		return test_type;
	}
	/**
	 * @param test_type The test_type to set.
	 */
	public void setTest_type(String[] test_type) {
		this.test_type = test_type;
	}
	/**
	 * @return Returns the tester.
	 */
	public String[] getTester() {
		return tester;
	}
	/**
	 * @param tester The tester to set.
	 */
	public void setTester(String[] tester) {
		this.tester = tester;
	}
	/**
	 * @return Returns the tf_comment.
	 */
	public String[] getTf_comment() {
		return tf_comment;
	}
	/**
	 * @param tf_comment The tf_comment to set.
	 */
	public void setTf_comment(String[] tf_comment) {
		this.tf_comment = tf_comment;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return Returns the listControl.
	 */
	public String getListControl() {
		return listControl;
	}
	/**
	 * @param listControl The listControl to set.
	 */
	public void setListControl(String listControl) {
		this.listControl = listControl;
	}
	/**
	 * @return Returns the record_id.
	 */
	public String getRecord_id() {
		return record_id;
	}
	/**
	 * @param record_id The record_id to set.
	 */
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	/**
	 * @return Returns the site_str.
	 */
	public String getSite_str() {
		return site_str;
	}
	/**
	 * @param site_str The site_str to set.
	 */
	public void setSite_str(String site_str) {
		this.site_str = site_str;
	}
}
