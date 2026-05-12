package com.mxic.oiplus.common;
import java.io.*;
import java.util.*;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AEBGradeAction extends Action{

	/**
	* This is the method called on by ActionServlet
	* when a request is made.
	*/
	public ActionForward execute(ActionMapping mapping,
			    	     	   ActionForm form,
			    	     	   HttpServletRequest request,
			    	     	   HttpServletResponse response
			    	     	  )

	{
		return mapping.findForward("success");
	} //End perform
} //End class
