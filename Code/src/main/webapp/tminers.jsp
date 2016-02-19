<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TMiners</title>
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
			<f:view>
				<h:form>
					<hr>
					<div class="form-container">
						<h:panelGrid columns="2">
							<h:outputText value="Keyword: " />
							<h:inputText id="keyword" value="#{twitterStream.keyword}"
								required="true" requiredMessage="Keyword is required"
								label="Keyword" maxlength="25"
								style="font-weight:bold;width:240px;height:25px" />
							<h:outputText value=""></h:outputText>
							<h:message for="keyword" style="color:red;font-weight:bold" />
							<h:outputText value=""></h:outputText>
							<h:commandButton type="submit" value="Generate Heat Map"
								action="#{twitterStream.getTweetsWordCount}" />
							<h:outputText value=""></h:outputText>
							<h:commandButton type="submit" value="Find Sentiment"
								action="#{twitterStream.getTweetsSentiment}" />
						</h:panelGrid>
					</div>
				</h:form>
			</f:view>
		</div>
	</center>
</body>
</html>