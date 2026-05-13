// composeApp/webpack.config.d/config.js
const webpack = require('webpack');

config.plugins.push(
    new webpack.DefinePlugin({
        '__IS_DEBUG__': config.mode === 'development'
    })
);