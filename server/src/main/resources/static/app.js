(function () {
  var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
    mode: "text/x-kotlin",
    lineNumbers: true,
    theme: "default",
    indentUnit: 4
  });

  var iframe = document.getElementById("preview");
  var statusDot = document.getElementById("statusDot");
  var statusText = document.getElementById("statusText");

  function updateStatus(ok, message) {
    statusText.textContent = message;
    if (ok) {
      statusDot.className = "status-dot";
    } else {
      statusDot.className = "status-dot error";
    }
  }

  function debounce(fn, delay) {
    var timer = null;
    return function () {
      var context = this;
      var args = arguments;
      if (timer) clearTimeout(timer);
      timer = setTimeout(function () {
        timer = null;
        fn.apply(context, args);
      }, delay);
    };
  }

  function compile() {
    var start = performance.now();
    var dsl = editor.getValue();

    fetch("/api/compile", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ dsl: dsl })
    })
      .then(function (response) {
        if (!response.ok) {
          throw new Error("HTTP " + response.status + " " + response.statusText);
        }
        return response.json();
      })
      .then(function (data) {
        var elapsed = Math.round(performance.now() - start);
        if (data.status === "ok") {
          iframe.srcdoc = data.html;
          updateStatus(true, "OK " + elapsed + "ms");
        } else {
          iframe.srcdoc = "<h2>Compile Error</h2><pre>" + escapeHtml(data.html || "Unknown error") + "</pre>";
          updateStatus(false, "Error " + elapsed + "ms");
        }
      })
      .catch(function (err) {
        iframe.srcdoc = "<h2>Compile Error</h2><pre>" + escapeHtml(err.message || "Network error") + "</pre>";
        updateStatus(false, "Error");
      });
  }

  function escapeHtml(str) {
    var div = document.createElement("div");
    div.appendChild(document.createTextNode(str));
    return div.innerHTML;
  }

  var debouncedCompile = debounce(compile, 200);
  editor.on("change", debouncedCompile);

  // Initial compile on load
  compile();

  // PDF export — exposed globally for onclick
  window.downloadPdf = function () {
    var btn = document.getElementById("dlBtn");
    btn.disabled = true;
    btn.textContent = "Generating...";
    var dsl = editor.getValue();
    fetch("/api/export", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ dsl: dsl })
    })
      .then(function (res) {
        if (!res.ok) throw new Error("HTTP " + res.status);
        return res.blob();
      })
      .then(function (blob) {
        var url = URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = url;
        a.download = "resume.pdf";
        a.click();
        URL.revokeObjectURL(url);
      })
      .catch(function (err) {
        alert("PDF export failed: " + err.message);
      })
      .finally(function () {
        btn.disabled = false;
        btn.textContent = "PDF";
      });
  };
})();
