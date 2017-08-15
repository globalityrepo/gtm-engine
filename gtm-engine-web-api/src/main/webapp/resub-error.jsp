<%@ page isErrorPage="true" import="java.io.*" contentType="text/html"%>
<html>
	<head>
		<title>GTM - Report de Execução</title>
	</head>
	<body style="font-family:verdana">
		<h2>GTM - GLOBAL TRANSACTION MANAGER</h2>		
		<h3>Operação: RESUBMIT<p>
			Status: <font color="#ff0000">ERRO</font><p>
			Mensagem:<p>&nbsp;&nbsp;&nbsp;&nbsp;<i><%=exception.getMessage()%></i>
		</h3>
	</body>
</html>

