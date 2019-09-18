const oracledb = require('oracledb');

exports.oracleConnection = async (req, res) => {
    let connection;

    const oracle_ip = process.env.ORACLE_IP || "35.222.1.145";
    const oracle_schema = process.env.ORACLE_SCHEMA || "xe";

    const dbConfig = {
        user: process.env.ORACLE_USER || "system",
        password: process.env.ORACLE_PASSWORD || "root",
        connectString: oracle_ip + ":1521/" + oracle_schema,
    }

    try {
        // Get a non-pooled connection
        connection = await oracledb.getConnection(dbConfig);
        console.log('Connection was successful!');

        const result = await connection.execute(`select 'Great!' from dual`,
            [], // no bind variables
            {
                resultSet: true // return a ResultSet (default is false)
            }
        );

        const rs = result.resultSet;
        let row;

        while ((row = await rs.getRow())) {
            res.send(row[0]);
        }

        // always close the ResultSet
        await rs.close();

        res.status(200);
    } catch (err) {
        console.error(err);
        res.status(500);
        res.send(err);
    } finally {
        if (connection) {
            try {
                await connection.close();
            } catch (err) {
                console.error(err);
                res.status(500);
                res.send(err);
            }
        }
    }
};