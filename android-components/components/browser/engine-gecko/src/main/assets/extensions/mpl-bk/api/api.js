class MessageType{
    /**
     * 
     * @param {String} type 
     */
    constructor(type){
        this.type = type
    }

    /**
     * 
     * @param {*} data 
     * @returns {Message}
     */
    wrap(data){
        return new Message(this.type, data)
    }

    toString(){
        return this.type
    }
}

class ContentMessageType extends MessageType{
    static BG_VERIFY_LOGIN = new ContentMessageType("bg-verify-login")
    static BG_ON_LOGIN_PAGE = new ContentMessageType("bg-on-login-page")
    static UI_LOGIN = new ContentMessageType("ui-login")
}

class Message{

    /**
     * 
     * @param {string} type 
     * @param {*} data 
     */
    constructor(type, data){
        this.type = type
        this.data = data
    }
}

