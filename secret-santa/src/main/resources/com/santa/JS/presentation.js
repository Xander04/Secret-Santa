var index = 0;
var gifts = [];
var hidden = true;

 const sleep = (delay) => new Promise((resolve) => setTimeout(resolve, delay))

function updateCard(sender, reciever, description) {
    if (index >= gifts.length) {
        const urlParams = new URLSearchParams(window.location.search);
        console.log(urlParams.toString());
        const url = '/Dashboard?' + urlParams.toString().replace("=","");
        window.location.replace(url)
    }
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
    hidden = false;
    await sleep(500);
    button.innerHTML = gifts[index]["SenderName"];
    button.className = "shown";
    button.style.transform='rotate('+0+'deg)'; 
    button.style.opacity = 0;
    button.style.animationName = "fade-in";
    button.style.opacity = 1;    
    
}

async function Animation() {
    var button = document.getElementById('name_from');
    while (hidden) {
        button.style.transform='rotate('+-5+'deg)'; 
        await sleep(1000);
        button.style.transform='rotate('+5+'deg)';
        await sleep(1000);
    }
    button.style.transform='rotate('+0+'deg)'; 
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