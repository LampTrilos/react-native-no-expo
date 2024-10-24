import React, {useEffect, useRef, useCallback} from "react";
import {
    Button, IconButton, Tooltip
} from 'react-native-paper';
import {Navigation} from '../utils/types.tsx';
import {createDrawerNavigator} from "@react-navigation/drawer";
import {DrawerActions, useFocusEffect} from '@react-navigation/native';
import FaceCapture from "./FaceCapture.tsx";
import Activation from "./Activation.tsx";
import customTheme from '../assets/Theme'
import CustomDrawerContent from "../components/TheDrawerContent.tsx";
import {StyleSheet, Text, View, AppState } from "react-native";
import PassportNavigator from "../screens/DataGathering/PassportCheck/PassportNavigator.tsx"
import {useDispatch, useSelector} from "react-redux";
import {ControlStatusTypes} from "../utils/model.ts";
import {setUser} from "../store/userData";
import ControlStatus, {setStatus} from "../store/controlStatus";
import BottomTabs from "./DataGathering/BottomTabs.tsx";


type Props = {
    navigation: Navigation;
};

export default function Dashboard({navigation}: Props) {
    const Drawer = createDrawerNavigator();

    // On load of the page, we open the Drawer programmatically
    const drawerRef = useRef(null);
    useEffect(() => {
        const openDrawer = () => {
            navigation.dispatch(DrawerActions.openDrawer());
        };
        // On load of the page, we open the Drawer programmatically
        openDrawer();

        // Listener for app state changes
        const appStateListener = AppState.addEventListener('change', (nextAppState) => {
            if (nextAppState === 'active') {
                // When the app comes to the foreground, trigger the function
                openDrawer();
            }
        });

        // Cleanup the listener when the component unmounts
        return () => {
            appStateListener.remove();
        };
    }, [navigation]);

    //Store section about the logged in user
    const currentUser = useSelector(state => state.userDataStore.value.user);
    //------------------End of store user section--------------------//

    //-------------------Status store section------------------------//
    //If all is well, navigate to the next Screen
    const dispatch = useDispatch();
    //Only when the screen is first shown, set the status to None, it won't fire against because there are no dependencies
    useEffect(() => {
        dispatch(setStatus(ControlStatusTypes.None));
    }, []);

    //-------------------End of status store section------------------------//
    //The actual content of the drawer (links) is declared in TheDrawerContent.tsx--//
    return (
        <Drawer.Navigator drawerRef={drawerRef} screenOptions={{
            drawerType: 'slide',
            drawerStyle: styles.drawer
        }}
                          initialRouteName="BottomTabs"
                          drawerContent={(props) =>
                              <CustomDrawerContent {...props} />}>
            {/*This is the first screen of our app*/}
            <Drawer.Screen name="BottomTabs" component={BottomTabs} options={({navigation}) => ({
                headerTitle: 'Διαβατηριακός Έλεγχος',
                headerShown: true,
                headerLeft: () => (<Button icon="menu"
                                           onPress={() => navigation.toggleDrawer()}/>),
                headerRight: () => (
                    <Tooltip title={
                        <>
                            <Text>{currentUser.fullName + ", "}</Text>
                            <Text>{currentUser.shift}</Text>
                        </>
                    } enterTouchDelay={10}>
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
            <Drawer.Screen name="FakeScreen2" component={FaceCapture} options={{headerShown: true}}/>
            <Drawer.Screen name="Activation" component={Activation} options={{headerShown: true}}/>
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
