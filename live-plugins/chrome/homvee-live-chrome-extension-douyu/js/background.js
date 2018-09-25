/**
*On the receiving end, you need to set up an runtime.
*onMessage event listener to handle the message. 
*This looks the same from a content script or extension page.
*Note: 
*If multiple pages are listening for onMessage events, 
*only the first to call sendResponse() for a particular event will succeed in sending the response.
*All other responses to that event will be ignored.
*/
// var url = "http://47.52.229.18:9527/content/chat";
var domain = "http://localhost";
var url = "http://localhost/content/chat";
var authKey = "d423e8dd0bbf44caad5a31ddc15055e0";
chrome.runtime.onMessage.addListener(
  function(params, sender, sendResponse) {

    console.log(sender.tab ? "from a content script:" + sender.tab.url : "from the extension");

    if (sendResponse){
        sendResponse({"msg" : "background accept ok"});
    }

    if (params.operate == "chat" || params.operate=="check"){
        params["url"] = sender.tab.url;
        params["authKey"] = authKey;
        switch (params.operate) {
            case "chat":
                ajaxFun(domain + "/content/chat" , params , sender);
                return;
           case "check":
               ajaxFun(domain + "/sms/check" , params , sender);
                return;
        }

    }else{
        console.log("参数错误：" + params);
        sendMsg2Content(null,sender.tab.id);
    }
});

/**
*Sending a request from the extension to a content script looks very similar, except that you need to specify which tab to send it to. 
*This example demonstrates sending a message to the content script in the selected tab.
*/
function sendMsg2Content(data,tabId){
 chrome.tabs.sendMessage(tabId, data, function(response) {
    console.log("content resp:"+ response);
  });
}

function ajaxFun(url , params , sender) {
    $.ajax({
        url:url,
        data:params || {},
        timeout: 60000,
        dataType:"json",
        success:function(data){
            console.log(data);
            if (data["flag"] == "success") {
                data = data["data"];
            }

            sendMsg2Content(data,sender.tab.id);
        },
        error:function () {
            sendMsg2Content({"operate":"wait","content" : 60000},sender.tab.id);
        }
    });
}
