/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./build/content_scripts/war.js":
/*!**************************************!*\
  !*** ./build/content_scripts/war.js ***!
  \**************************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   CLASS_NAME: () => (/* binding */ CLASS_NAME),\n/* harmony export */   GLOBAL_SELECTOR: () => (/* binding */ GLOBAL_SELECTOR),\n/* harmony export */   getDamageCalculation: () => (/* binding */ getDamageCalculation),\n/* harmony export */   injectDamageCalculation: () => (/* binding */ injectDamageCalculation)\n/* harmony export */ });\nvar GLOBAL_SELECTOR;\n(function (GLOBAL_SELECTOR) {\n    GLOBAL_SELECTOR[\"DAMAGE_INFO\"] = \"b[data-toggle='modal tooltip']\";\n})(GLOBAL_SELECTOR || (GLOBAL_SELECTOR = {}));\nvar CLASS_NAME;\n(function (CLASS_NAME) {\n    CLASS_NAME[\"DAMAGE_CALCULATION\"] = \"calc-damage\";\n})(CLASS_NAME || (CLASS_NAME = {}));\nfunction getDamageCalculation(damageInfo) {\n    let calculation = damageInfo.getAttribute(\"title\");\n    if (calculation == undefined || calculation.length == 0) {\n        calculation = damageInfo.getAttribute(\"data-original-title\");\n    }\n    return calculation !== null ? calculation : undefined;\n}\nfunction injectDamageCalculation(document) {\n    let damageInfos = document.querySelectorAll(GLOBAL_SELECTOR.DAMAGE_INFO);\n    damageInfos.forEach((damageInfo) => {\n        const damageCalculation = getDamageCalculation(damageInfo);\n        if (damageCalculation) {\n            const span = document.createElement(\"span\");\n            span.innerHTML = \"(\" + damageCalculation + \")\";\n            span.classList.add(\"small\", CLASS_NAME.DAMAGE_CALCULATION);\n            span.style.fontWeight = \"bold\";\n            span.style.display = \"block\";\n            const container = damageInfo.parentElement;\n            let damageDiff = container.querySelector(\"span.small\");\n            if (damageDiff) {\n                container.style.height = \"60px\";\n                container.insertBefore(span, damageDiff);\n            }\n            else {\n                container.appendChild(span);\n            }\n        }\n    });\n}\n\n\n//# sourceURL=webpack://mplbot/./build/content_scripts/war.js?");

/***/ }),

/***/ "./build/content_scripts/war_page.js":
/*!*******************************************!*\
  !*** ./build/content_scripts/war_page.js ***!
  \*******************************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var _war_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./war.js */ \"./build/content_scripts/war.js\");\n\n(0,_war_js__WEBPACK_IMPORTED_MODULE_0__.injectDamageCalculation)(document);\n\n\n//# sourceURL=webpack://mplbot/./build/content_scripts/war_page.js?");

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/define property getters */
/******/ 	(() => {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = (exports, definition) => {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	(() => {
/******/ 		__webpack_require__.o = (obj, prop) => (Object.prototype.hasOwnProperty.call(obj, prop))
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module can't be inlined because the eval devtool is used.
/******/ 	var __webpack_exports__ = __webpack_require__("./build/content_scripts/war_page.js");
/******/ 	
/******/ })()
;