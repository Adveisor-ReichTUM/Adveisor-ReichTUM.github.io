//-------Tradingpage------------


const playerID = 0;

const my_cards = [];
const opponent_cards = [];
const free_cards = [];

showing_selector_wuensche = false;
showing_selector_angebote = false;

value_wuensche = 0;
value_angebote = 0;

var streets_wuensche = [];
var streets_angebote = [];

//Player Id muss durch Aufruf festgelegt werden
function set_player_id(id) {
    playerID = id;
}

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

//test
/*function submit_trading_offer() {
    trade(streets_angebote, streets_wuensche, value_angebote, value_wuensche, playerID);
}*/

// Diese Funktion bekommt als Eingabe List<String>, wobei jeder String die ID einer Straße repräsentiert
//Zeigt Gegner Verhandlungsangebot mit Geldwert und Straßen
function show_offer(value_angebote, streets_angebote, value_wuensche, streets_wuensche) {
    document.getElementById("show_offer_angebote").style.display = "inline";
    document.getElementById("show_offer_wuensche").style.display = "inline";
    document.getElementById("trading_offer_decision").style.display = "inline";
    document.getElementById("overlay").style.display = "block";
    document.getElementById("angebote").style.display = "none";
    document.getElementById("wuensche").style.display = "none";
    document.getElementById("submit_offer").style.display = "none";
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

//Gegner kann annehmen oder ablehnen
function submit_offer_decision() {
    answer = document.querySelector('input[name="trading_offer_buttons"]:checked').value;
    if(answer == "Annehmen") {
        alert("Angebot wurde akzeptiert");
        //backend_offer_accepted(True);
    }
    else {
        alert("Angebot wurde abgelehnt");
        //backend_offer_accepted(False);
    }
    document.getElementById("overlay").style.display = "none";
}

//Diese Funktion soll Alex für den Spieler aufrufen, der das Angebot gemacht hat, bevor er den Screen updated, decision ist bool
//zeigt einem Entscheidung des Gegners
function show_opponent_decision(decision) {
    show_acception_alert(decision);
}



//Alex ruft diese Methode auf, wenn ein Angebot angenommen wurde, oder wenn eine neue Straße gekauft wurde
// my_cards, opponent_cards, free_cards: List<String> mit IDs
//Ordnet alle Karten dem korrekten Tabellenplatz zu
function update_streets_on_screen(my_cards, opponent_cards, free_cards) {
    document.getElementById("show_offer_angebote").style.display = "none";
    document.getElementById("show_offer_wuensche").style.display = "none";
    document.getElementById("trading_offer_decision").style.display = "none";
    document.getElementById("angebote").style.display = "inline";
    document.getElementById("wuensche").style.display = "inline";
    document.getElementById("submit_offer").style.display = "inline";
    // Update it
    
    for(var i = 0; i < 84; i++) {
        try {
            var street_element = document.getElementById("street_" + (i+1));
            document.getElementById("storage").appendChild(street_element);
        }
        catch (e) {

        }
    }
    for(var i = 0; i < my_cards.length; i++) {
        document.getElementById("street_field_" + (i + 1)).appendChild(document.getElementById(my_cards[i]));
    }
    for(var i = 0; i < opponent_cards.length; i++) {
        document.getElementById("street_field_" + (i + 29)).appendChild(document.getElementById(opponent_cards[i]));
    }
    for(var i = 0; i < free_cards.length; i++) {
        document.getElementById("street_field_" + (i + 57)).appendChild(document.getElementById(free_cards[i]));
    }
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
function show_acception_alert(decision) {
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

/*function submit_trading_offer(){
    show_opponent_decision(false);
}*/

//Alex kriegt Usernames von Login-Page, gibt ihnen playerId und ruft write-. mit den Variablen own-. und opponent-. auf
function write_username(own_username, opponent_username) {
    //Spieler 1 für eigenen Nutzernamen
    document.getElementById("own_username").innerHTML = own_username;
    //Spieler 2 für gegnerischen Nutzernamen
    document.getElementById("opponent_username").innerHTML = opponent_username;
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
    if(opponents_turn) {
        //verhindert Drücken auf Verhandeln oder Straßen
        document.getElementById("Verhandeln").href = "javascript: void(0)"; //alternativ: .href = "";
        document.getElementById("Straßen").href = "javascript: void(0)";
        //leitet zurück zu Hauptseite
        window.location.href = './homepage_index.html'; //ich bin mir nicht ganz sicher, ob es auch ohne ./ funktioniert
        //
        document.getElementsByTagName("body").style.opacity = 0.5;
    }
}





//-------Homepage------------






chance_cards=[];
community_cards=[];
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
    document.getElementById("kind_of_taken_card").innerHTML = "Ereigniskarte";
    //get image
    document.getElementById("taken_card_content").innerHTML = txt;
    //change index
    source = "Ereigniskarte Fragezeichen.jpg"
    //show image
    window.location.href = '../' + source;
    //prison card
    if(current_chance_card_id == prison_index) {
        free_prison_chance_dragged();
    }
}

//ziehe verschiedene Gemeinschaftskarten als Bild
function show_community_card() {
    document.getElementById("kind_of_taken_card").innerHTML = "Gemeinschaftskarte";
    //get image
    document.getElementById("taken_card_content").innerHTML = txt;
    //change index
    source = "Gemeinschaftskarte Truhe.jpg"
    //show image
    window.location.href = '../' + source;
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




//-------------loginpage--------------






var users = {
    'fabian_gruber': [false, true, true, false]
};

var record = [];

function user_login() {
    uname_exists = false;
    entered_username = document.getElementById("username").value;
    for(user in users) {
        if(users[user] == entered_username) {
            uname_exists = true;
            break;
        }
        else uname_exists = false;   
    }
    if(!uname_exists) {
        key = "username_" + parseInt(Object.keys(users).length + 1);
        users[key] = entered_username;
    }    
}

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

var app = angular.module('gameApp', []);
app.controller('gameController', function($scope){
        poll($scope);

        $scope.getOperation = function(url, callback){
            $.getJSON(url, function(json){
                    update($scope, json);
                    if(callback!=undefined){
                        callback(true);
                    }
                }
            )
            .fail(function(){
                if(callback!=undefined){
                    callback(false);
                }
            })
        }

        //to be continued
    }
);

// periodically making API requests to update scope object
function poll($scope){
    $.getJSON('game', function(json){
        update($scope, json);
        setTimeout(function(){
            poll($scope);
        }, 500);
    });
}

function update($scope, json){
    // load json package containing game object into scope.game variable
    $scope.game = json;

    // Update here every variable that is not directly addressed via $scope.game
    $scope.currentPlayer = scope.game.players[$scope.game.currentPlayer];

    // update GUI if game status changes
    if(laststatus!=$scope.game.currentStatusString){
        statusSwitch($scope);
        laststatus = $scope.game.currentStatusString;
    }

    $scope.$apply();
}

function statusSwitch($scope){
    // Adjust UI for inactive player
    if(($scope.username != $scope.currentPlayer.name) &&
        ($scope.game.currentStatusString == 'TURN' || $scope.game.currentStatusString == 'BUYPROPERTY'
        ||$scope.game.currentStatusString == 'DICE' || $scope.game.currentStatusString == 'JAIL')){
        // edit ui ....
    }
    else{
        // undo edit ui ....
    }

    if($scope.game.currentstatusString == 'AUCTION'){
        $scope.bid = $scope.game.highestBid;
    }
}