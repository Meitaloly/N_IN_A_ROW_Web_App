var refreshRate = 1000; //mili seconds
var USER_LIST_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/userslist");
var GAME_LIST_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/gameList");
var LOGOUT_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/logout");
var CREATE_GAME_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/createNewGame");
var COMP_PLAYER_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/compPlayer");
var UPDATE_USER_GAME_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/updateUserGameServlet");
var UPDATE_SIGNED_URL = buildUrlWithContextPath("/pages/GameLobby/Lobby/updatedSignedPlayers");
var SIGN_UP_PAGES = buildUrlWithContextPath("/pages/SignUp/SignUpPage.html");
var GAME_PAGE = buildUrlWithContextPath("/pages/CurrGame/Game.html");
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
        url: LOGOUT_URL,
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
            url: LOGOUT_URL,
            //data: {"username" : userName, "playerType" : playerType},
            success: function(response)
            {
                console.log("you loggedd out!");
                window.location.replace(SIGN_UP_PAGES);
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
                url: CREATE_GAME_URL,
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


    $.ajax({
        type: 'POST',
        url: COMP_PLAYER_URL,
        data: {"userName":loggedUser,"gameName": gameName},

         success:function(){
             updateGameName(loggedUser, gameName);
             updateNumOfPlayerInCurrGame(gameName);
             window.location.replace(GAME_PAGE+"?gameName=" + parent_id);
             console.log("computer enter ok")
         },
         error:function() {
             alert("Can't play game with only computer player, should be at least 1 human player.");
         }
    });

}

function updateGameName(userName,gameName){
    $.ajax({
        type: 'POST',
        url: UPDATE_USER_GAME_URL,
        data: {"username" : userName, "gameName" : gameName}
    })
}



function updateNumOfPlayerInCurrGame(gameName){
    $.ajax({
        type: 'POST',
        url: UPDATE_SIGNED_URL,
        data: {"gameName": gameName, "action": "add"},
    })
}

$(document).ready(function(){
    $("#newGameForm").on('submit', function (e)
    {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: CREATE_GAME_URL,
            //data: {"username" : userName, "playerType" : playerType},
            success: function(response)
            {
                console.log("you loggedd out!");
                window.location.replace(SIGN_UP_PAGES);
            },
        })
    })
})


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