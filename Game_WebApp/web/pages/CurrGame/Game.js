var refreshRate = 1000; //mili seconds
var PLAYER_LIST_URL = "/pages/CurrGame/Game/userlist";
var GAME_INFO_URL = "/pages/CurrGame/Game/GameInfo";
var ACTIVE_GAME_URL = "/pages/CurrGame/Game/isActiveGame";
var loggedUser;
var CurrGameName;
var gameBoard;
var isActive = false;
var gameUsers = [];


$(function() {
    setInterval(ajaxUsersList, refreshRate);
    setInterval(isActiveGame, refreshRate);
    getGameInfoByGameName();
    setInterval(getNextTurnInfoByGameName, refreshRate);

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
            if(JSON.stringify(users)!== JSON.stringify(gameUsers)) {
                gameUsers = users;
                refreshUsersList(gameUsers);
            }
        }

    });
    console.log("users servlet");
}

function isActiveGame()
{
    console.log("isActiveGame servlet");
        $.ajax({
            type: 'GET',
            url: ACTIVE_GAME_URL,
            data: {"gameName": CurrGameName},
            success: function (res) {
                if (JSON.stringify(res.isActive) !== JSON.stringify(isActive)) {
                    isActive = res.isActive;
                    if (res.isActive) {
                        drawGameBoard(res.gameBoard);
                        updateGameInfo(CurrGameName);
                    }
                }
            }
        });
}

function updateGameInfo(gameName)
{
    var id = gameName.replace(/\s/g,"-") +"-info";
    $.ajax({
        type: 'GET',
        url: GAME_INFO_URL,
        data:{"gameName": gameName},
        success: function (game) {
            {
                $('.status').text("game started!");
                //$('.playerName').text(game.playerTurn);
            }
        }
    });
}


function drawGameBoard(gameBoard) {
    console.log(gameBoard);
    var space =1;
    var rows = gameBoard.length;
    console.log("rows" + rows);
    var cols = gameBoard[0].length;
    console.log("cols" + cols);
    for (var i = 0; i <= rows+1; i++) {
        var ch = "";
        for(var j=0; j<cols; j++) {
            //ch += "<td class = '"+"tr"+ i + "X" + j+ "'></td>";
           // space++;
                if(i===0) {
                    var btn = document.createElement("BUTTON");
                    btn.setAttribute("class","arrowImg");
                    btn.setAttribute("id",j);
                    btn.onclick = insertDisc;

                    //console.log(btn.id);
                   // var btn = $("<button>").addClass("arrowImg").click( inputFunc ).setAttribute("id",j);
                    //console.log(btn.id);
                    $("#board").append(btn);
                }
                else if (i === rows+1 ){
                    var btn = document.createElement("BUTTON");
                    btn.setAttribute("class","popOut");
                    btn.setAttribute("id",j);
                    btn.onclick = popOutDisc;

                    //console.log(btn.id);
                    // var btn = $("<button>").addClass("arrowImg").click( inputFunc ).setAttribute("id",j);
                    //console.log(btn.id);
                    $("#board").append(btn);
                }
                else {
                    var newBall = $("<div>").addClass("sphere");
                    $("#board").append(newBall);
                }
        }
        var newline = $("<br/>");
        $("#board").append(newline);
    }
}

function insertDisc(){
    // only for check, each button get a number
    console.log("insert " + this.id);
    checkHumanTurnAndInsert();

}

function checkHumanTurnAndInsert()
{

}

function popOutDisc(){
    // only for check, each button get a number
    console.log("popOut "+this.id);
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
    console.log("show game Info servlet")
    $('.gameName').text(game.gameName);
    $('.variant').text(game.variant);
    $('.status').text("game hasn't start yet!");
    $('.target').text(game.target);
    $('.playerName').text(game.playerTurn);
    console.log("now play : " + game.playerTurn)
    // var template = $("#GameInfo-template").clone().html(function(i,html) {
    //     return html
    //         .replace('{{gameName}}', game.gameName)
    //         .replace('{{variant}}',game.variant)
    //         .replace('{{status}}', "game hasn't start yet!")
    //         .replace('{{target}}', game.target)
    //         .replace('{{playerName}}', "none");
    // });
    // template.attr('id', game.gameName.replace(/\s/g,"-")+"-info");
    // $(template).appendTo("#game_info").show();
}

function getNextTurnInfoByGameName()
{
    var urlArr = window.location.href.split("?");
    var nameArr = urlArr[1].split("=");
    CurrGameName = nameArr[1].replace("-"," ");

    console.log("next player Info servlet")
    $.ajax({
        type: 'POST',
        data: {"gameName" : CurrGameName},
        url: "/pages/CurrGame/Game/nextPlayer",
        success: function (game) {
            showNextTurnInfoOnScreen(game);
        }
    });
}

function showNextTurnInfoOnScreen(game)
{
    $('.playerName').text(game.playerTurn);

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