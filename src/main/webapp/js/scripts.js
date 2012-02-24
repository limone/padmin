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

displayError : function(message) {
  var elm = $('<div id="error" class="ui-state-error"></div>').html('<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>' + message + '</p>');
  $('body').append(elm);
  $('#error').dialog({
    close : function(event, ui) {
      $('#error').detach();
    }
  });
}
};