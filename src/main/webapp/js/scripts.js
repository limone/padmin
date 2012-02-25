var padmin = {
deleteLink : function(domainName, callback) {
  $('#domainToDelete').html(domainName);
  $("#deleteDialog").dialog({
  resizable : false,
  height : 140,
  modal : true,
  buttons : {
  "no" : function() {
    $(this).dialog("close");
  },
  "yes" : function() {
    $(this).dialog("close");
    callback();
  }
  }
  });
},

displayError : function(messages) {
  var errors = '';
  for (var m in messages) {
    var message = messages[m];
    errors += '<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>' + message + '</p>';
  }
  
  var elm = $('<div id="error" class="ui-state-error"></div>').html(errors);
  $('body').append(elm);
  $('#error').dialog({
    close : function(event, ui) {
      $('#error').detach();
    }
  });
}
};