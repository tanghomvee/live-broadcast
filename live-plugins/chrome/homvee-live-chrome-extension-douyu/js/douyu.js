$(function(){
var domain = "localhost";
var  securityStartTime = null;
var authKey = "d423e8dd0bbf44caad5a31ddc15055e0";
var websocket= null;
var oneMinute = 60 * 1000;
var interval = setInterval(function(){
    var sendBtn = $("#js-send-msg").find("div[data-type='send']");
    if (sendBtn && sendBtn.length > 0){
        var nameSpan = $("#header").find("span[class='l-txt']");
        var acctName = nameSpan.html();
        websocket = initWebSocket(acctName , roomUrl);
        clearInterval(interval);
    }
},oneMinute);

var roomInterval = setInterval(function(){
    if (!websocket){
        websocket = initWebSocket(acctName , getRoomUrl());
    }
},oneMinute * 5);

function chat(params){
	if (existAcctSecurityTip()){
		var now = new Date().getTime();
        if(!securityStartTime){
            securityStartTime = now;
        }else if((now - securityStartTime) / 1000 > 24*3600){
            window.location.reload();
		}
        var checkContent = "yz" , toPhoneNum= "10690329153656";
        var checkSmsContentSpan = $(".account-security").find(".account-security-text");
        if(checkSmsContentSpan && checkSmsContentSpan.length == 1){
             checkContent = $(checkSmsContentSpan).text() || checkContent;
            var toPhoneSpan = $(checkSmsContentSpan).next();
            if(toPhoneSpan && toPhoneSpan.length == 1){
                toPhoneNum = $(toPhoneSpan).text() || toPhoneNum;
            }
        }
        sendMsg2Bg({"operate": "SMS_CHECK" , "checkSMS" : {"toPhone" : toPhoneNum , "content" : checkContent}});

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

        if (websocket.readyState == websocket.OPEN) {
            var nameSpan = $("#header").find("span[class='l-txt']");
            var usrName = nameSpan.html();
            params["acctName"] = usrName;
            params["roomUrl"] = roomUrl;
            //调用后台handleTextMessage方法
            websocket.send(params);
        }

	}catch (e) {
        window.location.reload();
    }

}


function existAcctSecurityTip() {
	var securityTip = $(".account-security");
	if(!securityTip || securityTip.length < 1){
		return false;
	}
	return  $(securityTip).is(":visible");
}

function initWebSocket(acctName) {
    var ws = null;
    var  urlParams =  "acctName="+acctName+"&roomUrl="+getRoomUrl()+"&authKey="+authKey;
    if ("WebSocket" in  window){
        ws=new WebSocket("ws://" + domain +"/msg/socketServer?" + urlParams);
    }else {
        ws=new SockJS("ws://" + domain +"/msg/socketClient?" + urlParams)
    }

    ws.onopen=function(event){
        console.info("连接已建立..\n");
        console.info("发送测试数据..等待验证\n");
        ws.send("666");
    };
    ws.onmessage=function(event){
        var msg = event.data;
        if (!msg){
            return;
        }
        if (msg["flag"] == "success"){
            var data = msg["data"];
            if (data["operate"] == "SMS_CHECK"){
                var tmp =window.setTimeout(function(){
                    window.clearTimeout(tmp);
                    if (data.content && existAcctSecurityTip()){
                        var chkBtn = $(".account-security").find(".account-security-submit.account-security-next.button.js-account-security-button");
                        chkBtn.trigger("click");
                    }
                    },60000);
            }else if(data["operate"] == "CHAT"){
                chat(data);
            }else{

            }
        }
        console.info(msg+"\n");
    };

    ws.onclose=function(){
        console.info("连接已关闭..\n");
    };

    ws.onerror = function(){
        console.info("发生了错误..\n");   
    };

    window.close=function(){
        ws.onclose();
    }
}

function getRoomUrl() {
    return window.location.protocol + "//" + window.location.hostname + window.location.pathname;
}

});
