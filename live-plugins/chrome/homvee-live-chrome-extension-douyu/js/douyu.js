$(function(){

var  timeout = 10000;
var  securityStartTime = null;
var chatList= [];
var interval = setInterval(function(){
   var sendBtn = $("#js-send-msg").find("div[data-type='send']");
   if (sendBtn && sendBtn.length > 0){
       sendMsg2Bg();
   }
},timeout);

chrome.runtime.onMessage.addListener(function (params, sender, sendResponse) {

	if (sendResponse){
        sendResponse({"msg" : "Content accept ok"});
	}

	if (params && params.hasOwnProperty("operate")) {
		if(params.operate=="go" && params.content){
			window.location.href = params.content;
			return;
		}else if(params.operate=="chat" && params.content){
			clearInterval(interval);
			var len = getChats();
			chat(params);
            sendResponse({state:'发送成功！'});
            var tmp =window.setTimeout(function(){
                sendMsg2Bg();
                window.clearTimeout(tmp);
            },timeout);

			// if(getChats() > len){
			// 	sendResponse({state:'发送成功！'});
			// 	var tmp =window.setTimeout(function(){
			// 		sendMsg2Bg();
			// 		window.clearTimeout(tmp);
			// 	},timeout);
			// }else{
			// 	var tmp =window.setTimeout(function(){
             //        window.clearTimeout(tmp);
             //        if (getChats() > 0 && getChats() <= len){
             //            chat(params);
             //        }
			// 		sendMsg2Bg();
			// 	},timeout);
			// }
		}else if(params.operate=="wait" && params.content){
			var tmp =window.setTimeout(function(){
					sendMsg2Bg();
					window.clearTimeout(tmp);
				},params.content / 1);
		}
   }else{
      window.location.reload();
	}
});

function chat(params){
	if (existAcctSecurityTip()){
		var now = new Date().getTime();
        if(!securityStartTime){
            securityStartTime = now;
        }else if((now - securityStartTime) / 1000 > 24*3600){
            window.location.reload();
		}

        return;
	}
    var sendBtn = $("#js-send-msg").find("div[data-type='send']");
	var content = $("#js-send-msg").find("textarea");
	var chat =  params.content || "haha[emot:dy101] ^_^ ";
	
	content.val(chat);
	sendBtn.trigger("click");
}

function getChats(){
	var myChats = $("#js-chat-cont").find(".my-cont");
	if(!myChats || myChats.length < 1){
		return 0;
	}
	return myChats.length;
}

function sendMsg2Bg(){
	try{

        var nameSpan = $("#header").find("span[class='l-txt']");
        var usrName = nameSpan.html();
        chrome.runtime.sendMessage({"operate": "chat" , "acctName":usrName}, function(response) {
            console.log("bg resp " + response);
        });

	}catch (e) {
        window.location.reload();
    }

}

function getRandom(num) {
	return Math.floor(Math.random()*num);
}

function existAcctSecurityTip() {
	var securityTip = $(".account-security");
	if(!securityTip || securityTip.length < 1){
		return false;
	}
//$(securityTip).is(":hidden") ||
	return  $(securityTip).is(":visible");
}

});
