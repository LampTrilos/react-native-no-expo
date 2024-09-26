import {DefaultTheme} from 'react-native-paper';

//This is passed to Main.tsx
const customTheme = {
  ...DefaultTheme,
  version: 3,
  roundness: 2,
  //customTheme,
  //colors: customTheme.colors,
  colors: {
    //Includes buttons,
    "primary": "rgb(2,7,62)",
    "onPrimary": "rgb(255, 255, 255)",
    "primaryContainer": "rgb(223, 224, 255)",
    "onPrimaryContainer": "rgb(0, 13, 95)",

    "secondary": "rgb(91, 93, 114)",
    "onSecondary": "rgb(255, 255, 255)",
    //Includes Drawer
    "drawerContainer": "rgb(1,3,39)",
    "onDrawerContainer": "rgb(255, 255, 255)",
    //Includes Drawer item,
    "secondaryContainer":"rgb(255, 255, 255)",
    "onSecondaryContainer": "rgb(1,3,39)",
    //The inactive Drawer item
    "surfaceVariant": "rgb(227, 225, 236)",
    //The hints above the fields in Login Screen
    "onSurfaceVariant":  "rgb(75,88,98)",
    //The icon colour
    "iconColour" : "rgb(255,255,255)",

    "tertiary": "rgb(119, 83, 108)",
    "onTertiary": "rgb(255, 255, 255)",
    "tertiaryContainer": "rgb(255, 215, 240)",
    "onTertiaryContainer": "rgb(45, 18, 39)",
    "error": "rgb(186, 26, 26)",
    "onError": "rgb(255, 255, 255)",
    "errorContainer": "rgb(255, 218, 214)",
    "onErrorContainer": "rgb(65, 0, 2)",
    //Background of main Screens
    "background": "rgb(255, 251, 255)",
    "onBackground": "rgb(27, 27, 31)",
    //The inner colour of input texts
    "surface": "rgb(255,254,254)",
    "onSurface": "rgb(27, 27, 31)",

    "outline": "rgb(118, 118, 128)",
    "outlineVariant": "rgb(199, 197, 208)",
    "shadow": "rgb(0, 0, 0)",
    "scrim": "rgb(0, 0, 0)",
    "inverseSurface": "rgb(48, 48, 52)",
    "inverseOnSurface": "rgb(243, 240, 244)",
    "inversePrimary": "rgb(187, 195, 255)",
    "elevation": {
      "level0": "transparent",
      //Background of Card
      //This is the Card  level
      "level1": "rgb(237,239,241)",
      "level2": "rgb(240, 238, 249)",
      "level3": "rgb(235, 233, 247)",
      "level4": "rgb(233, 231, 246)",
      "level5": "rgb(229, 228, 245)"
    },
    "surfaceDisabled": "rgba(27, 27, 31, 0.12)",
    "onSurfaceDisabled": "rgba(27, 27, 31, 0.38)",
    "backdrop": "rgba(47, 48, 56, 0.4)"
  }

}

export default customTheme;