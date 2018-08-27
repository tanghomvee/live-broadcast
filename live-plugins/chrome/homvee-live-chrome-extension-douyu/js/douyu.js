$(function(){

var  timeout = 10000;
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
	var sendBtn = $("#js-send-msg").find("div[data-type='send']");
	var content = $("#js-send-msg").find("textarea");
	var chat =  params.content || "haha[emot:dy101] ^_^ ";


	/*var chats = $("#js-chat-cont").find(".jschartli.hy-chat").find(".chat-msg-item.text-cont");
	if(chats && chats.length > 0){
        //var index = getRandom(chats.length);
        var index = chats.length - 1;
        chat = $(chats[index]).text();
		if (chat) {
			if (chat.indexOf("美") > -1){
				chat = chat.replace("美" , "靓");
			}
			if (chat.indexOf("哈") > -1){
				chat = chat.replace("哈" , "拉");
			}
			if (chat.indexOf("什么") > -1){
				chat = chat.replace("什么" , "撒子");
			}
			if (chat.indexOf("姐姐") > -1){
				chat = chat.replace("姐姐" , "美女");
			}
			if (chat.indexOf("爱你") > -1){
				chat = chat.replace("爱你" , "love u");
			}
			if (chatList.indexOf(chat) == -1){
                chatList.push(chat);
            }
            chat =chatList.length > 30 ? chatList.shift() : "" ;
            chat = chat  + "[emot:dy101]" + "[emot:dy101]" ;

		}
	}else {
        chat = "[emot:dy101]";
	}*/

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

});
