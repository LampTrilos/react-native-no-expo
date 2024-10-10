import React, {useEffect, useRef} from "react";
import {
    Button, IconButton, Tooltip
} from 'react-native-paper';
import {Navigation} from '../utils/types.tsx';
import {createDrawerNavigator} from "@react-navigation/drawer";
import {DrawerActions} from '@react-navigation/native';
import FaceCapture from "./FaceCapture.tsx";
import customTheme from '../assets/Theme'
import CustomDrawerContent from "../components/TheDrawerContent.tsx";
import {StyleSheet, Text, View} from "react-native";
import BottomTabs from "../screens/DataGathering/BottomTabs.tsx"
import {useDispatch, useSelector} from "react-redux";
import {ControlStatusTypes} from "../utils/model.ts";
import {setUser} from "../store/userData";
import ControlStatus, {setStatus} from "../store/controlStatus";

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

    return (
        <Drawer.Navigator drawerRef={drawerRef} screenOptions={{
            drawerType: 'slide',
            drawerStyle: styles.drawer
        }}
                          initialRouteName="FaceCapture"
                          drawerContent={(props) =>
                              <CustomDrawerContent {...props} />}>
            <Drawer.Screen name="PassportCheck" component={BottomTabs} options={({navigation}) => ({
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
            <Drawer.Screen name="FakeScreen2" component={FaceCapture} options={{headerShown: false}}/>
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
