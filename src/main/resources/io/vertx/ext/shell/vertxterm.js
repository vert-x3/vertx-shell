;(function() {

  var VertxTerm = function(url, options) {
    options = options || {};
    var cols = options.cols || 80;
    var rows = options.rows || 24;
    url += (url.indexOf('?') > -1 ? '&' : '?') + "cols=" +  cols + "&rows=" + rows;
    var termOptions = options.termOptions || { screenKeys: true };
    termOptions.cols = cols;
    termOptions.rows = rows;
    var socket = new SockJS(url);
    socket.onopen = function () {
      var term = new Terminal(termOptions);
      socket.onmessage = function (event) {
        if (event.type === 'message') {
          term.write(event.data);
        }
      };
      socket.onclose = function () {
        socket.onmessage = null;
        socket.onclose = null;
        term.destroy();
      };
      term.on('data', function (data) {
        socket.send(JSON.stringify({action: 'read', data: data}));
      });
      term.open(document.body);
    };
  };

  if (typeof module !== 'undefined') {
    module.exports = VertxTerm;
  } else {
    this.VertxTerm = VertxTerm;
  }
})();