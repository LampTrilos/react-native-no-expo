//Indicates that this file should be formatted by Prettier, ensuring consistent code style throughout the project.
// @format


import * as React from 'react';
import {useEffect, useState} from "react";
import {SafeAreaView, StatusBar} from 'react-native';
import Drawer from 'react-native-paper';
import Index from "./screens/Index.tsx";
import {NavigationContainer} from "@react-navigation/native";
import {createDrawerNavigator} from "@react-navigation/drawer";
import { createStackNavigator } from '@react-navigation/stack';
import { Drawer as PaperDrawer } from 'react-native-paper';
//https://www.adhamroumie.com/blog/add-inavigation-to-the-dashboard-in-your-drawer-with-react-navigation/


function App(): React.JSX.Element {
    //const [active, setActive] = useState('');
    const Drawer = createDrawerNavigator();
    const Stack = createStackNavigator();

    return (
      <SafeAreaView style={{ flex: 1 }}>
        <StatusBar barStyle={'light-content'}/>
          <Index/>
           <NavigationContainer>
               {/*<Stack.Navigator initialRouteName="Home">*/}
               {/*    <Stack.Screen name="Home" component={Index} />*/}
               {/*</Stack.Navigator>*/}
               <Drawer.Navigator drawerContent={(props) => <CustomDrawerContent {...props} />}>
                   <Drawer.Screen name="Home" component={Index} />
               </Drawer.Navigator>
           </NavigationContainer>
      </SafeAreaView>
);
}

function CustomDrawerContent(props) {
    const [active, setActive] = React.useState('');

    return (
        <PaperDrawer.Section title="Some title">
            <PaperDrawer.Item
                label="First Item"
                active={active === 'first'}
                onPress={() => {
                    setActive('first');
                    props.navigation.navigate('FirstScreen');
                }}
            />
            <PaperDrawer.Item
                label="Second Item"
                active={active === 'second'}
                onPress={() => {
                    setActive('second');
                    props.navigation.navigate('SecondScreen');
                }}
            />
        </PaperDrawer.Section>
    );
}

export default App;
