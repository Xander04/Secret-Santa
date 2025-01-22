function displayText(id) {
    var text = document.getElementById(id);
    var revealbutton = document.getElementById(id + "a");
    var hidetext = document.getElementById(id + "b");
    text.style.display = "block";
    revealbutton.style.display = "none";
    hidetext.style.display = "block";
  }
  function hideText(id) {
    var text = document.getElementById(id);
    var revealbutton = document.getElementById(id + "a");
    var hidetext = document.getElementById(id + "b");
    text.style.display = "none";
    revealbutton.style.display = "block";
    hidetext.style.display = "none";
  }

  function dropFunction() {
    document.getElementById("eventDrop").classList.toggle("show");
    document.getElementById("eventDrop1").classList.toggle("show");
  }

  window.onclick = function(event) {
    if (!event.target.matches('.dropDown')) {
      var dropdowns = document.getElementsByClassName("dropdown-content-el");
      var i;
      for (i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
          openDropdown.classList.remove('show');
        }
      }
    }
  }

  //TODO: Make the events dropdown colour stay changed when events has been clicked
