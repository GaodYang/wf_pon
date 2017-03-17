<%@page contentType="text/html" pageEncoding="UTF-8"%>  
<!DOCTYPE html>  
<html>  
    <head>  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
        <title>JSP Page</title>  
          
        
    </head>  
    <body>  
        <h3>username = <%=request.getParameter("username")%></h3>
        <h3>prjcode = <%=request.getParameter("prjcode")%></h3>
        <h3>parentprjcode = <%=request.getParameter("parentprjcode")%></h3>
    </body>  
</html>  