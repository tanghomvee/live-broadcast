$(function(){

var  timeout = 10000;
var  securityStartTime = null;
var interval = setInterval(function(){
   var sendBtn = $("#js-send-msg").find("div[data-type='send']");
   if (sendBtn && sendBtn.length > 0){
       sendMsg2Bg({"operate": "chat"});
       clearInterval(interval);
   }
},timeout);


var intervalLive = setInterval(function(){
    var timetit = $("#anchor-info").find("a[data-anchor-info='timetit']");
    if (timetit && timetit.length > 0){
        sendMsg2Bg({"operate": "notify","live" : 0});
    }
},3600 * 1000);

chrome.runtime.onMessage.addListener(function (params, sender, sendResponse) {

	if (sendResponse){
        sendResponse({"msg" : "Content accept ok"});
	}

	if (params && params.hasOwnProperty("operate")) {
		if(params.operate=="go" && params.content){
			window.location.href = params.content;
			return;
		}else if(params.operate=="chat"){
			chat(params);
            sendResponse({state:'发送成功！'});
            var tmp =window.setTimeout(function(){
                sendMsg2Bg({"operate": "chat"});
                window.clearTimeout(tmp);
            },timeout);

		}else if(params.operate=="wait"){
			var tmp =window.setTimeout(function(){
					sendMsg2Bg({"operate": "chat"});
					window.clearTimeout(tmp);
				},(params.content || timeout) / 1);
		}else if(params.operate=="check"){
            var tmp =window.setTimeout(function(){
                window.clearTimeout(tmp);
                if (params.content && existAcctSecurityTip()){
                    //TODO
				}
            },timeout);
        }
   }else{
	  console.error("参数错误" + params);
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
        sendMsg2Bg({"operate": "check"});
        return;
	}
    var sendBtn = $("#js-send-msg").find("div[data-type='send']");
	var content = $("#js-send-msg").find("textarea");
	var chat =  params.content || "haha[emot:dy101] ^_^ ";
	
	content.val(chat);
	sendBtn.trigger("click");
}


function sendMsg2Bg(params){
	try{

        var nameSpan = $("#header").find("span[class='l-txt']");
        var usrName = nameSpan.html();
        params["acctName"] = usrName;
        chrome.runtime.sendMessage(params, function(response) {
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
