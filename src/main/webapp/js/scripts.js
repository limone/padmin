var padmin = {
  deleteLink : function(domainName, callback) {
    $('#domainToDelete').html(domainName);
    $("#deleteDialog").dialog({
      resizable: false,
      height:140,
      modal: true,
      buttons: {
        "no": function() {
          $( this ).dialog( "close" );
        },
        "yes": function() {
          $( this ).dialog( "close" );
          callback();
        }
      }
    });
  }
};