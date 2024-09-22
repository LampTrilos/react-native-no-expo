//Indicates that this file should be formatted by Prettier, ensuring consistent code style throughout the project.
// @format


import * as React from 'react';
import {useEffect, useState} from "react";
import {SafeAreaView, StatusBar} from 'react-native';
import Drawer from 'react-native-paper';
import {NavigationContainer, DefaultTheme } from "@react-navigation/native";
import {createDrawerNavigator} from "@react-navigation/drawer";
import { createStackNavigator } from '@react-navigation/stack';
import { Drawer as PaperDrawer } from 'react-native-paper';
import { PaperProvider, MD3LightTheme, adaptNavigationTheme } from 'react-native-paper';
import Login from "./screens/Login.tsx";
import Dashboard from "./screens/Dashboard.tsx";

//https://www.adhamroumie.com/blog/add-inavigation-to-the-dashboard-in-your-drawer-with-react-navigation/

function App(): React.JSX.Element {
    //const [active, setActive] = useState('');
    //const Drawer = createDrawerNavigator();
    const Stack = createStackNavigator();
    const Drawer = createDrawerNavigator();

    //const { LightTheme } = adaptNavigationTheme({ reactNavigationLight: DefaultTheme });

    return (
      <SafeAreaView style={{ flex: 1 }}>
        <StatusBar barStyle={'light-content'}/>
          <NavigationContainer>
              {/*First Stack of the app is composed of the Login screen and the Dashboard of the app*/}
              <Stack.Navigator initialRouteName="Login" >
                  <Stack.Screen name="Login" component={Login}  options={{ headerShown: false }} />
                  <Stack.Screen name="Dashboard" component={Dashboard} options={{ headerShown: false }}/>
              </Stack.Navigator>
              {/*<Drawer.Navigator initialRouteName="Dashboard" drawerContent={(props) => <CustomDrawerContent {...props} />}>*/}
              {/*    <Drawer.Screen name="Login" component={Login}  options={{ headerShown: false }} />*/}
              {/*    <Drawer.Screen name="Dashboard" component={Dashboard} options={{ headerShown: false }}/>*/}
              {/*</Drawer.Navigator>*/}
          </NavigationContainer>
      </SafeAreaView>
);
}

//
// function CustomDrawerContent(props) {
//     const [active, setActive] = React.useState('');
//
//     return (
//         <PaperDrawer.Section title="Some title">
//             <PaperDrawer.Item
//                 label="First Item"
//                 active={active === 'first'}
//                 onPress={() => {
//                     setActive('first');
//                     props.navigation.navigate('FirstScreen');
//                 }}
//             />
//             <PaperDrawer.Item
//                 label="Second Item"
//                 active={active === 'second'}
//                 onPress={() => {
//                     setActive('second');
//                     props.navigation.navigate('SecondScreen');
//                 }}
//             />
//         </PaperDrawer.Section>
//     );
// }

export default App;