$(document).ready(function (){
	var photosArea=$("#photos");
	var loader=$("#loader");
	
	var accInput=$("#acc");
	var maxWidth="230px"; 
	var errorTooltip = $(".field-error-message");
	
	var span = document.getElementsByClassName("close")[0];
	var modal = document.getElementById('myModal');
	var modalImg = document.getElementById("img01");
	
	$("#searchForm").submit(function(event){
		event.preventDefault();
		var enteredAccs=$("#instagrams").val();
		console.log("Entered accounts="+enteredAccs);
		if(enteredAccs===""){
			attentionToAccField();
			return;
		}
		var accountsArray = enteredAccs.split(" ");
		console.log("Array of accs="+accountsArray);
		if(accountsArray.length===0){
			attentionToAccField();
			return;
		}
		errorTooltip.css("visibility","hidden");
		photosArea.html("");
		loader.css("display","inline-block");
		loader.css("animation-play-state","running");
		$.get($(this).attr("action"),
			  {"instagrams":accountsArray},
			  function(result){
				  loader.css("display","none");
				  loader.css("animation-play-state","initial");
				  display(result);
			  });
		return false;
	});
	
	$("#btn-reset").click(function(){
		$("#instagrams").val("");
		photosArea.html("");
	});
	$(accInput).click(function(){
		accInput.animate({width:maxWidth},200);
		return;
	});
	$(accInput).focusout(function(){
		accInput.animate({width:"180px"},200);
		errorTooltip.css("visibility","hidden");
	});
	$("#btn-add").click(function(){
		var instagramsInput=$("#instagrams");
		var acc = accInput.val();
		if($.trim(acc)===""){
			attentionToAccField();
			return;
		}
		errorTooltip.css("visibility","hidden");
		accInput.animate({width:"180px"},200);
		var enteredAccs=instagramsInput.val();
		if(enteredAccs===""){
			instagramsInput.val(acc);
		}else{
			instagramsInput.val(enteredAccs+" "+acc);
		}
		accInput.val("");
	});
	function attentionToAccField(){
		accInput.focus();
		accInput.animate({width:maxWidth},500,function(){
			errorTooltip.css("visibility","visible");
		});
	}
	
	span.onclick = function() { 
		modal.style.display = "none";
	}
	
	/*displaying photo after downloading via ajax request*/
	function display(photos) {
		photosArea.html("");
		var numberOfPhoto = 0;
		$.each(photos,function(key,listOfUrls){
			photosArea.append("<div class='retrievedAcc'><h4>"+key+" - "+listOfUrls.length+" photos</h4></div>");
			$.each(listOfUrls,function(index,url){
				
				var $div = $("<div>",{"class":"photoWrap"});
			    $div.html("<img id="+numberOfPhoto+++" class = 'instaPhoto' src='"+url+ "' height='300' width='300'/>"); 
			    photosArea.append($div);
			})				
		});
		
		//dynamically added elements
		$("img.instaPhoto").on("click",function(){
			var imgId=$(this).attr("id");
			console.log("Clicked on img with class=instaPhoto and id="+imgId);
			modal.style.display = "block";
			modalImg.src = document.getElementById(imgId).src;
		});
	}
});


