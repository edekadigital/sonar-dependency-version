window.registerExtension("dependencyversion/report", function(options) {
    var isDisplayed = true;

    if (!document.querySelector("style#dependency-version-report")) {
        var style = document.createElement("style");
        style.id = "dependency-version-report";
        style.appendChild(document.createTextNode(""));
        document.head.appendChild(style);
        style.sheet.insertRule(".dependency-version-report-content {flex: 1 1 auto;}", 0);
        style.sheet.insertRule(".dependency-version-report-container {display: flex; flex-direction: column;}", 0);
    }

    window.SonarRequest.getJSON("/api/measures/component", {
        componentKey : options.component.key,
        metricKeys : "report"
    }).then(function(response) {
        if (isDisplayed) {
            var htmlString = response.component.measures.filter(measure => measure.metric === "report")[0].value;
            var currentEl = options.el;
            while (currentEl.id !== "container") {
                currentEl.classList.add("dependency-version-report-content");
                currentEl.classList.add("dependency-version-report-container");
                currentEl = currentEl.parentElement;
            }
            currentEl.classList.add("dependency-version-report-container");

            var reportFrame = document.createElement("iframe");
            reportFrame.sandbox.value = "allow-scripts allow-same-origin";
            reportFrame.style.border = "none";
            reportFrame.style.flex= "1 1 auto";
            reportFrame.srcdoc = htmlString;
            options.el.append(reportFrame);
        }
    });

    return function() {
        options.el.textContent = "";
        var currentEl = options.el;
        while (currentEl.id !== "container") {
            currentEl.classList.remove("dependency-version-report-content");
            currentEl.classList.remove("dependency-version-report-container");
            currentEl = currentEl.parentElement;
        }
        currentEl.classList.remove("dependency-version-report-container");
    };
});