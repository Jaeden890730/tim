package com.mxic.oiplus.util;


import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

public class Attachment {
  protected boolean isCreated = false;
  private int attach_seq;
  private int form_id;
  private String emp_id;
  private Date upload_time;
  private String field;
  private String file_name;
  private String mime;
  private byte[] content;
  private Connection connection = null;

  public Attachment() {
    // upload_time = Util.getToday();
  }

  public int getAttach_seq() {
    return attach_seq;
  }

  protected void setAttach_seq(int attach_seq) {
    this.attach_seq = attach_seq;
  }

  public void setForm_id(int form_id) {
    this.form_id = form_id;
  }

  public int getForm_id() {
    return form_id;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public byte[] getContent() {
    return content;
  }

  public void setEmp_id(String emp_id) {
    this.emp_id = emp_id;
  }

  public String getEmp_id() {
    return emp_id;
  }

  public void setUpload_time(Date upload_time) {
    this.upload_time = upload_time;
  }

  public Date getUpload_time() {
    return upload_time;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }

  public void setFile_name(String file_name) {
    this.file_name = file_name;
  }

  public String getFile_name() {
    return file_name;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }

  public String getMime() {
    return mime;
  }

  private Attachment[] translateFromArrayList(ArrayList tmp) {
    return (Attachment[]) tmp.toArray(new Attachment[0]);
  }

        /*
        public static void main(String[] args) {
                try {
                        File file = new File("c:\\1.doc");
                        int file_size = (int) file.length();
                        byte[] content = new byte[file_size];
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                        bis.read(content, 0, file_size);
                        Attachment att = new Attachment();

                        att.setContent(content);
                        att.setFile_name("1.doc");
                        att.setEmp_id("emp_id");
                        att.writeToDB();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        */
}
