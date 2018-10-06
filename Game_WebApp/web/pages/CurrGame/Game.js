var refreshRate = 1000; //mili seconds
var PLAYER_LIST_URL = "/pages/CurrGame/Game/userlist";
var GAME_INFO_URL = "/pages/CurrGame/Game/GameInfo";

$(function() {
    setInterval(ajaxUsersList, refreshRate);
    getGameInfoByGameName();
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
        $('<li>' + username.userName + " - " + username.type + '</li>').appendTo($("#playerList"));
    });
}

function getGameInfoByGameName()
{
    var urlArr = window.location.href.split("?");
    var nameArr = urlArr[1].split("=");
    var gameName = nameArr[1].replace("-"," ");

    $.ajax({
        type: 'POST',
        data: {"gameName" : gameName},
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