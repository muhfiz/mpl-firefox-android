class NativeMessageType extends MessageType {
    static NV_VERIFY_LOGIN = "nv-verify-login";
    static NV_ON_LOGIN_PAGE = "nv-on-login-page";
    static BG_LOGIN = "bg-login";
}

const MPL_LOGIN_URL = "https://myprofitland.com/";
const MPL_DOMAIN = "myprofitland.com";

let port = browser.runtime.connectNative("mplbot");
port.onMessage.addListener((message) => {
    console.log("port: onMessage " + message);
    switch (message.type) {
        case NativeMessageType.BG_LOGIN.toString():
            Messenger.sendMessageToActiveTab(
                ContentMessageType.UI_LOGIN.wrap(message.data)
            );
            break;
    }
});

function userLogin() {
    port.postMessage({
        type: NativeMessageType.NV_VERIFY_LOGIN,
    });
}

function onLoginPage() {
    port.postMessage({
        type: NativeMessageType.NV_ON_LOGIN_PAGE,
    });
}

class Messenger {
    static handle(message) {
        console.log("handle " + JSON.stringify(message));
        switch (message.type) {
            case ContentMessageType.BG_VERIFY_LOGIN.toString():
                userLogin();
                break;
            case ContentMessageType.BG_ON_LOGIN_PAGE.toString():
                onLoginPage();
                break;
        }
    }

    static async sendMessageToActiveTab(message) {
        console.log("sendMessageToActiveTab = " + JSON.stringify(message))
        const activeTabs = await browser.tabs.query({ active: true });       
        browser.tabs.sendMessage(activeTabs[0].id, message)
    }
}

browser.runtime.onMessage.addListener((message) => {
    Messenger.handle(message);
});
