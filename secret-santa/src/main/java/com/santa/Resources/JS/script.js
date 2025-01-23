function displayText(id) {
    var text = document.getElementById(id);
    var revealbutton = document.getElementById(id + "a");
    var hidetext = document.getElementById(id + "b");
    text.style.display = "inline-block";
    revealbutton.style.display = "none";
    hidetext.style.display = "inline-block";
  }
  function hideText(id) {
    var text = document.getElementById(id);
    var revealbutton = document.getElementById(id + "a");
    var hidetext = document.getElementById(id + "b");
    text.style.display = "none";
    revealbutton.style.display = "inline-block";
    hidetext.style.display = "none";
  }