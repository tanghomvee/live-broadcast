/**
*On the receiving end, you need to set up an runtime.
*onMessage event listener to handle the message.
*This looks the same from a content script or extension page.
*Note:
*If multiple pages are listening for onMessage events,
*only the first to call sendResponse() for a particular event will succeed in sending the response.
*All other responses to that event will be ignored.
*/
chrome.runtime.onMessage.addListener(
  function(params, sender, sendResponse) {

    console.log(sender.tab ? "from a content script:" + sender.tab.url : "from the extension");

    if (sendResponse){
        sendResponse({"msg" : "background accept ok"});
    }

      chrome.tabs.update(sender.tab.id, {url: params.url,active:true});

});

