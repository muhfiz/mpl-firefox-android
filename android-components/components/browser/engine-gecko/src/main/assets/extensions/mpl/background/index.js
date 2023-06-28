console.log("background");
chrome.runtime.onConnect.addListener((port) => {
    console.log(`onConnect: port.name = ${port.name}`);
});
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    sendResponse(sender);
});
export {};
