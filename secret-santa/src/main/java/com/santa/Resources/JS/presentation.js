var index = 0;
var gifts = [];
var hidden = true;

 const sleep = (delay) => new Promise((resolve) => setTimeout(resolve, delay))

function updateCard(sender, reciever, description) {
    document.getElementById("name_to").innerHTML = "To: " +  gifts[index]["RecipientName"];
    document.getElementById("name_from").innerHTML = "Secret Santa";
    document.getElementById("name_from").className = "hidden";
    document.getElementById("description").innerHTML = gifts[index]["GiftDescription"];

    hidden = true;
    Animation();
}

async function revealSender() {
    var button = document.getElementById('name_from');
    button.style.animationName = "fade-out";
    button.style.opacity = 0;
    await sleep(500);
    button.innerHTML = gifts[index]["SenderName"];
    button.className = "shown";
    button.style.transform='rotate('+0+'deg)'; 
    button.style.opacity = 0;
    button.style.animationName = "fade-in";
    button.style.opacity = 1;
    hidden = false;
}

async function Animation() {
    var button = document.getElementById('name_from');
    while (hidden) {
        button.style.transform='rotate('+-5+'deg)'; 
        await sleep(1000);
        button.style.transform='rotate('+5+'deg)';
        await sleep(1000);
    }
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