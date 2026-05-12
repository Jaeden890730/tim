package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class DocLinkageAddAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {

    String forward = "success";
    String path = TDSResource.getProperties("TIMPdf").getValue("jpg_tx.path") + File.separator;
    DocLinkageAddActionForm docLinkageAddActionForm = (DocLinkageAddActionForm) actionForm;
    FormFile myFile = docLinkageAddActionForm.getFormname(); //宣告myFile為FormFile物件

    String myFile_name = StringUtil.Utf8ToBig5(myFile.getFileName());

    int myFile_name_int = myFile_name.indexOf(".png");

    int myFile_name_str_int = myFile_name.length();
    if ((myFile_name_int == -1 && myFile_name_str_int == 0)) {
      //未上傳檔案
      String sid=String.valueOf(docLinkageAddActionForm.getSid());
      HttpSession session = request.getSession();
      session.setAttribute("list7",sid);
      return actionMapping.findForward("fail");
    } else if ((myFile_name_int == -1 && myFile_name_str_int > 0)) {
      //上傳非.png檔案
      String sid=String.valueOf(docLinkageAddActionForm.getSid());
      HttpSession session = request.getSession();
      session.setAttribute("list7",sid);
      return actionMapping.findForward("fail");
    } else {
      //String FullFileName = StringUtil.Utf8ToBig5(myFile.getFileName()); //將完整檔名轉成BIG5碼
      String doc_type = "";
      if (docLinkageAddActionForm.getCategory().equals("Yield Definition")) {
        doc_type = "Y";
      } else if (docLinkageAddActionForm.getCategory().equals("Test Flow")) {
        doc_type = "T";
      } else {
        doc_type = "M";
      }

      String seq = String.valueOf(docLinkageAddActionForm.getSeq());
      int seq_length = seq.length();
      if (seq_length == 1) {
        seq = "00" + seq;
      } else if (seq_length == 2) {
        seq = "0" + seq;
      }

      String file_name = DocLinkageAddService.file_name(
          docLinkageAddActionForm.getSid(),
          docLinkageAddActionForm.getCategory(),
          docLinkageAddActionForm.getSeq());

      if (file_name.equals("") || file_name == null) {
        if (doc_type.equals("M")){
          file_name = docLinkageAddActionForm.getPd_body() + "_" +
              //docLinkageAddActionForm.getBrand() + "_" +
              docLinkageAddActionForm.getVersion() + "_" +
              doc_type + ".png";
          DocLinkageAddService.update_file_name(docLinkageAddActionForm.getSid(),
                                                docLinkageAddActionForm.getPd_body(),
                                                docLinkageAddActionForm.getBrand(),
                                                docLinkageAddActionForm.getVersion(),
                                                docLinkageAddActionForm.getSeq(),
                                                docLinkageAddActionForm.getDoc_name(),
                                                file_name,
                                                docLinkageAddActionForm.getComment(),
                                                docLinkageAddActionForm.getCategory(),
                                                "M");
        } else {
          file_name = docLinkageAddActionForm.getPd_body() + "_" +
              //docLinkageAddActionForm.getBrand() + "_" +
              docLinkageAddActionForm.getVersion() + "_" +
              doc_type + "_1_" +
              seq + ".png";
          DocLinkageAddService.update_file_name(docLinkageAddActionForm.getSid(),
                                                docLinkageAddActionForm.getPd_body(),
                                                docLinkageAddActionForm.getBrand(),
                                                docLinkageAddActionForm.getVersion(),
                                                docLinkageAddActionForm.getSeq(),
                                                docLinkageAddActionForm.getDoc_name(),
                                                file_name,
                                                docLinkageAddActionForm.getComment(),
                                                docLinkageAddActionForm.getCategory(),
                                                "YT");
        }
      } else {
      }
      String FullFileName = StringUtil.Utf8ToBig5(file_name); //將完整檔名轉成BIG5碼
      String TmpFileName = new String(FullFileName + ".tmp");
      File newFile = new File(path + FullFileName);
      File tmpFile = new File(path + TmpFileName);
      try {
        //將上傳的檔案存在/citplus/File裡
        FileOutputStream fileOutput = new FileOutputStream(tmpFile);
        fileOutput.write(myFile.getFileData());
        fileOutput.flush();
        fileOutput.close();
        if ((!newFile.exists()) || (FileUtil.Compare(newFile, tmpFile) == 1)) {
          FileUtil.Copy(tmpFile, newFile);
          forward = "success";
        } else
          forward = "upload_skip";

        myFile.destroy();
        String sid=String.valueOf(docLinkageAddActionForm.getSid());
        HttpSession session = request.getSession();
        session.setAttribute("list7",sid);
      } catch (Exception e) {
        TDSLogger.println(e);
        e.printStackTrace();
      } finally {
        if (tmpFile.exists())
          tmpFile.delete();
      }
      return actionMapping.findForward(forward);
    }
  }
}
