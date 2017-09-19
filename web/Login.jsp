<%-- 
    Document   : Login
    Created on : 29-nov-2016, 8:11:06
    Author     : marit
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.theme.min.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>

    <body style="background-image: url('http://es.hdwall365.com/wallpapers/1601/Kiwi-slice-fresh-fruits_m.jpg')">
         <div align="center"> <br><br><br><br><br><br>
        <div class="container">
          <P><b> <h1><font face="tahoma"><font color="white">BOGOCENTRO MINIMARKET</font></font></h1></b>
           <br>
           
                <x:form action="/accesoAction">       
                    <div class="row">
                        <div class="col-sm-1 col-sm-offset-4">
                            <h5><font FACE="tahoma">  <font color="white"> USUARIO: </font></font></h5>
                        </div>
                        <div class="col-sm-2 ">
                            <input type="text" name="usuario" property="usuario" required/>
                        </div>
                    </div>   
                    <div class="row">
                        <div class="col-sm-1 col-sm-offset-4">
                            <h5><font FACE="tahoma">  <font color="white"> CONTRASEÑA: </font></font></h5>
                        </div>
                        <div class="col-sm-2 ">
                            <input type="password" name="contraseña" property="contraseña" required/>
                        </div>
                    </div>
                    <BR>
                    <div class="row">
                        <div class="col-sm-1 col-sm-offset-5">
                            <input type="submit" name="iniciar sesion" value="INICIAR SESION" />
                        </div>
                    </div>
                </x:form>
        </div>   
    <script src="//code.jquery.com/jquery.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script> 
         </div>
    </body>  
</html>
