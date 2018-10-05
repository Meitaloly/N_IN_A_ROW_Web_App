var refreshRate = 1000; //mili seconds
var USER_LIST_URL = "/pages/GameLobby/Lobby/userslist";
var loggedUser;

$(function() {
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
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