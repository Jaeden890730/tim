package com.mxic.fw8049.pdf;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.awt.*;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.mxic.oiplus.dao.TfChangeIpnLevelBean;
import com.mxic.oiplus.dao.TfChangeIpnLevelDao;
import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.oisearch.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class pdfService {
	public pdfService() {
	}

	/**
	 * <p>
	 * Title:
	 * </p>
	 *
	 * <p>
	 * Description:Get all the mask_option in the tf_test_parameter_ws table,
	 * and save them in javabean
	 * </p>
	 *
	 */
	public static WsTestBean[] ToGetMask_Option(ProTestRouteBeanAF fm,
			Connection conn, String table) {
		StringBuffer SelSQL = new StringBuffer();
		// StringBuffer sql=new StringBuffer();

		try {
			ArrayList tmp2 = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", fm.getSid());
			if (fm.getVendor() != null)
				whereStem.put("site", fm.getVendor());
			SelSQL.append("SELECT mask_option FROM tf_test_parameter_ws"
					+ table + "  ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("group by mask_option");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				WsTestBean wtb = new WsTestBean();
				wtb.setMask_option(rs.getString("mask_option"));
				tmp2.add(wtb);
			}

			return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return null;
	}

	/**
	 * <p>
	 * Title:
	 * </p>
	 *
	 * <p>
	 * Description:Get all the mask_option in the tf_test_parameter_ws table,
	 * and save them in javabean
	 * </p>
	 *
	 */
	public static FTTFormBean[] ToGetBackend_Option(ProTestRouteBeanAF fm,
			Connection conn, String table) {
		StringBuffer SelSQL = new StringBuffer();
		// StringBuffer sql=new StringBuffer();

		try {
			ArrayList tmp2 = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", fm.getSid());
			if (fm.getVendor() != null)
				whereStem.put("site", fm.getVendor());
			SelSQL.append("SELECT backend_option FROM tf_test_parameter_ft"
					+ table + "  ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("group by backend_option");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				FTTFormBean wtb = new FTTFormBean();
				wtb.setBackend_option(rs.getString("backend_option"));
				tmp2.add(wtb);
			}

			return (FTTFormBean[]) tmp2.toArray(new FTTFormBean[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return null;
	}

	/**
	 * <p>
	 * Title:
	 * </p>
	 *
	 * <p>
	 * Description:Get all the mask_option in the tf_test_parameter_ws table
	 * filtered by vendor name
	 * </p>
	 *
	 */
	public static WsTestBean[] ToGetMask_OptionForVendor(ProTestRouteBeanAF fm,
			Connection conn, String table) {
		StringBuffer SelSQL = new StringBuffer();
		// StringBuffer sql=new StringBuffer();

		try {
			ArrayList tmp2 = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", fm.getSid());
			whereStem.put("site", fm.getVendor());
			SelSQL.append("SELECT mask_option FROM tf_test_parameter_ws"
					+ table + "  ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("group by mask_option");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				WsTestBean wtb = new WsTestBean();

				wtb.setMask_option(rs.getString("mask_option"));
				tmp2.add(wtb);
			}
			return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return null;
	}

	public static String[] GetCoverPage(String pro_b, String br,
			String version, String vendor, Connection conn) {
		String[] rtn = { "", "", "", "" };
		try {
			String sql = "select approve_no,to_char(approve_date,'yyyy/mm/dd') approve_date, replace_tecr_no "
					+ "from tf_coverpage where product_body=? and brand=? and version =? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pro_b);
			ps.setString(2, br);
			ps.setString(3, version);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// rtn[0] = rs.getString("approve_no");
				// if (rtn[0] == null) rtn[0] = "";
				if (br.equals("KH"))
					rtn[0] = "8049K-" + pro_b;
				else
					rtn[0] = "8049-" + pro_b;
				rtn[1] = rs.getString("approve_date");
				rtn[2] = rs.getString("approve_no");
				rtn[3] = rs.getString("replace_tecr_no");
				if (rtn[1] == null)
					rtn[1] = "";
				if (rtn[2] == null)
					rtn[2] = "";
				if (rtn[3] == null)
					rtn[3] = "";
				break;
			}
			ps.clearParameters();
			ps.close();
			rs.close();

			if ((vendor != null) && (rtn[0].length() > 0)) {
				sql = "select tim_short_name short_name from ba_plant where plant_name = '"
						+ vendor + "'";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					String short_name = rs.getString("short_name");
					rtn[0] = rtn[0] + "." + short_name;
					break;
				}
				ps.clearParameters();
				ps.close();
				rs.close();
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		}
		return rtn;
	}

	public static PdfPTable CoverPage(PdfPTable tabExtend, String pro_b,
			String br, String version, Font F, Connection conn,
			boolean subconFlag) {

		try {
			String sql = "select TO_CHAR(approve_date,'YYYY/MM/DD') as aDate,"
					+ "version,approve_no,applicant,remark from tf_coverpage "
					+ "where product_body=? and brand=? and ";
			if (subconFlag)
				sql = sql + "version=? ";
			else
				sql = sql + "version<=? order by version ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pro_b);
			ps.setString(2, br);
			ps.setString(3, version);
			ResultSet rs = ps.executeQuery();
			// while ResultSet.next 時，將撈到的資料依欄位名稱的順序寫入table之中
			while (rs.next()) {
				if (rs.getString("version") == null) {
					tabExtend.addCell(new Phrase(new Chunk(" ", F)));
				} else {
					tabExtend.addCell(new Phrase(new Chunk(rs
							.getString("version"), F)));
				}
				if (rs.getString("approve_no") != null)
					tabExtend.addCell(new Phrase(new Chunk(rs
							.getString("approve_no"), F)));
				else
					tabExtend.addCell(new Phrase(new Chunk(" ", F)));
				if (rs.getString("aDate") != null)
					tabExtend.addCell(new Phrase(new Chunk(rs
							.getString("aDate"), F)));
				else
					tabExtend.addCell(new Phrase(new Chunk(" ", F)));
				/*
				 * if (rs.getString("approve_no") == null &&
				 * rs.getString("aDate") != null){ tabExtend.addCell(new
				 * Phrase(new Chunk(rs.getString("aDate"),F))); } else if
				 * (rs.getString("approve_no") != null && rs.getString("aDate")
				 * == null){ tabExtend.addCell(new Phrase(new
				 * Chunk(rs.getString("approve_no"),F))); } else if
				 * (rs.getString("approve_no") == null && rs.getString("aDate")
				 * == null){ tabExtend.addCell(new Phrase(new Chunk(" ",F))); }
				 * else { tabExtend.addCell(new Phrase(new
				 * Chunk(rs.getString("approve_no") +" & "
				 * +rs.getString("aDate"), F))); }
				 */
				if (rs.getString("applicant") == null) {
					tabExtend.addCell(new Phrase(new Chunk(" ", F)));
				} else {
					tabExtend.addCell(new Phrase(new Chunk(rs
							.getString("applicant"), F)));
				}
				if (rs.getString("remark") == null) {
					tabExtend.addCell(new Phrase(new Chunk(" ", F)));
				} else {
					tabExtend.addCell(new Phrase(new Chunk(rs
							.getString("remark"), F)));
				}
			}
			ps.clearParameters();
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		}
		return tabExtend;
	}

	/**
	 * <p>
	 * Title:
	 * </p>
	 *
	 * <p>
	 * Description:Get all the mask_option in the tf_test_parameter_ws table
	 * filtered by vendor name
	 * </p>
	 *
	 */

	public static void addCell(PdfPTable tabExtend, String info, Font theFont,
			Color color) {
		PdfPCell c1 = new PdfPCell(new Phrase(new Chunk(info, theFont)));
		if (color != null)
			c1.setBackgroundColor(color);
		tabExtend.addCell(c1);
		c1 = null;
	}

	// table = "_tx" for processing/approving oi, "" for released oi
	// vendor = "" for 'ALL' doc, others for individual subcons
	// route type = W (WS) or P (FT)
	public static PdfPTable tf_route_master_WSFT(PdfPTable tabExtend,
			Font theFont, ProTestRouteBeanAF fm, String vendor, String table,
			String route_type, boolean fieldRemark, Connection conn,
			String textFilename) {

		int color = 0;
		FileOutputStream os = null;
		PrintWriter pr = null;

		try {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("select s1.product_body,s1.brand,s1.version,'"
					+ route_type + "' type, s1.route_name\n");
			for (int i = 1; i < 16; i++) {
				sql2.append(",nvl(trim(s" + i + ".step),'－') step" + i
						+ ", nvl(trim(s" + i + ".stepr),'－') step" + i + "r\n");
			}
			sql2.append(",nvl(r.remark, ' ') remark\n");
			sql2.append("from\n");
			for (int i = 1; i < 16; i++) {
				sql2.append(" (select product_body,brand,version,route_name,step_name||','||decode(test_time,null,'',trim(to_char(test_time)))||','||nvl(time_unit,'')||','||tf_get_step1(temperature)||','|| decode(sampling_test,'Y','(抽測)')|| decode(sampling_cond,null,'', '(' ||sampling_cond||')') step,rework_step||' '||decode(test_time2,null,'',trim(to_char(test_time2)))||nvl(time_unit2,'') stepr\n");
				sql2.append(" from tf_product_route" + table);
				sql2.append(" where sid = " + fm.getSid() + " and step_seq = "
						+ i + ") s" + i + ",\n");
				if (i == 15)
					sql2.append("tf_route_master r\n");
			}

			sql2.append(" where\n");
			for (int i = 2; i < 16; i++) {
				if (i != 2)
					sql2.append("and ");
				sql2.append(" s" + i + ".route_name (+)= s1.route_name\n");
			}
			sql2.append(" and s1.route_name = r.route_name\n");
			sql2.append(" and r.type = '" + route_type + "'\n");

			if (route_type.equals("W"))
				sql2.append(" and substr(s1.route_name,2,1) = '" + route_type
						+ "'");
			else
				sql2.append(" and substr(s1.route_name,2,1) in ('P','Q')");

			TDSLogger.println(sql2.toString());
			PreparedStatement ps2 = conn.prepareStatement(sql2.toString());
			ResultSet rs2 = ps2.executeQuery();

			// 20090828 for e8049 data share project, Robin
			StringBuffer e8049share = new StringBuffer("");
			String heading = "8049-"
					+ fm.getProductbody()
					+ "."
					+ OiMaintainService
							.getVendorShortName(conn, fm.getVendor()) + "|"
					+ fm.getVersion() + "|" + fm.getProductbody() + "|"
					+ fm.getBrand() + "|"
					+ (route_type.equals("W") ? "WS|" : "FT|");
			if (table.equals("") && !vendor.equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-Route.txt",
						(route_type.equals("W") ? false : true));
				pr = new PrintWriter(os);
			}
			// select ecrtime for e8049 dat share project, Sophia
			String ecrtime[] = OiMaintainService.getEcrEffectTime(conn,
					fm.getProductbody(), fm.getVersion(), fm.getBrand());

			while (rs2.next()) {
				TFRouteMasterBean bean = new TFRouteMasterBean();
				bean.setType(rs2.getString("type"));
				bean.setRoute_name(rs2.getString("route_name"));
				bean.setStep1(rs2.getString("step1"));
				bean.setStep2(rs2.getString("step2"));
				bean.setStep3(rs2.getString("step3"));
				bean.setStep4(rs2.getString("step4"));
				bean.setStep5(rs2.getString("step5"));
				bean.setStep6(rs2.getString("step6"));
				bean.setStep7(rs2.getString("step7"));
				bean.setStep8(rs2.getString("step8"));
				bean.setStep9(rs2.getString("step9"));
				bean.setStep10(rs2.getString("step10"));
				bean.setStep11(rs2.getString("step11"));
				bean.setStep12(rs2.getString("step12"));
				bean.setStep13(rs2.getString("step13"));
				bean.setStep14(rs2.getString("step14"));
				bean.setStep15(rs2.getString("step15"));

				bean.setStep1r(rs2.getString("step1r"));
				bean.setStep2r(rs2.getString("step2r"));
				bean.setStep3r(rs2.getString("step3r"));
				bean.setStep4r(rs2.getString("step4r"));
				bean.setStep5r(rs2.getString("step5r"));
				bean.setStep6r(rs2.getString("step6r"));
				bean.setStep7r(rs2.getString("step7r"));
				bean.setStep8r(rs2.getString("step8r"));
				bean.setStep9r(rs2.getString("step9r"));
				bean.setStep10r(rs2.getString("step10r"));
				bean.setStep11r(rs2.getString("step11r"));
				bean.setStep12r(rs2.getString("step12r"));
				bean.setStep13r(rs2.getString("step13r"));
				bean.setStep14r(rs2.getString("step14r"));
				bean.setStep15r(rs2.getString("step15r"));
				bean.setRemark(rs2.getString("remark"));

				color ^= 1;

				addCell(tabExtend, bean.getRoute_name(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep1(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep2(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep3(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep4(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep5(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep6(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep7(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep8(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep9(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep10(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep11(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep12(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep13(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep14(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep15(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				if (fieldRemark)
					addCell(tabExtend, bean.getRemark(), theFont,
							(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, "rwk", theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep1r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep2r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep3r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep4r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep5r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep6r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep7r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep8r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep9r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep10r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep11r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep12r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep13r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep14r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));
				addCell(tabExtend, bean.getStep15r(), theFont,
						(color == 0 ? Color.LIGHT_GRAY : null));

				e8049share
						.append(heading)
						.append(bean.getRoute_name() + "|")
						.append(bean.getStep1() + "|")
						.append(bean.getStep2().equals("－") ? "" : bean
								.getStep2())
						.append("|")
						.append(bean.getStep3().equals("－") ? "" : bean
								.getStep3())
						.append("|")
						.append(bean.getStep4().equals("－") ? "" : bean
								.getStep4())
						.append("|")
						.append(bean.getStep5().equals("－") ? "" : bean
								.getStep5())
						.append("|")
						.append(bean.getStep6().equals("－") ? "" : bean
								.getStep6())
						.append("|")
						.append(bean.getStep7().equals("－") ? "" : bean
								.getStep7())
						.append("|")
						.append(bean.getStep8().equals("－") ? "" : bean
								.getStep8())
						.append("|")
						.append(bean.getStep9().equals("－") ? "" : bean
								.getStep9())
						.append("|")
						.append(bean.getStep10().equals("－") ? "" : bean
								.getStep10())
						.append("|")
						.append(bean.getStep11().equals("－") ? "" : bean
								.getStep11())
						.append("|")
						.append(bean.getStep12().equals("－") ? "" : bean
								.getStep12())
						.append("|")
						.append(bean.getStep13().equals("－") ? "" : bean
								.getStep13())
						.append("|")
						.append(bean.getStep14().equals("－") ? "" : bean
								.getStep14())
						.append("|")
						.append(bean.getStep15().equals("－") ? "" : bean
								.getStep15()).append("|");
				e8049share
						.append(bean.getStep1r().equals("－") ? "" : bean
								.getStep1r())
						.append("|")
						.append(bean.getStep2r().equals("－") ? "" : bean
								.getStep2r())
						.append("|")
						.append(bean.getStep3r().equals("－") ? "" : bean
								.getStep3r())
						.append("|")
						.append(bean.getStep4r().equals("－") ? "" : bean
								.getStep4r())
						.append("|")
						.append(bean.getStep5r().equals("－") ? "" : bean
								.getStep5r())
						.append("|")
						.append(bean.getStep6r().equals("－") ? "" : bean
								.getStep6r())
						.append("|")
						.append(bean.getStep7r().equals("－") ? "" : bean
								.getStep7r())
						.append("|")
						.append(bean.getStep8r().equals("－") ? "" : bean
								.getStep8r())
						.append("|")
						.append(bean.getStep9r().equals("－") ? "" : bean
								.getStep9r())
						.append("|")
						.append(bean.getStep10r().equals("－") ? "" : bean
								.getStep10r())
						.append("|")
						.append(bean.getStep11r().equals("－") ? "" : bean
								.getStep11r())
						.append("|")
						.append(bean.getStep12r().equals("－") ? "" : bean
								.getStep12r())
						.append("|")
						.append(bean.getStep13r().equals("－") ? "" : bean
								.getStep13r())
						.append("|")
						.append(bean.getStep14r().equals("－") ? "" : bean
								.getStep14r())
						.append("|")
						.append(bean.getStep15r().equals("－") ? "" : bean
								.getStep15r()).append("|");

				e8049share.append(ecrtime[0] + "|");
				e8049share.append(ecrtime[1] + "|");
				// 20090828, here should output e8049share string to file
				if (pr != null)
					pr.println(e8049share.toString());
				e8049share.delete(0, e8049share.length());

				if (fieldRemark)
					addCell(tabExtend, "", theFont,
							(color == 0 ? Color.LIGHT_GRAY : null));
			}
			rs2.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}
		return tabExtend;
	}

	// Write Product Group vs Route table
	// if vendor is empty, then output all information
	// if textFilename is not "",
	public static int WS_Product_Route(Document document, Font BasicFont,
			Font SmallFont, ProTestRouteBeanAF fm, String vendor, String table,
			Connection conn, String textFilename) {
		int result = 0;
		FileOutputStream os = null;
		PrintWriter pr = null;

		try {
			PdfPTable table1 = null;
			Paragraph sec = null;
			String productType = OiMaintainService.getProductType(conn,
					fm.getSid());

			String sql = "select distinct "
					+ "'Main' route_type, a.product_body,a.brand,a.version,a.mask_option,a.db_with_code,a.ws_route,a.ws_route_add,"
					+ "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,decode(a.ws_route_add,null,a.tf_ws_comment,'') tf_ws_comment , a.sales_form, c.hw_configure, c.pgm_special_control, a.mcp_flag "
					+ "from tf_bom_route" + table
					+ " a, tf_route_master_ws_view b, tf_test_parameter_ws"
					+ table + " c " + "where a.sid = ? "
					+ "and trim(ws_route) is not null and ws_route != 'NA' "
					+ "and a.ws_route = b.route_name " + "and a.sid = c.sid "
					+ "and a.mask_option = c.mask_option "
					+ "and b.test_mode = c.test_type " + "and a.tag != 2 ";
			if (!vendor.equals(""))
				sql = sql + "and c.site = '" + vendor + "' ";

			sql = sql
					+ "union "
					+ "select distinct "
					+ "'Add.' route_type, a.product_body,a.brand,a.version,a.mask_option,a.db_with_code,a.ws_route,a.ws_route_add, "
					+ "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,a.tf_ws_comment, a.sales_form, c.hw_configure, c.pgm_special_control, a.mcp_flag "
					+ "from tf_bom_route"
					+ table
					+ " a, tf_route_master_ws_view b, tf_test_parameter_ws"
					+ table
					+ " c "
					+ "where a.sid = ? "
					+ "and trim(ws_route_add) is not null and ws_route_add != 'NA' "
					+ "and a.ws_route_add = b.route_name "
					+ "and a.sid = c.sid "
					+ "and a.mask_option = c.mask_option "
					+ "and b.test_mode = c.test_type " + "and a.tag != 2 ";
			if (!vendor.equals(""))
				sql = sql + "and c.site = '" + vendor + "' ";

			sql = sql
					+ "order by product_body,mask_option,db_with_code,ws_route,ws_route_add,route_type desc,test_mode ";

			TDSLogger.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, String.valueOf(fm.getSid()));
			ps.setString(2, String.valueOf(fm.getSid()));
			ResultSet rs = ps.executeQuery();

			String preGroupKey = "";
			String curGroupKey = null;
			String speedList = "";
			String maxSite = "";
			int section = 0;

			// 20090825 for e8049 data share project, Robin
			StringBuffer e8049share = new StringBuffer("");
			String heading = "8049-"
					+ fm.getProductbody()
					+ "."
					+ OiMaintainService
							.getVendorShortName(conn, fm.getVendor()) + "|"
					+ fm.getVersion() + "|" + "WS|" + fm.getBrand() + "|";
			// released OI, Subcons PDF 才產生 text file
			if (table.equals("") && !vendor.equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-PGM.txt");
				pr = new PrintWriter(os);
			}
			// select ecrtime for e8049 dat share project, Sophia
			String ecrtime[] = OiMaintainService.getEcrEffectTime(conn,
					fm.getProductbody(), fm.getVersion(), fm.getBrand());

			while (rs.next()) {
				if (result == 0)
					document.add(new Paragraph(new Chunk("1-1. WS ROUTE",
							BasicFont)));
				result++;
				curGroupKey = null;
				curGroupKey = OiMaintainService.getGroupKey(productType,
						rs.getString("product_body"), rs.getString("brand"),
						rs.getString("mask_option"), null, null,
						rs.getString("db_with_code"), rs.getString("ws_route"),
						rs.getString("ws_route_add"),
						rs.getString("sales_form"), 0);
				if (!curGroupKey.equals(preGroupKey)) {
					if (section > 0) {
						document.add(table1);
					}
					speedList = null;
					speedList = OiMaintainService.getSpeedList(conn,
							rs.getString("product_body"),
							rs.getString("brand"), rs.getString("version"),
							rs.getString("ws_route"),
							rs.getString("ws_route_add"), table, "ws");
					section++;
					sec = null;
					if (speedList.equals(""))
						sec = new Paragraph(new Chunk("1-1-" + section
								+ ". Product Group Key - " + curGroupKey,
								BasicFont));
					else
						sec = new Paragraph(new Chunk(
								"1-1-" + section + ". Product Group Key - "
										+ curGroupKey
										+ " (Grade:I/C/W/Y/S, Speed:"
										+ speedList + ")", BasicFont));
					sec.setSpacingAfter(5);
					document.add(sec);
					table1 = null;
					// M200712056,20080130
					if (vendor.equals("")) {
						float[] widths = { 5, 5, 5, 10, 8, 5, 11, 8, 8, 16, 15,
								17 };// 23-->18,24-->19, add 10
						table1 = new PdfPTable(widths);
					} else {
						float[] widths = { 5, 5, 5, 12, 8, 5, 16, 8, 10, 15, 26 };// 20-->15,33-->28,
																					// add
																					// 10
						table1 = new PdfPTable(widths);
					}
					table1.setWidthPercentage(100);
					table1.setSpacingBefore(0);
					table1.addCell(new Phrase(
							new Chunk("Route Type", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Route", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Tester", SmallFont))); // 10
					table1.addCell(new Phrase(new Chunk("Site", SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("PGM Name", SmallFont))); // 10
																					// ->
																					// 11
					table1.addCell(new Phrase(new Chunk("Temperature",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("HW Configure",
							SmallFont))); // 10
					table1.addCell(new Phrase(new Chunk("PGM Special Control",
							SmallFont))); // 10
					table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont))); // 18
																					// ->
																					// 16

					// M200712056,20080130
					if (vendor.equals(""))
						table1.addCell(new Phrase(new Chunk("Route Comment",
								SmallFont))); // 19 -> 17
					// table1.addCell(new Phrase(new
					// Chunk("MCP Flag",SmallFont)));

					preGroupKey = curGroupKey;
				}

				table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),
						SmallFont)));
				if (rs.getString("route_type").equals("Main")) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ws_route"), SmallFont)));
				} else {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ws_route_add"), SmallFont)));
				}
				table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),
						SmallFont)));
				String temperature = " ";
				if (rs.getString("temperature") != null) {
					temperature = rs.getString("temperature");
					if (!temperature.equals("ROOM TEMP")
							&& !temperature.equals(""))
						temperature = temperature + "℃";
				}
				table1.addCell(new Phrase(new Chunk(rs.getString("tester"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("site"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("program_name"), SmallFont)));
				table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
				// HW Configure
				String hw_configure_split = PDFdiffService
						.getStringDataSplit(rs.getString("hw_configure"));
				table1.addCell(new Phrase(hw_configure_split, SmallFont));
				if (rs.getString("pgm_special_control") != null) {
					table1.addCell(new Phrase(rs
							.getString("pgm_special_control"), SmallFont));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}
				if (rs.getString("tf_comment") != null) {
					table1.addCell(new Phrase(rs.getString("tf_comment"),
							SmallFont));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}

				// M200712056,20080130
				if (vendor.equals("")) {
					if (rs.getString("tf_ws_comment") != null) {
						table1.addCell(new Phrase(
								rs.getString("tf_ws_comment"), SmallFont));
					} else {
						table1.addCell(new Phrase(" ", SmallFont));
					}
				}

				/*
				 * if (rs.getString("mcp_flag") != null){ table1.addCell(new
				 * Phrase(rs.getString("mcp_flag"), SmallFont)); } else {
				 * table1.addCell(new Phrase(" ", SmallFont)); }
				 */

				// 20090825, output e8049share string to file
				maxSite = "";
				maxSite = OiMaintainService.getMaxsite(conn, "PROD",
						rs.getString("pgm_id"));
				if (pr != null) {
					e8049share.append(heading + curGroupKey + "|");
					e8049share.append(rs.getString("route_type") + "|");
					if (rs.getString("route_type").equals("Main"))
						e8049share.append(rs.getString("ws_route") + "|");
					else
						e8049share.append(rs.getString("ws_route_add") + "|");
					e8049share.append(rs.getString("test_mode") + "||||||");
					e8049share.append(temperature + "||");
					e8049share.append(rs.getString("tester") + "|");
					e8049share.append(rs.getString("site") + "|");
					e8049share.append(rs.getString("program_name") + "||");
					if (rs.getString("tf_comment") != null)
						e8049share.append(rs.getString("tf_comment"));
					e8049share.append("||");
					e8049share.append("|||||");// AEB GRADE (S/P/Q/R/T)
					e8049share.append(ecrtime[0] + "|");
					e8049share.append(ecrtime[1] + "|");
					e8049share.append("NA℃|");// W
					e8049share.append("NA℃|");// Y
					e8049share.append("NA℃|");// J
					e8049share.append("NA℃|");// K
					e8049share.append("NA℃|");// L
					e8049share.append("NA℃|");// N
					e8049share.append("NA℃|");// B
					e8049share.append("NA℃|");// E
					e8049share.append("NA℃|");// AEB GRADE (U)
					e8049share.append(maxSite + "|");// MAX_SITE
					if (rs.getString("pgm_special_control") != null)
						e8049share.append(rs.getString("pgm_special_control")
								+ "|");
					else
						e8049share.append("|");
					pr.println(e8049share.toString());
					e8049share.delete(0, e8049share.length());
				}
			}
			if (table1 != null)
				document.add(table1);
			ps.clearParameters();
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return -1;
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}
		return result;
	}

	public static int WS_Product_Route_Mcp(Document document, Font BasicFont,
			Font SmallFont, ProTestRouteBeanAF fm, String vendor, String table,
			Connection conn, String textFilename) {
		int result = 0;
		FileOutputStream os = null;
		PrintWriter pr = null;

		try {
			PdfPTable table1 = null;
			Paragraph sec = null;
			String productType = OiMaintainService.getProductType(conn,
					fm.getSid());

			String sql = "select distinct "
					+ "'Main' route_type, a.product_body,a.brand,a.version,a.db_with_code,a.ws_route,a.ws_route_add,"
					+ "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,decode(a.ws_route_add,null,a.tf_ws_comment,'') tf_ws_comment , a.sales_form, c.hw_configure, c.pgm_special_control, "
					+ "a.component_no, a.com_prod_body, a.com_mask_option, a.com_backend_option"
					+ "from tf_bom_route_mcp" + table
					+ " a, tf_route_master_ws_view b, tf_test_parameter_ws"
					+ table + " c " + "where a.sid = ? "
					+ "and trim(ws_route) is not null and ws_route != 'NA' "
					+ "and a.ws_route = b.route_name " + "and a.sid = c.sid "
					+ "and a.com_mask_option = c.mask_option "
					+ "and b.test_mode = c.test_type " + "and a.tag != 2 ";
			if (!vendor.equals(""))
				sql = sql + "and c.site = '" + vendor + "' ";

			sql = sql
					+ "union "
					+ "select distinct "
					+ "'Add.' route_type, a.product_body,a.brand,a.version,a.db_with_code,a.ws_route,a.ws_route_add, "
					+ "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,a.tf_ws_comment, a.sales_form, c.hw_configure, c.pgm_special_control, "
					+ "a.component_no, a.com_prod_body, a.com_mask_option, a.com_backend_option"
					+ "from tf_bom_route_mcp"
					+ table
					+ " a, tf_route_master_ws_view b, tf_test_parameter_ws"
					+ table
					+ " c "
					+ "where a.sid = ? "
					+ "and trim(ws_route_add) is not null and ws_route_add != 'NA' "
					+ "and a.ws_route_add = b.route_name "
					+ "and a.sid = c.sid "
					+ "and a.com_mask_option = c.mask_option "
					+ "and b.test_mode = c.test_type " + "and a.tag != 2 ";
			if (!vendor.equals(""))
				sql = sql + "and c.site = '" + vendor + "' ";

			sql = sql
					+ "order by product_body,db_with_code,ws_route,ws_route_add,route_type desc,test_mode, component_no ";

			TDSLogger.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, String.valueOf(fm.getSid()));
			ps.setString(2, String.valueOf(fm.getSid()));
			ResultSet rs = ps.executeQuery();

			String preGroupKey = "";
			String curGroupKey = null;
			String speedList = "";
			String maxSite = "";
			int section = 0;

			// 20090825 for e8049 data share project, Robin
			StringBuffer e8049share = new StringBuffer("");
			String heading = "8049-"
					+ fm.getProductbody()
					+ "."
					+ OiMaintainService
							.getVendorShortName(conn, fm.getVendor()) + "|"
					+ fm.getVersion() + "|" + "WS|" + fm.getBrand() + "|";
			// released OI, Subcons PDF 才產生 text file
			if (table.equals("") && !vendor.equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-PGM.txt");
				pr = new PrintWriter(os);
			}
			// select ecrtime for e8049 dat share project, Sophia
			String ecrtime[] = OiMaintainService.getEcrEffectTime(conn,
					fm.getProductbody(), fm.getVersion(), fm.getBrand());

			while (rs.next()) {
				if (result == 0)
					document.add(new Paragraph(new Chunk("1-1. WS ROUTE",
							BasicFont)));
				result++;
				curGroupKey = null;
				curGroupKey = OiMaintainService.getGroupKey(productType,
						rs.getString("product_body"), rs.getString("brand"),
						rs.getString("com_mask_option"), null, null,
						rs.getString("db_with_code"), rs.getString("ws_route"),
						rs.getString("ws_route_add"),
						rs.getString("sales_form"), 0);
				if (!curGroupKey.equals(preGroupKey)) {
					if (section > 0) {
						document.add(table1);
					}
					speedList = null;
					speedList = OiMaintainService.getSpeedList(conn,
							rs.getString("product_body"),
							rs.getString("brand"), rs.getString("version"),
							rs.getString("ws_route"),
							rs.getString("ws_route_add"), table, "ws");
					section++;
					sec = null;
					if (speedList.equals(""))
						sec = new Paragraph(new Chunk("1-1-" + section
								+ ". Product Group Key - " + curGroupKey,
								BasicFont));
					else
						sec = new Paragraph(new Chunk(
								"1-1-" + section + ". Product Group Key - "
										+ curGroupKey
										+ " (Grade:I/C/W/Y/S, Speed:"
										+ speedList + ")", BasicFont));
					sec.setSpacingAfter(5);
					document.add(sec);
					table1 = null;
					// M200712056,20080130
					if (vendor.equals("")) {
						float[] widths = { 5, 5, 5, 10, 8, 5, 11, 8, 8, 16, 15,
								17 };// 23-->18,24-->19, add 10
						table1 = new PdfPTable(widths);
					} else {
						float[] widths = { 5, 5, 5, 12, 8, 5, 16, 8, 10, 15, 26 };
						// 20-->15,33-->28, add 10
						table1 = new PdfPTable(widths);
					}
					table1.setWidthPercentage(100);
					table1.setSpacingBefore(0);
					table1.addCell(new Phrase(
							new Chunk("Route Type", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Route", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Tester", SmallFont))); // 10
					table1.addCell(new Phrase(new Chunk("Site", SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("PGM Name", SmallFont))); // 10
																					// ->
																					// 11
					table1.addCell(new Phrase(new Chunk("Temperature",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("HW Configure",
							SmallFont))); // 10
					table1.addCell(new Phrase(new Chunk("PGM Special Control",
							SmallFont))); // 10
					table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont))); // 18
																					// ->
																					// 16

					// M200712056,20080130
					if (vendor.equals(""))
						table1.addCell(new Phrase(new Chunk("Route Comment",
								SmallFont))); // 19 -> 17
					preGroupKey = curGroupKey;
				}

				table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),
						SmallFont)));
				if (rs.getString("route_type").equals("Main")) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ws_route"), SmallFont)));
				} else {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ws_route_add"), SmallFont)));
				}
				table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),
						SmallFont)));
				String temperature = " ";
				if (rs.getString("temperature") != null) {
					temperature = rs.getString("temperature");
					if (!temperature.equals("ROOM TEMP")
							&& !temperature.equals(""))
						temperature = temperature + "℃";
				}
				table1.addCell(new Phrase(new Chunk(rs.getString("tester"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("site"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("program_name"), SmallFont)));
				table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
				// HW Configure
				String hw_configure_split = PDFdiffService
						.getStringDataSplit(rs.getString("hw_configure"));
				table1.addCell(new Phrase(hw_configure_split, SmallFont));
				if (rs.getString("pgm_special_control") != null) {
					table1.addCell(new Phrase(rs
							.getString("pgm_special_control"), SmallFont));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}
				if (rs.getString("tf_comment") != null) {
					table1.addCell(new Phrase(rs.getString("tf_comment"),
							SmallFont));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}

				// M200712056,20080130
				if (vendor.equals("")) {
					if (rs.getString("tf_ws_comment") != null) {
						table1.addCell(new Phrase(
								rs.getString("tf_ws_comment"), SmallFont));
					} else {
						table1.addCell(new Phrase(" ", SmallFont));
					}
				}
				maxSite = "";
				maxSite = OiMaintainService.getMaxsite(conn, "PROD",
						rs.getString("pgm_id"));

				// 20090825, output e8049share string to file
				if (pr != null) {
					e8049share.append(heading + curGroupKey + "|");
					e8049share.append(rs.getString("route_type") + "|");
					if (rs.getString("route_type").equals("Main"))
						e8049share.append(rs.getString("ws_route") + "|");
					else
						e8049share.append(rs.getString("ws_route_add") + "|");
					e8049share.append(rs.getString("test_mode") + "||||||");
					e8049share.append(temperature + "||");
					e8049share.append(rs.getString("tester") + "|");
					e8049share.append(rs.getString("site") + "|");
					e8049share.append(rs.getString("program_name") + "||");
					if (rs.getString("tf_comment") != null)
						e8049share.append(rs.getString("tf_comment"));
					e8049share.append("||");
					e8049share.append("|||||");// AEB GRADE(S/P/Q/R/T)
					e8049share.append(ecrtime[0] + "|");
					e8049share.append(ecrtime[1] + "|");
					e8049share.append("NA℃|");// W
					e8049share.append("NA℃|");// Y
					e8049share.append("NA℃|");// J
					e8049share.append("NA℃|");// K
					e8049share.append("NA℃|");// L
					e8049share.append("NA℃|");// N
					e8049share.append("NA℃|");// B
					e8049share.append("NA℃|");// E
					e8049share.append("NA℃|");// AEB GRADE (U)
					e8049share.append(maxSite + "|");// MAX_SITE
					if (rs.getString("pgm_special_control") != null)
						e8049share.append(rs.getString("pgm_special_control")
								+ "|");
					else
						e8049share.append("|");
					pr.println(e8049share.toString());
					e8049share.delete(0, e8049share.length());
				}
			}
			if (table1 != null)
				document.add(table1);
			ps.clearParameters();
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return -1;
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}
		return result;
	}

	// List AVI Vendors
	public static int AVI_List(Document doc, Font BasicFont, Font SmallFont,
			String section, String sid, String vendor, String table,
			Connection conn) {

		int result = 0;
		try {
			String sql = "SELECT * FROM tf_test_parameter_pbc" + table
					+ " where sid=? AND test_type='AVI' ";
			if (!vendor.equals(""))
				sql += " AND SITE = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			if (!vendor.equals(""))
				ps.setString(2, vendor);
			ResultSet rs = ps.executeQuery();
			String vendor_list = "Vendor list: \t";
			int i = 0;
			while (rs.next()) {
				result++;
				if (i == 0)
					doc.add(new Paragraph(new Chunk(section
							+ ". AVI Release Vendor", BasicFont)));
				if (++i > 1)
					vendor_list += ",";
				vendor_list = vendor_list + rs.getString("SITE");
			}
			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
			if (i > 0)
				doc.add(new Paragraph(new Chunk(vendor_list, BasicFont)));
			return result;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return -1;
		}
	}

	public static int FT_Product_Route(Document document, Font BasicFont,
			Font SmallFont, String section, ProTestRouteBeanAF fm,
			String vendor, String table, Connection conn, String textFilename) {
		int result = 0;
		FileOutputStream os = null;
		PrintWriter pr = null;

		try {
			PdfPTable table1 = null;
			Paragraph sec = null;
			String productType = OiMaintainService.getProductType(conn,
					fm.getSid());

			StringBuffer sql = new StringBuffer();
			sql.append("select distinct ");
			sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,''ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,decode(a.ft_route_add,null,a.tf_comment,'') tf_ft_comment,c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d , tf_product_route"
					+ table + " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
			sql.append("and a.ft_route = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route) ) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,''ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,decode(a.ft_route_add,null,a.tf_comment,'') tf_ft_comment,c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d , tf_product_route"
					+ table + " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
			sql.append("and a.ft_route = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ( (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");

			sql.append("select distinct ");
			sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d, tf_product_route" + table
					+ " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
			sql.append("and a.ft_route_add = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add2 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d, tf_product_route" + table
					+ " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
			sql.append("and a.ft_route_add2 = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add2) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add3 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d, tf_product_route" + table
					+ " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
			sql.append("and a.ft_route_add3 = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add3) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union ");
			sql.append("select distinct ");
			sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,'' ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,decode(a.ft_route_add,null,a.tf_comment,'') tf_ft_comment,c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
			sql.append("and a.ft_route = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
			sql.append("and a.ft_route_add = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add2 ft_route_add ,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
			sql.append("and a.ft_route_add2 = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add3 ft_route_add ,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
			sql.append("and a.ft_route_add3 = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			// sql.append("and nvl(a.mcp_flag,' ') != 'MCP' ");
			sql.append("AND (a.mcp_flag is null or a.mcp_flag != 'MCP') ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("order by product_body,backend_option,package_code,pin_count,fg_with_code,ft_route,ft_route_add,route_type desc,test_mode ");

			TDSLogger.println(sql.toString());
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, String.valueOf(fm.getSid()));
			ps.setString(2, String.valueOf(fm.getSid()));
			ps.setString(3, String.valueOf(fm.getSid()));
			ps.setString(4, String.valueOf(fm.getSid()));
			ps.setString(5, String.valueOf(fm.getSid()));
			ps.setString(6, String.valueOf(fm.getSid()));
			ps.setString(7, String.valueOf(fm.getSid()));
			ps.setString(8, String.valueOf(fm.getSid()));
			ps.setString(9, String.valueOf(fm.getSid()));
			ResultSet rs = ps.executeQuery();

			String preGroupKey = "";
			String curGroupKey = null;
			String i_grade_temperature = null;
			String c_grade_temperature = null;
			String w_grade_temperature = null;
			String y_grade_temperature = null;
			String j_grade_temperature = null;
			String k_grade_temperature = null;
			String l_grade_temperature = null;
			String n_grade_temperature = null;
			String b_grade_temperature = null;
			String e_grade_temperature = null;
			String s_grade_temperature = null;
			String hw_configure_split = null;
			String speedList = null;
			String maxSite = "";
			int subsection = 0;

			// 20090825 for e8049 data share project, Robin
			StringBuffer e8049share = new StringBuffer("");
			String heading = "8049-"
					+ fm.getProductbody()
					+ "."
					+ OiMaintainService
							.getVendorShortName(conn, fm.getVendor()) + "|"
					+ fm.getVersion() + "|" + "FT|" + fm.getBrand() + "|";
			// released OI, Subcons PDF 才產生 text file
			if (table.equals("") && !vendor.equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-PGM.txt", true);
				pr = new PrintWriter(os);
			}
			// select ecrtime for e8049 dat share project, Sophia
			String ecrtime[] = OiMaintainService.getEcrEffectTime(conn,
					fm.getProductbody(), fm.getVersion(), fm.getBrand());

			while (rs.next()) {
				if (result == 0)
					document.add(new Paragraph(new Chunk("1-" + section
							+ ". FT ROUTE", BasicFont)));
				result++;
				curGroupKey = OiMaintainService.getGroupKey(productType,
						rs.getString("product_body"), rs.getString("brand"),
						rs.getString("backend_option"),
						rs.getString("package_code"),
						rs.getString("pin_count"),
						rs.getString("fg_with_code"), rs.getString("ft_route"),
						rs.getString("ft_route_add"), null, 1);
				if (!curGroupKey.equals(preGroupKey)) {
					if (subsection > 0) {
						document.add(table1);
					}
					subsection++;
					speedList = null;
					speedList = OiMaintainService.getSpeedList(conn,
							rs.getString("product_body"),
							rs.getString("brand"), rs.getString("version"),
							rs.getString("ft_route"),
							rs.getString("ft_route_add"), table, "ft");
					sec = null;
					if (speedList.equals(""))
						sec = new Paragraph(new Chunk("1-" + section + "-"
								+ subsection + ". Product Group Key - "
								+ curGroupKey, BasicFont));
					else
						sec = new Paragraph(new Chunk("1-" + section + "-"
								+ subsection + ". Product Group Key - "
								+ curGroupKey + " (Grade:I/C/W/Y/S, Speed:"
								+ speedList + ")", BasicFont));
					sec.setSpacingAfter(5);
					document.add(sec);
					table1 = null;
					// M200712056,20080130
					if (vendor.equals("")) {
						float[] widths = { 5, 5, 4, 4, 5, 5, 8, 7, 7, 10, 6, 5,
								8, 8, 8, 7, 7, 8 };// 11-->9,11-->9,10-->8,10-->8add
													// 8
						table1 = new PdfPTable(widths);
					} else {
						float[] widths = { 5, 5, 4, 4, 5, 5, 8, 7, 7, 10, 8, 5,
								10, 8, 8, 9, 9 };// 11-->10,9-->8,13-->11,
													// 13-->11,12-->11,add 8
						table1 = new PdfPTable(widths);
					}
					table1.setWidthPercentage(100);
					table1.setSpacingBefore(0);
					table1.addCell(new Phrase(
							new Chunk("Route Type", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Route", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Pkg Code", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Pkg Name", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk(
							"Noraml Grade Temperature", SmallFont))); // 5
					/*
					 * table1.addCell(new Phrase(new
					 * Chunk("C Grade",SmallFont))); //5 table1.addCell(new
					 * Phrase(new Chunk("W Grade",SmallFont))); //5
					 * table1.addCell(new Phrase(new
					 * Chunk("Y Grade",SmallFont))); //5 table1.addCell(new
					 * Phrase(new Chunk("J Grade",SmallFont))); //5
					 * table1.addCell(new Phrase(new
					 * Chunk("K Grade",SmallFont))); //5
					 */
					table1.addCell(new Phrase(new Chunk(
							"AEB Grade Temperature", SmallFont))); // 7
					table1.addCell(new Phrase(new Chunk("Body Size", SmallFont))); // 7
					table1.addCell(new Phrase(new Chunk("Tester", SmallFont))); // 9
					table1.addCell(new Phrase(new Chunk("Site", SmallFont))); // 6
					table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Test Program Name",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("Actual Program Name",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("PGM Special Control",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("HW Configure",
							SmallFont))); // 7
					table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont))); // 7

					// M200712056,20080130
					if (vendor.equals(""))
						table1.addCell(new Phrase(new Chunk("Route Comment",
								SmallFont))); // 8

					preGroupKey = curGroupKey;
				}

				table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),
						SmallFont)));
				if (rs.getString("route_type").equals("Main")) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ft_route"), SmallFont)));
				} else {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ft_route_add"), SmallFont)));
				}
				table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("package_code"), SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("package_name"), SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("pin_count"),
						SmallFont)));

				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(i_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(c_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(w_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(y_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(j_grade_temperature,SmallFont)));
				 */

				i_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("i_grade"));
				if (i_grade_temperature != null
						&& !i_grade_temperature.equals("NA℃")
						&& !i_grade_temperature.equals("")) {
					i_grade_temperature = "I:" + i_grade_temperature + "\n";
				} else {
					i_grade_temperature = "";
				}

				c_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("c_grade"));
				if (c_grade_temperature != null
						&& !c_grade_temperature.equals("NA℃")
						&& !c_grade_temperature.equals("")) {
					c_grade_temperature = "C:" + c_grade_temperature + "\n";
				} else {
					c_grade_temperature = "";
				}

				w_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("w_grade"));
				if (w_grade_temperature != null
						&& !w_grade_temperature.equals("NA℃")
						&& !w_grade_temperature.equals("")) {
					w_grade_temperature = "W:" + w_grade_temperature + "\n";
				} else {
					w_grade_temperature = "";
				}

				y_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("y_grade"));
				if (y_grade_temperature != null
						&& !y_grade_temperature.equals("NA℃")
						&& !y_grade_temperature.equals("")) {
					y_grade_temperature = "Y:" + y_grade_temperature + "\n";
				} else {
					y_grade_temperature = "";
				}

				j_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("j_grade"));
				if (j_grade_temperature != null
						&& !j_grade_temperature.equals("NA℃")
						&& !j_grade_temperature.equals("")) {
					j_grade_temperature = "J:" + j_grade_temperature + "\n";
				} else {
					j_grade_temperature = "";
				}

				k_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("k_grade"));
				if (k_grade_temperature != null
						&& !k_grade_temperature.equals("NA℃")
						&& !k_grade_temperature.equals("")) {
					k_grade_temperature = "K:" + k_grade_temperature + "\n";
				} else {
					k_grade_temperature = "";
				}

				l_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("l_grade"));
				if (l_grade_temperature != null
						&& !l_grade_temperature.equals("NA℃")
						&& !l_grade_temperature.equals("")) {
					l_grade_temperature = "L:" + l_grade_temperature + "\n";
				} else {
					l_grade_temperature = "";
				}

				n_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("n_grade"));
				if (n_grade_temperature != null
						&& !n_grade_temperature.equals("NA℃")
						&& !n_grade_temperature.equals("")) {
					n_grade_temperature = "N:" + n_grade_temperature + "\n";
				} else {
					n_grade_temperature = "";
				}

				b_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("b_grade"));
				if (b_grade_temperature != null
						&& !b_grade_temperature.equals("NA℃")
						&& !b_grade_temperature.equals("")) {
					b_grade_temperature = "B:" + b_grade_temperature + "\n";
				} else {
					b_grade_temperature = "";
				}

				e_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("e_grade"));
				if (e_grade_temperature != null
						&& !e_grade_temperature.equals("NA℃")
						&& !e_grade_temperature.equals("")) {
					e_grade_temperature = "E:" + e_grade_temperature + "\n";
				} else {
					e_grade_temperature = "";
				}

				String grade = i_grade_temperature + c_grade_temperature
						+ w_grade_temperature + y_grade_temperature
						+ j_grade_temperature + k_grade_temperature
						+ l_grade_temperature + n_grade_temperature
						+ b_grade_temperature + e_grade_temperature;
				table1.addCell(new Phrase(new Chunk(grade, SmallFont)));
				s_grade_temperature = PDFdiffService.getTemperatureSplit(rs
						.getString("s_grade"));
				table1.addCell(new Phrase(new Chunk(s_grade_temperature,
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(PDFdiffService
						.NullConvert(rs.getString("body_size")), SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("tester"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("site"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("program_name"), SmallFont)));
				if (rs.getString("actual_file") != null) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("actual_file"), SmallFont)));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}
				if (rs.getString("pgm_special_control") != null) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("pgm_special_control"), SmallFont)));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}
				hw_configure_split = PDFdiffService.getStringDataSplit(rs
						.getString("hw_configure"));
				table1.addCell(new Phrase(new Chunk(hw_configure_split,
						SmallFont)));
				if (rs.getString("tf_comment") != null) {
					table1.addCell(new Phrase(rs.getString("tf_comment"),
							SmallFont));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}

				// M200712056,20080130
				if (vendor.equals("")) {
					if (rs.getString("tf_ft_comment") != null) {
						table1.addCell(new Phrase(
								rs.getString("tf_ft_comment"), SmallFont));
					} else {
						table1.addCell(new Phrase(" ", SmallFont));
					}
				}
				maxSite = "";
				maxSite = OiMaintainService.getMaxsite(conn, "PROD",
						rs.getString("pgm_id"));

				// 20090831, write to text file
				if (pr != null) {
					e8049share.append(heading + curGroupKey + "|");
					e8049share.append(rs.getString("route_type") + "|");
					if (rs.getString("route_type").equals("Main"))
						e8049share.append(rs.getString("ft_route") + "|");
					else
						e8049share.append(rs.getString("ft_route_add") + "|");

					e8049share.append(rs.getString("test_mode") + "|");
					e8049share.append(rs.getString("package_code") + "|");
					e8049share.append(rs.getString("package_name") + "|");
					e8049share.append(rs.getString("pin_count") + "||");
					e8049share.append(((i_grade_temperature == "") ? "NA℃"
							: i_grade_temperature).replaceAll("I:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((c_grade_temperature == "") ? "NA℃"
							: c_grade_temperature).replaceAll("C:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(PDFdiffService.NullConvert(rs
							.getString("body_size")) + "|");
					e8049share.append(rs.getString("tester") + "|");
					e8049share.append(rs.getString("site") + "|");
					e8049share.append(rs.getString("program_name") + "|");
					if (rs.getString("actual_file") != null)
						e8049share.append(rs.getString("actual_file") + "|");
					else
						e8049share.append("|");
					if (rs.getString("tf_comment") != null)
						e8049share.append(rs.getString("tf_comment"));
					e8049share.append("||");
					// e8049share.append(s_grade_temperature+"|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "S")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "P")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "Q")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "R")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "T")
							+ "|");
					e8049share.append(ecrtime[0] + "|");
					e8049share.append(ecrtime[1] + "|");
					e8049share.append(((w_grade_temperature == "") ? "NA℃"
							: w_grade_temperature).replaceAll("W:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((y_grade_temperature == "") ? "NA℃"
							: y_grade_temperature).replaceAll("Y:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((j_grade_temperature == "") ? "NA℃"
							: j_grade_temperature).replaceAll("J:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((k_grade_temperature == "") ? "NA℃"
							: k_grade_temperature).replaceAll("K:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((l_grade_temperature == "") ? "NA℃"
							: l_grade_temperature).replaceAll("L:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((n_grade_temperature == "") ? "NA℃"
							: n_grade_temperature).replaceAll("N:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((b_grade_temperature == "") ? "NA℃"
							: b_grade_temperature).replaceAll("B:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((e_grade_temperature == "") ? "NA℃"
							: e_grade_temperature).replaceAll("E:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "U")
							+ "|");
					e8049share.append(maxSite + "|");// MAX_SITE
					if (rs.getString("pgm_special_control") != null)
						e8049share.append(rs.getString("pgm_special_control")
								+ "|");
					else
						e8049share.append("|");

					pr.println(e8049share.toString());
					e8049share.delete(0, e8049share.length());
				}
			}
			if (table1 != null)
				document.add(table1);
			ps.clearParameters();
			ps.close();
			rs.close();
		} catch (Exception ex) {
			// ex.fillInStackTrace();
			// TDSLogger.println(ex.getMessage());
			TDSLogger.println(ex);
			return -1;
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}
		return result;
	}

	public static int FT_Product_Route_Mcp(Document document, Font BasicFont,
			Font SmallFont, String section, ProTestRouteBeanAF fm,
			String vendor, String table, Connection conn, String textFilename) {
		int result = 0;
		FileOutputStream os = null;
		PrintWriter pr = null;

		try {
			PdfPTable table1 = null;
			Paragraph sec = null;
			String productType = OiMaintainService.getProductType(conn,
					fm.getSid());

			StringBuffer sql = new StringBuffer();
			sql.append("select distinct ");
			sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,''ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,decode(a.ft_route_add,null,a.tf_comment,'') tf_ft_comment,c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d , tf_product_route"
					+ table + " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
			sql.append("and a.ft_route = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route) ) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");

			sql.append("select distinct ");
			sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,''ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,decode(a.ft_route_add,null,a.tf_comment,'') tf_ft_comment,c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d , tf_product_route"
					+ table + " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
			sql.append("and a.ft_route = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ( (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d, tf_product_route" + table
					+ " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
			sql.append("and a.ft_route_add = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add2 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d, tf_product_route" + table
					+ " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
			sql.append("and a.ft_route_add2 = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add2) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add3 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, c.pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_ft_view b, tf_test_parameter_ft"
					+ table + " c, ba_package_type d, tf_product_route" + table
					+ " e ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
			sql.append("and a.ft_route_add3 = b.route_name ");
			sql.append("and a.sid = c.sid and a.sid = e.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add3) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union ");
			sql.append("select distinct ");
			sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,'' ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,decode(a.ft_route_add,null,a.tf_comment,'') tf_ft_comment,c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
			sql.append("and a.ft_route = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
			sql.append("and a.ft_route_add = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add2 ft_route_add ,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
			sql.append("and a.ft_route_add2 = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("union \n");
			sql.append("select distinct ");
			sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type package_code,c.package_type package_name,a.pin_count,a.backend_option,a.fg_with_code, ");
			sql.append("a.ft_route,a.ft_route_add3 ft_route_add ,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,c.body_size,c.tf_comment,a.tf_comment tf_ft_comment, c.actual_file, '' pgm_special_control, c.hw_configure ");
			sql.append("from tf_bom_route_mcp" + table
					+ " a, tf_route_master_pbc_view b, tf_test_parameter_pbc"
					+ table + " c, ba_package_type d ");
			sql.append("where a.sid = ? ");
			sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
			sql.append("and a.ft_route_add3 = b.route_name ");
			sql.append("and a.sid = c.sid ");
			sql.append("and a.backend_option = c.backend_option ");
			sql.append("and b.test_mode = c.test_type ");
			sql.append("and a.pin_count = c.pin_count ");
			sql.append("and a.package_type = d.prm2_code ");
			sql.append("and d.package_type = c.package_type ");
			sql.append("and a.tag != 2 ");
			if (!vendor.equals(""))
				sql.append("and c.site = '" + vendor + "' ");

			sql.append("order by product_body,backend_option,package_code,pin_count,fg_with_code,ft_route,ft_route_add,route_type desc,test_mode ");

			TDSLogger.println(sql.toString());
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, String.valueOf(fm.getSid()));
			ps.setString(2, String.valueOf(fm.getSid()));
			ps.setString(3, String.valueOf(fm.getSid()));
			ps.setString(4, String.valueOf(fm.getSid()));
			ps.setString(5, String.valueOf(fm.getSid()));
			ps.setString(6, String.valueOf(fm.getSid()));
			ps.setString(7, String.valueOf(fm.getSid()));
			ps.setString(8, String.valueOf(fm.getSid()));
			ps.setString(9, String.valueOf(fm.getSid()));
			ResultSet rs = ps.executeQuery();

			String preGroupKey = "";
			String curGroupKey = null;
			String i_grade_temperature = null;
			String c_grade_temperature = null;
			String w_grade_temperature = null;
			String y_grade_temperature = null;
			String j_grade_temperature = null;
			String k_grade_temperature = null;
			String l_grade_temperature = null;
			String n_grade_temperature = null;
			String b_grade_temperature = null;
			String e_grade_temperature = null;
			String s_grade_temperature = null;
			String hw_configure_split = null;
			String speedList = null;
			String maxSite = "";
			int subsection = 0;

			// 20090825 for e8049 data share project, Robin
			StringBuffer e8049share = new StringBuffer("");
			String heading = "8049-"
					+ fm.getProductbody()
					+ "."
					+ OiMaintainService
							.getVendorShortName(conn, fm.getVendor()) + "|"
					+ fm.getVersion() + "|" + "FT|" + fm.getBrand() + "|";
			// released OI, Subcons PDF 才產生 text file
			if (table.equals("") && !vendor.equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-PGM.txt", true);
				pr = new PrintWriter(os);
			}
			// select ecrtime for e8049 dat share project, Sophia
			String ecrtime[] = OiMaintainService.getEcrEffectTime(conn,
					fm.getProductbody(), fm.getVersion(), fm.getBrand());

			while (rs.next()) {
				if (result == 0)
					document.add(new Paragraph(new Chunk("1-" + section
							+ ". FT ROUTE", BasicFont)));
				result++;
				curGroupKey = OiMaintainService.getGroupKey(productType,
						rs.getString("product_body"), rs.getString("brand"),
						rs.getString("backend_option"),
						rs.getString("package_code"),
						rs.getString("pin_count"),
						rs.getString("fg_with_code"), rs.getString("ft_route"),
						rs.getString("ft_route_add"), null, 1);
				if (!curGroupKey.equals(preGroupKey)) {
					if (subsection > 0) {
						document.add(table1);
					}
					subsection++;
					speedList = null;
					speedList = OiMaintainService.getSpeedList(conn,
							rs.getString("product_body"),
							rs.getString("brand"), rs.getString("version"),
							rs.getString("ft_route"),
							rs.getString("ft_route_add"), table, "ft");
					sec = null;
					if (speedList.equals(""))
						sec = new Paragraph(new Chunk("1-" + section + "-"
								+ subsection + ". Product Group Key - "
								+ curGroupKey, BasicFont));
					else
						sec = new Paragraph(new Chunk("1-" + section + "-"
								+ subsection + ". Product Group Key - "
								+ curGroupKey + " (Grade:I/C/W/Y/S, Speed:"
								+ speedList + ")", BasicFont));
					sec.setSpacingAfter(5);
					document.add(sec);
					table1 = null;
					// M200712056,20080130
					if (vendor.equals("")) {
						float[] widths = { 5, 5, 4, 4, 5, 5, 8, 7, 7, 10, 6, 5,
								8, 8, 8, 7, 7, 8 };// 11-->9,11-->9,10-->8,10-->8add
													// 8
						table1 = new PdfPTable(widths);
					} else {
						float[] widths = { 5, 5, 4, 4, 5, 5, 8, 7, 7, 10, 8, 5,
								10, 8, 8, 9, 9 };// 11-->10,9-->8,13-->11,
													// 13-->11,12-->11,add 8
						table1 = new PdfPTable(widths);
					}
					table1.setWidthPercentage(100);
					table1.setSpacingBefore(0);
					table1.addCell(new Phrase(
							new Chunk("Route Type", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Route", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Pkg Code", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Pkg Name", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont))); // 4
					table1.addCell(new Phrase(new Chunk(
							"Noraml Grade Temperature", SmallFont))); // 5
					/*
					 * table1.addCell(new Phrase(new
					 * Chunk("C Grade",SmallFont))); //5 table1.addCell(new
					 * Phrase(new Chunk("W Grade",SmallFont))); //5
					 * table1.addCell(new Phrase(new
					 * Chunk("Y Grade",SmallFont))); //5 table1.addCell(new
					 * Phrase(new Chunk("J Grade",SmallFont))); //5
					 * table1.addCell(new Phrase(new
					 * Chunk("K Grade",SmallFont))); //5
					 */
					table1.addCell(new Phrase(new Chunk(
							"AEB Grade Temperature", SmallFont))); // 7
					table1.addCell(new Phrase(new Chunk("Body Size", SmallFont))); // 7
					table1.addCell(new Phrase(new Chunk("Tester", SmallFont))); // 9
					table1.addCell(new Phrase(new Chunk("Site", SmallFont))); // 6
					table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont))); // 5
					table1.addCell(new Phrase(new Chunk("Test Program Name",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("Actual Program Name",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("PGM Special Control",
							SmallFont))); // 8
					table1.addCell(new Phrase(new Chunk("HW Configure",
							SmallFont))); // 7
					table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont))); // 7

					// M200712056,20080130
					if (vendor.equals(""))
						table1.addCell(new Phrase(new Chunk("Route Comment",
								SmallFont))); // 8

					preGroupKey = curGroupKey;
				}

				table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),
						SmallFont)));
				if (rs.getString("route_type").equals("Main")) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ft_route"), SmallFont)));
				} else {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("ft_route_add"), SmallFont)));
				}
				table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("package_code"), SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("package_name"), SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("pin_count"),
						SmallFont)));

				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(i_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(c_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(w_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(y_grade_temperature,SmallFont)));
				 */
				/*
				 * table1.addCell(new Phrase(new
				 * Chunk(j_grade_temperature,SmallFont)));
				 */

				i_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("i_grade"));
				if (i_grade_temperature != null
						&& !i_grade_temperature.equals("NA℃")
						&& !i_grade_temperature.equals("")) {
					i_grade_temperature = "I:" + i_grade_temperature + "\n";
				} else {
					i_grade_temperature = "";
				}

				c_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("c_grade"));
				if (c_grade_temperature != null
						&& !c_grade_temperature.equals("NA℃")
						&& !c_grade_temperature.equals("")) {
					c_grade_temperature = "C:" + c_grade_temperature + "\n";
				} else {
					c_grade_temperature = "";
				}

				w_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("w_grade"));
				if (w_grade_temperature != null
						&& !w_grade_temperature.equals("NA℃")
						&& !w_grade_temperature.equals("")) {
					w_grade_temperature = "W:" + w_grade_temperature + "\n";
				} else {
					w_grade_temperature = "";
				}

				y_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("y_grade"));
				if (y_grade_temperature != null
						&& !y_grade_temperature.equals("NA℃")
						&& !y_grade_temperature.equals("")) {
					y_grade_temperature = "Y:" + y_grade_temperature + "\n";
				} else {
					y_grade_temperature = "";
				}

				j_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("j_grade"));
				if (j_grade_temperature != null
						&& !j_grade_temperature.equals("NA℃")
						&& !j_grade_temperature.equals("")) {
					j_grade_temperature = "J:" + j_grade_temperature + "\n";
				} else {
					j_grade_temperature = "";
				}

				k_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("k_grade"));
				if (k_grade_temperature != null
						&& !k_grade_temperature.equals("NA℃")
						&& !k_grade_temperature.equals("")) {
					k_grade_temperature = "K:" + k_grade_temperature + "\n";
				} else {
					k_grade_temperature = "";
				}

				l_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("l_grade"));
				if (l_grade_temperature != null
						&& !l_grade_temperature.equals("NA℃")
						&& !l_grade_temperature.equals("")) {
					l_grade_temperature = "L:" + l_grade_temperature + "\n";
				} else {
					l_grade_temperature = "";
				}

				n_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("n_grade"));
				if (n_grade_temperature != null
						&& !n_grade_temperature.equals("NA℃")
						&& !n_grade_temperature.equals("")) {
					n_grade_temperature = "N:" + n_grade_temperature + "\n";
				} else {
					n_grade_temperature = "";
				}

				b_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("b_grade"));
				if (b_grade_temperature != null
						&& !b_grade_temperature.equals("NA℃")
						&& !b_grade_temperature.equals("")) {
					b_grade_temperature = "B:" + b_grade_temperature + "\n";
				} else {
					b_grade_temperature = "";
				}

				e_grade_temperature = PDFdiffService.getTemperature(rs
						.getString("e_grade"));
				if (e_grade_temperature != null
						&& !e_grade_temperature.equals("NA℃")
						&& !e_grade_temperature.equals("")) {
					e_grade_temperature = "E:" + e_grade_temperature + "\n";
				} else {
					e_grade_temperature = "";
				}

				String grade = i_grade_temperature + c_grade_temperature
						+ w_grade_temperature + y_grade_temperature
						+ j_grade_temperature + k_grade_temperature
						+ l_grade_temperature + n_grade_temperature
						+ b_grade_temperature + e_grade_temperature;
				table1.addCell(new Phrase(new Chunk(grade, SmallFont)));
				s_grade_temperature = PDFdiffService.getTemperatureSplit(rs
						.getString("s_grade"));
				table1.addCell(new Phrase(new Chunk(s_grade_temperature,
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(PDFdiffService
						.NullConvert(rs.getString("body_size")), SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("tester"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("site"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),
						SmallFont)));
				table1.addCell(new Phrase(new Chunk(rs
						.getString("program_name"), SmallFont)));
				if (rs.getString("actual_file") != null) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("actual_file"), SmallFont)));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}
				if (rs.getString("pgm_special_control") != null) {
					table1.addCell(new Phrase(new Chunk(rs
							.getString("pgm_special_control"), SmallFont)));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}
				hw_configure_split = PDFdiffService.getStringDataSplit(rs
						.getString("hw_configure"));
				table1.addCell(new Phrase(new Chunk(hw_configure_split,
						SmallFont)));
				if (rs.getString("tf_comment") != null) {
					table1.addCell(new Phrase(rs.getString("tf_comment"),
							SmallFont));
				} else {
					table1.addCell(new Phrase(" ", SmallFont));
				}

				// M200712056,20080130
				if (vendor.equals("")) {
					if (rs.getString("tf_ft_comment") != null) {
						table1.addCell(new Phrase(
								rs.getString("tf_ft_comment"), SmallFont));
					} else {
						table1.addCell(new Phrase(" ", SmallFont));
					}
				}
				maxSite = "";
				maxSite = OiMaintainService.getMaxsite(conn, "PROD",
						rs.getString("pgm_id"));

				// 20090831, write to text file
				if (pr != null) {
					e8049share.append(heading + curGroupKey + "|");
					e8049share.append(rs.getString("route_type") + "|");
					if (rs.getString("route_type").equals("Main"))
						e8049share.append(rs.getString("ft_route") + "|");
					else
						e8049share.append(rs.getString("ft_route_add") + "|");

					e8049share.append(rs.getString("test_mode") + "|");
					e8049share.append(rs.getString("package_code") + "|");
					e8049share.append(rs.getString("package_name") + "|");
					e8049share.append(rs.getString("pin_count") + "||");
					e8049share.append(((i_grade_temperature == "") ? "NA℃"
							: i_grade_temperature).replaceAll("I:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((c_grade_temperature == "") ? "NA℃"
							: c_grade_temperature).replaceAll("C:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(PDFdiffService.NullConvert(rs
							.getString("body_size")) + "|");
					e8049share.append(rs.getString("tester") + "|");
					e8049share.append(rs.getString("site") + "|");
					e8049share.append(rs.getString("program_name") + "|");
					if (rs.getString("actual_file") != null)
						e8049share.append(rs.getString("actual_file") + "|");
					else
						e8049share.append("|");
					if (rs.getString("tf_comment") != null)
						e8049share.append(rs.getString("tf_comment"));
					e8049share.append("||");
					// e8049share.append(s_grade_temperature+"|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "S")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "P")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "Q")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "R")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "T")
							+ "|");
					e8049share.append(ecrtime[0] + "|");
					e8049share.append(ecrtime[1] + "|");
					e8049share.append(((w_grade_temperature == "") ? "NA℃"
							: w_grade_temperature).replaceAll("W:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((y_grade_temperature == "") ? "NA℃"
							: y_grade_temperature).replaceAll("Y:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((j_grade_temperature == "") ? "NA℃"
							: j_grade_temperature).replaceAll("J:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((k_grade_temperature == "") ? "NA℃"
							: k_grade_temperature).replaceAll("K:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((l_grade_temperature == "") ? "NA℃"
							: l_grade_temperature).replaceAll("L:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((n_grade_temperature == "") ? "NA℃"
							: n_grade_temperature).replaceAll("N:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((b_grade_temperature == "") ? "NA℃"
							: b_grade_temperature).replaceAll("B:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(((e_grade_temperature == "") ? "NA℃"
							: e_grade_temperature).replaceAll("E:", "")
							.replaceAll("\n", "")
							+ "|");
					e8049share.append(PDFdiffService.getTemperatureAEBgrade(
							rs.getString("s_grade"), "U")
							+ "|");
					e8049share.append(maxSite + "|");// MAX_SITE
					if (rs.getString("pgm_special_control") != null)
						e8049share.append(rs.getString("pgm_special_control")
								+ "|");
					else
						e8049share.append("|");

					pr.println(e8049share.toString());
					e8049share.delete(0, e8049share.length());
				}
			}
			if (table1 != null)
				document.add(table1);
			ps.clearParameters();
			ps.close();
			rs.close();
		} catch (Exception ex) {
			// ex.fillInStackTrace();
			// TDSLogger.println(ex.getMessage());
			TDSLogger.println(ex);
			return -1;
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}
		return result;
	}

	public static boolean BasicInfo(Document doc, Font BasicFont,
			Font SmallFont, ProTestRouteBeanAF fm, String sid, String vendor,
			String table, Connection conn, String textFilename) {

		FileOutputStream os = null;
		PrintWriter pr = null;
		try {
			doc.newPage();
			doc.add(new Paragraph("4. BASIC INFORMATION"));

			String sql = "SELECT decode(options,'*',1,0) opt,decode(grade,'*',1,0) gra,a.* FROM tf_basic_info"
					+ table
					+ "_vw a where sid=? order by opt,gra,tester,options,grade,good_bin";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ResultSet rs = ps.executeQuery();

			// released OI, Subcons PDF 才產生 text file
			if (table.equals("") && !vendor.equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-Bin.txt");
				pr = new PrintWriter(os);
			}
			// select ecrtime for e8049 dat share project, Sophia
			String ecrtime[] = OiMaintainService.getEcrEffectTime(conn,
					fm.getProductbody(), fm.getVersion(), fm.getBrand());

			int i = 0;
			float[] widths = { 5, 5, 4, 4, 7, 7, 10, 24, 4, 5, 5, 12, 14 };// NVM,
																			// MROM
			String productType = OiMaintainService.getProductType(conn,
					fm.getSid());
			if (productType.equals("XROM"))
				widths = new float[] { 5, 5, 4, 4, 7, 7, 10, 28, 12, 14 };// XROM
			PdfPTable table5 = null;
			table5 = new PdfPTable(widths);
			table5.setWidthPercentage(100);
			table5.setSpacingBefore(5);
			table5.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
			table5.addCell(new Phrase(new Chunk("Options", SmallFont)));
			table5.addCell(new Phrase(new Chunk("Grade", SmallFont)));
			table5.addCell(new Phrase(new Chunk("IB Bin", SmallFont)));
			table5.addCell(new Phrase(new Chunk("DB Bin", SmallFont)));
			table5.addCell(new Phrase(new Chunk("Binning Description",
					SmallFont)));
			table5.addCell(new Phrase(new Chunk("Inkless Grade", SmallFont)));
			table5.addCell(new Phrase(new Chunk("IPN Action", SmallFont)));
			if (!productType.equals("XROM")) {
				table5.addCell(new Phrase(new Chunk("KTD Bin Flag", SmallFont)));
				table5.addCell(new Phrase(new Chunk("EPN Speed", SmallFont)));
				table5.addCell(new Phrase(new Chunk("Test Speed", SmallFont)));
			}
			table5.addCell(new Phrase(new Chunk("Down Grade", SmallFont)));
			table5.addCell(new Phrase(new Chunk("Remark", SmallFont)));
			while (rs.next()) {
				i++;
				table5.addCell(new Phrase(new Chunk(rs.getString("tester"),
						SmallFont)));
				table5.addCell(new Phrase(new Chunk(rs.getString("options"),
						SmallFont)));
				table5.addCell(new Phrase(new Chunk(rs.getString("grade"),
						SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ib_bin")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(rs.getString("good_bin"),
						SmallFont)));
				table5.addCell(new Phrase(new Chunk(rs
						.getString("bin_type_str"), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("inkless_grade")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(rs
						.getString("ipn_action_str"), SmallFont)));
				if (!productType.equals("XROM")) {
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("ktd_bin_flag")),
							SmallFont)));
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("epn_speed")), SmallFont)));
					table5.addCell(new Phrase(
							new Chunk(StringUtil.NullConvert(rs
									.getString("test_speed")), SmallFont)));
				}
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("down_grade")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("remark")), SmallFont)));

				// 20091214 for e8049 data share project, Sophia
				StringBuffer e8049share = new StringBuffer("");
				String heading = "8049-"
						+ fm.getProductbody()
						+ "."
						+ OiMaintainService.getVendorShortName(conn,
								fm.getVendor()) + "|" + fm.getVersion() + "|"
						+ fm.getBrand() + "|";
				if (pr != null) {
					e8049share.append(heading);
					e8049share.append(rs.getString("tester") + "|");
					e8049share.append(rs.getString("options") + "|");
					e8049share.append(rs.getString("grade") + "|");
					e8049share.append(rs.getString("good_bin") + "|");
					e8049share.append(rs.getString("bin_type_str") + "|");
					e8049share.append(rs.getString("ipn_action_str") + "|");
					e8049share.append(StringUtil.formatNull(rs
							.getString("epn_speed")) + "|");
					e8049share.append(StringUtil.formatNull(rs
							.getString("test_speed")) + "|");
					e8049share.append(StringUtil.formatNull(rs
							.getString("down_grade")) + "|");
					e8049share.append(StringUtil.formatNull(rs
							.getString("remark")) + "|");
					e8049share.append(StringUtil.formatNull(rs
							.getString("inkless_grade")) + "|");
					e8049share.append(ecrtime[0] + "|");
					e8049share.append(ecrtime[1] + "|");
					// e8049share.append(StringUtil.formatNull(rs.getString("ktd_bin_flag"))+"|");
					// 2020-4-20 外包廠用不到, 所以建議E-File 不新增 phoebe,km
					pr.println(e8049share.toString());
					e8049share.delete(0, e8049share.length());
				}

			}

			doc.add(table5);
			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
			String comments = com.mxic.oiplus.oimaintain.TFIMBasicService
					.getBAComment(conn, Integer.parseInt(sid),
							(table.equals("") ? "R" : "P"));
			doc.add(new Paragraph(new Chunk("Comments :\n", BasicFont)));
			if (comments.equals(""))
				doc.add(new Paragraph(new Chunk("NA", SmallFont)));
			else
				doc.add(new Paragraph(comments, SmallFont));

		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}
		return false;
	}

	// output yield definition chart
	public static boolean Yield(Document doc, Font BasicFont, Font SmallFont,
			String section, String Path, ProTestRouteBeanAF fm, String table,
			Connection conn, String textFilename) {

		String sql;
		PreparedStatement ps;
		ResultSet rs;
		int subsection = 0;
		FileOutputStream os = null;
		PrintWriter pr = null;

		try {
			sql = "SELECT * FROM tf_information where sid=? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "" + fm.getSid());
			rs = ps.executeQuery();
			String product_type = "";
			while (rs.next()) {
				product_type = rs.getString("product_type").toString();
			}

			doc.newPage();
			doc.add(new Paragraph(section + ". YIELD CRITERIA"));

			PdfPTable table5 = null;
			float[] widths = null;

			/* JA201400042 START */
			StringBuffer e8049share = new StringBuffer("");
			String heading = "8049-"
					+ fm.getProductbody()
					+ "."
					+ OiMaintainService
							.getVendorShortName(conn, fm.getVendor()) + "|"
					+ fm.getVersion() + "|" + fm.getBrand() + "|";

			if (table.equals("") && fm.getVendor() != null
					&& !fm.getVendor().equals("")
					&& !fm.getVendor().equals("TEST1")) {
				os = new FileOutputStream(textFilename + "-Yield.txt");
				pr = new PrintWriter(os);
			}
			/* JA201400042 END */

			for (int facility = 0; facility < 2; facility++) {
				YieldDefBean[] ydb = null;
				for (int nvm_ws = 0, other = 0; nvm_ws < 2; nvm_ws++, other++) {

					if (facility == 0 && product_type.equals("NVM")
							&& nvm_ws == 1) { // 假如為WS且為NVM，Dgrade Criteria
												// 只要取消一個CHANGE_IPN欄位即可
						widths = new float[9];
						widths[0] = 5;
						widths[1] = 4;
						widths[2] = 4;
						widths[3] = 25;
						widths[4] = 8;
						widths[5] = 8;
						widths[6] = 7;
						widths[7] = 7;
						widths[8] = 30;
					} else if (facility == 1 && product_type.equals("NVM")) { // FT需要三個欄位都取消，並加上一個DGRADE
																				// ACTION
						widths = new float[7];
						widths[0] = 5;
						widths[1] = 4;
						widths[2] = 4;
						widths[3] = 25;
						widths[4] = 8;
						widths[5] = 12;
						widths[6] = 26;
					} else if (/* facility == 1 || */(facility == 0
							&& product_type.equals("NVM") && nvm_ws == 0)) { // ws/nvm/hold的時候，需要三個欄位都取消
						widths = new float[6];
						widths[0] = 5;
						widths[1] = 4;
						widths[2] = 4;
						widths[3] = 25;
						widths[4] = 8;
						widths[5] = 30;
					} else { // 一般的情況
						widths = new float[9];
						widths[0] = 5;
						widths[1] = 4;
						widths[2] = 4;
						widths[3] = 25;
						widths[4] = 8;
						widths[5] = 10;
						widths[6] = 7;
						widths[7] = 7;
						widths[8] = 30;
					}

					String hold_dgrade = "";
					if (product_type.equals("NVM")) {
						if (facility != 0) {
							nvm_ws = 1; // if facility is not ws && not NVM, run
										// one time
						}
					}

					if (facility == 0 && product_type.equals("NVM")
							&& nvm_ws == 0) {
						ydb = com.mxic.oiplus.oimaintain.YieldDefService
								.RetrieveYieldNVMWSHoldCriteriaPDF(
										"" + fm.getSid(), conn, table,
										(facility == 0 ? "WS" : "FT"),
										product_type, "Hold");
						hold_dgrade = "Hold Criteria";
					} else if (facility == 0 && product_type.equals("NVM")
							&& nvm_ws == 1) {
						ydb = com.mxic.oiplus.oimaintain.YieldDefService
								.RetrieveYieldNVMWSHoldCriteriaPDF(
										"" + fm.getSid(), conn, table,
										(facility == 0 ? "WS" : "FT"),
										product_type, "Dgrade");
						hold_dgrade = "Dgrade Criteria";
					} else {
						if (!product_type.equals("NVM")) {
							if (other == 0) {
								ydb = com.mxic.oiplus.oimaintain.YieldDefService
										.RetrieveYield("" + fm.getSid(), conn,
												table, (facility == 0 ? "WS"
														: "FT"), product_type,
												"hcs", false);
								hold_dgrade = "Hold Criteria";
							} else if (other == 1) {
								ydb = com.mxic.oiplus.oimaintain.YieldDefService
										.RetrieveYield("" + fm.getSid(), conn,
												table, (facility == 0 ? "WS"
														: "FT"), product_type,
												"acs", false);
								hold_dgrade = "Action Criteria";
							} else {
								ydb = com.mxic.oiplus.oimaintain.YieldDefService
										.RetrieveYield("" + fm.getSid(), conn,
												table, (facility == 0 ? "WS"
														: "FT"), product_type,
												false);
							}
						} else {
							ydb = com.mxic.oiplus.oimaintain.YieldDefService
									.RetrieveYield("" + fm.getSid(), conn,
											table,
											(facility == 0 ? "WS" : "FT"),
											product_type, false);
						}
					}

					BaseFont bfChinese = BaseFont.createFont("MHei-Medium",
							"UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
					Font RedSmallFont8 = new Font(bfChinese, 8);
					RedSmallFont8.setColor(0xEE, 0, 0);

					if (ydb != null) {
						for (int i = 0; i < ydb.length; i++) {
							if (i == 0) {
								doc.add(new Paragraph(
										section
												+ "-"
												+ (++subsection)
												+ " "
												+ (facility == 0 ? "WS" : "FT")
												+ " "
												+ hold_dgrade
												+ " "
												+ "Yield"
												+ (facility == 0
														&& product_type
																.equals("NVM")
														&& other == 1 ? " (Dgrade IPN 內容依 MXIC 發送之 Dgrade table 執行)"
														: ""), BasicFont));
								if (facility == 0 && product_type.equals("NVM")
										&& nvm_ws == 1) {
									doc.add(new Paragraph("Action Priority",
											RedSmallFont8));
									doc.add(new Paragraph(
											"Priority 1: Dgrade , Priority 2: Change IPN",
											RedSmallFont8));
								}
								table5 = null;
								table5 = new PdfPTable(widths);
								table5.setWidthPercentage(100);
								table5.setSpacingBefore(5);
								table5.addCell(new Phrase(new Chunk(
										"Product Code", SmallFont)));
								if (facility == 0 && product_type.equals("NVM"))
									table5.addCell(new Phrase(new Chunk(
											"Prod Level", SmallFont)));
								else
									table5.addCell(new Phrase(new Chunk(
											"Brand", SmallFont)));
								table5.addCell(new Phrase(new Chunk(
										"Test Mode", SmallFont)));
								if (facility == 0)
									table5.addCell(new Phrase(new Chunk(
											"Criteria (By Wafer)", SmallFont)));
								else
									table5.addCell(new Phrase(new Chunk(
											"Criteria", SmallFont)));
								if (!product_type.equals("NVM") && other == 1)
									table5.addCell(new Phrase(new Chunk(
											"Extra Action", SmallFont)));
								else
									table5.addCell(new Phrase(new Chunk(
											"Action", SmallFont)));

								if (product_type.equals("NVM")) {
									if (facility == 0) {
										if (nvm_ws == 1) {
											table5.addCell(new Phrase(
													new Chunk(
															"By Lot Downgrade",
															SmallFont)));
											table5.addCell(new Phrase(
													new Chunk("Route Name",
															SmallFont)));
											table5.addCell(new Phrase(
													new Chunk("Start Step",
															SmallFont)));
										}
									}
									if (facility == 1) {
										table5.addCell(new Phrase(new Chunk(
												"DGrade Action", SmallFont)));
									}
								} else if (product_type.equals("XROM")) {
									table5.addCell(new Phrase(new Chunk(
											"Change IPN", SmallFont)));
									table5.addCell(new Phrase(new Chunk(
											"Route Name", SmallFont)));
									table5.addCell(new Phrase(new Chunk(
											"Start Step", SmallFont)));
								} else if (product_type.equals("MROM")) {
									table5.addCell(new Phrase(new Chunk(
											"Change IPN", SmallFont)));
									table5.addCell(new Phrase(new Chunk(
											"Route Name", SmallFont)));
									table5.addCell(new Phrase(new Chunk(
											"Start Step", SmallFont)));
								}

								/*
								 * if ((facility == 0 &&
								 * product_type.equals("NVM")) || facility == 1)
								 * { // // nothing; } else { table5.addCell(new
								 * Phrase(new Chunk("Change IPN",SmallFont))); }
								 * if (facility == 1 &&
								 * product_type.equals("NVM")) {//FT / NVM
								 * table5.addCell(new Phrase(new
								 * Chunk("DGrade Action", SmallFont))); }else
								 * if(facility ==0) {//WS
								 * if(product_type.equals("NVM")){
								 * if(nvm_ws==1){//nvm_ws=1->Dgrade , 0->Hold
								 * table5.addCell(new Phrase(new
								 * Chunk("By Lot Downgrade", SmallFont)));
								 * table5.addCell(new Phrase(new
								 * Chunk("Route Name",SmallFont)));
								 * table5.addCell(new Phrase(new
								 * Chunk("Start Step",SmallFont))); }
								 * }else{//WS->MROM/XROM table5.addCell(new
								 * Phrase(new Chunk("Route Name",SmallFont)));
								 * table5.addCell(new Phrase(new
								 * Chunk("Start Step",SmallFont))); } }
								 */

								/*
								 * if (facility == 1 || (facility == 0 &&
								 * product_type.equals("NVM") && nvm_ws==0)) {
								 * if(facility == 1) table5.addCell(new
								 * Phrase(new Chunk("DGrade Action",
								 * SmallFont))); } else { table5.addCell(new
								 * Phrase(new Chunk("By Lot Downgrade",
								 * SmallFont))); table5.addCell(new Phrase(new
								 * Chunk("Route Name",SmallFont)));
								 * table5.addCell(new Phrase(new
								 * Chunk("Start Step",SmallFont))); }
								 */

								table5.addCell(new Phrase(new Chunk("Remark",
										SmallFont)));
							}
							table5.addCell(new Phrase(new Chunk(ydb[i]
									.getProduct_code(), SmallFont)));
							table5.addCell(new Phrase(
									new Chunk(StringUtil.NullConvert(ydb[i]
											.getBrands()), SmallFont)));
							table5.addCell(new Phrase(new Chunk(ydb[i]
									.getTest_mode(), SmallFont)));
							table5.addCell(new Phrase(new Chunk(ydb[i]
									.getFull_items(), SmallFont)));
							table5.addCell(new Phrase(new Chunk(ydb[i]
									.getAction(), SmallFont)));

							if (product_type.equals("NVM")) {
								if (facility == 0) {
									if (nvm_ws == 1) {
										table5.addCell(new Phrase(new Chunk(
												StringUtil.NullConvert(ydb[i]
														.getBy_lot_dg()),
												SmallFont)));
										table5.addCell(new Phrase(new Chunk(
												StringUtil.NullConvert(ydb[i]
														.getRoute_name()),
												SmallFont)));
										table5.addCell(new Phrase(new Chunk(
												StringUtil.NullConvert(ydb[i]
														.getStart_step()),
												SmallFont)));
									}
								}
								if (facility == 1) {
									table5.addCell(new Phrase(
											new Chunk(StringUtil
													.NullConvert(ydb[i]
															.getDg_action()),
													SmallFont)));
								}
							} else if (product_type.equals("XROM")) {
								table5.addCell(new Phrase(new Chunk(StringUtil
										.NullConvert(ydb[i].getChange_ipn()),
										SmallFont)));
								table5.addCell(new Phrase(new Chunk(StringUtil
										.NullConvert(ydb[i].getRoute_name()),
										SmallFont)));
								table5.addCell(new Phrase(new Chunk(StringUtil
										.NullConvert(ydb[i].getStart_step()),
										SmallFont)));
							} else if (product_type.equals("MROM")) {
								table5.addCell(new Phrase(new Chunk(StringUtil
										.NullConvert(ydb[i].getChange_ipn()),
										SmallFont)));
								table5.addCell(new Phrase(new Chunk(StringUtil
										.NullConvert(ydb[i].getRoute_name()),
										SmallFont)));
								table5.addCell(new Phrase(new Chunk(StringUtil
										.NullConvert(ydb[i].getStart_step()),
										SmallFont)));
							}

							/*
							 * if ((facility == 0 && product_type.equals("NVM"))
							 * || facility == 1) { // nothing; } else {
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getChange_ipn()),SmallFont))); } if
							 * (facility == 1 && product_type.equals("NVM"))
							 * {//FT / NVM table5.addCell(new Phrase(new
							 * Chunk(StringUtil
							 * .NullConvert(ydb[i].getDg_action()),
							 * SmallFont))); }else if(facility ==0) {//WS
							 * if(product_type.equals("NVM")){
							 * if(nvm_ws==1){//nvm_ws=1->Dgrade , 0->Hold
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getBy_lot_dg()), SmallFont)));
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getRoute_name()),SmallFont)));
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getStart_step()),SmallFont))); }
							 * }else{//WS->MROM/XROM table5.addCell(new
							 * Phrase(new
							 * Chunk(StringUtil.NullConvert(ydb[i].getRoute_name
							 * ()),SmallFont))); table5.addCell(new Phrase(new
							 * Chunk
							 * (StringUtil.NullConvert(ydb[i].getStart_step(
							 * )),SmallFont))); } }
							 */

							/*
							 * if (facility == 1 || (facility == 0 &&
							 * product_type.equals("NVM") && nvm_ws==0)) {
							 * if(facility == 1 && product_type.equals("NVM") )
							 * { table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getDg_action()), SmallFont))); } } else {
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getBy_lot_dg()), SmallFont)));
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getRoute_name()),SmallFont)));
							 * table5.addCell(new Phrase(new
							 * Chunk(StringUtil.NullConvert
							 * (ydb[i].getStart_step()),SmallFont))); }
							 */
							table5.addCell(new Phrase(
									new Chunk(StringUtil.NullConvert(ydb[i]
											.getRemark()), SmallFont)));

							/* JA201400042 E-FILE 增加 */
							e8049share.append(heading);
							e8049share.append(ydb[i].getProduct_code() + "|");
							if (product_type.equals("NVM") && facility == 0) {
								e8049share.append(StringUtil.formatNull(ydb[i]
										.getBrands()) + "|");
								e8049share.append("|");
							} else {
								e8049share.append("|");
								e8049share.append(StringUtil.formatNull(ydb[i]
										.getBrands()) + "|");
							}
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getTest_mode()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getLower_limit()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getFlag1()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getFull_items()) + "|");// 20181214-getItem()
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getFlag2()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getUpper_limit()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getAction()) + "|");
							if (product_type.equals("NVM") && facility == 0) {
								e8049share.append(StringUtil.formatNull(ydb[i]
										.getBy_lot_dg()) + "|");
								e8049share.append(StringUtil.formatNull(ydb[i]
										.getDgrade_special_ipn()) + "|");
							} else {
								e8049share.append("|");
								e8049share.append("|");
							}
							if (facility == 0) {
								e8049share.append("|");
							} else {
								e8049share.append(StringUtil.formatNull(ydb[i]
										.getDg_action()) + "|");
							}
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getChange_ipn()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getRoute_name()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getStart_step()) + "|");
							e8049share.append(StringUtil.formatNull(ydb[i]
									.getRemark()) + "|");

							if (pr != null)
								pr.println(e8049share.toString());
							e8049share.delete(0, e8049share.length());

						}
						if (table5 != null)
							doc.add(table5);
						table5 = null;
					}
				}
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
			}
		}

		try {
			sql = "SELECT * FROM tf_yield" + table
					+ " where sid=? order by seq";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "" + fm.getSid());
			rs = ps.executeQuery();

			PdfPTable table5 = null;
			int line_no = 0;
			while (rs.next()) {
				if (++line_no == 1) {
					if (subsection != 0)
						doc.add(new Paragraph(section + "-" + (++subsection)
								+ " Yield Table", BasicFont));
					float[] widths2 = { 6, 4, 9, 9, 11, 11, 9, 9, 9, 4, 19 };
					table5 = null;
					table5 = new PdfPTable(widths2);
					table5.setWidthPercentage(100);
					table5.setSpacingBefore(5);
					table5.addCell(new Phrase(new Chunk("Product Code",
							SmallFont)));
					table5.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
					table5.addCell(new Phrase(new Chunk("Auto Ship", SmallFont)));
					table5.addCell(new Phrase(new Chunk("Hold PE", SmallFont)));
					table5.addCell(new Phrase(new Chunk("Hold BIN", SmallFont)));
					table5.addCell(new Phrase(new Chunk("Hold Bin Criteria",
							SmallFont)));
					table5.addCell(new Phrase(
							new Chunk("Auto Scrap", SmallFont)));
					table5.addCell(new Phrase(new Chunk("Stop", SmallFont)));
					table5.addCell(new Phrase(new Chunk("OOC", SmallFont)));
					table5.addCell(new Phrase(new Chunk("Sampling Yield",
							SmallFont)));
					table5.addCell(new Phrase(new Chunk("Notes", SmallFont)));
				}
				table5.addCell(new Phrase(new Chunk(rs
						.getString("product_code"), SmallFont)));
				table5.addCell(new Phrase(new Chunk(rs.getString("test_mode"),
						SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("auto_ship")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("hold_pe")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("hold_bin")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("hold_bin_cri")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("auto_scrap")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("stop")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("mrb")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("sampling_yield")), SmallFont)));
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("notes")), SmallFont)));
			}
			if (line_no > 0)
				if (table5 != null)
					doc.add(table5);
			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
		}

		try {
			sql = "SELECT * FROM tf_document_linkage" + table
					+ " a where sid = ? and doc_type='Y' ";
			if (table.equals("_tx"))
				sql = sql
						+ "and not exists (select 1 from tf_document_linkage_tx b "
						+ "where a.sid = b.sid " + "and b.tag = 1 "
						+ "and a.doc_type = b.doc_type " + "and a.seq = b.seq "
						+ "and a.rowid != b.rowid) ";
			sql = sql + " order by seq";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "" + fm.getSid());
			rs = ps.executeQuery();
			while (rs.next()) {
				doc.newPage();
				doc.add(new Paragraph(section + "-" + (++subsection) + " "
						+ rs.getString("doc_name"), BasicFont));
				doc.add(new Paragraph(rs.getString("tf_comment"), BasicFont));
				Image jpg1 = Image.getInstance(Path + File.separator
						+ rs.getString("file_name"));
				jpg1.scaleAbsolute(500, 500);
				doc.add(jpg1);
				jpg1 = null;
			}
			ps.clearParameters();
			rs.close();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
		return false;
	}

	// output yield definition chart
	public static boolean ProdWaferlevel(Document doc, Font BasicFont,
			Font SmallFont, String section, String Path, ProTestRouteBeanAF fm,
			String table, Connection conn, String textFilename) {

		String sql;
		PreparedStatement ps;
		ResultSet rs;
		try {
			sql = "SELECT m.VERSION, m.PRODUCT_BODY, b.FORM_NO, b.REASON_DETAILS, ANNOTATION_ISSUES from tf_fw_information m,fw_basic_info b where m.sid = b.sid and m.sid = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "" + fm.getSid());
			rs = ps.executeQuery();
			String product_type = "";

			BaseFont bfChinese = BaseFont.createFont("MHei-Medium",
					"UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);

			Font font = new Font(bfChinese, 10, Font.NORMAL);

			// 3. 建立表格（6 欄）
			float[] columnWidths = { 10, 20, 20, 30, 30, 30 };
			PdfPTable fwtable = new PdfPTable(columnWidths);
			fwtable.setWidthPercentage(100);
			fwtable.setSpacingBefore(10);

			// 4. 表頭
			String[] headers = { "rev", "product code", "form no", "變更原因",
					"變更內容\n(註解欄)", "待解決事項" };
			for (String header : headers) {
				PdfPCell cell = new PdfPCell(new Phrase(header, font));
				cell.setNoWrap(false);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(new Color(200, 200, 200)); // 淺灰底色
				fwtable.addCell(cell);
			}

			// 5. 填入查詢結果
			String version = null;
			String productBody = null;
			String formNo = null;
			String reasonDetails = null;
			String annotationAll = "";
			boolean firstRow = true;
			while (rs.next()) {
			    if (firstRow) { // 只在第一筆時讀這五個欄位
			        version = rs.getString("VERSION");
			        productBody = rs.getString("PRODUCT_BODY");
			        formNo = rs.getString("FORM_NO");
			        reasonDetails = rs.getString("REASON_DETAILS");
			        firstRow = false;
			    }
			    // 收集 annotation，多筆用換行分隔
			    if (annotationAll.length() > 0) {
			        annotationAll += "\n";
			    }
			    annotationAll += rs.getString("ANNOTATION_ISSUES");
			}
			if (version != null) { // 確認有資料
			    fwtable.addCell(new Phrase(version, font));
			    fwtable.addCell(new Phrase(productBody, font));
			    fwtable.addCell(new Phrase(formNo, font));
			    fwtable.addCell(new Phrase(reasonDetails, font));

			    PdfPCell annotationCell = new PdfPCell(new Phrase(annotationAll, font));
			    annotationCell.setNoWrap(false); // 允許換行
			    fwtable.addCell(annotationCell);
			    fwtable.addCell(new Phrase("N/A", font));
			}

			// 6. 加入 PDF
			doc.add(fwtable);

			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			TDSLogger.println(ex.getMessage());
		} finally {
			
		}

		return false;
	}

	// Draw Test Flow Chart
	public static boolean WS_FT_Test_FLow_Chart(Document doc, Font BasicFont,
			String section, String Path, String sid, String table,
			Connection conn) {
		try {
			String sql = "SELECT * FROM tf_document_linkage" + table
					+ " a where sid = ? and doc_type='T' ";
			if (table.equals("_tx"))
				sql = sql
						+ "and not exists (select 1 from tf_document_linkage_tx b "
						+ "where a.sid = b.sid " + "and b.tag = 1 "
						+ "and a.doc_type = b.doc_type " + "and a.seq = b.seq "
						+ "and a.rowid != b.rowid) ";
			sql = sql + "order by seq";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ResultSet rs = ps.executeQuery();

			int i = 0;
			while (rs.next()) {
				doc.newPage();
				if (i++ == 0)
					doc.add(new Paragraph(section + ". WS & FT TEST FLOW"));
				doc.add(new Paragraph(section + "-" + i + " "
						+ rs.getString("doc_name"), BasicFont));
				doc.add(new Paragraph(rs.getString("tf_comment"), BasicFont));
				Image jpg1 = Image.getInstance(Path + File.separator
						+ rs.getString("file_name"));
				jpg1.scaleAbsolute(500, 540);
				doc.add(jpg1);
				jpg1 = null;
			}
			ps.clearParameters();
			rs.close();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
		return false;
	}

	// Draw User Comments for whole OI
	public static boolean Comment_Image(Document doc, String section,
			String Path, String sid, String table, Connection conn) {

		// Font BasicFont = new Font(Font.HELVETICA, 8, Font.NORMAL,
		// Color.BLACK);
		try {
			String sql = "SELECT * FROM tf_document_linkage" + table
					+ " a where sid = ? and doc_type='M' ";
			if (table.equals("_tx"))
				sql = sql
						+ "and not exists (select 1 from tf_document_linkage_tx b "
						+ "where a.sid = b.sid " + "and b.tag = 1 "
						+ "and a.doc_type = b.doc_type " + "and a.seq = b.seq "
						+ "and a.rowid != b.rowid) ";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ResultSet rs = ps.executeQuery();

			int i = 0;
			while (rs.next()) {
				if (++i == 1) {
					doc.newPage();
					doc.add(new Paragraph(section + ". COMMENTS"));
				}
				Image jpg1 = Image.getInstance(Path + File.separator
						+ rs.getString("file_name"));
				jpg1.scaleAbsolute(500, 540);
				doc.add(jpg1);
				jpg1 = null;
			}
			ps.clearParameters();
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return i > 0;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
	}

	// Print out BOM table for MH000 Venson Lee
	public static boolean BOM_Table(Document doc, Font BasicFont,
			String section, String sid, String table, Connection conn) {

		try {
			doc.newPage();
			doc.add(new Paragraph(section + ". BOM vs. Product Route"));

			String sql = "SELECT a.*,tf_check_step('AVI',ws_route) avi,DECODE(tf_check_step('INK',WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',WS_ROUTE)) ink  FROM tf_bom_route"
					+ table
					+ " a where sid=? and tag != 2 "
					+ "order by backend_option,fg_with_code,pin_count,package_type,ft_route_code";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ResultSet rs = ps.executeQuery();

			int i = 0;
			// float[] widths = {2,4,4,4,4,4,8,8,16,4,4,4,8,10,16};
			// float[] widths = {4,4,4,4,4,8,8,8,8, 14,4,4,4,8,10, 14, 6,10};
			float[] widths = null;
			String brand = OiMaintainService.getBrand(conn, sid);
			if (brand.equals("MX"))
				widths = new float[] { 4, 4, 4, 4, 4, 8, 8, 8, 8, 4, 14, 6, 10,
						4, 4, 4, 4, 8, 10, 4, 14, 4, 4 };
			else
				widths = new float[] { 4, 4, 4, 4, 4, 8, 8, 8, 8, 4, 14, 4, 4,
						4, 4, 8, 10, 4, 14, 4, 4 };
			PdfPTable table5 = null;
			table5 = new PdfPTable(widths);
			table5.setWidthPercentage(100);
			table5.setSpacingBefore(5);
			// table5.addCell(new Phrase(new Chunk("St",BasicFont)));
			table5.addCell(new Phrase(new Chunk("BE Opt.", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FG With Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Pin Count", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Pkg Type", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FT Route Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FT Route", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("FT Add. Route 1", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("FT Add. Route 2", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("FT Add. Route 3", BasicFont)));// 8
			table5.addCell(new Phrase(
					new Chunk("FT Special Control", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FT Comment", BasicFont)));// 14
			if (brand.equals("MX")) {
				table5.addCell(new Phrase(new Chunk("Quality Level", BasicFont)));// 6
				table5.addCell(new Phrase(new Chunk("Quality Level Comment",
						BasicFont)));// 10
			}
			table5.addCell(new Phrase(new Chunk("MCP Flag", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Mask Opt.", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("DB With Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Sort Route Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("WS Route", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("WS Add. Route", BasicFont)));// 10
			table5.addCell(new Phrase(
					new Chunk("WS Special Control", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("WS Comment", BasicFont)));// 14
			table5.addCell(new Phrase(new Chunk("AVI", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("INK", BasicFont)));// 4

			while (rs.next()) {
				i++;
				/*
				 * String tag = rs.getString("tag"); if (tag.equals("2"))
				 * table5.addCell(new Phrase(new Chunk("E",BasicFont))); else
				 * table5.addCell(new Phrase(new Chunk("",BasicFont)));
				 */
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("backend_option")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("fg_with_code")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs.getString("pin_count"),
						BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("package_type")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_code")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_add")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_add2")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_add3")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("endurance")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("tf_comment")), BasicFont)));// 14
				if (rs.getString("brand").equals("MX")) {
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("quality_level")),
							BasicFont)));// 6
					table5.addCell(new Phrase(new Chunk(
							StringUtil.NullConvert(rs
									.getString("quality_level_comment")),
							BasicFont)));// 10
				}
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("mcp_flag")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(
						rs.getString("mask_option"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs
						.getString("db_with_code"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs
						.getString("sort_route_code"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs.getString("ws_route"),
						BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ws_route_add")), BasicFont)));// 10
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("wsspecialcontrol")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("tf_ws_comment")), BasicFont)));// 14
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("avi")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ink")), BasicFont)));// 4

			}
			doc.add(table5);
			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
		return false;
	}

	public static boolean BOM_Mcp_Table(Document doc, Font BasicFont,
			String section, String sid, String table, Connection conn) {

		try {
			doc.newPage();
			doc.add(new Paragraph(section + ". MCP BOM vs. Product Route"));

			String sql = "SELECT a.*,tf_check_step('AVI',ws_route) avi,DECODE(tf_check_step('INK',WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',WS_ROUTE)) ink, \n"
					+ "component_no, com_prod_body, com_mask_option, com_backend_option\n"
					+ "FROM tf_bom_route_mcp"
					+ table
					+ " a where sid=? and tag != 2 "
					+ "order by a.ft_route_code, a.component_no";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ResultSet rs = ps.executeQuery();

			int i = 0;
			// float[] widths = {2,4,4,4,4,4,8,8,16,4,4,4,8,10,16};
			// float[] widths = {4,4,4,4,4,8,8,8,8, 14,4,4,4,8,10, 14, 6,10};
			String brand = OiMaintainService.getBrand(conn, sid);
			float[] widths = null;
			if (brand.equals("MX"))
				widths = new float[] { 4, 4, 4, 4, 4, 8, 8, 8, 8, 4, 14, 6, 10,
						5, 7, 5, 5, 4, 4, 8, 10, 4, 14, 4, 4 };
			else
				widths = new float[] { 4, 4, 4, 4, 4, 8, 8, 8, 8, 4, 14, 5, 7,
						5, 5, 4, 4, 8, 10, 4, 14, 4, 4 };
			PdfPTable table5 = null;
			table5 = new PdfPTable(widths);
			table5.setWidthPercentage(100);
			table5.setSpacingBefore(5);
			// table5.addCell(new Phrase(new Chunk("St",BasicFont)));
			table5.addCell(new Phrase(new Chunk("BE Opt.", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FG With Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Pin Count", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Pkg Type", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FT Route Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FT Route", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("FT Add. Route 1", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("FT Add. Route 2", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("FT Add. Route 3", BasicFont)));// 8
			table5.addCell(new Phrase(
					new Chunk("FT Special Control", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("FT Comment", BasicFont)));// 14
			if (brand.equals("MX")) {
				table5.addCell(new Phrase(new Chunk("Quality Level", BasicFont)));// 6
				table5.addCell(new Phrase(new Chunk("Quality Level Comment",
						BasicFont)));// 10
			}
			// table5.addCell(new Phrase(new Chunk("Mask Opt.", BasicFont)));//
			// 4
			table5.addCell(new Phrase(new Chunk("Component No", BasicFont)));// 5
			table5.addCell(new Phrase(new Chunk("Component Product", BasicFont)));// 5
			table5.addCell(new Phrase(new Chunk("COM Mask Opt.", BasicFont)));// 5
			table5.addCell(new Phrase(new Chunk("COM BE Option", BasicFont)));// 5
			table5.addCell(new Phrase(new Chunk("DB With Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("Sort Route Code", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("WS Route", BasicFont)));// 8
			table5.addCell(new Phrase(new Chunk("WS Add. Route", BasicFont)));// 10
			table5.addCell(new Phrase(
					new Chunk("WS Special Control", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("WS Comment", BasicFont)));// 14
			table5.addCell(new Phrase(new Chunk("AVI", BasicFont)));// 4
			table5.addCell(new Phrase(new Chunk("INK", BasicFont)));// 4

			while (rs.next()) {
				i++;
				/*
				 * String tag = rs.getString("tag"); if (tag.equals("2"))
				 * table5.addCell(new Phrase(new Chunk("E",BasicFont))); else
				 * table5.addCell(new Phrase(new Chunk("",BasicFont)));
				 */
				table5.addCell(new Phrase(new Chunk(rs
						.getString("backend_option"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs
						.getString("fg_with_code"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs.getString("pin_count"),
						BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs
						.getString("package_type"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs
						.getString("ft_route_code"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs.getString("ft_route"),
						BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_add")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_add2")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ft_route_add3")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("endurance")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("tf_comment")), BasicFont)));// 14
				if (rs.getString("brand").equals("MX")) {
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("quality_level")),
							BasicFont)));// 6
					table5.addCell(new Phrase(new Chunk(
							StringUtil.NullConvert(rs
									.getString("quality_level_comment")),
							BasicFont)));// 10
				}
				// table5.addCell(new Phrase(new
				// Chunk(rs.getString("mask_option"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(rs
						.getString("component_no"), BasicFont)));// 5
				table5.addCell(new Phrase(new Chunk(rs
						.getString("com_prod_body"), BasicFont)));// 5
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("com_mask_option")), BasicFont)));// 5
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("com_backend_option")), BasicFont)));// 5
				table5.addCell(new Phrase(new Chunk(rs
						.getString("db_with_code"), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("sort_route_code")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ws_route")), BasicFont)));// 8
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ws_route_add")), BasicFont)));// 10
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("wsspecialcontrol")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("tf_ws_comment")), BasicFont)));// 14
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("avi")), BasicFont)));// 4
				table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs
						.getString("ink")), BasicFont)));// 4

			}
			doc.add(table5);
			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
		return false;
	}

	// for Wip Handling Control
	public static boolean WipControl(Document doc, String productbody,
			Font BasicFont, String section, String sid, String table,
			Connection conn) {

		// ProTestRouteBeanAF fm = new ProTestRouteBeanAF();
		// fm.setSid(sid);
		// fm.setVendor(vendor);
		// WsTestBean[] wtb = ToGetMask_Option( fm, conn, table);
		//
		// if (wtb.length == 0) // 沒有 WS data 不需要處理
		// return true;

		try {

			String sql = "SELECT * FROM tf_wip" + table + " where sid=? "
					+ "order by pgname1";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ResultSet rs = ps.executeQuery();
			String ctrl_type = "";

			int i = 0;
			float[] widths = { 20, 20, 20, 20, 20 };
			PdfPTable table5 = null;
			table5 = new PdfPTable(widths);
			table5.setWidthPercentage(100);
			table5.setSpacingBefore(5);
			table5.addCell(new Phrase(
					new Chunk("Tested 1st PG Name", BasicFont)));
			table5.addCell(new Phrase(
					new Chunk("Tested 2nd PG Name", BasicFont)));
			table5.addCell(new Phrase(
					new Chunk("Tested 3rd PG Name", BasicFont)));
			table5.addCell(new Phrase(
					new Chunk("Tested 4th PG Name", BasicFont)));
			table5.addCell(new Phrase(
					new Chunk("Tested 5th PG Name", BasicFont)));

			while (rs.next()) {
				if (i == 0) {
					ctrl_type = rs.getString("ctrl_type");
					if (rs.getString("ctrl_type").equals("0"))
						return false;
					doc.newPage();
					doc.add(new Paragraph(section + ". Wip Handling Control"));
					StringBuffer data = new StringBuffer("Wip Control for: ");

					if (rs.getString("optionlist") != null) {
						String options[] = rs.getString("optionlist")
								.split(",");
						int j = 0;
						for (; j < options.length - 1; j++)
							data.append(productbody + options[j] + ",");
						data.append(productbody + options[j] + "\n");
					} else
						data.append("\n");
					doc.add(new Phrase(new Chunk(data.toString(), BasicFont)));

					data.delete(0, data.length());
					data.append(StringUtil.Utf8ToBig5_new(TDSResource
							.getProperties("TIMPdf").getValue(
									"ctrl" + rs.getString("ctrl_type"))));
					if (rs.getString("ctrl_type").equals("2-4-1"))
						data.append(rs.getString("pgname1"));
					doc.add(new Phrase(new Chunk(data.toString(), BasicFont)));
				}
				i++;
				if (rs.getString("ctrl_type").equals("3-1")) {
					table5.addCell(new Phrase(new Chunk(
							rs.getString("pgname1"), BasicFont)));
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("pgname2")), BasicFont)));
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("pgname3")), BasicFont)));
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("pgname4")), BasicFont)));
					table5.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(rs.getString("pgname5")), BasicFont)));
				}
			}
			if (ctrl_type.equals("3-1"))
				doc.add(table5);
			rs.close();
			ps.clearParameters();
			ps.close();
			rs = null;
			ps = null;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
		return true;
	}

	public static boolean PackageMapping(Connection conn, Document doc,
			ProTestRouteBeanAF fm, String section) {

		Font BasicFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);

		try {
			String sql = "select distinct b.package_type, prm2_code package_code from ";
			sql += "tf_test_parameter_ft a, ba_package_type b ";
			sql += "where sid = ? and version = ? and a.package_type = b.package_type";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, fm.getSid());
			ps.setString(2, fm.getVersion());

			ResultSet rs = ps.executeQuery();

			int i = 0;
			PdfPTable tableWS = null;
			while (rs.next()) {
				if (i++ == 0) {
					doc.newPage();
					doc.add(new Paragraph(section + ". Package Mapping"));
					tableWS = new PdfPTable(2);
					tableWS.setSpacingBefore(10);
					tableWS.setWidthPercentage(100);
					tableWS.addCell(new Phrase(new Chunk("Package Name",
							BasicFont)));
					tableWS.addCell(new Phrase(new Chunk("Package Code",
							BasicFont)));
				}
				tableWS.addCell(new Phrase(new Chunk(rs
						.getString("package_type"), BasicFont)));
				tableWS.addCell(new Phrase(new Chunk(rs
						.getString("package_code"), BasicFont)));
			}
			doc.add(tableWS);
			ps.clearParameters();
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		}
		return false;
	}

	public static String getFileName(Connection conn, String productBody,
			String brand, String version, String vendor, boolean diffFlag,
			String tx) {
		String fileName = null;
		String diff = null;
		String oiname = "8049";

		if (vendor == null)
			vendor = "";
		else
			vendor = OiMaintainService.getVendorShortName(conn, vendor);
		if (diffFlag)
			diff = "c";
		else
			diff = "";

		// if (vendor.equals("MX01")) { oiname = "8046"; vendor = ""; }

		if (brand.equals("KH"))
			fileName = oiname + "k-" + productBody + vendor + diff + "v"
					+ version + tx;
		else
			fileName = oiname + "-" + productBody + vendor + diff + "v"
					+ version + tx;
		fileName = fileName.toLowerCase();
		return fileName;
	}

}
