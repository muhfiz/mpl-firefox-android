+(function () {
    function getOriginalTitle(element) {
        let originalTitle = "";
        let titleAttribute = element.getAttribute("title");
        let dataOriginalTitleAttribute = element.getAttribute(
            "data-original-title"
        );
        if (titleAttribute) {
            originalTitle = titleAttribute;
        }
        if (dataOriginalTitleAttribute) {
            originalTitle = dataOriginalTitleAttribute;
        }
        return originalTitle;
    }

    let damageInfos = document.querySelectorAll(
        "b[data-toggle='modal tooltip']"
    );
    damageInfos.forEach((damageInfo) => {
        const originalTitle = getOriginalTitle(damageInfo);

        if (originalTitle) {
            let damageDiff =
                damageInfo.parentElement.querySelector("span.small");
            const br = document.createElement("br");
            const damageCalc = document.createElement("span");
            damageCalc.innerHTML = "(" + originalTitle + ")";
            damageCalc.classList.add("small");
            damageCalc.style.fontWeight = "bold";
            if (damageDiff) {
                damageInfo.parentElement.style.height = "60px";
                damageInfo.parentElement.insertBefore(damageCalc, damageDiff);
                damageInfo.parentElement.insertBefore(br, damageDiff);
            } else {
                damageInfo.parentElement.appendChild(br);
                damageInfo.parentElement.appendChild(damageCalc);
            }
        }
    });
})();
