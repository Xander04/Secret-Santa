function submitEventId() {
    var id = ""
        id += document.getElementById("EventId1").value;
        id += document.getElementById("EventId2").value;
        id += document.getElementById("EventId3").value;
        id += document.getElementById("EventId4").value;
        console.log(id);

        const url = '/Participant?' + id;
        window.location.replace(url);
}

var elts = document.getElementsByName('EventId');
console.log(elts);
Array.from(elts).forEach(function(elt){
    console.log("add listener for " + elt);
  elt.addEventListener("oninput", function(event) {
    console.log("keypress" + event.keyCode);
    // Number 13 is the "Enter" key on the keyboard
    //if (event.keyCode === 13 || elt.value.length == 1) {
      // Focus on the next sibling
      elt.nextElementSibling.focus();
    //}
  });
})