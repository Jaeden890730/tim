package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.FileOutputStream;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;


public class OIMigrationUploadAction extends Action {
  public OIMigrationUploadAction() {
  }

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    OIMigrationUploadActionForm oIMigrationUploadActionForm =
        (OIMigrationUploadActionForm) actionForm;
    String fileExt = null;

    String path = TDSResource.getProperties("TIMPdf").getValue("migration.dir");
    //  FormFile myFile = oIMigrationUploadActionForm.getFormname();
    //  String FullFileName = StringUtil.Utf8ToBig5(myFile.getFileName());
    String pd_body = oIMigrationUploadActionForm.getPd_body();
    String brand = oIMigrationUploadActionForm.getBrand();
    String version = oIMigrationUploadActionForm.getVersion();

    String productType = OiMaintainService.getProductType(pd_body, brand, version);
    /*if (productType.equals("NVM"))
    	fileExt = ".csv";
    else
    	fileExt = ".xls";*/
    fileExt = ".csv";

    FormFile bom_route_xrom = oIMigrationUploadActionForm.getTf_bom_route_xrom();
    String bom_route_xrom_name = StringUtil.Utf8ToBig5(bom_route_xrom.getFileName());

    FormFile bom_reroute_xrom = oIMigrationUploadActionForm.getTf_bom_reroute_xrom();
    String bom_reroute_xrom_name = StringUtil.Utf8ToBig5(bom_reroute_xrom.getFileName());

    FormFile parameter_ws = oIMigrationUploadActionForm.getTf_test_parameter_ws();
    String parameter_ws_name = StringUtil.Utf8ToBig5(parameter_ws.getFileName());

    FormFile parameter_ft = oIMigrationUploadActionForm.getTf_test_parameter_ft();
    String parameter_ft_name = StringUtil.Utf8ToBig5(parameter_ft.getFileName());

    FormFile parameter_pbc = oIMigrationUploadActionForm.getTf_test_parameter_pbc();
    String parameter_pbc_name = StringUtil.Utf8ToBig5(parameter_pbc.getFileName());

    //FormFile basic_info = oIMigrationUploadActionForm.getTf_basic_information();
    //String basic_info_name = StringUtil.Utf8ToBig5(basic_info.getFileName());

    int bom_route_xrom_int = bom_route_xrom_name.indexOf(fileExt);
    int bom_route_xrom_str_int=bom_route_xrom_name.length();
    int bom_reroute_xrom_int = bom_reroute_xrom_name.indexOf(fileExt);
    int bom_reroute_xrom_str_int=bom_reroute_xrom_name.length();
    int parameter_ws_int = parameter_ws_name.indexOf(fileExt);
    int parameter_ws_str_int=parameter_ws_name.length();
    int parameter_ft_int = parameter_ft_name.indexOf(fileExt);
    int parameter_ft_str_int=parameter_ft_name.length();
    int parameter_pbc_int = parameter_pbc_name.indexOf(fileExt);
    int parameter_pbc_str_int=parameter_pbc_name.length();
    //int basic_info_int = basic_info_name.indexOf(fileExt);
    //int basic_info_str_int=basic_info_name.length();
    String success = "";
    StringBuffer fail = new StringBuffer(" ");

    if ((bom_route_xrom_int == -1 && bom_route_xrom_str_int==0) &&
        (bom_reroute_xrom_int == -1 && bom_reroute_xrom_str_int==0) &&
        (parameter_ws_int == -1 && parameter_ws_str_int==0) &&
        (parameter_ft_int == -1 && parameter_ft_str_int==0) &&
        (parameter_pbc_int == -1 && parameter_pbc_str_int==0) ) {// &&(basic_info_int == -1 && basic_info_str_int==0)
      servletRequest.setAttribute("information","請上傳資料!!");
      return actionMapping.findForward("migr");
    } else if ((bom_route_xrom_int == -1 && bom_route_xrom_str_int >0) ||
               (bom_reroute_xrom_int == -1 && bom_reroute_xrom_str_int >0) ||
               (parameter_ws_int == -1 && parameter_ws_str_int >0) ||
               (parameter_ft_int == -1 && parameter_ft_str_int >0) ||
               (parameter_pbc_int == -1 && parameter_pbc_str_int >0)) { //||(basic_info_int == -1 && basic_info_str_int >0)

      //其中有非csv之資料
      /*if (productType.equals("NVM"))
    	servletRequest.setAttribute("information","檔案中有非csv文件,請重新上傳!!");
      else
    	servletRequest.setAttribute("information","檔案中有非xls文件,請重新上傳!!");*/
      servletRequest.setAttribute("information","檔案中有非csv文件,請重新上傳!!");
      return actionMapping.findForward("migr");
    } else if ((bom_route_xrom_int == -1 && bom_route_xrom_str_int==0) ||
               (bom_reroute_xrom_int == -1 && bom_reroute_xrom_str_int==0) ||
               (parameter_ws_int == -1 && parameter_ws_str_int==0) ||
               (parameter_ft_int == -1 && parameter_ft_str_int==0)) {// ||(basic_info_int == -1 && basic_info_str_int==0)
      //其中有幾項沒上傳
    } else {
    }

    //上傳bom_route_xrom資料如果是空值
    if (bom_route_xrom_int == -1) {
    } else {
      //do上傳檔案及service的動作
      try {
        String file_name = path + bom_route_xrom_name;
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(file_name);

        fileOutput.write(bom_route_xrom.getFileData());
        fileOutput.flush();
        fileOutput.close();
        bom_route_xrom.destroy();
        if (!OIMigrationService.ins_bom_route_xrom_info(pd_body, brand, version, file_name, fail))
            success = success + "BOM table" ;

      } catch (Exception e) {
        e.printStackTrace();
        return actionMapping.findForward("migr");
      } finally {
      }
    }
    //上傳bom_reroute_xrom資料如果是空值
    if (bom_reroute_xrom_int == -1) {
    } else {
      //do上傳檔案及service的動作
      try {
        String file_name = path + bom_reroute_xrom_name;
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(file_name);

        fileOutput.write(bom_reroute_xrom.getFileData());
        fileOutput.flush();
        fileOutput.close();
        bom_reroute_xrom.destroy();
        if (!OIMigrationService.ins_bom_reroute_xrom_info(pd_body, brand, version, file_name, fail))
            success = success +  "BOM table" ;

      } catch (Exception e) {
        e.printStackTrace();
        return actionMapping.findForward("migr");
      } finally {
      }
    }
    //上傳parameter_ws資料如果是空值
    //
    Vector vendors = OIMigrationService.getVendors();
    Vector testerType = null;
    if (parameter_ws_int == -1) {
    } else {
      TDSLogger.println("parameter_ws_name");
      //do上傳檔案及service的動作
      try {
        String file_name = path + parameter_ws_name;
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(file_name);

        fileOutput.write(parameter_ws.getFileData());
        fileOutput.flush();
        fileOutput.close();
        parameter_ws.destroy();
        testerType = OIMigrationService.getTesterType(0);
        if (!OIMigrationService.ins_parameter_ws_info(pd_body, brand, version, file_name, vendors, testerType,fail))
          {
            if (!success.toString().equals(""))
              success = success + "," ;
            success = success + "WS Test Parameter" ;
          }

        //boolean success1 = (new File(file_name)).delete();
      } catch (Exception e) {
        e.printStackTrace();
        return actionMapping.findForward("migr");
      } finally {
      }
    }
    //上傳parameter_tf資料如果是空值
    //
    if (parameter_ft_int == -1) {
    } else {
      //do上傳檔案及service的動作
      TDSLogger.println("parameter_ft_name");
      try {
        String file_name = path + parameter_ft_name;
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(file_name);

        fileOutput.write(parameter_ft.getFileData());
        fileOutput.flush();
        fileOutput.close();
        parameter_ft.destroy();
        testerType = OIMigrationService.getTesterType(1);
        if (!OIMigrationService.ins_parameter_ft_info(pd_body, brand, version, file_name, vendors, testerType, fail))
        {
            if (!success.toString().equals(""))
              success = success + "," ;
            success = success + "FT Test Parameter" ;
        }

        //boolean success1 = (new File(file_name)).delete();
      } catch (Exception e) {
        e.printStackTrace();
        return actionMapping.findForward("migr");
      } finally {
      }
    }
    //上傳parameter_pbc資料如果是空值
    //
    if (parameter_pbc_int == -1) {
    } else {
      //do上傳檔案及service的動作
      TDSLogger.println("parameter_pbc_name");
      try {
        String file_name = path + parameter_pbc_name;
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(file_name);

        fileOutput.write(parameter_pbc.getFileData());
        fileOutput.flush();
        fileOutput.close();
        parameter_ft.destroy();
        if (!OIMigrationService.ins_parameter_pbc_info(pd_body, brand, version, file_name, vendors, fail))
        {
            if (!success.equals(""))
              success = success + ",";
            success = success + "PBC Test Parameter";
        }

        //boolean success1 = (new File(file_name)).delete();
      } catch (Exception e) {
        e.printStackTrace();
        return actionMapping.findForward("migr");
      } finally {
      }
    }
    //上傳basic_info資料如果是空值
    /*if (basic_info_int == -1) {
    } else {
      //do上傳檔案及service的動作
      TDSLogger.println("basic_info_name");
      try {
        String file_name = path + basic_info_name;
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(file_name);

        fileOutput.write(basic_info.getFileData());
        fileOutput.flush();
        fileOutput.close();
        basic_info.destroy();
        if (productType.equals("NVM")) {
          if (!OIMigrationService.ins_basic_info(pd_body, brand, version, file_name))
          {
            if (!success.equals(""))
              success = success + ",";
            success = success + "Basic Information";
          }
        } else {
          if (!OIMigrationService.ins_basic_info_ASM(pd_body, brand, version, file_name))
          {
            if (!success.equals(""))
              success = success + ",";
            success = success + "Basic Information";
          }
        }
        // boolean success1 = (new File(file_name)).delete();
      } catch (Exception e) {
        e.printStackTrace();
        return actionMapping.findForward("migr");
      } finally {
      }
    }*/
    if (success.toString().equals(""))
      servletRequest.setAttribute("information","上傳成功!!");
    else
      servletRequest.setAttribute("information","上傳成功!!但 " + success + " 部份資料有問題!! " + fail.toString());
    return actionMapping.findForward("migr");
  }
}
