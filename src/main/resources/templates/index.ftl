<#ftl encoding="UTF-8" output_format="HTML">
<#import "/spring.ftl" as spring/>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway" >
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Arima+Madurai">
	<link rel="stylesheet" type = "text/css" href="<@spring.url '/resources/css/custom.css'/>"/>
	<link rel="stylesheet" type = "text/css" href="<@spring.url '/resources/css/zoomphoto.css'/>"/>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<script src="https://cdn.jsdelivr.net/jquery.validation/1.15.1/jquery.validate.min.js"></script>
	<script defer src="<@spring.url '/resources/js/main.js'/>"></script>
</head>
<body>
	<div id="myModal" class="modal">
	  <span class="close">x</span>
	  <img class="modal-content" id="img01">
	</div>
	<div id="container">
		<div id="header">
			<span>Instaphotos.com</span>
		</div>
		<div id="content">
			<!-- s_frolova  vitaliyorekhov kievphoto ubkbeach
			<h3>Enter instagram accounts by whitespaces:</h3> -->
			<div id="top">
				<div id="searchDiv">
					<form action="<@spring.url '/loadimages'/>" id="searchForm" name="searchForm">
						
						<input type="text" id="instagrams" 
						title="There are displayed instagram accounts"
						value="norimyxxxo" disabled 
						placeholder="Instagram accounts" name="instagrams"/>
						<div id="form-buttons">
							<button type="submit" id="btn-search">
								Get photos</button><button type="button" id="btn-reset">Reset
							</button>
						</div>
					</form>
				</div>
				
				<div id="addDiv">
					<div class="field-wrapper">
						<input type="text" title="Enter instagram account here" 
						id="acc" placeholder="Instagram account">
						<div class="field-error-message">
							Please, input name of account.
						</div>
					<button type="button" id="btn-add">
						Add
					</button>
					</div>
				</div>
			</div>
			<div id="photos"></div>
			<div id="loader"></div>
		</div>
		<div id="footer">
			<span>Copyright @ 2016</span>
		</div>
	</div>
</body>
</html>