package com.mxic.fw8049.eif;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.mxic.fw8049.dao.FwReleaseDao;
import com.mxic.fwrs.applyform.BaseActionForm;
import com.mxic.fwrs.applyform.FwrsNclFwAppActionForm;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.rs.TDSConfig;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class EifFWRelease {
	public static void main(String[] args) {
		
        Connection con = null;

        try {
        	
            con = DBConnection.getConnection();
            List<String> noList = FwReleaseDao.queryANoList(con);
            
            if (noList == null || noList.isEmpty()) {
                TDSLogger.println("IF_FWNO table 查無 FLAG = U 的資料");
                DBConnection.commit(con);
                return;
            }
            
            
            List<FwrsNclFwAppActionForm> fmList = FwReleaseDao.queryNclFWAppFormList(con, noList);

            for (FwrsNclFwAppActionForm fm : fmList) {
            	seteffective_date(con, fm);
                sendmodify(con, fm);
			}

            DBConnection.commit(con);
            TDSLogger.println("EifFWRelease 執行完成，APP_ID：");
        } catch (Exception e) {
            TDSLogger.println("EifFWRelease 執行失敗：" + e.getMessage());
            TDSLogger.println(e);
            DBConnection.rollback(con);
        } finally {
            DBConnection.close(con);
        }
    }

    public static void seteffective_date(Connection con, BaseActionForm fm) throws Exception {
        FwrsNclFwAppActionForm thisfm = (FwrsNclFwAppActionForm) fm;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String appId = fm.getApp_id();
        String EffectiveDateStr = sdf.format(new java.util.Date());
        Timestamp EffectiveDate = Timestamp.valueOf(EffectiveDateStr);

        String sql = "update fwrs_ncl_fw_app set EFFECTIVE_DATE = ? where app_id = ?";
        GPRSDB.execDML(con, sql, new Object[] { EffectiveDate, appId });

        try {
            if ("Y".equals(thisfm.getHsm())) {
                Path provwaitFile = getPath("fwrs_ncl_wait_path", thisfm.getNcl_form_no() + "_PROD.swp");
                Path devwaitFile = getPath("fwrs_ncl_wait_path", thisfm.getNcl_form_no() + "_DEV.swp");
                Path provreleaseFile = getPath("fwrs_ncl_release_path", thisfm.getNcl_form_no() + "_PROD.swp");
                Path devreleaseFile = getPath("fwrs_ncl_release_path", thisfm.getNcl_form_no() + "_DEV.swp");

                Files.move(provwaitFile, provreleaseFile, StandardCopyOption.REPLACE_EXISTING);
                Files.move(devwaitFile, devreleaseFile, StandardCopyOption.REPLACE_EXISTING);
                TDSLogger.println("檔案已成功轉移至release");
            } else if ("N".equals(thisfm.getHsm())) {
                Path nohsmwaitFile = getPath("fwrs_ncl_wait_path", thisfm.getNcl_form_no() + ".swp");
                Path nohsmreleaseFile = getPath("fwrs_ncl_release_path", thisfm.getNcl_form_no() + ".swp");
                Files.move(nohsmwaitFile, nohsmreleaseFile, StandardCopyOption.REPLACE_EXISTING);
                TDSLogger.println("檔案已成功轉移至release");
            }

            Path txtFile = getPath("fwrs_ncl_wait_path", getTxtFileName(thisfm));
            Path txtRelFile = getPath("fwrs_ncl_release_path", getTxtFileName(thisfm));
            Files.move(txtFile, txtRelFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            TDSLogger.println("移動檔案時發生錯誤：" + e.getMessage());
        }

        sql = "SELECT A.* FROM FWRS_NCL_FW_APP A \n"
                + "WHERE A.PROD_BODY = ? \n"
                + "AND A.BE_OPTION = ? \n"
                + "AND A.MAJOR = ? \n"
                + "AND A.MINOR = ? \n"
                + "AND A.status = '已結案' \n"
                + "AND A.EXPIRATION_DATE IS NULL \n"
                + "AND APP_ID != ? \n";

        HashMap<String, String>[] hm = GPRSDB.qryHashMapBySql(con, sql,
                new Object[] {
                        thisfm.getProd_body(),
                        thisfm.getBe_option(),
                        thisfm.getMajor(),
                        thisfm.getMinor(),
                        thisfm.getApp_id()
                });

        if (hm.length > 0) {
            try {
                String oldNclFormNo = hm[0].get("NCL_FORM_NO");

                if ("Y".equals(thisfm.getHsm())) {
                    Path provwaitFile = getPath("fwrs_ncl_release_path", oldNclFormNo + "_PROD.swp");
                    Path devwaitFile = getPath("fwrs_ncl_release_path", oldNclFormNo + "_DEV.swp");
                    Path provreleaseFile = getPath("fwrs_ncl_backup_path", oldNclFormNo + "_PROD.swp");
                    Path devreleaseFile = getPath("fwrs_ncl_backup_path", oldNclFormNo + "_DEV.swp");

                    Files.move(provwaitFile, provreleaseFile, StandardCopyOption.REPLACE_EXISTING);
                    Files.move(devwaitFile, devreleaseFile, StandardCopyOption.REPLACE_EXISTING);
                    TDSLogger.println("release檔案已成功轉移至backup");
                } else if ("N".equals(thisfm.getHsm())) {
                    Path nohsmwaitFile = getPath("fwrs_ncl_release_path", oldNclFormNo + ".swp");
                    Path nohsmreleaseFile = getPath("fwrs_ncl_backup_path", oldNclFormNo + ".swp");
                    Files.move(nohsmwaitFile, nohsmreleaseFile, StandardCopyOption.REPLACE_EXISTING);
                    TDSLogger.println("release檔案已成功轉移至backup");
                }

                String fileName = hm[0].get("PROD_BODY") + hm[0].get("BE_OPTION") + "_"
                        + hm[0].get("MAJOR") + "_"
                        + hm[0].get("MINOR") + "_"
                        + hm[0].get("TEST_FW_VERSION") + "_"
                        + hm[0].get("HSM_KEY_FTF") + ".txt";

                // 原程式碼此段是 wait_path -> release_path，如需備份舊版 txt，請確認是否應改為 release_path -> backup_path。
                Path txtFile = getPath("fwrs_ncl_wait_path", fileName);
                Path txtRelFile = getPath("fwrs_ncl_release_path", fileName);
                Files.move(txtFile, txtRelFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                TDSLogger.println("移動檔案時發生錯誤：" + e.getMessage());
            }

            sql = "update fwrs_ncl_fw_app set EXPIRATION_DATE = ? where app_id = ?";
            GPRSDB.execDML(con, sql, new Object[] { EffectiveDate, hm[0].get("APP_ID") });
        }
    }

    public static void sendmodify(Connection con, BaseActionForm fm) throws Exception {
        FwrsNclFwAppActionForm thisfm = (FwrsNclFwAppActionForm) fm;

        String table_name = "FWRS_NCL_FW_APP";
        String appId = fm.getApp_id();
        String status = thisfm.getStatus();

        if ("QE簽核VERIFYPE驗證中".equals(status)) {
            status = "驗證中";
        }

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("TABLE_NAME", table_name);
        values.put("USER_NAME", appId);
        values.put("STATUS", status);

        GPRSDB.insert(con, "tds.ba_modify_log", values);
    }

    private static Path getPath(String configKey, String fileName) throws Exception {
        return Paths.get(TDSConfig.getProperty("TDS").getProperty(configKey) + File.separator + fileName);
    }

    /**
     * txt 檔名規則：FW_(ProductCode)_(Major)_(Minor)_(FW_Version)_(HSM_KEY_FTF).txt
     * 原程式目前組法為：PROD_BODY + BE_OPTION + "_" + MAJOR + "_" + MINOR + "_" + TEST_FW_VERSION + "_" + HSM_KEY_FTF + ".txt"
     */
    private static String getTxtFileName(FwrsNclFwAppActionForm fm) {
        return fm.getProd_body()
                + fm.getBe_option()
                + "_"
                + fm.getMajor()
                + "_"
                + fm.getMinor()
                + "_"
                + fm.getTest_fw_version()
                + "_"
                + fm.getHsm_key_ftf()
                + ".txt";
    }
}
