var refreshRate = 1000; //mili seconds
var USER_LIST_URL = "/pages/GameLobby/Lobby/userslist";
var GAME_LIST_URL = "/pages/GameLobby/Lobby/gameList";
var currGames ={};
var loggedUser;

$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxGamesList, refreshRate);
})

$(function(){
    $.ajax({
        type: 'GET',
        url: "/pages/GameLobby/Lobby/logout",
        success: function(user) {
            loggedUser = user;
            $(".currUser").text(user);
        }
    });
})

function ajaxUsersList() {
    $.ajax({
        type: 'GET',
        url: USER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}

function ajaxGamesList() {

    $.ajax({
        type: 'GET',
        url: GAME_LIST_URL,
        success: function (games) {
            if(JSON.stringify(currGames)!== JSON.stringify(games))
            {
                currGames = games;
                refreshGamesList(games);
            }
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

function refreshGamesList(games) {
    //clear all current users
    // rebuild the list of users: scan all users and add them to the list of users
    if (Object.keys(games).length > 0) {
        $("#gameList").empty();
        $.each(games || [], function (index, game) {
            console.log("Adding game #" + index + ": " + game.gameName);
            //create a new <option> tag with a value in it and
            //append it to the #userList (div with id=userList) element
            var template = $("#mock-template").clone().html(function(i,html) {
                return html
                    .replace('{{gameName}}', game.gameName)
                    .replace('{{user}}', game.userOwner)
                    .replace('{{rows}}', game.rows)
                    .replace('{{cols}}', game.cols)
                    .replace('{{target}}', game.target)
                    .replace('{{variant}}',game.variant)
                    .replace('{{status}}', game.status)
                    .replace('{{signed}}', game.currNumOfPlayersInGame)
                    .replace('{{required}}', game.numOfPlayersRequired);
                    // .replace('{{join}}', "Join game");
            });
            template.attr('id', game.gameName.replace(/\s/g,"-"));
            $(template).appendTo("#gameList").show();
            //
            // if(game.numOfPlayersRequired == game.currNumOfPlayersInGame ) {
            //     template.replace('{{join}}', "Game started");
            // }
            var gameClass = game.gameName.replace(/\s/g, "-")

            if(game.status.toLowerCase() === "active") {
                $('#' + gameClass).children('.btn').addClass('btnDisable');
                $('#' + gameClass).children('.btn').removeClass('btnShown');

            }
            else
            {
                $('#' + gameClass).children('.btn').removeClass('btnDisable');
                $('#' + gameClass).children('.btn').addClass('btnShown');


            }
        });
    }
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
    });


    $("input:file").change(function() {
        var inputFile = document.getElementById("newGameForm");
        var file = inputFile[0].files[0];

        var formData = new FormData();
        formData.append(loggedUser, file);

        var arr = file.name.split(".");
        if(arr[arr.length-1].toLowerCase() !== "xml")
        {
            alert("Not XML file! try load another file!");
        }

        else {
            $.ajax({
                method: 'POST',
                data:formData,
                //data: file,
                url: "/pages/GameLobby/Lobby/createNewGame",
                processData: false, // Don't process the files
                contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                //timeout: 4000,
                error: function (e) {
                    console.error("Failed to submit");
                    $("#result").text("Failed to get result from server " + e);
                },
                success: function (r) {
                    alert(r);
                }
            });
        }

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
});

function openGame(btn) {
    var parent_id = $(btn).parent().attr('id');
    var gameName = parent_id.replace("-"," ");
    var isActive = updateNumOfPlayerInCurrGame(gameName);
    updateGameName(loggedUser,gameName);
    window.location.replace("/pages/CurrGame/Game.html?gameName=" + parent_id);

};

function updateGameName(userName,gameName){
    $.ajax({
        type: 'POST',
        url: "/pages/GameLobby/Lobby/updateUserGameServlet",
        data: {"username" : userName, "gameName" : gameName}
    })
}



function updateNumOfPlayerInCurrGame(gameName){
    var isActive = false;
    $.ajax({
        type: 'POST',
        url: "/pages/GameLobby/Lobby/updatedSignedPlayers",
        data: {"gameName": gameName, "action": "add"},
        success: function(response) {
            console.log("game is active");
            isActive = true;
        }
    })

    return isActive;
}

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
})