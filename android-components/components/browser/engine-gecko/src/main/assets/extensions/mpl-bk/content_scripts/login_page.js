// const captcha = document.querySelector(".g-recaptcha")
// if(captcha) { captcha.remove() }
console.log("login_page")

const loginForm = document.querySelector('form[action="https://myprofitland.com/authy.php"]')
if(loginForm){
    browser.runtime.sendMessage(
        ContentMessageType.BG_ON_LOGIN_PAGE.wrap()
    )
}

browser.runtime.onMessage.addListener(message => {
    console.log("onMessage = " + message)
    switch(message.type){
        case ContentMessageType.UI_LOGIN.toString():
            const email = message.data.email
            const password = message.data.password
            const inputEmail = loginForm.querySelector('input[type="email"]')
            const inputPassword = loginForm.querySelector('input[type="password"]')
            inputEmail.value = email
            inputPassword.value = password
            loginForm.submit()
            break;
    }
})