package com.mxic.oiplus.util;
import java.io.*;

public class SafeExec {

  private String[] cmd;
  public static void main(String[] args) {
  /* this method is for test only.
     Do not call it in your program.
                         -- Jason
  */

  StringBuffer cmd = new StringBuffer();

  if (args.length==0) {
    //cmd.append("cmd.exe /C dir");
    cmd.append("cmd.exe /C echocmd");

  } else {
    for (int i=0; i<args.length; i++)
      cmd.append(args[i] + "\n");
  }

  StringBuffer bf1 = new StringBuffer();
  StringBuffer bf2 = new StringBuffer();

  SafeExec se = new SafeExec( cmd.toString() );
  TDSLogger.println("Return Value = " + se.perform(bf1, bf2));
  TDSLogger.println("Std Output = \n" + bf1);
  TDSLogger.println("Err Output = \n" + bf2);
  }

  public SafeExec(String command) {
  /*
     Pass the shell command here.
     Remember to insert "cmd.exe /C " in front if you are running Windows Internal Commands, such as DIR, COPY.
		*/
		this.cmd = new String[3];
		this.cmd[0] = "/bin/sh";
		this.cmd[1] = "-c";
		this.cmd[2] = command;
  }

  public int perform(StringBuffer stdOut, StringBuffer errOut) {
  /* Perform the shell command in seperate process, wait for completion and get the result.
     Append Standard Output in stdOut StringBuffer, and Error Output in errOut String Buffer.
     Return the return code of executed shell command. (Normally 0 means success)
  */

    int exitVal= -1;
    try
        {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            // any error message?
            StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), errOut);

            // any output?
            StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), stdOut);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            exitVal = proc.waitFor();
            return(exitVal);
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
        return(exitVal);

  }

  private class StreamGobbler extends Thread
{
    InputStream is;
    StringBuffer output;

    StreamGobbler(InputStream is, StringBuffer output)
    {
        this.is = is;
        this.output = output;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                output.append( line + "\n" );
            } catch (IOException ioe)
              {
                TDSLogger.println(ioe);
              }
    }
}
}






