var refreshRate = 1000; //mili seconds
var LOGGED_USER_SERVLET = buildUrlWithContextPath("/pages/CurrGame/Game/loggedUser");
var PLAYER_LIST_URL = buildUrlWithContextPath("/pages/CurrGame/Game/userlist");
var GAME_INFO_URL = buildUrlWithContextPath("/pages/CurrGame/Game/GameInfo");
var ACTIVE_GAME_URL = buildUrlWithContextPath("/pages/CurrGame/Game/isActiveGame");
var INSERT_DISK = buildUrlWithContextPath("/pages/CurrGame/Game/insertDisk");
var GAME_BOARD_URL = buildUrlWithContextPath("/pages/CurrGame/Game/GameBoard");
var POPOUT_DISK = buildUrlWithContextPath("/pages/CurrGame/Game/popOutDisk");
var RESET_GAME_URL = buildUrlWithContextPath("/pages/CurrGame/Game/resetGame");
var ACTIVE_PLAYERS_URL = buildUrlWithContextPath("/pages/CurrGame/Game/activePlayers");
var IS_COMP_TURN_URL = buildUrlWithContextPath("/pages/CurrGame/Game/isCompTurn");
var LOGOUT_FROM_CURR_GAME = buildUrlWithContextPath("/pages/CurrGame/Game/LogoutFromCurrGameServlet");
var NEXT_PLAYER_URL =  buildUrlWithContextPath("/pages/CurrGame/Game/nextPlayer");
var LOBBY = buildUrlWithContextPath("/pages/GameLobby/Lobby.html");

var loggedUser;
var CurrGameName;
var gameBoard;
var isActive = false;
var gameUsers = [];
var gameBoardUpdateInervalID;
var isWinner = false;
var isWinnerIntervalID;
var winnerMsg;

$(function() {
    var urlArr = window.location.href.split("?");
    var nameArr = urlArr[1].split("=");
    CurrGameName = nameArr[1].replace("-"," ");

    resetGame();
    setInterval(ajaxUsersList, refreshRate);
    isActiveIntervalId = setInterval(isActiveGame, refreshRate);
    getGameInfoByGameName();
   // setInterval(checkComputerTurnAndPlay, 3000);
    setInterval(getNextTurnInfoByGameName, refreshRate);
    isWinnerIntervalID = setInterval(checkWinner, 3000);

    $(function(){
        $.ajax({
            type: 'GET',
            url: LOGGED_USER_SERVLET,
            success: function(user) {
                loggedUser = user;
                $(".currUser").text(user);
            }
        });

    })


})



function checkComputerTurnAndPlay()
{
     $.ajax({
         type: 'GET',
         url: IS_COMP_TURN_URL,
         data: {"gameName": CurrGameName},
         success:function(){
             getNextTurnInfoByGameName();
         }
     });
}

function checkWinner() {
    if (isWinner) {
        clearInterval(isWinnerIntervalID);
        resetGame();
        logOutFromGame(isWinner);

    }
    else {
        $.ajax({
            type: 'GET',
            url: ACTIVE_PLAYERS_URL,
            data: {"gameName": CurrGameName},
            success: function (res) {
                if (res) {
                    $("#winnerArea").text("Everyone left the game - YOU WON!");
                    isWinner = true;
                }
            }
        });
    }
}

function resetGame()
{
    isActive = false;
    $.ajax({
        type: 'POST',
        url: RESET_GAME_URL,
        data: {"gameName": CurrGameName}
    });



}
function updateGameBoardAjax()
{
    $.ajax({
        type: 'GET',
        url: GAME_BOARD_URL,
        data: {"gameName": CurrGameName},
        success: function (res) {
            if (JSON.stringify(res.gameBoard) !== JSON.stringify(gameBoard) || res.winners.length > 0) {
                gameBoard = res.gameBoard;
                updateGameBoard(res.gameBoard);
                if(res.winners.length > 0)
                {
                    winnerMsg = "";
                    if(res.winners.length === 1)
                    {
                        winnerMsg = res.winners[0] + " is the winner!";
                    }
                    else
                    {
                        winnerMsg = "The winners are: ";
                        for(var j=0; j<res.winners.length; j++)
                        {
                            winnerMsg += " " + res.winners[j];
                        }
                    }
                    $("#winnerArea").text(winnerMsg);
                    //alert(winnerMsg);
                    console.log("winner msg: ",winnerMsg);
                    clearInterval(gameBoardUpdateInervalID);
                    isWinner = true;
                    //setTimeout(function(){ logOutFromGame(true); }, 3000);
                }
            }
        }
    });
}
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
                //setInterval(checkComputerTurnAndPlay, refreshRate);
                if (JSON.stringify(res.isActive) !== JSON.stringify(isActive)) {
                    isActive = res.isActive;
                    if (res.isActive) {
                        drawGameBoard(res.gameBoard, res.gameType);
                        updateGameInfo(CurrGameName);
                        gameBoardUpdateInervalID = setInterval(updateGameBoardAjax,refreshRate);
                        checkComputerTurnAndPlay();
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
            }
        }
    });
}


function drawGameBoard(gameBoard, gameType) {
    console.log(gameBoard);
    var rows = gameBoard.length;
    var cols = gameBoard[0].length;
    for (var i = 0; i <= rows+1; i++) {
        for(var j=0; j<cols; j++) {
                if(i===0) {
                    var btn = document.createElement("BUTTON");
                    btn.setAttribute("class","arrowImg");
                    btn.setAttribute("id",j);
                    btn.onclick = insertDisc;
                    $("#board").append(btn);
                }
                else if (i === rows+1) {
                    if (gameType.toLowerCase() === "popout") {
                        var btn = document.createElement("BUTTON");
                        btn.setAttribute("class", "popOut");
                        btn.setAttribute("id", j);
                        btn.onclick = popOutDisc;
                        $("#board").append(btn);
                    }
                }
                else {
                    var currId = (i-1)+"X"+j;
                    var newBall = $('<div/>', {
                        id: currId,
                        class: 'sphere',
                    });
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
    $.ajax({
        type: 'GET',
        url: INSERT_DISK,
        data: {"gameName": CurrGameName , "playerName": loggedUser, "col":this.id},
    });
}

function updateGameBoard(gameBoard)
{
    for(var i =0 ; i<gameBoard.length; i++)
    {
        for(var j =0 ; j<gameBoard[i].length ; j++) {
            setColorOnCell(gameBoard[i][j], i, j);
        }
    }
}

function setColorOnCell(value, row, col )
{
    var cellId = row+"X"+col;
    var color;

    switch(value) {
        case 0:
            color = "pink";
            break;
        case 1:
            color = "blue";
            break;
        case 2:
            color = "red";
            break;
        case 3:
            color = "yellow";
            break;
        case 4:
            color = "green";
            break;
        case 5:
            color = "aqua";
            break;
        default:
            color = "noColor";
            break;
    }

    $("#"+cellId).removeClass();
    $("#"+cellId).addClass("sphere");
    if(color !== "noColor") {
        $("#" + cellId).addClass(color);
    }
}


function popOutDisc(){
    // only for check, each button get a number
    console.log("popOut "+this.id);
    $.ajax({
        type: 'GET',
        url: POPOUT_DISK,
        data: {"gameName": CurrGameName , "playerName": loggedUser, "col":this.id},
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
        url: NEXT_PLAYER_URL,
        success: function (game) {
            showNextTurnInfoOnScreen(game);
        }
    });
}

function showNextTurnInfoOnScreen(game)
{
    $('.playerName').text(game.playerTurn);

}

function logOutFromGame(isWinner) {
        $.ajax({
            type: 'POST',
            url: LOGOUT_FROM_CURR_GAME,
            data: {"username" : loggedUser, "isWinner" : isWinner},
            success: function (response) {
                console.log("you loggedd out!");
                window.location.replace(LOBBY);
                if (!isWinner) {
                    clearInterval(isActiveIntervalId);
                    isActive = false;
                    updateGameBoardAjax();
                }
            },
        });

         $.ajax({
            type: 'POST',
            url: UPDATE_SIGNED_URL,
            data: {"gameName": CurrGameName, "action": "remove"},

           /* error:function() {
                alert("Only one player remains in the game\n" +
                    "Technical victory");
            },*/
         });
}


// extract the context path using the window.location data items
function calculateContextPath() {
    var pathWithoutLeadingSlash = window.location.pathname.substring(1);
    var contextPathEndIndex = pathWithoutLeadingSlash.indexOf('/');
    return pathWithoutLeadingSlash.substr(0, contextPathEndIndex)
}

// returns a function that holds within her closure the context path.
// the returned function is one that accepts a resource to fetch,
// and returns a new resource with the context path at its prefix
function wrapBuildingURLWithContextPath() {
    var contextPath = calculateContextPath();
    return function(resource) {
        return "/" + contextPath + "/" + resource;
    };
}

// call the wrapper method and expose a final method to be used to build complete resource names (buildUrlWithContextPath)
var buildUrlWithContextPath = wrapBuildingURLWithContextPath();