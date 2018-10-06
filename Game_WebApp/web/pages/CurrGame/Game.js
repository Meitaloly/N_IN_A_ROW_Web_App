var refreshRate = 1000; //mili seconds
var PLAYER_LIST_URL = "userslist";

$(function() {
    setInterval(ajaxUsersList, refreshRate);
})

function ajaxUsersList() {
    $.ajax({
        type: 'GET',
        url: PLAYER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}

function refreshPlayersList(users) {
    //clear all current users
    $("#playerList").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);
        //create a new <option> tag with a value in it and
        //append it to the #userList (div with id=playerList) element
        $('<li>' + username.userName + " - " + username.type + '</li>').appendTo($("#playerList"));
    });
}