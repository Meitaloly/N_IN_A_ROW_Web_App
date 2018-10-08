var refreshRate = 1000; //mili seconds
var PLAYER_LIST_URL = "/pages/CurrGame/Game/userlist";
var GAME_INFO_URL = "/pages/CurrGame/Game/GameInfo";
var loggedUser;
var CurrGameName;
var gameBoard;

$(function() {
    setInterval(ajaxUsersList, refreshRate);
    getGameInfoByGameName();
    $(function(){
        $.ajax({
            type: 'GET',
            url: "/pages/CurrGame/Game/loggedUser",
            success: function(user) {
                loggedUser = user;
                $(".currUser").text(user);
            }
        });
    })

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

function refreshUsersList(users) {
    //clear all current users
    $("#playerList").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);
        //create a new <option> tag with a value in it and
        //append it to the #userList (div with id=playerList) element
        var str2 = username.game;
        var isEqual = CurrGameName.localeCompare(str2);
        if (isEqual === 0) {
            $('<li>' + username.userName + " - " + username.type + '</li>').appendTo($("#playerList"));
        }
    });
}



function getGameInfoByGameName()
{
    var urlArr = window.location.href.split("?");
    var nameArr = urlArr[1].split("=");
    CurrGameName = nameArr[1].replace("-"," ");

    $.ajax({
        type: 'POST',
        data: {"gameName" : CurrGameName},
        url: GAME_INFO_URL,
        success: function (game) {
            showGameInfoOnScreen(game);
        }
    });
}

function showGameInfoOnScreen(game)
{
    var template = $("#GameInfo-template").clone().html(function(i,html) {
        return html
            .replace('{{gameName}}', game.gameName)
            .replace('{{variant}}',game.variant)
            .replace('{{status}}', "game hasn't start yet!")
            .replace('{{target}}', game.target)
            .replace('{{playerName}}', "none");
    });
    template.attr('id', game.gameName.replace(/\s/g,"-")+"-info");
    $(template).appendTo("#game_info").show();
}

function logOutFromGame() {
        $.ajax({
            type: 'POST',
            url: "/pages/CurrGame/Game/LogoutFromCurrGameServlet",
            data: {"username" : loggedUser},
            success: function (response) {
                console.log("you loggedd out!");
                window.location.replace("/pages/GameLobby/Lobby.html");
            },
        })

    $.ajax({
            type: 'POST',
            url: "/pages/GameLobby/Lobby/updatedSignedPlayers",
            data: {"gameName": CurrGameName, "action": "remove"}
    })
}