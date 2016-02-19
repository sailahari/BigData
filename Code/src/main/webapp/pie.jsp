<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sentiment Analysis Chart</title>
<link rel="stylesheet" type="text/css" href="main.css">
</head>
<body background="background1.jpg">
	<center>
		<div class="form">
			<h2>TMiners Twitter Analysis</h2>
			<hr>
			<ul>
				<li><a href="tminers.jsp">Analysis</a></li>
			</ul>
			<hr>
			<f:view>
				<div align="center" class="form-container">
					 
					<h:form>
						<h:graphicImage value="JFreechart.png" height="480" width="640" />
					</h:form>
				</div>
			</f:view>
		</div>
	</center>
</body>
</html>