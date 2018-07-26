/**
*On the receiving end, you need to set up an runtime.
*onMessage event listener to handle the message. 
*This looks the same from a content script or extension page.
*Note: 
*If multiple pages are listening for onMessage events, 
*only the first to call sendResponse() for a particular event will succeed in sending the response.
*All other responses to that event will be ignored.
*/
var url = "http://localhost/get";
chrome.runtime.onMessage.addListener(
  function(params, sender, sendResponse) {
    console.log(sender.tab ? "from a content script:" + sender.tab.url : "from the extension");
    if (params.operate == "chat"){
    	$.ajax({
		    url:url,
		    data:params || {},
		    dataType:"json",
		    success:function(data){
		        console.log(data);
		        sendMsg2Content(data,sender.tab.id);
		       sendResponse({state: "ok"});
		    }
		}); 	
    }
});

/**
*Sending a request from the extension to a content script looks very similar, except that you need to specify which tab to send it to. 
*This example demonstrates sending a message to the content script in the selected tab.
*/
function sendMsg2Content(data,tabId){
 chrome.tabs.sendMessage(tabId, data, function(response) {
    console.log(response);
  });
}