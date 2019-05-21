<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="common/taglib.jsp" %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Content-Language"  content="ja" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<title>DEMO</title>
<link href="${path }/public/js/webuploader-0.1.5/webuploader.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${path }/public/js/jquery.min.js"></script>
<script type="text/javascript" src="${path }/public/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${path }/public/js/webuploader-0.1.5/webuploader.js"></script>
<script type="text/javascript" src="${path }/public/js/webuploader-0.1.5/webuploader.min.js"></script>
<script type="text/javascript" src="${path }/public/js/upload.js"></script>
</head>
<body>
<div id="uploader" class="wu-example">
    <!--用来存放文件信息-->
    <div id="thelist" class="uploader-list"></div>
    <div class="btns">
        <div id="attach"></div>
        <input type="button" value="上传" id="upload"/> 
    </div>
</div>



</body>
</html>