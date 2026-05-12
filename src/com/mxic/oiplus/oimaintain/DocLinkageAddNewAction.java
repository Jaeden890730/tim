package com.mxic.oiplus.oimaintain;

import java.io.File;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.*;

public class DocLinkageAddNewAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {

    DocLinkageAddNewActionForm docLinkageAddNewActionForm =
        (DocLinkageAddNewActionForm) actionForm;
    String path_tx = TDSResource.getProperties("TIMPdf").getValue("jpg_tx.path") + File.separator;
    String path_route = TDSResource.getProperties("TIMPdf").getValue("jpg_route.path") + File.separator;
    
	/*
    FormFile myFile = docLinkageAddNewActionForm.getFormname(); //宣告myFile為FormFile物件

    String myFile_name = StringUtil.Utf8ToBig5(myFile.getFileName());

    int myFile_name_int = myFile_name.toLowerCase().indexOf(".png");

    int myFile_name_str_int = myFile_name.length();
    if ((myFile_name_int == -1 && myFile_name_str_int == 0)) {
      //未上傳檔案
      String sid = String.valueOf(docLinkageAddNewActionForm.getSid());
      HttpSession session = request.getSession();
      session.setAttribute("list7", sid);
      return actionMapping.findForward("fail");
    } else if ((myFile_name_int == -1 && myFile_name_str_int > 0)) {
      //上傳非.png檔案
      String sid = String.valueOf(docLinkageAddNewActionForm.getSid());
      HttpSession session = request.getSession();
      session.setAttribute("list7", sid);
      return actionMapping.findForward("fail");
    } else {
    */
      //String FullFileName = StringUtil.Utf8ToBig5(myFile.getFileName()); //將完整檔名轉成BIG5碼
      String doc_type = "";
      TDSLogger.println(docLinkageAddNewActionForm.getCategory());
      TDSLogger.println("KX");
      if (docLinkageAddNewActionForm.getCategory().equals("Yield Definition")) {
        doc_type = "Y";
      } else if (docLinkageAddNewActionForm.getCategory().equals("Test Flow")) {
        doc_type = "T";
      }
      String file_name = "";
      file_name = DocLinkageAddService.add_new(docLinkageAddNewActionForm.getSid(),
                                               docLinkageAddNewActionForm.getPd_body(),
                                               docLinkageAddNewActionForm.getBrand(),
                                               docLinkageAddNewActionForm.getVersion(),
                                               docLinkageAddNewActionForm.getDoc_name(),
                                               docLinkageAddNewActionForm.getComment(),
                                               doc_type);
      String FullFileName = StringUtil.Utf8ToBig5(file_name); //將完整檔名轉成BIG5碼
      String TmpFileName = docLinkageAddNewActionForm.getFile_name();//new String(FullFileName + ".tmp");
      File newFile = new File(path_tx + FullFileName);
      File tmpFile = new File(path_route + TmpFileName);

      String sid = String.valueOf(docLinkageAddNewActionForm.getSid());
      HttpSession session = request.getSession();
      try {
        //將上傳的檔案存在/citplus/File裡
    	  /*
        FileOutputStream fileOutput = new FileOutputStream(path + FullFileName);
        fileOutput.write(myFile.getFileData());
        fileOutput.flush();
        fileOutput.close();
        myFile.destroy();
        */
      	if (tmpFile.exists())
    		FileUtil.Copy(tmpFile, newFile);

        session.setAttribute("list7", sid);
      } catch (Exception e) {
        e.printStackTrace();
        session.setAttribute("list7", sid);
        return actionMapping.findForward("fail");
      } finally {
      }
      return actionMapping.findForward("addnew");
    //}
  }
}
