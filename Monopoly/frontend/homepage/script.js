chance_cards=[4,3,2];
community_cards=[3,2,2];
chance_index = 0;
community_index = 0;

//globale Variablen für countUpTimer()
var timerVariable = setInterval(function() {
    if(!isPaused) {
        countUpTimer();
    }
}, 1000);
var totalSeconds = 0;
var isPaused = false;

function pad(num, size) {
    num = num.toString();
    while (num.length < size) num = "0" + num;
    return num;
}

function countUpTimer() {
    ++totalSeconds;
    var hour = Math.floor(totalSeconds / 3600);
    var minute = Math.floor((totalSeconds - hour * 3600) / 60);
    var seconds = totalSeconds - (hour * 3600 + minute * 60);
    document.getElementById("count_up_timer").innerHTML = pad(hour, 2) + ":" + pad(minute, 2) + ":" + pad(seconds, 2);
}

function pause_timer() {
    isPaused = !isPaused;
    if(isPaused)
        document.getElementById("break_count_up_timer").innerHTML = "Weiter";
    else
        document.getElementById("break_count_up_timer").innerHTML = "Pause";
}


//Geldzähler geht bis 1500
const counters = document.querySelectorAll('.counter');

counters.forEach(counter => {
    counter.innerText = '0';
    counter.setAttribute('data-target', 1500);
    const updateCounter = () => {
        const target = +counter.getAttribute('data-target');
        const c = +counter.innerText;

        const increment = target / 200;

        if(c < target) {
            counter.innerText = `${Math.ceil(c + increment)}`;
            setTimeout(updateCounter, 1);
        }
        else{
            counter.innerText = target;
        }
    };
    updateCounter();
});

//dynamisches Update des Geldcounters
function updateCounter_2(new_counter_value, is_opponent) {
    // is_oppenent entscheidet, ob Du den Wert für den Gegner oder den Spieler aktualisierst
    var new_counters;
    if(is_opponent) {
        new_counters = document.querySelectorAll('.counter-opponent');
    } else {
        new_counters = document.querySelectorAll('.counter');
    }
    new_counters.forEach(counter => {
        counter.innerText = '0';
        counter.setAttribute('data-target', new_counter_value);
        
        const updateCounter = () => {
            const target = +counter.getAttribute('data-target');
            const c = +counter.innerText;
            
            const increment = target / 200;
            
            if(c < target) {
                counter.innerText = `${Math.ceil(c + increment)}`;
                setTimeout(updateCounter, 1);
            }
            else{
                counter.innerText = target;
            }
        };
        updateCounter();
    });
}

//Integer-Liste mit Indices der gemischten Ereigniskarten
function set_chance_order(chance_cards_merged) {
    chance_cards = chance_cards_merged;
}

//Integer-Liste mit Indices der gemischten Gemeinschaftskarten
function set_community_order(community_cards_merged) {
    community_cards = community_cards_merged;
}

//ziehe verschiedene Ereigniskarten als Bild
function show_chance_card() {
    current_chance_card_id = chance_cards[chance_index];
    //get image
    source = "Ereigniskarte_" + current_chance_card_id + ".png";
    //change index
    chance_index = (chance_index + 1) % chance_cards.length;
    //show image
    window.location.href = './' + source;
    //prison card
    if(current_chance_card_id == prison_index) {
        free_prison_chance_dragged();
    }
}

//ziehe verschiedene Gemeinschaftskarten als Bild
function show_community_card() {
    current_community_card_id = community_cards[community_index];
    //get image
    source = "Gemeinschaftskarte_" + current_community_card_id + ".png";
    //change index
    community_index = (community_index + 1) % community_cards.length;
    //show image
    window.location.href = './' + source;
    //prison card
    if(current_community_card_id == prison_index) {
        free_prison_community_dragged();
    }

}

//Umgang mit Du kommst aus dem Gefängnis frei
function free_prison_chance_dragged() {
    document.getElementById("prison_chance_click").disabled = false;
    document.getElementById("prison_chance_click").style.opacity = 1;
}

//Umgang mit Du kommst aus dem Gefängnis frei
function free_prison_community_dragged() {
    document.getElementById("prison_community_click").disabled = false;
    document.getElementById("prison_community_click").style.opacity = 1;
}

function free_prison_community_button_clicked() {
    if(/*free_prison_card_played()*/ true) {
        document.getElementById("prison_community_click").disabled = true;
        document.getElementById("prison_community_click").style.opacity = 0.5;
    }
    //backend: free_prison_card_played();
}

function free_prison_chance_button_clicked() {
    if(/*free_prison_card_played()*/ true) {
        document.getElementById("prison_chance_click").disabled = true;
        document.getElementById("prison_chance_click").style.opacity = 0.5;
    }
    //backend: free_prison_card_played();
}