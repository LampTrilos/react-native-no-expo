//Indicates that this file should be formatted by Prettier, ensuring consistent code style throughout the project.
// @format


import * as React from 'react';
import {SafeAreaView, StatusBar} from 'react-native';
import {NavigationContainer, DefaultTheme } from "@react-navigation/native";
import { createStackNavigator } from '@react-navigation/stack';
import Login from "./screens/Login.tsx";
import Dashboard from "./screens/Dashboard.tsx";

//https://www.adhamroumie.com/blog/add-inavigation-to-the-dashboard-in-your-drawer-with-react-navigation/

function App(): React.JSX.Element {
    const Stack = createStackNavigator();

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
          </NavigationContainer>
      </SafeAreaView>
);
}


export default App;