//-------Tradingpage------------


const playerID = 0;
const userID = 0;

showing_selector_wuensche = false;
showing_selector_angebote = false;

value_wuensche = 0;
value_angebote = 0;

var streets_wuensche = [];
var streets_angebote = [];
var homepage = true;

//Notwendig für Drag&Drop
function allowDrop(ev) {
    ev.preventDefault();
}

//Notwendig für Drag&Drop
function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
    ev.dataTransfer.setData("parent", document.getElementById(ev.target.id).parentElement.id);
}

//von gegnerischen Karten zu Wünsche ziehen
function drop_to_wuensche_field(ev) {
    ev.preventDefault();
    var street_id = ev.dataTransfer.getData("text");
    var street_field_id = (ev.dataTransfer.getData("parent"));
    if(ev.target.childElementCount < 4  && ev.target.id == "wuensche_drop" && access_streets_wuensche(street_field_id)) {
        ev.target.appendChild(document.getElementById(street_id));
        append_streets_wuensche(street_id);
    }
}

//von eigenen Karten zu Angeboten ziehen
function drop_to_angebote_field(ev) {
    ev.preventDefault();
    var street_id = ev.dataTransfer.getData("text");
    var street_field_id = (ev.dataTransfer.getData("parent"));
    if(ev.target.childElementCount < 4  && ev.target.id == "angebote_drop" && access_streets_angebote(street_field_id)) {
        ev.target.appendChild(document.getElementById(street_id));
        append_streets_angebote(street_id);
    }
}

//Aus Angebot-/Wunschfeld zurück zu entsprechendem Spielerfeld draggen & droppen
function drop_to_street_field(ev) {
    ev.preventDefault();
    var street_id = ev.dataTransfer.getData("text");
    var container_field_id = (ev.dataTransfer.getData("parent"));
    var street_field_id = ev.target.id;
    if(ev.target.childElementCount == 0  && ev.target.id.startsWith("street_field") && access_streets_field(container_field_id, street_field_id)) {
        ev.target.appendChild(document.getElementById(street_id));
    }
    id = parseInt(street_field_id.split("_")[2]);
    if(id <= 28) {
        remove_streets_angebote(street_id);
    }
    if(id > 28 && id <= 56) {
        remove_streets_wuensche(street_id);
    }
}

//Zeigt Geldschieber für Wünsche
function show_range_shifter_wuensche() {
    if(showing_selector_wuensche) {
        document.getElementById("shifter_wuensche").style.display = "none";
        reset_value_wuensche();
    }
    else {
        document.getElementById("shifter_wuensche").style.display = "inline";
    }
    showing_selector_wuensche = !showing_selector_wuensche;
}

//Zeigt Geldschieber für Angebote
function show_range_shifter_angebote() {
    if(showing_selector_angebote) {
        document.getElementById("shifter_angebote").style.display = "none";
        reset_value_angebote();
    }
    else {
        document.getElementById("shifter_angebote").style.display = "inline";
    }
    showing_selector_angebote = !showing_selector_angebote;
}

//Synchronisiert Geldwert, wenn Schieber eingestellt wird bei Wünschen
function update_value_wuensche() {
    value_wuensche = document.getElementById("shifter_input_wuensche").value;
    document.getElementById("quantity_wuensche").value = value_wuensche;
}

//Synchronisiert Geldwert, wenn Schieber eingestellt wird bei Angeboten
function update_value_angebote() {
    value_angebote = document.getElementById("shifter_input_angebote").value;
    document.getElementById("quantity_angebote").value = value_angebote;
}

//Synchronisiert Schieber, wenn Geldwert eingestellt wird bei Wünschen
function update_quantity_wuensche() {
    value_wuensche = document.getElementById("quantity_wuensche").value;
    document.getElementById("shifter_input_wuensche").value = value_wuensche;
}

//Synchronisiert Schieber, wenn Geldwert eingestellt wird bei Angeboten
function update_quantity_angebote() {
    value_angebote = document.getElementById("quantity_angebote").value;
    document.getElementById("shifter_input_angebote").value = value_angebote;
}

//Setzt Wünsche-Regler und -Geldwert zurück auf 0
function reset_value_wuensche() {
    value_wuensche = 0;
    document.getElementById("shifter_input_wuensche").value = value_wuensche;
    document.getElementById("quantity_wuensche").value = value_wuensche;
}

//Setzt Angebote-Regler und -Geldwert zurück auf 0
function reset_value_angebote() {
    value_angebote = 0;
    document.getElementById("shifter_input_angebote").value = value_angebote;
    document.getElementById("quantity_angebote").value = value_angebote;
}

//Nur Karten des Gegners dürfen in Wünsche gedroppt werden
function access_streets_wuensche(street_id) {
    streets_field = parseInt(street_id.split("_")[2]);
    if(streets_field > 28 && streets_field <= 56) {
        return true;
    }
    return false;
}

//Nur eigene Karten dürfen in Wünsche gedroppt werden
function access_streets_angebote(street_field_id) {
    streets_field = parseInt(street_field_id.split("_")[2]);
    if(streets_field <= 28) {
        return true;
    }
    return false;
}

//Karten dürfen nur dahin zurückgezogen werden, wo sie herkommen
function access_streets_field(container_field_id, street_field_id) {
    id = parseInt(street_field_id.split("_")[2]);
    if(container_field_id == "angebote_drop" && id <= 28) {
        return true;
    }
    if(container_field_id == "wuensche_drop" && id > 28 && id <= 56) {
        return true;
    }
    return false;
}

function append_streets_wuensche(element) {
    streets_wuensche.push(element);
}

function append_streets_angebote(element) {
    streets_angebote.push(element);
}

function remove_streets_wuensche(element) {
    var index = streets_wuensche.indexOf(element);
    if(index != -1) {
        streets_wuensche.splice(index, 1);
    }
}

function remove_streets_angebote(element) {
    var index = streets_angebote.indexOf(element);
    if(index != -1) {
        streets_angebote.splice(index, 1);
    }
}

// Diese Funktion bekommt als Eingabe List<String>, wobei jeder String die ID einer Straße repräsentiert
//Zeigt Gegner Verhandlungsangebot mit Geldwert und Straßen
function show_offer($scope) {
    if($scope.currentPlayer.name != $scope.username) {
        document.getElementById("show_offer_angebote").style.display = "inline";
        document.getElementById("show_offer_wuensche").style.display = "inline";
        document.getElementById("trading_offer_decision").style.display = "inline";
        document.getElementById("overlay").style.display = "block";

        for(var i = 0; i < streets_angebote.length; i++) {
            newNode = document.getElementById(streets_angebote[i]).cloneNode(true);
            newNode.setAttribute('draggable', false);
            document.getElementById("offer_angebote_streets").appendChild(newNode);
        }
        for(var i = 0; i < streets_wuensche.length; i++) {
            newNode = document.getElementById(streets_wuensche[i]).cloneNode(true);
            newNode.setAttribute('draggable', false);
            document.getElementById("offer_wuensche_streets").appendChild(newNode);
        }
        document.getElementById("offer_wuensche_value").innerHTML = value_wuensche + " Ω";
        document.getElementById("offer_angebote_value").innerHTML = value_angebote + " Ω";
    }
    else {
        while(submit_offer_decision()) {
            document.getElementById("submit_offer").disabled = true;
        }
        document.getElementById("submit_offer").disabled = false;
    }
}

//Gegner kann annehmen oder ablehnen
function submit_offer_decision($scope) {
    wait = true;
    answer = document.querySelector('input[name="trading_offer_buttons"]:checked').value;
    target = $scope.currentPlayer.name == $scope.username;
    if(answer == "Annehmen") {
        wait = false;
        hide_offer();
        $scope.trade();
        show_acception_alert(true, target);
    }
    else if(answer == "Ablehnen"){
        wait = false;
        hide_offer();
        show_acception_alert(false, target);
    }
    document.getElementById("overlay").style.display = "none";
    return wait;
}


//Alex ruft diese Methode auf, wenn ein Angebot angenommen wurde, oder wenn eine neue Straße gekauft wurde
// my_cards, opponent_cards, free_cards: List<String> mit IDs
//Ordnet alle Karten dem korrekten Tabellenplatz zu
function hide_offer() {
    document.getElementById("show_offer_angebote").style.display = "none";
    document.getElementById("show_offer_wuensche").style.display = "none";
    document.getElementById("trading_offer_decision").style.display = "none";
    document.getElementById("overlay").style.display = "none";
}

//test
/*function submit_trading_offer() {
    angebote = ["street_1", "street_2", "street_4"];
    wuensche = ["street_14", "street_24", "street_21"];
    angebote_value = 200;
    wuensche_value = 500;
    show_offer(value_angebote, angebote, value_wuensche, wuensche);
}
//test
function submit_trading_offer() {
    my_cards_test = ["street_1", "street_2", "street_3"];
    opponent_cards_test = ["street_31", "street_32", "street_33"];
    free_cards_test = ["street_59", "street_60", "street_61"];
    update_streets_on_screen(my_cards_test, opponent_cards_test, free_cards_test);
}*/

//Zeigt an, wenn Straße mit Hypothek belastet ist
function show_mortgage(id) {
    //ändere Durchsichtigkeit
    street_id = "street_" + id; //wenn als Integer übergeben wird
    document.getElementById(street_id).style.opacity = 0.5;
    //ändere Hintergrundfarbe
    street_field = document.getElementById(street_id).parentElement.id;
    document.getElementById(street_field).style.background = "red";
    //schalte draggable aus
    document.getElementById(street_id).setAttribute('draggable', false);
}

//Löst Hypothekansicht wieder auf
function end_mortgage(id) {
    //ändere Durchsichtigkeit
    street_id = "street_" + id; //wenn als Integer übergeben wird
    document.getElementById(street_id).style.opacity = 1;
    //ändere Hintergrundfarbe
    street_field = document.getElementById(street_id).parentElement.id;
    document.getElementById(street_field).style.background = "none";
    //schalte draggable ein
    document.getElementById(street_id).setAttribute('draggable', true);
}
//test
/*function submit_trading_offer() {
    hide_decline();
 }*/

//test
/*function end_the_mortgage() {
    end_mortgage(1);
}*/

//Zeigt 10 Sekunden, ob Angebot vom Gegner angenomen oder abgelehnt wurde, danach verschwindet es wieder
function show_acception_alert(decision, target) {
    if(target) {
        $(document).ready(function hide_acception(){
            var counter = 10;
            var timer_status_accept = setInterval(function(){
                document.getElementById("show_acception").style.display = "inline";
                counter--;
                if(counter > 0 && decision){
                    document.getElementById("decision_variable").innerHTML = "Das Angebot wurde angenommen.";
                    var msg = + counter + ' s'
                        $('#timer_status_accept').text(msg);
                }
                else if(counter > 0 && !decision){
                    document.getElementById("decision_variable").innerHTML = "Das Angebot wurde abgelehnt.";
                    var msg = + counter + ' s'
                        $('#timer_status_accept').text(msg);
                }
                else{
                        //$('#time_status').text('hide successfully after 10 seconds');
                        $('#show_acception').hide();
                        clearInterval(timer_status_accept);
                }
            }, 1000);
        });
    }
}

/*function submit_trading_offer(){
    show_opponent_decision(false);
}*/

//Alex kriegt Usernames von Login-Page, gibt ihnen playerId und ruft write-. mit den Variablen own-. und opponent-. auf







//-------Homepage------------






//globale Variablen für countUpTimer()
var timerVariable = setInterval(function() {
    if(!isPaused) {
        countUpTimer();
    }
}, 1000);
var totalSeconds = 0;
var isPaused = true;

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


//ziehe verschiedene Ereigniskarten als HTML
function show_chance_card($scope) {
    //txt aus Backend
    txt = $scope.game.cardDescription;
    //Zeige Karte
    document.getElementById("card_field").style.visibility = "visible";
    //Zeige Ereigniskarte
    document.getElementById("kind_of_taken_card").innerHTML = "Ereigniskarte";
    //Zeige Text
    document.getElementById("taken_card_content").innerHTML = txt;
    //Zeige Fragezeichen
    document.getElementById("chance_questionmark").style.display = "inline";
    //prison card
    if(txt == "Sie haben die Wiederholungsprüfung bestanden! Behalten Sie diese Karte, bis Sie sie benötigen oder verkaufen.") {
        free_prison_chance_dragged();
    }
}

//ziehe verschiedene Gemeinschaftskarten als HTML
function show_community_card($scope) {
    //txt aus Backend
    //window.alert($scope.game.cardDescription);
    txt = $scope.game.cardDescription;
    //Zeige Karte
    document.getElementById("card_field").style.visibility = "visible";
    //Blende Fragezeichen aus
    document.getElementById("chance_questionmark").style.display = "none";
    //Zeige Titel
    document.getElementById("kind_of_taken_card").innerHTML = "Gemeinschaftskarte";
    //Zeige Text
    document.getElementById("taken_card_content").innerHTML = txt;
    //prison card
    if(txt == "Sie haben die Wiederholungsprüfung bestanden! Behalten Sie diese Karte, bis Sie sie benötigen oder verkaufen.") {
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


function hide_card_after_dragged() {
    document.getElementById("card_field").visibility = "hidden";
}

function show_whos_turn() {
    // Get the modal
    var modal = document.getElementById("alert_turn");
    
    // Get the button that opens the modal
    var btn = document.getElementById("myBtn");
    
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    
    // When the user clicks the button, open the modal 
    btn.onclick = function() {
        modal.style.display = "block";
    }
    
    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }
    
    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
    hide_modal();
    
}

function hide_modal() {
    var counter = 3;
    var timer_status = setInterval(function(){
        counter--;
        if(!(counter > 0)) {
            $('#alert_turn').hide();
            clearInterval(timer_status);
        }
    }, 1000);
}

function disable_trading_page() {
    //verhindert Drücken auf Verhandeln oder Straßen
    document.getElementById("Verhandeln").setAttribute('onclick', 'null');
    document.getElementById("Straßen").setAttribute('onclick', 'null');
    //Graue den Bildschirm ein
    document.body.style.opacity = 0.1;
    document.body.style.background.setAttribute('background-blend-mode', 'overlay');
}

function enable_trading_page() {
    document.getElementById("Verhandeln").setAttribute('onclick', 'switch_sites("tradingpage", "homepage")');
    document.getElementById("Straßen").setAttribute('onclick', 'switch_sites("tradingpage", "homepage")');
    document.body.style.opacity = 1;
}

function switch_sites(shown, hidden) {
    document.getElementById(shown).style.display='inline';
    document.getElementById(hidden).style.display='none';
    return false;
}



//Öffne Straßen in neuem Fenster




function array_tal() {
    myTab = window.open('Array Tal.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function printf_straße() {
    myTab = window.open('Printf Straße.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function and_gatter() {
    myTab = window.open('AND Gatter.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function byte_allee() {
    myTab = window.open('Byte Allee.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function multiplexer_platz() {
    myTab = window.open('Multiplexer Platz.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function mmix_gebirge() {
    myTab = window.open('MMIX Gebirge.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function linearegleichungs_allee() {
    myTab = window.open('Linearegleichungs Allee.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function spannungsquelle() {
    myTab = window.open('Spannungsquelle.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function skalarplatz() {
    myTab = window.open('Skalarplatz.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function eigenwertstraße() {
    myTab = window.open('Eigenwertstraße.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function or_gatter() {
    myTab = window.open('OR Gatter.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function determigasse() {
    myTab = window.open('Determigasse.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function zeilenstufenweg() {
    myTab = window.open('Zeilenstufenweg.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function eigenvalley() {
    myTab = window.open('Eigenvalley.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function bernoullevard() {
    myTab = window.open('Bernoullevard.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function lhospital_allee() {
    myTab = window.open('L\'Hospit-Allee.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function cau_chysee() {
    myTab = window.open('Cau-Chysee.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function nand_gatter() {
    myTab = window.open('NAND Gatter.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function sandwich_platz() {
    myTab = window.open('Sandwich-Platz.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function leibniz_hospital() {
    myTab = window.open('Leibniz Hospital.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function stromquelle() {
    myTab = window.open('Stromquelle.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function grenzweg() {
    myTab = window.open('Grenzweg.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function kirchhoffsche_moschee() {
    myTab = window.open('Kirchhoffsche Moschee.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function norator() {
    myTab = window.open('Norator.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function tayl_tor() {
    myTab = window.open('Tayl-Tor.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function nor_gatter() {
    myTab = window.open('NOR Gatter.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function zirkulator() {
    myTab = window.open('Zirkulator.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}

function ohmplatz() {
    myTab = window.open('Ohmplatz.png', '_blank', 'height=500,width=500');
    setTimeout(() => {
        if(myTab) myTab.close();
    }, 3000);
}



//-------------loginpage--------------


/*function user_login_test() {
    users = {
        'username_1': 'admin',
        'username_2': 'user_1'
    }
    user_login(users);
    for(user in users) {
        alert(users[user]);
    }
}*/



//-------Angular Module------------



var laststatus = null;
var lastbal1 = 0;
var lastbal2 = 0;
var test = false;

angular.module('gameApp', []).controller('gameController', function($scope){
    poll($scope);

    $scope.username = window.location.search.split("=")[1];
    $scope.temp_bool = true;
    $scope.lastbal1 = 0;
    $scope.freecardslength = 0;
    $scope.myplayercards = null;
    $scope.opplayercards = null;
    $scope.waitshow = false;
    $scope.counter = 0;
    $scope.myplayer = "Player1";
    $scope.opplayer = "Player2";

    $scope.getOperation = function(url, callback){
        $.getJSON(url, function(json){
            update($scope, json);
            if(callback!==undefined){
                callback(true);
            }
        }
        )
        .fail(function(){
            if(callback!==undefined){
                callback(false);
            }
        });
    }
    
    // making controller methods accessible
    
    $scope.join = function(){
        $scope.getOperation('join?name=' + $scope.username);
    }
    
    $scope.start = function(){
        $scope.getOperation('start');
        if($scope.game.currentStatusString!=="WAITING") pause_timer();
    }
    
    $scope.reset = function(){
        $scope.getOperation('reset');
    }
    
    $scope.end = function(){
        $scope.getOperation('end');
    }
    
    $scope.endTurn = function(){
        $scope.getOperation('endturn');
    }
    
    $scope.decideJail = function(choice){
        $scope.getOperation('jaildecision?choice=' + choice);
    }
    
    $scope.useJailCard = function(){
        $scope.getOperation('jailcard');
    }
    
    $scope.buy = function(){
        $scope.getOperation('buyproperty');
    }

    $scope.sellProp2Bank = function(fieldIndex){
        $scope.getOperation('sellpropertybank?fieldIndex=' + fieldIndex);
    }
    
    $scope.auction = function(){
        $scope.getOperation('auction?fieldIndex=' + $scope.currentPlayer.position);
    }
    
    $scope.trade = function(){
        $scope.getOperation('trading?offer=' + streets_angebote + '&receive=' + streets_wuensche + '&moneyOffer=' + value_angebote
        + '&moneyReceive=' + value_wuensche + '&partnerId=' + (1-$scope.currentPlayer));
    }
    
    $scope.showoffer = function(){
        show_offer($scope);
    }
    
    $scope.submitoffer = function(){
        submit_offer_decision($scope);
    }
    
    $scope.bid = function(){
        $scope.getOperation('bid?name=' + $scope.username + '&bid=' + $scope.bid);
    }
    
    $scope.startMortgage = function(fieldIndex){
        $scope.getOperation('startmortgage?fieldIndex=' + fieldIndex);
    }
    
    $scope.endMortgage = function(fieldIndex){
        $scope.getOperation('endmortgage?fieldIndex=' + fieldIndex);
    }
    
    $scope.buyHouse = function(fieldIndex){
        $scope.getOperation('buyHouse?fieldIndex=' + fieldIndex);
    }
        
    $scope.sellHouse = function(fieldIndex){
        $scope.getOperation('sellHouse?fieldIndex=' + fieldIndex);
    }
        
    $scope.getFieldsByPlayer = function() {
        $scope.getOperation('fieldsByPlayer?playerId=');
    }
        
    $scope.getFreeCards = function() {
        $scope.getOperation('freecards');
    }
        
    $scope.show_chance_card = function() {
        show_chance_card($scope);
    }
        
    $scope.show_community_card = function() {
        show_community_card($scope);
    }
        
    $scope.free_prison_community_button_clicked = function() {
        if($scope.currentPlayer.inJail) {
            document.getElementById("prison_community_click").disabled = true;
            document.getElementById("prison_community_click").style.opacity = 0.5;
        }
        $scope.useJailCard();
    }
        
    $scope.free_prison_chance_button_clicked = function() {
        if($scope.currentPlayer.inJail) {
            document.getElementById("prison_chance_click").disabled = true;
            document.getElementById("prison_chance_click").style.opacity = 0.5;
        }
        $scope.useJailCard();
    }

    $scope.buy_out_of_prison = function() {
        if($scope.currentPlayer.inJail) {
            document.getElementById("buy_out_of_prison").style.display = "inline";
            $scope.decideJail(true);
        }
        else document.getElementById("buy_out_of_prison").style.display = "none";
    }
        
    $scope.roll_out_of_prison = function() {
        if($scope.currentPlayer.inJail) {
            document.getElementById("roll_out_of_prison").style.display = "inline";
            $scope.decideJail(false);
        }
        else document.getElementById("roll_out_of_prison").style.display = "none";
    }

    $scope.update_cards1 = function(){
        $scope.getFreeCards();
        $scope.getFieldsByPlayer();
        $scope.waitshow = true;
        $scope.update_cards2();
    }

    $scope.update_cards2 = function(){

        for(var i = 0; i < 84; i++) {
            try {
                var street_element = document.getElementById("street_" + (i+1));
                document.getElementById("storage").appendChild(street_element);
            }
            catch (e) {

            }
        }

        $scope.game.mycards.forEach(function(index, i){
            document.getElementById("street_field_" + (i + 1)).appendChild(document.getElementById("street_" + index));
        });
        $scope.game.opcards.forEach(function(index, i){
            document.getElementById("street_field_" + (i + 1)).appendChild(document.getElementById("street_" + index));
        });
        $scope.game.freecards.forEach(function(index, i){
            document.getElementById("street_field_" + (i + 57)).appendChild(document.getElementById("street_" + index));
        });

        if($scope.waitshow){
            setTimeout(function(){
                $scope.update_cards2();
            }, 1000);
        }
    }
        //to be continued
});
    
    // periodically making API requests to update scope object
function poll($scope){
    $.getJSON('game', function(json){
        $scope.$apply(function(){
            update($scope, json);
        });
        setTimeout(function(){
            poll($scope);
        }, 1000);
    });
}

function update($scope, json){
    // load json package containing game object into scope.game variable
    $scope.game = json;
    $scope.myplayer = $scope.game.players[$scope.currentPlayer];
    $scope.opplayer = $scope.game.players[1-$scope.currentPlayer];
    //if($scope.game.players.length>=2) $scope.opplayer = $scope.game.players[1-$scope.currentPlayer].name;
    //window.alert("hello");
    if($scope.temp_bool===true){
        $scope.join();
        $scope.start();
        $scope.temp_bool = false;
    }
    $scope.waitshow = false;

    checkBalanceAdjustment($scope);
        
    // Update here every variable that is not directly addressed via $scope.game
    if($scope.temp_bool===false) $scope.currentPlayer = $scope.game.players[$scope.game.currentPlayer];
    // $scope.opponent = $scope.game.players[1-$scope.game.currentPlayer];

    $scope.game.board.fields.forEach(function(field, i) {
        if(field.Hypothek) show_mortgage(i);
        else end_mortgage(i);
    }
    );

    // update GUI if game status changes
    if(laststatus!=$scope.game.currentStatusString){
        statusSwitch($scope);
        laststatus = $scope.game.currentStatusString;
    }
    $scope.$apply();
    /*if(homepage){
        switch_sites('tradingpage', 'homepage');
        switch_sites('homepage', 'tradingpage');
    }
    else{
        switch_sites('homepage', 'tradingpage');
        switch_sites('tradingpage', 'homepage');
    }*/
}

function checkBalanceAdjustment($scope) {
    if($scope.lastbal1 !== $scope.game.players[0].balance) {
        isOpponent = $scope.game.players[0].name !== $scope.username;
        updateCounter_2($scope.game.players[0].balance, isOpponent);
        $scope.lastbal1 = $scope.game.players[0].balance;
    }
    if($scope.game.players.length>=2){
        if($scope.lastbal2 !== $scope.game.players[1].balance) {
            isOpponent = $scope.game.players[1].name !== $scope.username;
            updateCounter_2($scope.game.players[1].balance, isOpponent);
            $scope.lastbal2 = $scope.game.players[1].balance;
        }
    }
}

function statusSwitch($scope){
    // Adjust UI for inactive players
    if(($scope.username !== $scope.currentPlayer.name) &&
        ($scope.game.currentStatusString === 'TURN' || $scope.game.currentStatusString === 'BUYPROPERTY'
        ||$scope.game.currentStatusString === 'DICE' || $scope.game.currentStatusString === 'JAIL')){
        disable_trading_page();
    }
    else{
        enable_trading_page();
        show_whos_turn();
    }

    if($scope.game.currentstatusString === 'AUCTION'){
        $scope.bid = $scope.game.highestBid;
    }
}
