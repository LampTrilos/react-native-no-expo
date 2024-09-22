module.exports = {
  //presets: ['@babel/preset-typescript', 'module:@react-native/babel-preset'],
  presets: ['module:@react-native/babel-preset'],
  plugins: [
    'react-native-paper/babel',
    'react-native-reanimated/plugin'
  ],
  // env: {
  //   production: {
  //     //plugins: ['react-native-paper/babel', 'react-native-reanimated/plugin']
  //     //react-native-reanimated/plugin has to be listed last.
  //   },
  // },
};
