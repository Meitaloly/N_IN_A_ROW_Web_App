var refreshRate = 1000; //mili seconds
var USER_LIST_URL = "/pages/GameLobby/Lobby/userslist";

$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
})

$(function(){
    $.ajax({
        type: 'GET',
        url: "/pages/GameLobby/Lobby/logout",
        success: function(user) {
            $(".currUser").text(user);
        }
    });
})

    function ajaxUsersList() {
    $.ajax({
        type: 'GET',
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function refreshUsersList(users) {
    //clear all current users
    $("#userList").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);
        //create a new <option> tag with a value in it and
        //append it to the #userList (div with id=userList) element
        $('<li>' + username.userName + " - " + username.type + '</li>').appendTo($("#userList"));
    });
}

$(document).ready(function(){
    $("#logoutForm").on('submit', function (e)
    {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: "/pages/GameLobby/Lobby/logout",
            //data: {"username" : userName, "playerType" : playerType},
            success: function(response)
            {
                console.log("you loggedd out!");
                window.location.replace("/pages/SignUp/SignUpPage.html");
            },
        })
    })
});


$(document).ready(function(){
    $("#newGameForm").on('submit', function (e)
    {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: "/pages/GameLobby/Lobby/createNewGame",
            //data: {"username" : userName, "playerType" : playerType},
            success: function(response)
            {
                console.log("you loggedd out!");
                window.location.replace("/pages/SignUp/SignUpPage.html");
            },
        })
    })
});

