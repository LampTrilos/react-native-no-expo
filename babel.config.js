module.exports = {
  //presets: [, 'module:@react-native/babel-preset'],
  presets: ['module:@react-native/babel-preset', '@babel/preset-typescript'],
  //react-native-reanimated/plugin has to be listed last.
  plugins: [
    'react-native-paper/babel',
    'react-native-reanimated/plugin'
  ],
  // env: {
  //   production: {
  //     //plugins: ['react-native-paper/babel', 'react-native-reanimated/plugin']
  //
  //   },
  // },
};
