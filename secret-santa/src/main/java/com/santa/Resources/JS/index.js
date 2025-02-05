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