export var GLOBAL_SELECTOR;
(function (GLOBAL_SELECTOR) {
    GLOBAL_SELECTOR["DAMAGE_INFO"] = "b[data-toggle='modal tooltip']";
})(GLOBAL_SELECTOR || (GLOBAL_SELECTOR = {}));
export var CLASS_NAME;
(function (CLASS_NAME) {
    CLASS_NAME["DAMAGE_CALCULATION"] = "calc-damage";
})(CLASS_NAME || (CLASS_NAME = {}));
export function getDamageCalculation(damageInfo) {
    let calculation = damageInfo.getAttribute("title");
    if (calculation == undefined || calculation.length == 0) {
        calculation = damageInfo.getAttribute("data-original-title");
    }
    return calculation !== null ? calculation : undefined;
}
export function injectDamageCalculation(document) {
    let damageInfos = document.querySelectorAll(GLOBAL_SELECTOR.DAMAGE_INFO);
    damageInfos.forEach((damageInfo) => {
        const damageCalculation = getDamageCalculation(damageInfo);
        if (damageCalculation) {
            const span = document.createElement("span");
            span.innerHTML = "(" + damageCalculation + ")";
            span.classList.add("small", CLASS_NAME.DAMAGE_CALCULATION);
            span.style.fontWeight = "bold";
            span.style.display = "block";
            const container = damageInfo.parentElement;
            let damageDiff = container.querySelector("span.small");
            if (damageDiff) {
                container.style.height = "60px";
                container.insertBefore(span, damageDiff);
            }
            else {
                container.appendChild(span);
            }
        }
    });
}
