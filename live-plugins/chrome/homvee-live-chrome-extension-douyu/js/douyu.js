$(function(){
    console.info("初始化开始:"+ window.location.href +"-"+ new Date());
var domain = "localhost";
var  securityStartTime = null;
var authKey = "d423e8dd0bbf44caad5a31ddc15055e0";
var websocket= null;
//检查此房间是否在当前账号定时器
var roomCheckInterval = null;
var oneMinute = 60 * 1000;
//日期格式化
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth() + 1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        "S" : this.getMilliseconds()
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

var interval = setInterval(function(){
    var acctName = getAcctName();
    if (acctName){
        clearInterval(interval);
        websocket = initWebSocket(acctName);
    }else {
        console.info("未登录" + window.location.href + "-" + new Date());
    }

},oneMinute);

//心跳检测,每20s心跳一次
var heartCheck = {
    timeout: oneMinute / 3,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function(){
        if (this.timeoutObj){
            clearTimeout(this.timeoutObj);
        }

        if (this.serverTimeoutObj){
            clearTimeout(this.serverTimeoutObj);
        }
        return this;
    },
    start: function(){
        var self = this;
        this.timeoutObj = setTimeout(function(){
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            var acctName = getAcctName();
            if (acctName){
                console.info("向服务器发送心跳:" + new Date().format("yyyy-MM-dd hh:mm:ss"));
                sendMsg2Bg({"operate": "HEART_CHECK","acctName":acctName});
                //如果超过一定时间还没重置，说明后端主动断开了
                self.serverTimeoutObj = setTimeout(function(){
                    //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                    websocket.close();
                    refresh(getRoomUrl());
                }, oneMinute);
            }
        }, self.timeout)
    }
};

console.info("初始化完成:"+ window.location.href +"-"+ new Date());
function chat(params){
	if (existAcctSecurityTip()){
		var now = new Date().getTime();
        if(!securityStartTime){
            securityStartTime = now;
        }else if((now - securityStartTime) / 1000 > 24*3600){
            refresh(getRoomUrl());
            return;
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
	if (getAcctName()){
        var sendBtn = $("#js-send-msg").find("div[data-type='send']");
        var content = $("#js-send-msg").find("textarea");
        var chat =  params.content || "haha[emot:dy101] ^_^ ";
        content.val(chat);
        if (sendBtn.length != 1 || sendBtn.hasClass("b-btn-gray")){
            console.info("发送按钮无效");
            websocket.close();
            refresh(getRoomUrl());
        } else{
            sendBtn.trigger("click");
            console.info("发言成功:"+getAcctName()+"-" +chat );
        }
    }else{
	    console.info("发言失败:" + getRoomUrl())
    }
}


function sendMsg2Bg(params){
	try{

        if (websocket && websocket.readyState == websocket.OPEN) {
            params["acctName"] = getAcctName();
            //调用后台handleTextMessage方法
            websocket.send(JSON.stringify(params));
        }

	}catch (e) {
        websocket.close();
        refresh(getRoomUrl());
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
    try{
        var  urlParams =  "acctName="+acctName+"&roomUrl="+getRoomUrl()+"&authKey="+authKey;
        if ("WebSocket" in  window){
            ws=new WebSocket("ws://" + domain +"/msg/socketServer?" + urlParams);
        }else {
            ws=new SockJS("ws://" + domain +"/msg/socketClient?" + urlParams);
        }

        ws.onopen=function(event){
            console.info("连接已建立");
            console.info(event);
            console.info("发送测试数据..等待验证");
            ws.send(JSON.stringify({"operate": "ROOM_CHECK","acctName":acctName , "checkRoom" : {"roomUrl" : getRoomUrl()}}));

            roomCheckInterval= setInterval(function(){
                var acctName = getAcctName();
                if (acctName){
                    sendMsg2Bg({"operate": "ROOM_CHECK","acctName":acctName , "checkRoom" : {"roomUrl" : getRoomUrl()}});
                }

            },oneMinute * 30);

            //如果获取到消息，心跳检测重置
            heartCheck.reset().start();
        };
        ws.onmessage=function(event){
            //如果获取到消息，心跳检测重置
            heartCheck.reset().start();
            var msg = event.data;
            if (!msg){
                return;
            }
            console.info("接收到服务器数据:" + msg);
            msg = JSON.parse(msg);
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
                }else if(data["operate"] == "ROOM_CHECK"){
                    if (data["content"] != getRoomUrl()){
                        console.info("切换房间到:" + data["content"]);
                        websocket.close();
                        refresh(data["content"]);
                    }
                }else if(data["operate"] == "HEART_CHECK"){
                    console.info("收到服务器的心跳:" + new Date().format("yyyy-MM-dd hh:mm:ss"));
                }
            }
        };

        ws.onclose=function(event){
            console.info("连接已关闭:");
            console.info(event);

            if (roomCheckInterval){
                clearInterval(roomCheckInterval);
                roomCheckInterval=null;
            }

            //心跳检测重置
            heartCheck.reset();

            var eventCode= event.code / 1;
            if (eventCode == 1000){
                //window.close();
            } else if (eventCode > 1000){
                console.info("发生错误reload:" + window.location.href);
                refresh(getRoomUrl());
            }
        };

        ws.onerror = function(event){
            console.info("发生了错误:");
            console.info(event);
        };
        window.close=function(){
            ws.onclose();
        }

    }catch (e) {
        ws = null;
    }

    return ws;
}

function getRoomUrl() {
    return window.location.protocol + "//" + window.location.hostname + window.location.pathname;
}
function getAcctName() {
    var nameSpan = $("#header").find("span[class='l-txt']");
    var acctName = nameSpan.is(":visible") ? nameSpan.html() : "";
    if (!acctName){
        console.info("未获取到连接的登陆用户:" + getRoomUrl());
    }
    return acctName;
}

function refresh(url) {
    chrome.runtime.sendMessage({"url":url}, function(response) {
        console.log("bg resp " + response);
    });
}

});
