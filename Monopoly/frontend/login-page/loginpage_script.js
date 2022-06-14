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