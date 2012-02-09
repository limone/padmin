exports.domains = function(req, res) {
    req.app.pg.query("SELECT * FROM domains", function(err, result) {
        console.log("Row count: %d", result.rows.length);
        for (var x = 0; x<result.rows.length; x++) {
            var domain = result.rows[x];
            console.log(domain);
        }
    });
    res.send(JSON.stringify(''));
};