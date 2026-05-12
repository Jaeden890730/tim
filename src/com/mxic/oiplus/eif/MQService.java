package com.mxic.oiplus.eif;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import com.ibm.mq.*;            // Include the MQ package
import java.util.*;
import java.io.*;

import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.*;
public class MQService {
  private MQQueueManager qMgr;                 // define a queue manager object
  private MQQueue queue;                       // define a queue object
  private String eifName;
  private final String filePath = "EIF";
  private String hostname;           // define the name of your host to connect to
  private String channel; // define name of channel for client to use
                                                           // Note. assumes MQ Server is listening on
                                                           // the default TCP/IP port of 1414
  public String qManager;          // define name of queue manager object to
                                                           // connect to.
  public String qName;              // difine name of queue object to connect to.
  private int port;
  private int ccsid;

  private boolean isWaitUnLimit = false;

  private LogWriter log;

  private static final int MQ_GET_WAITTIME = 10000;

  public MQService(String eifName,LogWriter log) {
          Properties prop = TDSResource.getProperties(filePath);
          this.eifName = eifName;
          this.log = log;
          hostname = prop.getProperty(".hostName");
          channel = prop.getProperty(".channel");
          qManager = prop.getProperty(".queueManager");
          qName = prop.getProperty(eifName+".queueName");
          port = Integer.parseInt(prop.getProperty(".port"));
          ccsid = Integer.parseInt(prop.getProperty(".ccsid"));

  }

  public int MQConnect(){
          try{
                log.WriterToLog(eifName+"|Queue Mananger : "+qManager);
                log.WriterToLog(eifName+"|Queue Name     : "+qName);
                log.WriterToLog(eifName+"|Establishing MQ Connection");
                init();
                return MQException.MQCC_OK;
          }
          catch (MQException ex)
          {
            TDSLogger.println("An MQ error occurred : Completion code " +
                               ex.completionCode +
                               " Reason code " + ex.reasonCode);

                log.WriterToLog(eifName+"|Connection FAILED. MQ Reason Code "+ex.reasonCode);
                return ex.completionCode;
          }
  }

  public int OpenQueue(){
          try{
                int openOptions = MQC.MQOO_INPUT_AS_Q_DEF |
                                  MQC.MQOO_FAIL_IF_QUIESCING |
                                  MQC.MQOO_OUTPUT;
                if(qName.endsWith("R")){
                      openOptions = MQC.MQOO_FAIL_IF_QUIESCING |
                                    MQC.MQOO_OUTPUT;
                }
                queue = qMgr.accessQueue
                              (qName,
                               openOptions,
                               null,           // default q manager
                               null,           // no dynamic q name
                               null);          // no alternate user id
                return MQException.MQCC_OK;
          }catch (MQException ex){
                TDSLogger.println("An MQ error occurred : Completion code " +
                                   ex.completionCode +
                                   " Reason code " + ex.reasonCode);
                log.WriterToLog(eifName+"|Open Queue FAILED. MQ Reason Code "+ex.reasonCode);
                return ex.completionCode;
          }
  }

  public int MQGet(StringBuffer mqBuffer,String reasonCode){
          try{
                MQMessage retrievedMessage = new MQMessage();
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                retrievedMessage.messageId = MQC.MQMI_NONE;
                gmo.options = MQC.MQGMO_WAIT ;
                gmo.waitInterval = MQ_GET_WAITTIME;   //Wait 10 sec
                if(isWaitUnLimit){
                    gmo.waitInterval = MQC.MQWI_UNLIMITED; //MQC.MQWI_UNLIMITED;
                }
                queue.get(retrievedMessage,gmo);
                mqBuffer.delete(0,mqBuffer.length());
                mqBuffer.append(retrievedMessage.readString(retrievedMessage.getMessageLength()));
                log.WriterToLog("CCSID : "+MQEnvironment.CCSID);
                reasonCode = Integer.toString(MQException.MQRC_NONE);
                return MQException.MQCC_OK;
          }catch (MQException ex){
                TDSLogger.println("An MQ error occurred : Completion code " +
                                   ex.completionCode +
                                   " Reason code " + ex.reasonCode);
                reasonCode = Integer.toString(ex.reasonCode);
                if(ex.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE){
                        log.WriterToLog(eifName+"-MQ-GET|No Message found in the ["+qName+"] Local Queue");
                }else if(ex.completionCode == MQException.MQCC_FAILED){
                        log.WriterToLog(eifName+"-MQ-GET|Message Receive Error from "+qName+" , Received MQ Reason Code is "+ex.reasonCode);
                }else{
                        log.WriterToLog(eifName+"-MQ-GET|Message Receive Error "+qName+" "+ex.getMessage());
                }
                return ex.completionCode;
          }catch (IOException ex){
                reasonCode = Integer.toString(MQException.MQRC_FORMAT_ERROR);
                log.WriterToLog(eifName+"-MQ-GET|Message Receive Error from "+qName+" , Received IO Exception : "+ex.getMessage());
                return MQException.MQCC_FAILED;
          }catch (Exception ex){
                log.WriterToLog(eifName+"-MQ-GET|Message Receive Error "+qName+" "+ex.getMessage());
                TDSLogger.println(ex);
                return MQException.MQCC_UNKNOWN;
          }

  }

  public int MQSend(StringBuffer mqBuffer,String reasonCode){
          try{
                MQMessage sendMessage = new MQMessage();
                MQPutMessageOptions pmo = new MQPutMessageOptions();
                sendMessage.writeString(mqBuffer.toString());
                queue.put(sendMessage,pmo);
                reasonCode = String.valueOf(MQException.MQRC_NONE);
                return MQException.MQCC_OK;
          }catch (MQException ex){
                TDSLogger.println("An MQ error occurred : Completion code " +
                                   ex.completionCode +
                                   " Reason code " + ex.reasonCode);
                reasonCode = Integer.toString(ex.reasonCode);
                return ex.completionCode;
          }catch (IOException ex){
                TDSLogger.println("An MQ error occurred : " +
                                   ex.getMessage() );
                return MQException.MQCC_FAILED;
          }

  }

  public int MQSend(StringBuffer mqBuffer,String reasonCode, String messageId, String correlId){
      try{
            MQMessage sendMessage = new MQMessage();
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            if ((messageId != null) && (messageId.length() > 0))
            	sendMessage.messageId = messageId.getBytes();
            if ((correlId != null) && (correlId.length() > 0))
            	sendMessage.correlationId = correlId.getBytes();
            sendMessage.writeString(mqBuffer.toString());
            queue.put(sendMessage,pmo);
            reasonCode = String.valueOf(MQException.MQRC_NONE);
            return MQException.MQCC_OK;
      }catch (MQException ex){
            TDSLogger.println("An MQ error occurred : Completion code " +
                               ex.completionCode +
                               " Reason code " + ex.reasonCode);
            reasonCode = Integer.toString(ex.reasonCode);
            return ex.completionCode;
      }catch (IOException ex){
            TDSLogger.println("An MQ error occurred : " +
                               ex.getMessage() );
            return MQException.MQCC_FAILED;
      }
  }
  
  public void CloseMQ(){
          try{
                if(queue.isOpen()){
                    queue.close();
                }
          }catch(MQException ex){
                TDSLogger.println("An MQ error occurred : Completion code " +
                           ex.completionCode +
                               " Reason code " + ex.reasonCode);
          }
  }

  public void DisconnectionMQ(){
          try{
                if(qMgr.isConnected()){
                      qMgr.disconnect();
                      TDSLogger.println("disconnection!!!");
                }
          }catch(MQException ex){
                TDSLogger.println("An MQ error occurred : Completion code " +
                           ex.completionCode +
                               " Reason code " + ex.reasonCode);
          }
  }

  public void setWaitUnLimit(boolean unlimit){
          isWaitUnLimit = unlimit;
  }

  private void init()throws MQException
          {
               // Set up MQ environment
               TDSLogger.println("connection!!!");
               MQEnvironment.hostname = hostname;              // Could have put the hostname & channel
               MQEnvironment.channel  = channel;               // string directly here!
               MQEnvironment.port     = port;
               MQEnvironment.CCSID    = ccsid;
               qMgr = new MQQueueManager(qManager);             // Default port number
            //
          } // end of init
  public static void main(String[] args) {
    //MQService MQService1 = new MQService();
  }
}