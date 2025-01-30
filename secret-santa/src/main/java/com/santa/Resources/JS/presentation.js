var index = 0;
var gifts = [];

function updateCard(sender, reciever, description) {
    document.getElementById("name_to").innerHTML = "To: " +  gifts[index]["RecipientName"];
    document.getElementById("name_from").innerHTML = "Secret Santa";
    document.getElementById("name_from").className = "hidden";
    document.getElementById("description").innerHTML = gifts[index]["GiftDescription"];
}

function revealSender() {
    document.getElementById("name_from").innerHTML = gifts[index]["SenderName"];
    document.getElementById("name_from").className = "shown";
}

function next() {
    index++;
    updateCard();
}

function main(id) {
    console.log("get presentation data");
    var req = new XMLHttpRequest();
    req.open('GET', "/presentation-data?" + id, true);
    req.send(null);
    req.onload = function() {
    gifts = JSON.parse(req.getResponseHeader("giftcontent"));
    console.log(gifts);


    updateCard();
    };
}