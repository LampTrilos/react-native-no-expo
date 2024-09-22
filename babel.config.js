module.exports = {
  presets: ['@babel/preset-typescript', 'module:@react-native/babel-preset'],
  env: {
    production: {
      //plugins: ['react-native-paper/babel', 'react-native-reanimated/plugin']
      plugins: ['react-native-reanimated/plugin']
    },
  },
};
