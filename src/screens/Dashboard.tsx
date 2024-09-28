import React, {useEffect, useRef} from "react";
import {
    Button, IconButton, Tooltip
} from 'react-native-paper';
import {Navigation} from '../utils/types.tsx';
import {createDrawerNavigator} from "@react-navigation/drawer";
import {DrawerActions} from '@react-navigation/native';
import FakeScreen from "./FakeScreen.tsx";
import customTheme from '../assets/Theme'
import PassportCheck from "./PassportCheck/PassportCheck.tsx";
import CustomDrawerContent from "../components/TheDrawerContent.tsx";
import { StyleSheet } from "react-native";

type Props = {
    navigation: Navigation;
};

export default function Dashboard({navigation}: Props) {
    const Drawer = createDrawerNavigator();

    // On load of the page, we open the Drawer programmatically
    const drawerRef = useRef(null);
    useEffect(() => {
        navigation.dispatch(DrawerActions.openDrawer());
    }, []);


    return (
        <Drawer.Navigator drawerRef={drawerRef} screenOptions={{
            drawerType: 'slide',
            drawerStyle: styles.drawer
        }}
                          initialRouteName="FakeScreen"
                          drawerContent={(props) =>
                              <CustomDrawerContent {...props} />}>
            <Drawer.Screen name="PassportCheck" component={PassportCheck} options={({navigation}) => ({
                headerShown: true,
                headerLeft: () => (<Button icon="menu"
                                           onPress={() => navigation.toggleDrawer()}/>),
                headerRight: () => (
                    <Tooltip title="Test User" enterTouchDelay={10}>
                        <IconButton
                            icon="account" // You can change this to any Material icon name
                            iconColor={customTheme.colors.iconColour}
                            size={25}      // Custom icon size
                            onPress={() => console.log('Pressed')}
                            style={styles.iconButton} // Custom button style
                        />
                    </Tooltip>),
            })}
            />
            <Drawer.Screen name="FakeScreen2" component={FakeScreen} options={{headerShown: false}}/>
            {/*<Drawer.Screen name="Dashboard" component={Dashboard} options={{ headerShown: false }}/>*/}
        </Drawer.Navigator>)
};
const styles = StyleSheet.create({
    drawer: {
        backgroundColor: customTheme.colors.drawerContainer,
    },
    iconButton: {
        backgroundColor: customTheme.colors.drawerContainer, // Custom background color\
    },
});