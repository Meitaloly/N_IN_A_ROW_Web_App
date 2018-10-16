var LOGIN_URL = buildUrlWithContextPath("pages/SignUp/login");


$(document).ready(function(){
  $("#formId").on('submit', function (e)
  {
      e.preventDefault();

      var userName = $('.userNameInput').val();

      var radios = document.getElementsByName('playerType');

      for (var i = 0, length = radios.length; i < length; i++)
      {
          if (radios[i].checked)
          {
              // do whatever you want with the checked radio
              var playerType = radios[i].value;

              // only one radio can be logically checked, don't check the rest
              break;
          }
      }

      $.ajax({
          type: 'POST',
          url: LOGIN_URL,
          data: {"username" : userName, "playerType" : playerType},
          success: function(response)
          {
              console.log("success!");
              window.location.replace("/pages/GameLobby/Lobby.html");
          },
          error: function(xhttp, sth, msg)
          {
              console.log("user exist!!");
              $(".errorLine").fadeIn("fast");
          }
      })
  })
});

$('.userNameInput').on('input', function(e){
    if(e.target.value.length >0)
    {
        $('.loginBtn').attr('disabled', false);
    }
    else
    {
        $('.loginBtn').attr('disabled', true);
    }
})

