export var MessageTypeBGtoUI;
(function (MessageTypeBGtoUI) {
    MessageTypeBGtoUI["LOGIN"] = "bg-ui-login";
})(MessageTypeBGtoUI || (MessageTypeBGtoUI = {}));
export var MessageTypeUItoBG;
(function (MessageTypeUItoBG) {
    MessageTypeUItoBG["VERIFY_USER"] = "ui-bg-verify-user";
})(MessageTypeUItoBG || (MessageTypeUItoBG = {}));
class MessageHandler {
    onMessage(message) {
        switch (message.type) {
            case MessageTypeBGtoUI.LOGIN:
                break;
            case MessageTypeUItoBG.VERIFY_USER:
                break;
        }
    }
}
