$(function(){

var interval = setInterval(function(){
 sendMsg2Bg();
},10000);

chrome.runtime.onMessage.addListener(function (params, sender, sendResponse) {
	if (params) {
		if(params.operate=="go" && params.content){
			window.location.href = params.content;
			return;
		}else if(params.operate=="chat" && params.content){
			clearInterval(interval);
			chat(params);
			if(isOk(params)){
				sendResponse({state:'发送成功！'});
				var tmp =window.setTimeout(function(){
					sendMsg2Bg();
					window.clearTimeout(tmp);
				},10000);	
			}else{
				var tmp =window.setTimeout(function(){
					chat(params);
					sendMsg2Bg();
					window.clearTimeout(tmp);
				},10000);
			}        	
        	return;
		}else if(params.operate=="wait" && params.content){
			var tmp =window.setTimeout(function(){
					sendMsg2Bg();
					window.clearTimeout(tmp);
				},params.content / 1);
		}
   }
});

function chat(params){
	var sendBtn = $("#js-send-msg").find("div[data-type='send']");
	var content = $("#js-send-msg").find("textarea");
	content.val(params.content);
	sendBtn.trigger("click");
}

function isOk(params){
	var myChats = $("#js-chat-cont").find("p[class='my-cont']");
	if(!myChats || myChats.length < 1){
		return false;
	}

	for( myChat in myChats){
		var span = $(myChat).find("span[class='chat-msg-item m']")
		if(span && span.html() == params.content){
			return true;
		}
	}
	return false;
}

function sendMsg2Bg(){
	var nameSpan = $("#header").find("span[class='l-txt']");
	var usrName = nameSpan.html();
	chrome.runtime.sendMessage({operate: "chat" , name:usrName}, function(response) {
  		console.log("bg resp " + response);
	});
}

});
