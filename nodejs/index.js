const express = require('express');
const app = express();
const functionNode = require('./function');

app.get('/', (req, res) => {
    functionNode.oracleConnection(req,res)
});

const port = process.env.PORT || 8080;
app.listen(port, () => {});